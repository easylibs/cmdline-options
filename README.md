# Java Command Line Parser - Easylibs Options
[Easylib Options](https://github.com/easylibs/cmdline-options) is a minimalist, easy to use command line argument parser for the Java programming language. Using a tiny footprint, you can parse command line arguments with just a few lines of code.

![Preview - Example 7](https://user-images.githubusercontent.com/18365790/78848880-04999d00-79e1-11ea-82af-e3eb1f26c306.png)
[Source: Example 7](https://github.com/easylibs/cmdline-options/blob/master/src/example/org/easylibs/options/examples/Example7.java)

**Easylibs Options** API is provided in 2 different packages
+ **Getopt SDK** which provides very minimalistic in about **14KB** of jar file size
+ **Options SDK** full featured API that abstracts working with options, beans and properties for configuration of your application and is up about 50KB in size
  * **Simple options** very basic in implementation where defined `Option` objects can be queries
  * **Java Bean options** which allow class members to both act as option definers and where command line arguments are stored
  * **Java Properties** options where simple `Properties` object defines the options and the properties are automatically updated based on the command line arguments

## How It Works
Most users will want to use the full featured **Options** package as it is the easiest to work with to parse command line arguments. You can parse a command line in as little as a single line of code:
```java
Args args = Args.of(argv, Option.of("optionA", int.class));
``` 
That's it! pass in the command line argument array `String[] argv` from your main method, define a single option named `optionA` and parse the command line, all in a single step. To query or printout the values of matched options, most of it can also be done in a single of code:
```java
args.forEach(System.out::println);
```
which will iterate over each option which was present on the command line and print out its state information, including the option's integer argument. The output may look something like this:
```
Option [name='optionA', value='987', matches=1, arg-required, type=int]
```
if a `--optionA=987` was specified on the command line.

The preferred and encouraged usage is to use the **Bean Options** to quickly define and have the command line state be reflected immediately in you java class members (or beans).

![Screenshot from 2020-04-08 22-28-12](https://user-images.githubusercontent.com/18365790/78851634-48dc6b80-79e8-11ea-91b9-f5069da7cddd.png)
[Source: Example 6](https://github.com/easylibs/cmdline-options/blob/master/src/example/org/easylibs/options/examples/Example6.java)

The example uses a **Bean Option** parser, where the `Example 6` annotated class members are used to construct `options` and at the same time parse the command line. The method returns an `Optional<Args>` parser result, if there were no errors, otherwise the default error handler is called and an empty optional is returned. For this example the output looks something like this:
```
-- Example 6 - matched options:
Option [name='filenames', value='[f1, f2, f3, f4, f5]', matches=1, arg-required, Bean [field=Example6::filenames], type=List]

Unmatched options:
Option [name='option2', Bean [field=Example6::option2], type=boolean]

Unmatched args:
--
random string
```
--
**Note:** If you need the smallest footprint possible or you are very familiar with the *unix/gnu* version of `getopt(3)` implementation, you can use the **getopt** package which provides lower level API but a less convenient parser, all in an extremely tiny footprint.
## Installation and Confirguration
### Prerequisites
+ None
### Java JRE Requirement
+ JRE 8 or above
### Installation
+ Download the jar file
+ Compile the `src/getopt` and `src/options` sources
+ **Todo:** release popular binary packages such as `maven`
## License
+ [MIT License](https://choosealicense.com/licenses/mit)
