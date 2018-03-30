package me.david.sploty4.features;

import me.david.sploty4.Sploty;
import me.david.sploty4.storage.FileComponent;
import me.david.sploty4.storage.FileSerializer;
import me.david.sploty4.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class History implements FileComponent {

    private ArrayList<HistoryEntry> history = new ArrayList<>();
    private ArrayList<Listener> listeners = new ArrayList<>();
    private File historyFile = new File(Sploty.getDirectory(), "history.txt");

    public History(){
        try {
            if(historyFile.exists()) {
                FileSerializer serializer = new FileSerializer();
                serializer.readFile(historyFile);
                read(serializer);
            } else historyFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileSerializer serializer = new FileSerializer();
            try {
                write(serializer);
                serializer.writeFile(historyFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "History Save"));
    }

    @Override
    public void read(FileSerializer serializer) throws IOException {
        int max = serializer.readVarInt();
        for(int i = 0; i < max;i++) {
            HistoryEntry entry = new HistoryEntry();
            entry.read(serializer);
            history.add(entry);
        }
    }

    @Override
    public void write(FileSerializer serializer) throws IOException {
        serializer.writeVarInt(history.size());
        for(HistoryEntry entry : history)
            entry.write(serializer);
    }

    public interface Listener {
        void onUpdate();
    }

    public static class HistoryEntry implements FileComponent {

        private String title;
        private String url;
        private long time;

        public HistoryEntry(){}
        public HistoryEntry(String title, String url, long time) {
            this.title = title;
            this.url = url;
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            Sploty.getInstance().getHistory().listeners.forEach(Listener::onUpdate);
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            Sploty.getInstance().getHistory().listeners.forEach(Listener::onUpdate);
            this.url = url;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            Sploty.getInstance().getHistory().listeners.forEach(Listener::onUpdate);
            this.time = time;
        }

        @Override
        public void read(FileSerializer serializer) throws IOException {
            title = serializer.readString();
            url = serializer.readString();
            time = serializer.readLong();
        }

        @Override
        public void write(FileSerializer serializer) throws IOException {
            serializer.writeString(title);
            serializer.writeString(url);
            serializer.writeLong(time);
        }

        public String getDisplayname(){
            return StringUtil.isEmpty(getTitle())?getUrl():getTitle();
        }
    }

    public HistoryEntry put(String url){
        HistoryEntry entry = new HistoryEntry("", url, System.currentTimeMillis());
        history.add(entry);
        listeners.forEach(Listener::onUpdate);
        return entry;
    }

    public void addListener(Listener listener){
        if(!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(Listener listener){
        if(listeners.contains(listener)) listeners.remove(listener);
    }

    public File getHistoryFile() {
        return historyFile;
    }


    public ArrayList<Listener> getListeners() {
        return listeners;
    }

    public ArrayList<HistoryEntry> getHistory() {
        return history;
    }
}
