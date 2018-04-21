package me.david.sploty4.io;

import me.david.sploty4.Sploty;
import me.david.sploty4.constants.AppConstants;
import me.david.sploty4.setting.CacheState;
import me.david.sploty4.setting.settings.CacheSettings;
import me.david.sploty4.setting.settings.ConnectionSettings;
import me.david.sploty4.setting.settings.DiskCacheSettings;
import me.david.sploty4.setting.settings.RamCacheSettings;
import me.david.sploty4.storage.SQLite;
import me.david.sploty4.util.FileUtil;
import me.david.sploty4.util.MimeUtil;
import me.david.sploty4.util.StringUtil;
import me.david.sploty4.util.obj.Pair;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Connection {

    private URLConnection connection;
    private InputStream inputStream;
    private String encoding, contentType;
    private String charset = "UTF-8";
    private URL url;
    private boolean local;
    private File file;
    private long length;
    private CacheSettings.CacheSetting ramCacheSetting;
    private CacheSettings.CacheSetting diskCacheSetting;
    private int error;

    public Connection(URL url){
        this.url = url;
        local = false;
    }

    public Connection(File file){
        this.file = file;
        local = true;
    }

    public void connect(){
        if(local){
            try {
                inputStream = new FileInputStream(file);
                contentType = MimeUtil.getMimeByFile(file);
                length = file.length();
                error = 200;
            } catch (FileNotFoundException e) {
                error = 404;
            }
        }else {
            try {
                DiskCacheSettings disk = (DiskCacheSettings) Sploty.getSettingManager().getSettingByClass(DiskCacheSettings.class);
                RamCacheSettings ram = (RamCacheSettings) Sploty.getSettingManager().getSettingByClass(RamCacheSettings.class);
                ConnectionSettings conSet = (ConnectionSettings) Sploty.getSettingManager().getSettingByClass(ConnectionSettings.class);
                int timeout;
                switch (FilenameUtils.getExtension(url.toExternalForm())){
                    case "css":
                        ramCacheSetting = ram.getCss();
                        diskCacheSetting = disk.getCss();
                        timeout = (int) conSet.getTimeOutCss();
                        break;
                    case "gif": case "ico": case "jpeg": case "jpg": case "png": case "svg": case "tif": case "tiff": case "webp":
                        ramCacheSetting = ram.getImage();
                        diskCacheSetting = disk.getImage();
                        timeout = (int) conSet.getTimeOutImage();
                        break;
                    case "js":
                        ramCacheSetting = ram.getJavascript();
                        diskCacheSetting = disk.getJavascript();
                        timeout = (int) conSet.getTimeOutJavascript();
                        break;
                    case "html": case "htm":
                        ramCacheSetting = ram.getHtml();
                        diskCacheSetting = disk.getHtml();
                        timeout = (int) conSet.getTimeoutHtml();
                        break;
                    default:
                        ramCacheSetting = ram.getOther();
                        diskCacheSetting = disk.getOther();
                        timeout = (int) conSet.getTimeOutOther();
                        break;
                }
                if(loadDirectly() || checkFailed()) {
                    error = -4;
                    return;
                }
                connection = url.openConnection(conSet.getCurrentProxy());
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.addRequestProperty("User-Agent", "Sploty/" + AppConstants.VERSION);
                HttpURLConnection.setFollowRedirects(true);

                SQLite.SiteData data = SQLite.INSTANCE.getSiteData(url);
                if(data != null && diskCacheSetting.getWhen() == CacheState.MODIFIED) {
                    if(!StringUtil.isEmpty(data.getEtag())) connection.addRequestProperty("If-None-Match", data.getEtag());
                    if(!StringUtil.isEmpty(data.getLastModified())) connection.addRequestProperty("If-Modified-Since", data.getLastModified());
                }
                connection.connect();
                error = ((HttpURLConnection) connection).getResponseCode();
                if(error != 200 && error != 304) return;
                encoding = connection.getContentEncoding();
                contentType = connection.getContentType().replaceAll(" ", "");
                String[] split = contentType.split(";");
                contentType = split[0];
                if(split.length == 2)
                    for(String key : split){
                        if(key.startsWith("charset=")) charset = key.substring(8);
                        else contentType = key;
                    }
                if (((HttpURLConnection)connection).getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    inputStream = loadFromCache();
                    ((HttpURLConnection) connection).disconnect();
                    return;
                }
                String etag = connection.getHeaderField("ETag");
                String lastModified = connection.getHeaderField("Last-Modified");
                if(etag == null) etag = "";
                boolean needSave = true;
                if(lastModified == null) {
                    lastModified = "";
                    needSave = false;
                }
                SQLite.INSTANCE.updateSite(url, etag, lastModified);

                if (encoding != null && encoding.equalsIgnoreCase("gzip")) inputStream = new GZIPInputStream(connection.getInputStream());
                else if (encoding != null && encoding.equalsIgnoreCase("deflate")) inputStream = new InflaterInputStream(connection.getInputStream(), new Inflater(true));
                else inputStream = connection.getInputStream();
                length = connection.getContentLength();
                if(needSave && diskCacheSetting.getWhen() == CacheState.MODIFIED) saveToCache();
                if(ramCacheSetting.getWhen() == CacheState.ALWAYS)
                    Sploty.getInstance().getDocumentHandler().getRamCache().put(url.getHost() + "/" + url.getPort() + "/" + url.getPath(), new Pair<>(ArrayUtils.toObject(IOUtils.toByteArray(inputStream)), System.currentTimeMillis()));
            } catch (IOException e) {
                if(e instanceof UnknownHostException) error = -2;
                else if(e instanceof SSLHandshakeException) {
                    if(e.getMessage().contains("timestamp check failed")) error = -100;
                    else if(e.getMessage().contains("No subject alternative DNS name matching")) error = -101;
                    else if(e.getMessage().contains("unable to find valid certification path to requested target")) error = -102;
                    else if(e.getMessage().contains("Certificate has been revoked")) error = -103;
                    else if(e.getMessage().contains("DHPublicKey does not comply to algorithm constraints")) error = -105;
                    else if(e.getMessage().contains("handshake_failure")) error = -106;
                    else error = -199;
                }else if(e instanceof SSLException) {
                    if(e.getMessage().contains("Could not generate DH keypair")) error = -104;
                    else error = -198;
                } else error = -3;
                Sploty.getInstance().getDocumentHandler().getFailed().put(url.getHost() + "/" + url.getPort() + "/" + url.getPath(), System.currentTimeMillis());
                SQLite.INSTANCE.saveFailed(url.getHost() + "/" + url.getPort() + "/" + url.getPath());
                e.printStackTrace();
            }
        }
    }

    private boolean checkFailed(){
        Long ram = Sploty.getInstance().getDocumentHandler().getFailed().get(url.getHost() + "/" + url.getPort() + "/" + url.getPath());
        if(ram != null && ramCacheSetting.isCacheFailed() &&  System.currentTimeMillis()-ram < ramCacheSetting.getFailedCacheTime().getValue())
            return true;
        long failed = SQLite.INSTANCE.getFailed(url.getHost() + "/" + url.getPort() + "/" + url.getPath());
        return failed != Long.MIN_VALUE && diskCacheSetting.isCacheFailed() && System.currentTimeMillis() - failed < diskCacheSetting.getFailedCacheTime().getValue();
    }

    private void saveToCache(){
        File file = new File(Sploty.getDirectory(), "cache/" + url.getHost() + "/" + url.getPort() + "/" + url.getPath().replace('/', '|'));
        try {
            FileUtil.toFile(inputStream, file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadDirectly() throws IOException {
        //File Cache
        File file = new File(Sploty.getDirectory(), "cache/" + url.getHost() + "/" + url.getPort() + "/" + url.getPath().replace('/', '|'));
        if(file.exists() && diskCacheSetting.isCache() && diskCacheSetting.getWhen() == CacheState.ALWAYS){
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            long delay = System.currentTimeMillis()-attr.creationTime().toMillis();
            if(delay < diskCacheSetting.getCacheTime().getValue()) {
                inputStream = new FileInputStream(file);
                return true;
            }
        }
        //Ram Cache
        Pair<Byte[], Long> pair = Sploty.getInstance().getDocumentHandler().getRamCache().get(url.getHost() + "/" + url.getPort() + "/" + url.getPath());
        if(pair != null && ramCacheSetting.isCache() && ramCacheSetting.getWhen() == CacheState.ALWAYS){
            long delay = System.currentTimeMillis()-pair.getTwo();
            if(delay < ramCacheSetting.getCacheTime().getValue()){
                inputStream = new ByteArrayInputStream(ArrayUtils.toPrimitive(pair.getOne()));
                return true;
            }
        }
        return false;
    }

    private InputStream loadFromCache(){
        File file = new File(Sploty.getDirectory(), "cache/" + url.getHost() + "/" + url.getPort() + "/" + url.getPath().replace('/', '|'));
        if(!file.exists()) return null;
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getError() {
        return error;
    }

    public String getFileName(){
        return local?file.getName():FilenameUtils.getName(url.getPath());
    }

    public URLConnection getConnection() {
        return connection;
    }

    public void setConnection(URLConnection connection) {
        this.connection = connection;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getCharset() {
        return charset;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getEncoding() {
        return encoding;
    }

    public long getLength() {
        return length;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public CacheSettings.CacheSetting getRamCacheSetting() {
        return ramCacheSetting;
    }

    public void setRamCacheSetting(CacheSettings.CacheSetting ramCacheSetting) {
        this.ramCacheSetting = ramCacheSetting;
    }

    public CacheSettings.CacheSetting getDiskCacheSetting() {
        return diskCacheSetting;
    }

    public void setDiskCacheSetting(CacheSettings.CacheSetting diskCacheSetting) {
        this.diskCacheSetting = diskCacheSetting;
    }
}
