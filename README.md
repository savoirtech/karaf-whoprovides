WhoProvides
====

This bundle provides the following shell commands when installed:

  bundle:find-package - locate bundles that export, or contain a specific bundle.


Building from source:
===

To build, invoke:
 
    mvn install


To install in Karaf, invoke from console:

    install -s bundle:install -s mvn:com.savoirtech.karaf.commands/whoprovides


find-package Examples:
====

    bundle:find-package com.savoirtech.karaf.commands.whoprovides com.savoirtech.karaf.commands org.apache.camel


find-package Runtime Options:
===

    --format <format> set the report output format (use "help" to list supported formats; "list" and "table" are
                   supported as of this writing).
 
    -e --export-only  only show package exports.
