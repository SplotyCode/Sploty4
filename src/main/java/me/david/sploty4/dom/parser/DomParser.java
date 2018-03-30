package me.david.sploty4.dom.parser;

import java.util.List;

public interface DomParser<O, I> {

    O parse(I input);
    int getIndex();

    DomReader[] getReaders();
    List<DomReader> getActivReaders();
    void disableReader(DomReader reader);

    void skip(int i);
    void skip();
    boolean skipIfFollow(String next);

    void stopCurrent();

    void setLocked(DomReader reader);
    boolean isLocked();
    DomReader getLocked();

}
