package me.david.sploty4.features;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import me.david.sploty4.util.FileUtil;
import me.david.sploty4.util.StringUtil;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadManager {

    private ArrayList<Download> downloads = new ArrayList<>();
    private ArrayList<ActivityListener> activityListener = new ArrayList<>();
    private ArrayList<ShowErrorListenr> showErrorListenrs = new ArrayList<>();
    private boolean isActive, isError;
    private PopOver popOver = new PopOver();
    private VBox downloadsBox = new VBox();

    public interface ActivityListener {
        void onActivityChange(boolean isActive);
    }

    public interface DownloadListener {
        void onDownloadChange(long percentage, boolean done);
    }

    public interface ErrorListener {
        void error(String message);
    }

    public interface ShowErrorListenr {
        void change(boolean show);
    }

    public DownloadManager(){
        popOver.setTitle("Downloads");
        popOver.setContentNode(downloadsBox);
    }

    public void updateActivity(){
        for(Download download : downloads)
            if(!download.finished && !download.failed && !isActive) {
                for (ActivityListener listener : activityListener) {
                    isActive = true;
                    listener.onActivityChange(true);
                }
                return;
            }
        isActive = false;
        activityListener.forEach(listener -> listener.onActivityChange(false));
    }

    public void openDownloads(Node button){
        if(popOver.isShowing()) {
            popOver.hide();
            updateActivity();
            isError = false;
            showErrorListenrs.forEach(listener -> listener.change(false));
        }else {
            popOver.show(button);
        }
    }

    public class Download {
        private InputStream stream;
        private File outputFile;
        private long done, total;
        private boolean finished, failed;
        private String failedMessage, name;
        private List<DownloadListener> listeners = new ArrayList<>();
        private List<ErrorListener> errorListeners = new ArrayList<>();

        public Download(InputStream stream, File outputFile, long done, long total, String name) {
            this.stream = stream;
            this.outputFile = outputFile;
            this.done = done;
            this.total = total;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
        }

        public boolean isFailed() {
            return failed;
        }

        public String getFailedMessage() {
            return failedMessage;
        }

        public void setFailedMessage(String failedMessage) {
            this.failedMessage = failedMessage;
        }

        public List<DownloadListener> getListeners() {
            return listeners;
        }

        public void addListener(DownloadListener listener){
            if(!listeners.contains(listener)) listeners.add(listener);
        }

        public void removeListener(DownloadListener listener){
            if(listeners.contains(listener)) listeners.remove(listener);
        }

        public void addErrorListener(ErrorListener listener){
            if(!errorListeners.contains(listener)) errorListeners.add(listener);
        }

        public void removeErrorListener(ErrorListener listener){
            if(errorListeners.contains(listener)) errorListeners.remove(listener);
        }

        public void start(){
            try {
                FileUtil.toFile(new FileUtil.Updater() {
                    @Override
                    public void onUpdate(long done) {
                        Download.this.done = done;
                        listeners.forEach((listener -> listener.onDownloadChange(done/total*100, false)));
                    }
                    @Override
                    public void onFinished() {
                        listeners.forEach((listener -> listener.onDownloadChange(total, true)));
                        activityListener.forEach(lis -> lis.onActivityChange(true));
                        isActive = true;
                    }
                }, stream, outputFile, false, 1024);
            } catch (IOException e) {
                e.printStackTrace();
                failed = true;
                failedMessage = e.getMessage();
                errorListeners.forEach(listeners -> listeners.error(e.getMessage()));
                showErrorListenrs.forEach(listeners -> listeners.change(true));
                isError = true;
            }
        }

        public InputStream getStream() {
            return stream;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        public File getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(File outputFile) {
            this.outputFile = outputFile;
        }

        public long getDone() {
            return done;
        }

        public void setDone(long done) {
            this.done = done;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }
    }

    public void download(InputStream stream, File file, long max, DownloadListener listener, ErrorListener errorListener){
        Download download = new Download(stream, file, 0, max, file.getName());
        download.addListener(listener);
        download.addErrorListener(errorListener);
        downloads.add(download);

        HBox downloadBox = new HBox();
        Label title = new Label(download.getName());
        title.setFont(Font.font(null, FontWeight.BOLD, 9));
        Label bytes = new Label("0/0");
        Label status = new Label("Idle...");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0.0001);
        download.addListener((percentage, done) -> {
            progressBar.setProgress(percentage/100);
            if(done) status.setText("Done");
            else status.setText("Download...");
            bytes.setText(StringUtil.humanReadableBytes(download.done) + "/" + StringUtil.humanReadableBytes(download.total));
        });
        download.addErrorListener(message -> status.setText("Error: " + message));
        downloadBox.getChildren().addAll(bytes, status, progressBar);
        downloadsBox.getChildren().addAll(title, downloadBox);

        download.start();
        isActive = true;
        activityListener.forEach(lis -> lis.onActivityChange(true));
    }

    public boolean isActive() {
        return isActive;
    }

    public void addListener(ActivityListener listener){
        if(!activityListener.contains(listener))
            activityListener.add(listener);
    }

    public void removeListener(ActivityListener listener){
        if(activityListener.contains(listener))
            activityListener.remove(listener);
    }

    public void addListener(ShowErrorListenr listener){
        if(!showErrorListenrs.contains(listener))
            showErrorListenrs.add(listener);
    }

    public void removeListener(ShowErrorListenr listener){
        if(showErrorListenrs.contains(listener))
            showErrorListenrs.remove(listener);
    }

    public boolean isError() {
        return isError;
    }
}
