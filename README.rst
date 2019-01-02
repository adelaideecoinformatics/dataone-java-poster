===========
eml_pusher
===========

Note: the naming is a bit dated as this tool also pushes iso19139 too.

Quick start running with Docker
-----------

Optionally, you can build the docker container locally with: ``./docker/docker-build.sh``

To run the container, use:

.. code-block:: bash

  docker run \
    --detach \
    --name eml_pusher \
    --env CERT_BASE64=`sh -c 'cat /path/to/cert.pem | base64'` \
    --env CERT_KEY_BASE64=`sh -c 'cat /path/to/key.pem | base64'` \
    --env RECORDS_DIR_PATH=/data/dir-you-configured-in-joai \
    --env MN_URL=https://dataone-dev.tern.org.au/mn \
    --env CRON_SCHEDULE='* * * * *' \
    ternaustralia/ecoinf-dataone:1.0.4

 # let's have a look at what the container is doing
 docker logs -f eml_pusher

Quick start with docker-compose
-----------

1. clone this repo
2. change to the docker dir::

    cd docker/
3. optionally, build the container for this repo otherwise it'll be pulled from docker hub. Build by running::

    ./docker-build.sh
4. copy the runner script::

    cp start-or-restart.sh.example start-or-restart.sh
5. get a client certificate and private key from the local CA on the DataONE server. For how to generate these, see: https://dataone-python.readthedocs.io/en/latest/gmn/setup/ubuntu/gmn/authn-ca.html?highlight=certificate#generate-a-client-side-certificate
6. edit ``start-or-restart.sh`` and update all the values with a ``TODO`` comment
7. make the runner script executable::

    chmod +x start-or-restart.sh
8. launch the stack by running::

    ./start-or-restart.sh
