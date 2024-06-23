package io.github.renatolsjf.utils.string.casestring;

/**
 * Extracts a value corresponding to a given CaseString from a given source
 * @param <T> The expected value type
 * @param <U> The expected source type
 */
public abstract class ValueExtractor<T, U> {

    /**
     * The CaseString to be used to match the key to which the value will be extracted
     */
    protected CaseString caseString;

    protected ValueExtractor(CaseString caseString) {
        this.caseString = caseString;
    }

    /**
     * Extracts the value for the CaseString from the given source.
     * @param source The source in which the key will be looked from
     * @param matchOriginalInput Whether the original input should be used to match a key or only the parsed cases
     * @return A value for the given key or null, if no such key is found
     */
    public abstract  T extractValue(U source, boolean matchOriginalInput);

    /**
     * Extracts the value for the CaseString from the given source.
     * @param source The source in which the key will be looked from
     * @param matchOriginalInput Whether the original input should be used to match a key or only the parsed cases
     * @return A value for the given key
     * @throws UnavailableKeyException if no such key represented by the CaseString is found
     */
    public abstract T extractValueOrThrowException(U source, boolean matchOriginalInput) throws UnavailableKeyException;

}
