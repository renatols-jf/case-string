package io.github.renatolsjf.utils.string.casestring;

public class UnavailableKeyException extends Exception {

    public UnavailableKeyException() {

    }

    public UnavailableKeyException(final String message) {
        super(message);
    }

    public UnavailableKeyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnavailableKeyException(final Throwable cause) {
        super(cause);
    }

}
