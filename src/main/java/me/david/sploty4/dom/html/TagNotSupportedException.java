package me.david.sploty4.dom.html;

public class TagNotSupportedException extends RuntimeException {
    public TagNotSupportedException() {
    }

    public TagNotSupportedException(String s) {
        super(s);
    }

    public TagNotSupportedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TagNotSupportedException(Throwable throwable) {
        super(throwable);
    }

    public TagNotSupportedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
