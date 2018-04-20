package me.david.sploty4.dom.parser;

import java.util.List;

public interface DomParser<O, I, R extends DomParser> {

    O parse(I input);
    int getIndex();
    char getChar();
    char getChar(int next);
    void rehandle();

    DomReader<R>[] getReaders();
    List<DomReader<R>> getActivReaders();
    void disableReader(DomReader<R> reader);

    void skip(int i);
    void skip();
    boolean skipIfFollow(String next);
    boolean skipIfFollowIgnoreCase(String text);

    void stopCurrent();

    void setLocked(DomReader<R> reader);
    boolean isLocked();
    DomReader<R> getLocked();

}
