package io.github.renatolsjf.utils.string.casestring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a String in various different CaseTypes
 */
public class CaseString {

    /**
     * The available CaseTypes
     * Kebab uses hyphen as separation, as in this-is-a-kebak-case
     * Snake uses underscore as separation, as in this_is_a_snake_case
     * Camel starts with a lowercase and each new word has its first letter in upper case, as in thisIsACamelCase
     * Pascal starts with an upper case and each new word has its first letter in upper case, as in ThisIsAPascalCase
     * Spaced is not really a case, but it exists to match space separated words to create the other cases.
     * It's separated by spaces, as in this is a spaced case.
     */
    public enum CaseType {
        KEBAB,
        SNAKE,
        CAMEL,
        PASCAL,
        SPACED
    }

    private List<CaseBuffer> buffers = new ArrayList<>();
    private String input;

    private CaseString() {
        this.buffers.add(new KebabCaseBuffer());
        this.buffers.add(new SnakeCaseBuffer());
        this.buffers.add(new CamelCaseBuffer());
        this.buffers.add(new PascalCaseBuffer());
        this.buffers.add(new SpacedCaseBuffer());
    }

    /**
     * Verifies if a given String matches any available case. This is a comparison between the given
     * String directly to the available cases. Basically, it will return true if the given String
     * is equal to any case representation of the String parsed in this CaseString.
     * If you wish to compare if a given String will create, after parsed,
     * cases that are equal to the cases created by this CaseString, use equalsString.
     * instead.
     * @param s The String to be matched against the parsed String
     * @return A boolean indicating whether a match was found or not
     */
    public boolean isCaseRepresentation (String s) {
        return this.getCaseValues().contains(s);
    }

    /**
     * Gets a list of Strings containing the parsed String variations for each available CaseType
     * @return A List of Strings with the variations for each available CaseType
     */
    public List<String> getCaseValues() {
        return this.buffers.stream()
                .map(cs -> cs.toString())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets the String corresponding for the given CaseType
     * @param type The desired CaseType
     * @return A String corresponding to the give CaseType
     */
    public String getCaseValue(CaseType type) {
        return this.buffers.stream()
                .filter(cs -> cs.isOfCaseType(type))
                .map(cs -> cs.toString())
                .findFirst()
                .orElse(null);
    }

    /**
     * The original input associated with this CaseString
     * @return The original String used to create this CaseString
     */
    public String original() {
        return this.input;
    }

    /**
     * Gets the String corresponding to the kebab case
     * @return A String parsed by the CaseType.KEBAB
     */
    public String kebab() {
        return this.getCaseValue(CaseType.KEBAB);
    }

    /**
     * Gets the String corresponding to the snake case
     * @return A String parsed by the CaseType.SNAKE
     */
    public String snake() {
        return this.getCaseValue(CaseType.SNAKE);
    }


    /**
     * Gets the String corresponding to the camel case
     * @return A String parsed by the CaseType.CAMEL
     */
    public String camel() {
        return this.getCaseValue(CaseType.CAMEL);
    }

    /**
     * Gets the String corresponding to the pascal case
     * @return A String parsed by the CaseType.PASCAL
     */
    public String pascal() {
        return this.getCaseValue(CaseType.PASCAL);
    }

    /**
     * Gets the String corresponding to the spaced case
     * @return A String parsed by the CaseType.SPACED
     */
    public String spaced() {
        return this.getCaseValue(CaseType.SPACED);
    }

    /**
     * Creates a ValueExtractor that extracts values from a Map
     * @param valueType The class representing the expected type to be extracted
     * @return The ValueExtractor
     * @param <T> The expected type to be extracted
     */
    public <T> ValueExtractor<T, Map> createMapExtractor(Class<T> valueType) {
        return new MapValueExtractor<>(this);
    }

    /**
     * Creates a ValueExtractor that extracts values from a Map. The expected extracted value type is an Object
     * @return The ValueExtractor
     */
    public ValueExtractor<Object, Map> createMapExtractor() {
        return new MapValueExtractor<>(this);
    }

    /**
     * Matches the generated Strings for the cases irrespective of the String used to create both CaseStrings.
     * E.g: The cases for the input "aString-to_parse" would match the cases for the input "a string to parse"
     * @param other The CaseString to be matched against
     * @return A boolean representing case string values generated by this CaseString equals the case string values
     * generated by other
     */
    public boolean equalsIgnoreInput(CaseString other) {
        if (other == null) return false;
        return this.getCaseValues().containsAll(other.getCaseValues());
    }

    /**
     * A given String will be deemed as equal to this CaseString if, after being parsed, the generated Strings for the cases
     * are equivalent as the ones generated by this CaseString
     * @param s A String to be compared with this CaseString
     * @return A boolean representing if the generated cases for the given String are the same as the ones in this CaseString
     */
    public boolean equalsString(String s) {
        if (s == null) return false;
        return this.equalsIgnoreInput(CaseString.parse(s));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaseString other = (CaseString) o;
        return Objects.equals(input, other.input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input);
    }

    /**
     * Creates a CaseString using a String as input
     * @param s A String to be parsed and create the CaseString
     * @return A CaseString containing representations for the available cases for the input String
     */
    public static CaseString parse(String s) {

        if (s == null) {
            throw new NullPointerException();
        }

        CaseString cs = new CaseString();
        cs.input = s;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            cs.buffers.forEach(b -> b.next(c));
        }

        return cs;

    }

