===========
eml_pusher
===========
A simple tool to allow upload and update of EML files held in a DataOne repository.
-----------

It can traverse a directory tree of files, and will select those files with a ``.xml`` suffix, that contain valid (parsable) XML, and that have a valid *packageId* attribute.

The *packageId* is used to determine the name and version of the package.  The packageId is the DataOne PID, but is formed from the package name with a datestamp appended as a version number.
Those local packages that are not visible on the DataOne MN will be uploaded, along with a dynamically created sysmeta description.  Packages that have a newer timestamp than found on the MN will also be uploaded and the sysmeta data will indicate that the new file obsoletes the existing one. The old version will cease to be indexed by DataOne. If the packageId does not contain a valid timestamp, the file will still be used, and the timestamp considered to have a value of zero. Thus a packageId with any timestamp superseeds a packageId without one.

Timestamps are found with a simple (and not especially robust) heuristic. Any trailing format strings (ie ``/html``) are removed from the object's URL. Then the component after the last ``.`` is checked to see if it is an integer.  If it is, it is assumed that this is a timestamp.  The URL is then split to form a base name, and timestamp. 

The tool can also be used to synchronise two directories of eml files - which is useful for testing. Rather than specify a ``--destination_url`` specify a ``--destination_path``.

Since write access is required to the DataOne server an approriate credentials file must be provided via ``--cert_file``. 

The tool can also be run in trial mode, where it simply works out the files that would need to be uploaded.  Since no write occurs, this can be run without credentials.  If ``--trial_run`` is used, adding ``--verbose`` will list the files to be uloaded.

The tool uses the ``python.logger`` package to manage logging output. By default log messages come to standard output.  They may also be sent to a file via ``--log_file``, or if complex logging is required, (perhaps as part of setting the tool up to run via a cron job, where log rollover and the like are useful) a logger config file may be specified with ``--config_log_file``.

Adding ``--debug`` will add debug information to the log, which may be useful to determine the source of a problem in synchronising. ``--dataone_debug`` turns on logging of the DataOne library's internal operation at the same level as logging specified for this tool. 

The tool attempts to avoid unnecessary uploads in a simple manner. If content on the DataOne node and the local content are identical in all respects except for the datestamp, no upload will occur.  This requires that the files are byte for byte identical except for the timestamp.  This is determined by comparing the checksums calculated for the content. If it is desired to force an update (which will cause the new timestamp to appear in the DataOne node) the ``--force_update`` flag will force the tool to not perform the checksum comparison.

Access and replication configuration may be configured with a YAML format configuration file specified with ``--yaml_config``. By default the rights holder (which by default then allows ``read``, ``write``, ``changePermission``) is set to ``authenticatedUser`` which is a special authentication token used by DataOne, and read access is allowed to ``public``.  This configuration is only useful if there is only one user authenticated, as it allows any user full rights to any data.  Long term, a better solution will be to use the name of the individual user, perhaps as found in the authentication certificate. The configuration file format can be found by dumping the default configuration with the ``--dump_yaml`` flag.  This will print the configuration in use to standard output in YAML format. This output can be used as the starting point for a customised configuration file.
