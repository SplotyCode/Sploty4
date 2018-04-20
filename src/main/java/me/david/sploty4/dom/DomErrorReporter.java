package me.david.sploty4.dom;

import me.david.sploty4.document.SyntaxException;
import me.david.sploty4.dom.error.ErrorEntry;
import me.david.sploty4.dom.error.ErrorType;
import me.david.sploty4.dom.error.StackErrorEntry;
import me.david.sploty4.objects.listener.ListenerHandler;
import me.david.sploty4.objects.listener.SimpleListener;

import java.util.ArrayList;
import java.util.List;

public class DomErrorReporter extends ListenerHandler<SimpleListener<ErrorEntry>> {

    private List<ErrorEntry> errors = new ArrayList<>();


    public void report(ErrorType type, String message, Throwable throwable){
        report(new StackErrorEntry(message, type, throwable));
    }

    public void report(ErrorEntry entry, Throwable throwable){
        if(entry instanceof StackErrorEntry){
            ((StackErrorEntry) entry).setThrowable(throwable);
            report(entry);
        } else report(new StackErrorEntry(entry.getMessage(), entry.getType(), throwable));
    }

    public void report(Throwable throwable){
        report(new StackErrorEntry(throwable.getMessage(), throwable instanceof SyntaxException?ErrorType.WARNING:ErrorType.ERROR, throwable));
    }

    public void report(ErrorType type, String message){
        report(new ErrorEntry(message, type));
    }

    public void report(ErrorEntry entry){
        errors.add(entry);
        call(listener -> listener.event(entry));
    }

    public List<ErrorEntry> getErrors() {
        return errors;
    }
}