9. populate the jOAI instance with config (so you don't need to do it by hand in the UI)::

    ./configure-joai.sh

Now the various eml_pusher containers will sync records to the DataONE MN node on the cron schedule you specified. Note that the jOAI instance also has a schedule for harvesting so if you want to kick start things, go into the jOAI dashboard and manually trigger the harvests.

Warning: the jOAI instance has no security/auth so don't expose it to the internet. Or if you do, add some security. A nice way to connect to the jOAI dashboard on a VM without opening the firewall is to use SSH local port forwarding (https://help.ubuntu.com/community/SSH/OpenSSH/PortForwarding#Local_Port_Forwarding).

Quick start installing direct from GitHub (without cloning)
-----------
.. code-block:: bash

  mkdir pusher_venv
  cd pusher_venv
  virtualenv .
  . bin/activate
  pip install --upgrade https://github.com/ternaustralia/ecoinf-dataone/archive/master.zip
  # get this certificate and key from the DataONE MN server
  cp /path/to/certificate.pem cert.pem
  cp /path/to/certificate-key.pem cert-key.pem
  eml_pusher \
    --verbose \
    --source_dir /path/to/dir/with/records \
    --cert_file cert.pem \
    --cert_key_file cert-key.pem \
    --destination_url https://dataone-dev.tern.org.au/mn \
    --log_file output.log
  # check out cron-job.sh in this repo for a automation template


A simple tool to allow upload and update of EML files held in a DataOne repository.
-----------------------------------------------------------------------------------

It can traverse a directory tree of files, and will select those files with a ``.xml`` suffix, that contain valid (parsable) XML, and that have a valid *packageId* attribute.

The *packageId* is used to determine the name and version of the package.  The packageId is the DataOne PID, but is formed from the package name with a datestamp appended as a version number.
Those local packages that are not visible on the DataOne MN will be uploaded, along with a dynamically created sysmeta description.  Packages that have a newer timestamp than found on the MN will also be uploaded and the sysmeta data will indicate that the new file obsoletes the existing one. The old version will cease to be indexed by DataOne. If the packageId does not contain a valid timestamp, the file will still be used, and the timestamp considered to have a value of zero. Thus a packageId with any timestamp superseeds a packageId without one.

Timestamps are found with a simple (and not especially robust) heuristic. Any trailing format strings (ie ``/html``) are removed from the object's URL. Then the component after the last ``.`` is checked to see if it is an integer.  If it is, it is assumed that this is a timestamp.  The URL is then split to form a base name, and timestamp. 

The tool can also be used to synchronise two directories of eml files - which is useful for testing. Rather than specify a ``--destination_url`` specify a ``--destination_path``.

Since write access is required to the DataOne server an approriate credentials file must be provided via ``--cert_file``. The DataONE MN server runs its own local Certificate Authority (CA) and issues certificates to clients (like us) that can be used for auth when we try to make HTTP API calls.

The tool can also be run in trial mode, where it simply works out the files that would need to be uploaded.  Since no write occurs, this can be run without credentials.  If ``--trial_run`` is used, adding ``--verbose`` will list the files to be uloaded.

The tool uses the ``python.logger`` package to manage logging output. By default log messages come to standard output.  They may also be sent to a file via ``--log_file``, or if complex logging is required, (perhaps as part of setting the tool up to run via a cron job, where log rollover and the like are useful) a logger config file may be specified with ``--config_log_file``.

Adding ``--debug`` will add debug information to the log, which may be useful to determine the source of a problem in synchronising. ``--dataone_debug`` turns on logging of the DataOne library's internal operation at the same level as logging specified for this tool. 

The tool attempts to avoid unnecessary uploads in a simple manner. If content on the DataOne node and the local content are identical in all respects except for the datestamp, no upload will occur.  This requires that the files are byte for byte identical except for the timestamp.  This is determined by comparing the checksums calculated for the content. If it is desired to force an update (which will cause the new timestamp to appear in the DataOne node) the ``--force_update`` flag will force the tool to not perform the checksum comparison.

Access and replication configuration may be configured with a YAML format configuration file specified with ``--yaml_config``. By default the rights holder (which by default then allows ``read``, ``write``, ``changePermission``) is set to ``authenticatedUser`` which is a special authentication token used by DataOne, and read access is allowed to ``public``.  This configuration is only useful if there is only one user authenticated, as it allows any user full rights to any data.  Long term, a better solution will be to use the name of the individual user, perhaps as found in the authentication certificate. The configuration file format can be found by dumping the default configuration with the ``--dump_yaml`` flag.  This will print the configuration in use to standard output in YAML format. This output can be used as the starting point for a customised configuration file.

Fixing VersionConflict
----------------------
If you get an error that talks about the version of dataone.libclient being wrong like the following:

.. code:: bash

  $ eml_pusher
  Traceback (most recent call last):
    File "/var/local/dataone/gmn/bin/eml_pusher", line 5, in <module>
      from pkg_resources import load_entry_point
    File "/var/local/dataone/gmn/local/lib/python2.7/site-packages/pkg_resources.py", line 2829, in <module>
      working_set = WorkingSet._build_master()
    File "/var/local/dataone/gmn/local/lib/python2.7/site-packages/pkg_resources.py", line 451, in _build_master
      return cls._build_from_requirements(__requires__)
    File "/var/local/dataone/gmn/local/lib/python2.7/site-packages/pkg_resources.py", line 464, in _build_from_requirements
      dists = ws.resolve(reqs, Environment())
    File "/var/local/dataone/gmn/local/lib/python2.7/site-packages/pkg_resources.py", line 643, in resolve
      raise VersionConflict(dist, req) # XXX put more info here
  pkg_resources.VersionConflict: (dataone.libclient 1.2.21 (/var/local/dataone/gmn/lib/python2.7/site-packages), Requirement.parse('dataone.libclient==1.2.6'))

... the fix is to edit the requires.txt file for dataone.cli to set it to the correct version. This is a bug with the dataone code as it still has an old requirement even though it's been updated.

.. code:: bash

  $ pip show dataone.cli # find the site-packages dir
  ---
  Name: dataone.cli
  Version: 1.2.5
  Location: /var/local/dataone/gmn/lib/python2.7/site-packages
  Requires: dataone.libclient

  $ cd /var/local/dataone/gmn/lib/python2.7/site-packages/dataone.cli*
  $ vim requires.txt # change the contents to be: dataone.libclient == 1.2.21

Now you should be able to run the eml_pusher without error.

A note on SSL/TLS certs
-----------------------

At the time of writing, our GMN server is using a TLS/SSL certificate that is signed by the ``QuoVadis Root CA 2`` root certificate.
QuoVadis seem to be a provider or free certificates to a number of universities, so the certificates are somewhat common. This root
certificate is in the python certifi bundle *and* in my system CA bundle (``/etc/ssl/certs/ca-certificates.crt``) but for some reason
python requests still can't validate. I'm not sure why and I've spent enough time on it.

Our fix is to allow a CA bundle to be specified with an environment variable that is used by our script. The DataONE code has its
own default logic (that looks at the system certs) so we are effectively just exposing the ``REQUESTS_CA_BUNDLE`` override like
requests does. We have the CA bundle included in this repo: ``docker/ca-bundle.crt``.

Beware, this bundle *WILL EXPIRE*. One day this app will stop working and the logs will same something like:

.. code:: bash

  requests.exceptions.SSLError: HTTPSConnectionPool(host='dataone-dev.tern.org.au', port=443): Max retries exceeded with url: /mn/v1/object?count=500&start=0 (Caused by SSLError(SSLError("bad handshake: Error([('SSL routines', 'tls_process_server_certificate', 'certificate verify failed')],)",),))

Or something similar. When that happens, do the following to generate a new CA bundle:

1. open the site (e.g: dataone-dev.tern.org.au) in your browser (Chrome, Firefox, etc)
2. view the SSL cert for the site
3. export all part of the chain: root, intermediate(s) and whatever the lowest level cert is called (the one for our site)
4. if they aren't already one file, ``cat`` them all together. Not sure if the order matters. If it does, put the root first
5. overwrite the existing bundle in this repo and rebuild the docker container
6. re-deploy the docker containers

Making authenticated API calls via HTTP
---------------------------------------

As part of debugging something, you might want to make a call directly on the HTTP API using something like ``curl``. The API uses client certificates, so the command has some extra parameters. For Tier 1 calls, or ones that the public can make, they're just a regular curl command. We're dealing with the higher tier calls here, as they require auth.

Let's pick on updateSystemMetadata (https://releases.dataone.org/online/api-documentation-v2.0/apis/MN_APIs.html#MNStorage.updateSystemMetadata) as fixing a sysmeta record with a problem is something that sounds plausable.

We have a number of files in our working directory:

:sysmeta.xml:
  XML document with system metadata. It should have **no whitespace**.
:cert.pem:
  client certificate we use for auth
:key.pem:
  matching private key for the client cert
:ca-bundle.crt:
  bundle containing the full certificate chain because your system
  might not have trust for the Root CA that's signed our server's cert

.. code:: bash

  curl \
    -v \
    --cert ./cert.pem \
    --key ./key.pem \
    --cacert ./ca-bundle.crt \
    -X PUT \
    -F pid="aekos.org.au/collection/nsw.gov.au/nsw_atlas/vis_flora_module/RYDE2006.20170515" \
    -F sysmeta=@sysmeta.xml \
    https://dataone-dev.tern.org.au/mn/v2/meta

The noteworthy points are:

- we don't need to explicitly pass the ``session`` param as that comes from the TLS handshake using our client cert
- we provide the ``pid`` as a literal
- the ``sysmeta`` param is read from a file, which is what the ``@`` symbol means

Lack of ISO19139 support
------------------------

DataONE *does* support ISO19139 but it appears there are a few flavours of this standard (yeah, I know!?!). The supported
flavour (from NOAA I think) is NOT what we're using.

At the time of writing, we have two datasources that provide metadata in this format: ACEF and Auscover. If you try to
load these records using this tool, you'll likely have it fail and see an error like the following in the ``gmn.log`` log file:

.. code:: text

  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288 Internal exception:
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288   Name: XMLSchemaParseError
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288   Value: Element '{http://www.w3.org/2001/XMLSchema}element', attribute 'ref': The QName value '{http://www.isotc211.org/2005/gmd}AbstractDQ_PositionalAccuracy' does not resolve to a(n) element declaration., line 54
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288   Args: <no args>
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288   TraceInfo:
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     base.py(124)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     contextlib.py(52)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     external.py(101)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     decorators.py(167)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     external.py(525)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     create.py(88)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     scimeta.py(71)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     xml_schema.py(67)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     xml_schema.py(74)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     xml_schema.py(106)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     xmlschema.pxi(86)
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     Type: <class 'lxml.etree.XMLSchemaParseError'>
  2019-01-02 04:58:26 ERROR    root exception_handler 26229 139824168044288     Value: Element '{http://www.w3.org/2001/XMLSchema}element', attribute 'ref': The QName value '{http://www.isotc211.org/2005/gmd}AbstractDQ_PositionalAccuracy' does not resolve to a(n) element declaration., line 54
  2019-01-02 04:58:26 ERROR    root response_handler 26229 139824168044288 View exception:
  2019-01-02 04:58:26 ERROR    django.request log 26229 139824168044288 Internal Server Error: /mn/v1/object
  Traceback (most recent call last):
    File "/var/local/dataone/gmn_venv_py3/lib/python3.6/site-packages/django/core/handlers/exception.py", line 34, in inner
      response = get_response(request)
    File "/var/local/dataone/gmn_venv_py3/lib/python3.6/site-packages/d1_gmn/app/middleware/request_handler.py", line 54, in __call__
      response, request.allowed_method_list
    File "/var/local/dataone/gmn_venv_py3/lib/python3.6/site-packages/d1_gmn/app/views/headers.py", line 90, in add_cors_headers_to_response
      response['Allow'] = opt_method_list
  TypeError: 'ServiceFailure' object does not support item assignment

