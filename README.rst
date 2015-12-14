============
eml_pusher
===========


This is a simple tool to allow upload and update of EML files to a DataOne repository.

It can traverse a directory tree of files, and will select those files with a ``.xml`` suffix that also contain  valid (parsable) XML and that have a valid *packageId* attribute.

The *packageId* is used to determine the name and version of the package.  The packageId is the DataOne PID, but is semantically formed of the package name with a datestamp appended as a version number.
Those local packages that are not visible on the DataOne MN will be uploaded, along with a dynamically created sysmeta description.  Packages that have a newer timestamp than found on the MN will also be uploaded and the sysmeta data will indicate that the new file obsoletes the existing one. The old version will cease to be indexed by DataOne. If the packageId does not contain a valid timestamp, the file will still be used, and the timestamp considered to have a value of zero. Thus a packageId with any timestamp superseeds a packageId without one.

The tool can also be used to synchronise two directories of eml files - which is useful for testing. Rather than specify a ``--destination_url`` specify a ``--destination_path``.

Since write access is required to the DataOne server an approriate credentials file must be provided via ``--cred_file``. 

However the tool can also be run in trial mode, where it simply works out the files that would need to be uploaded.  Since no write occurs, this can be run without credentials.  If ``--trial_run`` is used, adding ``--verbose`` will list the files to be uloaded.

The tool uses the python.logger package to output. By default log messages come to standard output.  They may also be sent to a file via ``--log_file``, or if complex logging is required (perhaps as part of setting the tool up to run via a cron job, where log rollover and the like are useful) a logger config file may be specified with ``--config_log_file``.

Adding ``--debug`` will add debug information to the log, which may be useful to determine the source of a problem in synchronising.

Currently the tool simply starts at the top of the DataOne node's content index, and traverses everything visible.  In principle, using ``--path`` to designate a sub-tree should limit this, currently this is not implemented. In the future this may be useful to limit the amount of data transferred during negotiating the content to upload.

The tool attempts to avoid unnecessary uploads in a simple manner. If content on the DataOne node and the local content are identical in all respects except for the datestamp, no upload will occur.  This requires that the files are byte for byte identical except for the 8 bytes in the timestamp.  This is determined by comparing the checksums calculated for the content. If it is desired to force an update (which will cuase the new timestamp to appear in the DataOne node) the ``--force_update`` flag will cause the tool to not perform the checksum comparison.
