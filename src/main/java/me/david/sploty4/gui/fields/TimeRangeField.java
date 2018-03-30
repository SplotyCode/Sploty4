package me.david.sploty4.gui.fields;

import javafx.scene.control.ComboBox;

import java.util.function.Consumer;

public class TimeRangeField {

    private NumberTextField field = new NumberTextField();
    private ComboBox<TimeUnit> timeUnit = new ComboBox<>();
    private Consumer<TimeRange> event = (time) -> {};

    public TimeRangeField(TimeRange range){
        field.setText(range.getTime() + "");
        timeUnit.getItems().setAll(TimeUnit.values());
        timeUnit.setValue(range.getUnit());
        timeUnit.setMaxWidth(100);
        timeUnit.setMinWidth(100);
        field.setOnAction((event -> this.event.accept(new TimeRange(Long.valueOf(field.getText()), timeUnit.getValue()))));
        timeUnit.setOnAction((event -> this.event.accept(new TimeRange(Long.valueOf(field.getText()), timeUnit.getValue()))));
    }

    public void setDisable(boolean bool){
        field.setDisable(bool);
        timeUnit.setDisable(bool);
    }

    public Consumer<TimeRange> getEvent() {
        return event;
    }

    public void setEvent(Consumer<TimeRange> event) {
        this.event = event;
    }

    public enum TimeUnit {

        MILLISECONDS(1),
        SECONDS(1000),
        MINUTES(1000*60),
        HOURS(1000*60*60),
        DAYS(1000*60*60*24),
        WEEKS(1000*60*60*24*7),
        MONTHS(1000L*60*60*24*7*4),
        YEARS(1000L*60*60*24*7*4*12);

        private final long multiply;
        TimeUnit(long multiply){
            this.multiply = multiply;
        }

        public long getMultiply() {
            return multiply;
        }
    }

    public static class TimeRange {

        private long time;
        private TimeUnit unit;

        public TimeRange(long time, TimeUnit unit) {
            this.time = time;
            this.unit = unit;
        }

        public long getValue(){
            return time*unit.getMultiply();
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }
    }

    public ComboBox<TimeUnit> getTimeUnit() {
        return timeUnit;
    }

    public NumberTextField getField() {
        return field;
    }
}
