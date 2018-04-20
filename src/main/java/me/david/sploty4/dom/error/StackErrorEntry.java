package me.david.sploty4.dom.error;

import lombok.Getter;
import lombok.Setter;

public class StackErrorEntry extends ErrorEntry implements Cloneable {

    @Getter @Setter private Throwable throwable;

    public StackErrorEntry(String message, ErrorType type, Throwable throwable) {
        super(message, type);
        this.throwable = throwable;
    }

    @Override
    public StackErrorEntry clone() {
        return (StackErrorEntry) super.clone();
    }
}
