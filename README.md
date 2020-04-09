# Easylib Options
[Easylib Options](https://github.com/easylibs/cmdline-options) is a minimalist easy to use command line argument parser for the Java programming language. Using a tiny footprint, you can parse command line arguments with a single line of code.

![Preview - Example 7](https://user-images.githubusercontent.com/18365790/78848880-04999d00-79e1-11ea-82af-e3eb1f26c306.png)

**Easylibs Options** API is provided in 2 different packages
+ **Getopt SDK** which provides very minimalistic in about **14KB** of jar file size
+ **Options SDK** full featured API that abstracts working with options, beans and properties for configuration of your application and is up about 50KB in size
  * Simple options very basic in implementation where defined `Option` objects can be queries
  * Java Bean options which allow class members to both act as option definers and where command line arguments are stored
  * Java Properties options where simple `Properties` object defines the options and the properties are automatically updated based on the command line arguments
