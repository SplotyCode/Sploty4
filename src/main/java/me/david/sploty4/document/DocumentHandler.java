package me.david.sploty4.document;

import me.david.sploty4.document.archive.ZipArchiveDocument;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.document.text.RawText;
import me.david.sploty4.io.Connection;
import me.david.sploty4.util.obj.Pair;

import java.util.HashMap;

public class DocumentHandler {

    private HashMap<String, Pair<Byte[], Long>> ramCache = new HashMap<>();
    private HashMap<String, Long> failed = new HashMap<>();

    private HashMap<String, Document> types = new HashMap<>();

    public DocumentHandler(){
        ZipArchiveDocument zipArchiveDocument = new ZipArchiveDocument();
        types.put("application/zip", zipArchiveDocument);
        types.put("application/java-archive", zipArchiveDocument);
        types.put("application/gzip", zipArchiveDocument);

        RawText rawText = new RawText();
        types.put("text/plain", rawText);
        types.put("text/css", rawText);
        types.put("application/javascript", rawText);
        types.put("application/ecmascript", rawText);

        HtmlDocument htmlDocument = new HtmlDocument();
        types.put("text/html", htmlDocument);
    }

    public Document handleFile(Connection connection){
        Document document = types.get(connection.getContentType());
        if(document != null){
            try {
                return document.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new DownloadDocument();
    }


    public HashMap<String, Pair<Byte[], Long>> getRamCache() {
        return ramCache;
    }

    public HashMap<String, Long> getFailed() {
        return failed;
    }
}