    /**
     * Parses the given String directly to a CaseType
     * @param caseType The CaseType which will be used to parse the given String
     * @param s A String to be parsed
     * @return A String parsed by the given CaseType
     */
    public static String getValue(CaseType caseType, String s) {
        CaseString cs = parse(s);
        return cs.getCaseValue(caseType);
    }

    /**
     * Parses the given String directly to kebab case
     * @param s A String to be parsed
     * @return A String in the kebab case
     */
    public static String toKebabCase(String s) {
        return CaseString.parse(s).kebab();
    }

    /**
     * Parses the given String directly to snake case
     * @param s A String to be parsed
     * @return A String in the snake case
     */
    public static String toSnakeCase(String s) {
        return CaseString.parse(s).snake();
    }

    /**
     * Parses the given String directly to camel case
     * @param s A String to be parsed
     * @return A String in the camel case
     */
    public static String toCamelCase(String s) {
        return CaseString.parse(s).camel();
    }

    /**
     * Parses the given String directly to pascal case
     * @param s A String to be parsed
     * @return A String in the pascal case
     */
    public static String toPascalCase(String s) {
        return CaseString.parse(s).pascal();
    }

    /**
     * Parses the given String directly to spaced case
     * @param s A String to be parsed
     * @return A String in the spaced case
     */
    public static String toSpacedCase(String s) {
        return CaseString.parse(s).spaced();
    }

}

abstract class CaseBuffer {

    protected StringBuffer valueBuffer = new StringBuffer();
    protected List<Character> supportedDelimiters = new ArrayList<>();
    protected boolean lastCharWasDelimiter = false;
    protected CaseString.CaseType type;

    CaseBuffer(CaseString.CaseType type) {
        this.type = type;
        this.supportedDelimiters.add('-');
        this.supportedDelimiters.add('_');
        this.supportedDelimiters.add(' ');
    }

    void next(char c) {
        if (this.supportedDelimiters.contains(c)) {
            if (this.valueBuffer.isEmpty() || this.lastCharWasDelimiter) {
                return;
            } else {
                this.lastCharWasDelimiter = true;
            }
        } else if(Character.isUpperCase(c)) {
            this.appendUpperCase(c);
            this.lastCharWasDelimiter = false;
        } else {
            this.appendLowerCase(c);
            this.lastCharWasDelimiter = false;
        }
    }

    abstract void appendUpperCase(char c);
    abstract void appendLowerCase(char c);

    String bufferValue() {
        return this.valueBuffer.toString();
    }

    @Override
    public String toString() {
        return this.bufferValue();
    }

    public boolean isOfCaseType(CaseString.CaseType type) {
        return this.type.equals(type);
    }

}

abstract class SeparatorCaseBuffer extends CaseBuffer {

    protected String selectedDelimiter;

    SeparatorCaseBuffer(CaseString.CaseType type, String selectedDelimiter) {
        super(type);
        this.selectedDelimiter = selectedDelimiter;
    }

    @Override
    void appendUpperCase(char c) {
        if(!(this.valueBuffer.isEmpty()) && this.selectedDelimiter != null) {
            this.valueBuffer.append(this.selectedDelimiter);
        }
        this.valueBuffer.append(Character.toLowerCase(c));
    }

    @Override
    void appendLowerCase(char c) {
        if (this.lastCharWasDelimiter && this.selectedDelimiter != null) {
            this.valueBuffer.append(this.selectedDelimiter);
        }
        this.valueBuffer.append(c);
    }

}

class KebabCaseBuffer extends SeparatorCaseBuffer {
    KebabCaseBuffer() {
        super(CaseString.CaseType.KEBAB, "-");
    }
}

class SnakeCaseBuffer extends SeparatorCaseBuffer {
    SnakeCaseBuffer() {
        super(CaseString.CaseType.SNAKE, "_");
    }
}

class SpacedCaseBuffer extends SeparatorCaseBuffer {
    SpacedCaseBuffer() {super(CaseString.CaseType.SPACED, " ");}
}

class CamelCaseBuffer extends CaseBuffer {

    CamelCaseBuffer() {
        super(CaseString.CaseType.CAMEL);
    }

    @Override
    void appendUpperCase(char c) {
        if(!(this.valueBuffer.isEmpty())) {
            this.valueBuffer.append(c);
        } else {
            this.valueBuffer.append(Character.toLowerCase(c));
        }

    }

    @Override
    void appendLowerCase(char c) {
        if (this.lastCharWasDelimiter) {
            this.valueBuffer.append(Character.toUpperCase(c));
        } else {
            this.valueBuffer.append(c);
        }
    }

}

class PascalCaseBuffer extends CaseBuffer {

    PascalCaseBuffer() {
        super(CaseString.CaseType.PASCAL);
    }

    @Override
    void appendUpperCase(char c) {
        this.valueBuffer.append(c);
    }

    @Override
    void appendLowerCase(char c) {
        if (this.lastCharWasDelimiter || this.valueBuffer.isEmpty()) {
            this.valueBuffer.append(Character.toUpperCase(c));
        } else {
            this.valueBuffer.append(c);
        }
    }

}
