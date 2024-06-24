package io.github.renatolsjf.utils.string.casestring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class MapValueExtractor<T> extends ValueExtractor<T, Map> {

    protected MapValueExtractor(CaseString caseString) {
        super(caseString);
    }

    @Override
    public T extractValue(Map source, boolean matchOriginalInput) {
        try {
            return this.extractValueOrThrowException(source, matchOriginalInput);
        } catch (UnavailableKeyException ex) {
            return null;
        }
    }

    @Override
    public T extractValueOrThrowException(Map source, boolean matchOriginalInput) throws UnavailableKeyException {

        List<String> parsedValues =  new ArrayList<>();
        parsedValues.addAll(this.caseString.getCaseValues());
        if (matchOriginalInput) {
            parsedValues.add(this.caseString.original());
        }

        return (T) parsedValues.stream()
                .filter(s -> source.containsKey(s))
                .map(s -> source.get(s))
                .findFirst()
                .orElseThrow(() -> new UnavailableKeyException());

    }

}
