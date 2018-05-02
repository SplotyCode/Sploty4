package me.david.sploty4.gui.tab.drag;

import com.sun.javafx.css.converters.PaintConverter;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PositionMarker extends Group {

    public Tab draggedTab, targetTab;
    public Bounds bounds;
    public DropType dropType;

    public PositionMarker(Tab draggedTab, Tab targetTab, Bounds bounds, DropType dropType) {
        this.draggedTab = draggedTab;
        this.targetTab = targetTab;
        this.bounds = bounds;
        this.dropType = dropType;

        setMouseTransparent(true);
        getStyleClass().add("position-marker");
        Circle outer = new Circle(8);
        outer.setFill(Color.WHITE);

        getChildren().add(outer);
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(6);
        line.setEndX(0);
        line.setEndY(40);
        line.setStrokeWidth(7);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStroke(Color.WHITE);
        getChildren().add(line);

        Circle c = new Circle(6);
        c.fillProperty().bind(fillProperty());
        getChildren().add(c);

        Circle inner = new Circle(3);
        inner.setFill(Color.WHITE);
        getChildren().add(inner);

        Line l = new Line();
        l.setStartX(0);
        l.setStartY(6);
        l.setEndX(0);
        l.setEndY(40);
        l.setStrokeWidth(3);
        l.setStrokeLineCap(StrokeLineCap.ROUND);
        l.strokeProperty().bind(this.fill);
        getChildren().add(l);

        setEffect(new DropShadow(3, Color.BLACK));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionMarker that = (PositionMarker) o;

        if (draggedTab != null ? !draggedTab.equals(that.draggedTab) : that.draggedTab != null) return false;
        if (targetTab != null ? !targetTab.equals(that.targetTab) : that.targetTab != null) return false;
        if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
        return dropType == that.dropType;
    }

    private final ObjectProperty<Paint> fill = new SimpleStyleableObjectProperty<>(FILL, this, "fill", Color.rgb(0, 139, 255));

    private ObjectProperty<Paint> fillProperty() {
        return this.fill;
    }

    private static final CssMetaData<PositionMarker, Paint> FILL = new CssMetaData<PositionMarker, Paint>("-fx-fill", PaintConverter.getInstance(), Color.rgb(0, 139, 255)) { //$NON-NLS-1$

        @Override
        public boolean isSettable(PositionMarker node) {
            return !node.fillProperty().isBound();
        }

        @SuppressWarnings("unchecked")
        @Override
        public StyleableProperty<Paint> getStyleableProperty(PositionMarker node) {
            return (StyleableProperty<Paint>) node.fillProperty();
        }

    };

    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    static {
        List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Node.getClassCssMetaData());
        styleables.add(FILL);
        STYLEABLES = Collections.unmodifiableList(styleables);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

}
