package me.david.sploty4.util;

public enum AlmostBoolean {

    YES,
    NO,
    MAYBE;

    public static final AlmostBoolean fromBoolean(final boolean value) {
        return value ? YES : NO;
    }

    public boolean decide(boolean optimistically) {
        return optimistically?this != NO:this == YES;
    }

}