package me.david.sploty4.document;

public class SyntaxException extends RuntimeException {

    public SyntaxException() {
    }

    public SyntaxException(String s) {
        super(s);
    }

    public SyntaxException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SyntaxException(Throwable throwable) {
        super(throwable);
    }

    public SyntaxException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
