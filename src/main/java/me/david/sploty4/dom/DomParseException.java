package me.david.sploty4.dom;

import me.david.sploty4.document.SyntaxException;

public class DomParseException extends SyntaxException {

    public DomParseException() {
    }

    public DomParseException(String s) {
        super(s);
    }

    public DomParseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DomParseException(Throwable throwable) {
        super(throwable);
    }

    public DomParseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
