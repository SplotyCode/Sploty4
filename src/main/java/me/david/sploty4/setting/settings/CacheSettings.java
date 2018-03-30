package me.david.sploty4.setting.settings;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.fields.TimeRangeField;
import me.david.sploty4.setting.CacheState;
import me.david.sploty4.setting.Setting;
import me.david.sploty4.storage.FileComponent;
import me.david.sploty4.storage.FileSerializer;

import java.io.File;
import java.io.IOException;

public class CacheSettings extends Setting {

    private CacheSetting html, css, javascript, image, other;

    public CacheState[] getValidCacheStates(){
        return CacheState.values();
    }

    public CacheSettings(String name) {
        super("Cacheing-" + name, new File(Sploty.getDirectory(), "settings/cache_" + name.toLowerCase() + ".txt"));
        html = css = javascript = image = other = new CacheSetting();
    }

    @Override
    public void render(Tab tab, Stage stage) {
        VBox main = new VBox();
        main.getChildren().addAll(cacheToBox(html, "Html"), cacheToBox(css, "CSS - Stylesheet"), cacheToBox(javascript, "Javascript"), cacheToBox(image, "Images"), cacheToBox(other, "Other"));
        tab.setContent(main);
    }

    private VBox cacheToBox(CacheSetting cache, String name){
        VBox box = new VBox();
        Text text = new Text(name);
        text.setFont(Font.font(null, FontWeight.BOLD, 24));

        HBox whenBox = new HBox();
        ComboBox<CacheState> when = new ComboBox<>();
        when.getItems().setAll(getValidCacheStates());
        when.setValue(cache.getWhen());
        whenBox.getChildren().addAll(new Label("When: "), when);

        HBox main = new HBox();
        CheckBox enable = new CheckBox();
        enable.setSelected(cache.isCache());
        CheckBox failEnable = new CheckBox();
        failEnable.setSelected(cache.isCacheFailed());
        main.getChildren().addAll(new Label("Enable: "), enable, new Label("Enable Fail: "), failEnable);

        HBox timeBox = new HBox();
        TimeRangeField time = new TimeRangeField(cache.getCacheTime());
        timeBox.getChildren().addAll(new Label("Time: "), time.getField(), time.getTimeUnit());

        HBox timeFailBox = new HBox();
        TimeRangeField failTime = new TimeRangeField(cache.getFailedCacheTime());
        timeFailBox.getChildren().addAll(new Label("Fail Time: "), failTime.getField(), failTime.getTimeUnit());

        enable.setOnAction((event -> {
            cache.setCache(enable.isSelected());
            time.setDisable(!enable.isSelected());
        }));
        failEnable.setOnAction((event -> {
            cache.setCacheFailed(failEnable.isSelected());
            failTime.setDisable(!failEnable.isSelected());
        }));

        time.setEvent(cache::setCacheTime);
        failTime.setEvent(cache::setCacheTime);

        when.valueProperty().addListener(((observableValue, cacheState, t1) -> {
            cache.setWhen(when.getValue());
            boolean bool = cache.getWhen() == CacheState.ALWAYS;
            enable.setDisable(!bool);
            time.setDisable(!bool);
        }));

        boolean bool = cache.getWhen() != CacheState.ALWAYS;
        enable.setDisable(bool);
        time.setDisable(bool);

        box.getChildren().addAll(text, whenBox, main, timeBox, timeFailBox);
        return box;
    }

    @Override
    public void read(FileSerializer serializer) throws IOException {
        html.read(serializer);
        css.read(serializer);
        javascript.read(serializer);
        image.read(serializer);
        other.read(serializer);
    }

    @Override
    public void write(FileSerializer serializer) throws IOException {
        html.write(serializer);
        css.write(serializer);
        javascript.write(serializer);
        image.write(serializer);
        other.write(serializer);
    }

    public static class CacheSetting implements FileComponent {

        private TimeRangeField.TimeRange cacheTime, failedCacheTime;
        private boolean cache, cacheFailed;
        private CacheState when;

        public CacheSetting(){}
        public CacheSetting(TimeRangeField.TimeRange cacheTime, TimeRangeField.TimeRange failedCacheTime, boolean cache, boolean cacheFailed, CacheState when) {
            this.cacheTime = cacheTime;
            this.failedCacheTime = failedCacheTime;
            this.cache = cache;
            this.cacheFailed = cacheFailed;
            this.when = when;
        }

        @Override
        public void read(FileSerializer serializer) throws IOException {
            cacheTime = new TimeRangeField.TimeRange(serializer.readLong(), serializer.readEnum(TimeRangeField.TimeUnit.class));
            failedCacheTime = new TimeRangeField.TimeRange(serializer.readLong(), serializer.readEnum(TimeRangeField.TimeUnit.class));
            cache = serializer.readBoolean();
            cacheFailed = serializer.readBoolean();
            when = serializer.readEnum(CacheState.class);
        }

        @Override
        public void write(FileSerializer serializer) throws IOException {
            serializer.writeLong(cacheTime.getTime());
            serializer.writeEnum(cacheTime.getUnit());
            serializer.writeLong(failedCacheTime.getTime());
            serializer.writeEnum(failedCacheTime.getUnit());
            serializer.writeBoolean(cache);
            serializer.writeBoolean(cacheFailed);
            serializer.writeEnum(when);
        }

        public TimeRangeField.TimeRange getCacheTime() {
            return cacheTime;
        }

        public void setCacheTime(TimeRangeField.TimeRange cacheTime) {
            this.cacheTime = cacheTime;
        }

        public TimeRangeField.TimeRange getFailedCacheTime() {
            return failedCacheTime;
        }

        public void setFailedCacheTime(TimeRangeField.TimeRange failedCacheTime) {
            this.failedCacheTime = failedCacheTime;
        }

        public boolean isCache() {
            return cache;
        }

        public void setCache(boolean cache) {
            this.cache = cache;
        }

        public boolean isCacheFailed() {
            return cacheFailed;
        }

        public void setCacheFailed(boolean cacheFailed) {
            this.cacheFailed = cacheFailed;
        }

        public CacheState getWhen() {
            return when;
        }

        public void setWhen(CacheState when) {
            this.when = when;
        }
    }

    public CacheSetting getHtml() {
        return html;
    }

    public void setHtml(CacheSetting html) {
        this.html = html;
    }

    public CacheSetting getCss() {
        return css;
    }

    public void setCss(CacheSetting css) {
        this.css = css;
    }

    public CacheSetting getJavascript() {
        return javascript;
    }

    public void setJavascript(CacheSetting javascript) {
        this.javascript = javascript;
    }

    public CacheSetting getImage() {
        return image;
    }

    public void setImage(CacheSetting image) {
        this.image = image;
    }

    public CacheSetting getOther() {
        return other;
    }

    public void setOther(CacheSetting other) {
        this.other = other;
    }
}
