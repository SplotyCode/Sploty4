package me.david.sploty4.dom.parser;

import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.parser.htmlreader.CommentReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomHtmlParser implements DomParser<Node, String>  {

    private String content;
    private DomReader<DomHtmlParser> locked;
    private int index;
    private List<DomReader<DomHtmlParser>> disaledReaders = new ArrayList<>();
    private boolean skipThis;
    private Node base;

    public DomHtmlParser(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Node parse(String input) {
        for(char c : content.toCharArray()){
            if(isLocked()) getLocked().readNext(c, this);
            else for(DomReader reader : getActivReaders()) {
                reader.readNext(c, this);
                if(skipThis) {
                    skipThis = false;
                    break;
                }
            }
            index++;
        }
        return base;
    }

    public Node getBaseNode() {
        return base;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public DomReader[] getReaders() {
        return new DomReader[]{new CommentReader()};
    }

    @Override
    public List<DomReader> getActivReaders() {
        List<DomReader> readers = new ArrayList<>(Arrays.asList(getReaders()));
        readers.removeAll(disaledReaders);
        return readers;
    }

    @Override
    public void disableReader(DomReader reader) {
        disaledReaders.add(reader);
    }

    @Override
    public void skip(int i) {
        index += i;
    }

    @Override
    public void skip() {
        index++;
    }

    @Override
    public boolean skipIfFollow(String next) {
        if(index+next.length() > content.length()) return false;
        for(int i = 0;i < next.length();i++)
            if (content.charAt(i + index) != next.charAt(i)) return false;
        index += next.length();
        return true;
    }

    @Override
    public void stopCurrent() {
        skipThis = true;
    }

    @Override
    public void setLocked(DomReader reader) {
        locked = reader;
    }

    @Override
    public boolean isLocked() {
        return locked != null;
    }

    @Override
    public DomReader getLocked() {
        return locked;
    }
}
