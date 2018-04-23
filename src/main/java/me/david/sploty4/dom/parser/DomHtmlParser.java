package me.david.sploty4.dom.parser;

import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.Sploty;
import me.david.sploty4.dom.DomErrorReporter;
import me.david.sploty4.dom.Node;
import me.david.sploty4.dom.nodes.BaseNode;
import me.david.sploty4.dom.nodes.DocTypeNode;
import me.david.sploty4.dom.parser.htmlreader.CommentReader;
import me.david.sploty4.dom.parser.htmlreader.DocTypeReader;
import me.david.sploty4.dom.parser.htmlreader.MainHtmlReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomHtmlParser implements DomParser<Node, String, DomHtmlParser>  {

    @Getter private String content;
    private DomReader<DomHtmlParser> locked;
    @Getter @Setter private List<DomReader<DomHtmlParser>> disabledReaders = new ArrayList<>();

    @Setter private int index = 0, line = 1;
    @Getter @Setter private boolean skipThis = false, rehandle = false;
    @Getter private Node base = new BaseNode(), currentParent = base;
    @Getter @Setter private DomErrorReporter errorReporter = null;

    @Override
    public Node parse(String input) {
        content = input;
        if(errorReporter == null){
            errorReporter = new DomErrorReporter();
            Sploty.getLogger().info("[INFO] No Error Reporter was set! Need to create one for this parse!");
        }
        while (index < content.length()){
            char c = content.charAt(index);
            if(isLocked()) getLocked().readNext(c, this);
            else for(DomReader<DomHtmlParser> reader : getActivReaders()) {
                try {
                    reader.readNext(c, this);
                }catch (Throwable throwable){
                    Sploty.getLogger().exception(throwable, "Exception was thrown (Index: " + index + " Line: " + line + "): ");
                    errorReporter.report(throwable);
                }
                if(skipThis) {
                    skipThis = false;
                    break;
                }
            }
            if(rehandle) rehandle = false;
            else {
                if(c == '\n') line++;
                index++;
            }
        }
        for(DomReader<DomHtmlParser> reader : readers)
            reader.parseDone();
        return base;
    }

    public void setCurrentParent(Node currentParent) {
        Sploty.getLogger().debug("Set Parent: " + currentParent.getName());
        this.currentParent = currentParent;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public char getChar() {
        return content.charAt(index);
    }

    @Override
    public char getChar(int next) {
        return content.charAt(index+next);
    }

    @Override
    public void rehandle() {
        rehandle = true;
    }

    private DomReader<DomHtmlParser>[] readers = new DomReader[]{new CommentReader(), new DocTypeReader(), new MainHtmlReader()};
    @Override
    public DomReader<DomHtmlParser>[] getReaders() {
        return readers;
    }

    @Override
    public List<DomReader<DomHtmlParser>> getActivReaders() {
        List<DomReader<DomHtmlParser>> readers = new ArrayList<>(Arrays.asList(getReaders()));
        readers.removeAll(disabledReaders);
        return readers;
    }

    @Override
    public void disableReader(DomReader<DomHtmlParser> reader) {
        disabledReaders.add(reader);
    }

    @Override
    public void skip(int i) {
        line += content.substring(index, index + i).split("\r\n|\r|\n").length;
        index += i;
    }

    @Override
    public void skip() {
        skip(1);
    }

    @Override
    public boolean skipIfFollow(String next) {
        if(index+next.length() > content.length()) return false;
        for(int i = 0;i < next.length();i++)
            if (content.charAt(i + index) != next.charAt(i)) return false;
        line += content.substring(index, index + next.length() - 1).split("\r\n|\r|\n").length;
        index += next.length()-1;
        return true;
    }

    @Override
    public boolean skipIfFollowIgnoreCase(String text) {
        if(index+text.length() > content.length()) return false;
        for(int i = 0;i < text.length();i++)
            if (Character.toLowerCase(content.charAt(i + index)) != text.charAt(i)) return false;
        line += content.substring(index, index + text.length() - 1).split("\r\n|\r|\n").length;
        index += text.length()-1;
        return true;
    }

    @Override
    public void stopCurrent() {
        skipThis = true;
    }

    @Override
    public void setLocked(DomReader<DomHtmlParser> reader) {
        locked = reader;
    }

    @Override
    public boolean isLocked() {
        return locked != null;
    }

    @Override
    public DomReader<DomHtmlParser> getLocked() {
        return locked;
    }
}
