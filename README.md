# case-string

# What is this project?
This is a simple lib that aims to convert Strings into the following available cases:
- kebab (this-is-a-kebab-case-string)
- snake (this_is_a_snake_case_string)
- camel (thisIsASnakeCaseString)
- pascal (ThisIsAPascalCaseString)
- spaced (this is a spaced case string)

While spaced is not a case in itself, it exists here as a means to parse Strings which use space as a separator and convert
them to the other available cases. For all practical purposes, spaced is treated as any other case, being able to be used
to convert a String from or to as well.

# Motivation
Not much to say here. This is a common problem, which is already solved by a few - or maybe a lot - of other libs. Be that as it
may, I wanted to create a lightweight lib that I could reuse in my projects, personal or otherwise.

# How does it work?
The main goal of this lib is **NOT** to convert from one case to another, although this is obviously possible. 
The idea is to parse a given String, no matter the case, and make its case representations available. This means that
an input String, hereby called parsed String, can be laid in any case available, even in a combination of cases, and it will
still be parsed and create the available cases as it should. What does this mean exactly? It means that a String such as:

```
   This   is-a_--Random String thatWill--- _ -    BeParsed
```

will be parsed into:

```
kebab: this-is-a-random-string-that-will-be-parsed
snake: this_is_a_random_string_that_will_be_parsed
camel: thisIsARandomStringThatWillBeParsed
pascal: ThisIsARandomStringThatWillBeParsed
spaced: this is a random string that will be parsed 
```

After parsing a String, all cases will be available for comparisons, getting the value, etc.

# How to use it?

To use this project, you need to update your pom.xml if using Maven
```
<dependency>
    <groupId>io.github.renatols-jf</groupId>
    <artifactId>case-string</artifactId>
    <version>0.0.3</version>
</dependency>
```

or your build.gradle if using Gradle
```
implementation group: 'io.github.renatols-jf', name: 'case-string', version: '0.0.3'
```

To create a [CaseString](https://github.com/renatols-jf/case-string/blob/master/src/main/java/io/github/renatolsjf/utils/string/casestring/CaseString.java)
, simply call `CaseString.parse("aString")`, replacing `aString` for the actual desired value. This will create
a `CaseString` object, which will give you options between value retrieval and comparisons. While you should read
the javadoc for better understanding the available behavior, a few remarks are made here:
- `isCaseRepresentation(String s)`: Checks if a String s is a case representation of the parsed String. This will
  only return true if the String s is equal to one of the case outputs for the parsed String. This means that
  `CaseString.parse("This is a string").isCaseRepresentation("This is a string")` will return `false`, while
  `CaseString.parse("This is a string").isCaseRepresentation("this_is_a_string")` will return `true`.
- Direct comparison can be made in 3 distinct ways:
  - `equals(Object o)`: The default Java comparison mechanism.  It will return true if, and only if, the Strings parsed
    are exactly the same for two given `CaseString`. While a `CaseString` created by parsing the String `This is a String`
    and one created by parsing the String `this is a string` will produce the exact same case outputs, equals will
    return `false`.
  - `equalsIgnoreInput(CaseString caseString)`: Unlike equals, the parsed string is not taken into account, and this
    method will return true as long as 2 given `CaseString` produce the same case outputs.
  - `equalsString(String s)`: This will compare a `CaseString` directly to a java `String`, without the need to manually
    parse the String s. This will return true if the String s produces the same case outputs should it be parsed.
    `CaseString.parse("this-is-a-string").equalsString("This---is_aString")` will return true.
- All case outputs are available by calling `getCaseValues()`, or to get a specific case, just call its name,
  such as `CaseString.parse("A String").kebab()`
- If you only need to convert a String to a case, you don't need to get a reference to a `CaseString`, you can use
  static methods to do so, such as `CaseString.toKebabCase("This Is A String")`, which will return `this-is-a-string`.
- If you wish to extract a value from a Map but are unsure which case the to look up, you can use `createMapExtractor()`,
  as in `CaseString.parse("aString").createMapExtractor()`. This is useful for lookups in tools in which the user provides
  the config name. For example, in yaml, it's common to find projects which specify the properties in camelCase, kebab case,
  or snake case. This creates an issue while parsing the data. `createMapExtractor` will create a 
  [ValueExtractor](https://github.com/renatols-jf/case-string/blob/master/src/main/java/io/github/renatolsjf/utils/string/casestring/ValueExtractor.java)
  that looks into the map for all possible case transformations. Note that, if more than one case representation is a key in the map,
  such as `aKey`, and `a-key`, the value returned will correspond to the first key found. In the future, it will be possible to extract
  value from POJOs as well. Here's an example of extraction:

```
Map<String, String> map = new HashMap<>();
map.put("a-key", "a-value");
map.put("anotherKey, "anotherValue");
map.put("nullKey", null);
map.put("LastKey", "LastValue");

CaseString.parse("a key").createMapExtractor().extractValue(map, false); //This will return "a-value", and the expected return of the method is Object.
CaseString.parse("AnotherKey").createMapExtractor(String.class).extractValue(map, false); //This will return "anotherValue", and the expected return of the method is String
CaseString.parse("NoKey").createMapExtractor().extractValue(map, false); //This will return null, as no key is found.
CaseString.parse("NoKey").createMapExtractor().extractValueOrThrowException(map, false); //This will throw UnavailableKeyException, as no key is found.
CaseString.parse("null-key").createMapExtractor().extractValueOrThrowException(map, false); //This will return null, as the key is found, it just so happens its value is null;
CaseString.parse("LastKey").createMapExtractor().extractValue(map, false); //This will return null, since only the case representations will be used to lookup the map and not the original input.
CaseString.parse("LastKey").createMapExtractor().extractValue(map, true); //This will return "LastValue" as the original input will be also used to do the lookup.
```