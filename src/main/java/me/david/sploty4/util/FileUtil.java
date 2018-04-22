package me.david.sploty4.util;

import me.david.sploty4.Sploty;
import org.apache.commons.io.IOUtils;

import java.io.*;

public final class FileUtil {

    public static void toFile(InputStream is, File file, boolean close) throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        OutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1)
            outStream.write(buffer, 0, bytesRead);
        IOUtils.closeQuietly(outStream);
        if(close) IOUtils.closeQuietly(is);
    }

    public interface Updater {
        void onUpdate(long done);
        void onFinished();
    }

    public static void toFile(Updater listener, InputStream is, File file, boolean close, int chuckSize) throws IOException {
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        OutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[chuckSize];
        int bytesRead;
        long totalread = 0;
        while ((bytesRead = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
            totalread += bytesRead;
            listener.onUpdate(totalread);
        }
        listener.onFinished();
        IOUtils.closeQuietly(outStream);
        if(close) IOUtils.closeQuietly(is);
    }

    public static boolean isValidFileName(final String fileName) {
        File file = new File(fileName);
        try {
            return file.getCanonicalFile().getName().equals(fileName);
        } catch (IOException ex) {
            return false;
        }
    }

    public static File createFile(File file){
        file.getParentFile().mkdirs();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Sploty.getLogger().exception(e, "Failed creating File...");
            }
            return file;
        }
        int i = 0;
        while (new File(file.getPath() + "(" + i + ")").exists())
            i++;
        file = new File(file.getPath() + "(" + i + ")");
        try {
            file.createNewFile();
        } catch (IOException e) {
            Sploty.getLogger().exception(e, "Failed creating File...");
        }
        return file;
    }
}
