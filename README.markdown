CI-Status
========

This applicaiton provides a way to see the statuses of builds on a jenkins ci server without having to visit its page.

This is done by displaying a status icon for each build.  For most Linux users they will also get a notification, informing them when the status of a build changes.

Dependencies/Requirements
-------------------------

### For Usage

  Java Runtime Enviroment (JRE)

### For Development

  The [jsoup](http://github.com/jhy/jsoup "jsoup") HTML parser which is included in the lib.

Usage
-----

Download the jar file from this repository.

You can execute the program with `$ java -jar CiStatus.jar`

The application takes command line arguments. 

* `--addr <Jenkins Address>` The http address of the jenkins website.
* `--delay <Seconds>` The delay between updating build statuses.    

License
-------

MIT License. Copyright 2011 - David Elliott
