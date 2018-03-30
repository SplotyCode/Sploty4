package me.david.sploty4.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class MimeUtil {

    private static HashMap<String, String> map = new HashMap<>();

    static {
        map.clear();
        //Text
        map.put("txt", "text/plain");
        map.put("css", "text/css");
        map.put("csv", "text/csv");
        map.put("html", "text/html");
        map.put("htm", "text/html");
        map.put("ics", "text/calendar");
        map.put("xhtml", "application/xhtml+xml");
        map.put("xml", "application/xml");

        //Audio
        map.put("aac", "audio/aac");
        map.put("mid", "audio/midi");
        map.put("midi", "audio/midi");
        map.put("oga", "audio/ogg");
        map.put("wav", "audio/x-wav");

        //Video
        map.put("mpeg", "video/mpeg");
        map.put("ogv", "video/ogg");
        map.put("webm", "video/webm");

        //Images
        map.put("gif", "image/gif");
        map.put("ico", "image/x-icon");
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("png", "image/png");
        map.put("svg", "image/svg+xml");
        map.put("tif", "image/tiff");
        map.put("tiff", "image/tiff");
        map.put("webp", "image/webp");

        //Fonts
        map.put("otf", "font/otf");
        map.put("woff", "font/woff");
        map.put("woff2", "font/woff2");
        map.put("ttf", "font/ttf");

        //Scripts
        map.put("es", "application/ecmascript");
        map.put("js", "application/javascript");
        map.put("json", "application/json");

        //Archives
        map.put("7z", "application/x-7z-compressed");
        map.put("zip", "application/zip");
        map.put("rar", "application/x-rar-compressed");
        map.put("gz", "application/gzip");
        map.put("zip", "application/java-archive");

        //Other
        map.put("pdf", "application/pdf");
    }

    public static String getMimeByExtension(String extension){
        String mime = map.get(extension);
        if(mime == null) return "application/octet-stream";
        return mime;
    }

    public static String getMimeByFile(File file){
        return getMimeByExtension(FilenameUtils.getExtension(file.getName()));
    }

    public static String getExtensionByMime(String mime){
        for(Map.Entry<String, String> entry : map.entrySet())
            if(entry.getValue().equals(mime)){
                return entry.getKey();
            }
        return "dat";
    }
}
