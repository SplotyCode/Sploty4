package me.david.sploty4.dom.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ErrorEntry implements Cloneable {

    @Getter @Setter private String message;
    @Getter @Setter private ErrorType type;

    @Override
    public ErrorEntry clone() {
        try {
            return (ErrorEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
