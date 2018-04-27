package me.david.sploty4.document;

import me.david.sploty4.Sploty;
import me.david.sploty4.document.archive.ZipArchiveDocument;
import me.david.sploty4.document.other.DownloadDocument;
import me.david.sploty4.document.text.HtmlDocument;
import me.david.sploty4.document.text.RawTextDocument;
import me.david.sploty4.io.Connection;
import me.david.sploty4.objects.Pair;

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

        RawTextDocument rawText = new RawTextDocument();
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
                Sploty.getLogger().exception(e, "Failed creating document Instance");
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
