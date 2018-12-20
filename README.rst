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
    ternaustralia/ecoinf-dataone:1

   # let's have a look at what the container is doing
   docker logs -f eml_pusher

Quick start with docker-compose
-----------

1. clone this repo
1. change to the docker dir: ``cd docker/``
1. optionally, build the container for this repo by running: ``./docker-build.sh``, otherwise it'll be pulled from docker hub
1. copy the runner script: ``cp start-or-restart.sh.example start-or-restart.sh``
1. edit ``start-or-restart.sh`` and update all the values with a TODO comment
1. make the runner script executable: ``chmod +x start-or-restart.sh``
1. launch the stack by running ``./start-or-restart.sh``
1. populate the jOAI instance with config: ``./configure-joai.sh``

Now the various eml_pusher containers will sync records to the DataONE MN node on the cron schedule you specified. Note that the jOAI instance also has a schedule for harvesting so if you want to kick start things, go into the jOAI dashboard and manually trigger the harvests.

Warning: the jOAI instance has no security/auth so don't expose it to the internet. Or if you do, add some security. I nice way to connect to the jOAI dashboard on a VM without opening the firewall is to use SSH local port forwarding (https://help.ubuntu.com/community/SSH/OpenSSH/PortForwarding#Local_Port_Forwarding).

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
