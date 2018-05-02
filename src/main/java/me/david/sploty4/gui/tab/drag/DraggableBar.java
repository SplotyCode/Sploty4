package me.david.sploty4.gui.tab.drag;

import com.sun.javafx.scene.control.skin.TabPaneSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import me.david.sploty4.gui.tab.BrowserTab;

import java.lang.reflect.Field;

public class DraggableBar extends TabPane {

    private static Tab DRAGGED_TAB;
    private PositionMarker marker = null;
    public static final DataFormat TAB_MOVE = new DataFormat("DnDTabPane:tabMove");

    private Object noneEnum;
    private StyleableProperty<Object> openAnimation;
    private StyleableProperty<Object> closeAnimation;
    private TabPaneSkin skin = (TabPaneSkin) getSkin();
    private Pane pane;


    private ObjectProperty<EventHandler<TabDraggedEvent>> onTabDragged = new SimpleObjectProperty<>(null);

    public DraggableBar(Pane pane) {
        this.pane = pane;
        try {
            setSkin(new DraggableBarSkin(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DraggableBarSkin extends TabPaneSkin {
        private DraggableBarSkin(TabPane tabPane) throws Exception {
            super(tabPane);
            Field field_tabHeaderArea = TabPaneSkin.class.getDeclaredField("tabHeaderArea");
            field_tabHeaderArea.setAccessible(true);

            Pane tabHeaderArea = (StackPane) field_tabHeaderArea.get(this);
            setOnDragOver(Event::consume);
            Field f_headersRegion = tabHeaderArea.getClass().getDeclaredField("headersRegion");
            f_headersRegion.setAccessible(true);
            Pane headersRegion = (StackPane) f_headersRegion.get(tabHeaderArea);
            EventHandler<MouseEvent> handler = DraggableBar.this::tabPane_handleDragStart;
            EventHandler<DragEvent> handlerFinished = DraggableBar.this::tabPane_handleDragDone;

            for (Node tabHeaderSkin : headersRegion.getChildren()) {
                tabHeaderSkin.addEventHandler(MouseEvent.DRAG_DETECTED, handler);
                tabHeaderSkin.addEventHandler(DragEvent.DRAG_DONE, handlerFinished);
            }

            headersRegion.getChildren().addListener((javafx.collections.ListChangeListener.Change<? extends Node> change) -> {
                while (change.next()) {
                    if (change.wasRemoved()) {
                        for (Node node : change.getRemoved()) {
                            node.removeEventHandler(MouseEvent.DRAG_DETECTED, handler);
                        }
                        for (Node node : change.getRemoved()) {
                            node.removeEventHandler(DragEvent.DRAG_DONE, handlerFinished);
                        }
                    }
                    if (change.wasAdded()) {
                        for (Node node : change.getAddedSubList()) {
                            node.addEventHandler(MouseEvent.DRAG_DETECTED, handler);
                        }
                        for (Node node : change.getAddedSubList()) {
                            node.addEventHandler(DragEvent.DRAG_DONE, handlerFinished);
                        }
                    }
                }
            });

            tabHeaderArea.addEventHandler(DragEvent.DRAG_OVER, (e) -> tabPane_handleDragOver(tabHeaderArea, headersRegion, e));
            tabHeaderArea.addEventHandler(DragEvent.DRAG_DROPPED, (e) -> tabPane_handleDragDropped(tabHeaderArea, headersRegion, e));
            tabHeaderArea.addEventHandler(DragEvent.DRAG_EXITED, DraggableBar.this::tabPane_handleDragDone);

            Field field = TabPaneSkin.class.getDeclaredField("openTabAnimation");
            field.setAccessible(true);
            DraggableBar.this.openAnimation = (StyleableProperty<Object>) field.get(this);

            field = TabPaneSkin.class.getDeclaredField("closeTabAnimation");
            field.setAccessible(true);
            DraggableBar.this.closeAnimation = (StyleableProperty<Object>) field.get(this);

            for (Class<?> cl : getClass().getDeclaredClasses())
                if ("TabAnimation".equals(cl.getSimpleName())) {
                    for (Enum<?> enumConstant : (Enum<?>[]) cl.getEnumConstants()) {
                        if ("NONE".equals(enumConstant.name())) {
                            DraggableBar.this.noneEnum = enumConstant;
                            break;
                        }
                    }
                    break;
                }
        }
    }

    void tabPane_handleDragStart(MouseEvent event) {
        try {
            Field f_tab = event.getSource().getClass().getDeclaredField("tab");
            f_tab.setAccessible(true);
            Tab t = (Tab) f_tab.get(event.getSource());

            if (t != null) {
                DRAGGED_TAB = t;
                Node node = (Node) event.getSource();
                Dragboard db = node.startDragAndDrop(TransferMode.MOVE);

                WritableImage snapShot = node.snapshot(new SnapshotParameters(), null);
                PixelReader reader = snapShot.getPixelReader();
                int padX = 10;
                int padY = 10;
                int width = (int) snapShot.getWidth();
                int height = (int) snapShot.getHeight();
                WritableImage image = new WritableImage(width + padX, height + padY);
                PixelWriter writer = image.getPixelWriter();

                int h = 0;
                int v;
                while (h < width + padX) {
                    v = 0;
                    while (v < height + padY) {
                        if (h >= padX && h <= width + padX && v >= padY && v <= height + padY) {
                            writer.setColor(h, v, reader.getColor(h - padX, v - padY));
                        } else {
                            writer.setColor(h, v, Color.TRANSPARENT);
                        }

                        v++;
                    }
                    h++;
                }

                db.setDragView(image, image.getWidth(), image.getHeight() * -1);

                ClipboardContent content = new ClipboardContent();
                String data = System.identityHashCode(t) + "";
                content.put(TAB_MOVE, data);
                db.setContent(content);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void tabPane_handleDragOver(Pane tabHeaderArea, Pane headersRegion, DragEvent event) {
        Tab draggedTab = DRAGGED_TAB;
        if (draggedTab == null) {
            return;
        }
        event.consume();

        double x = event.getX() - headersRegion.getBoundsInParent().getMinX();

        Node referenceNode = null;
        DropType type = DropType.AFTER;
        for (Node n : headersRegion.getChildren()) {
            Bounds b = n.getBoundsInParent();
            if (b.getMaxX() > x) {
                if (b.getMinX() + b.getWidth() / 2 > x) {
                    referenceNode = n;
                    type = DropType.BEFORE;
                } else {
                    referenceNode = n;
                    type = DropType.AFTER;
                }
                break;
            }
        }

        if (referenceNode == null && headersRegion.getChildren().size() > 0) {
            referenceNode = headersRegion.getChildren().get(headersRegion.getChildren().size() - 1);
            type = DropType.AFTER;
        }

        if (referenceNode != null) {
            try {
                Field field = referenceNode.getClass().getDeclaredField("tab");
                field.setAccessible(true);
                Tab tab = (Tab) field.get(referenceNode);

                boolean noMove = false;
                if (tab == draggedTab) {
                    noMove = true;
                } else if (type == DropType.BEFORE) {
                    int idx = getTabs().indexOf(tab);
                    if (idx > 0) {
                        if (getTabs().get(idx - 1) == draggedTab) {
                            noMove = true;
                        }
                    }
                } else {
                    int idx = getTabs().indexOf(tab);

                    if (idx + 1 < getTabs().size()) {
                        if (getTabs().get(idx + 1) == draggedTab) {
                            noMove = true;
                        }
                    }
                }

                if (noMove) {
                    doneHandler(draggedTab, null, null, DropType.NONE);
                    return;
                }

                Bounds b = referenceNode.getBoundsInLocal();
                b = referenceNode.localToScene(b);
                b = sceneToLocal(b);

                doneHandler(draggedTab, tab, b, type);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            event.acceptTransferModes(TransferMode.MOVE);
        } else {
            doneHandler(draggedTab, null, null, DropType.NONE);
        }
    }

    void tabPane_handleDragDropped(Pane tabHeaderArea, Pane headersRegion, DragEvent event) {
        Tab draggedTab = DRAGGED_TAB;
        if (draggedTab == null) {
            return;
        }
        double x = event.getX() - headersRegion.getBoundsInParent().getMinX();

        Node referenceNode = null;
        DropType type = DropType.AFTER;
        for (Node n : headersRegion.getChildren()) {
            Bounds b = n.getBoundsInParent();
            if (b.getMaxX() > x) {
                if (b.getMinX() + b.getWidth() / 2 > x) {
                    referenceNode = n;
                    type = DropType.BEFORE;
                } else {
                    referenceNode = n;
                    type = DropType.AFTER;
                }
                break;
            }
        }
        if (referenceNode == null && headersRegion.getChildren().size() > 0) {
            referenceNode = headersRegion.getChildren().get(headersRegion.getChildren().size() - 1);
            type = DropType.AFTER;
        }
        if (referenceNode != null) {
            try {
                Field field = referenceNode.getClass().getDeclaredField("tab");
                field.setAccessible(true);
                Tab tab = (Tab) field.get(referenceNode);

                boolean noMove = false;
                if( tab == null ) {
                    event.setDropCompleted(false);
                    return;
                } else if (tab == draggedTab) {
                    noMove = true;
                } else if (type == DropType.BEFORE) {
                    int idx = getTabs().indexOf(tab);
                    if (idx > 0) {
                        if (getTabs().get(idx - 1) == draggedTab) {
                            noMove = true;
                        }
                    }
                } else {
                    int idx = getTabs().indexOf(tab);

                    if (idx + 1 < getTabs().size()) {
                        if (getTabs().get(idx + 1) == draggedTab) {
                            noMove = true;
                        }
                    }
                }
                if (!noMove) {
                    StyleOrigin openOrigin = this.openAnimation.getStyleOrigin();
                    StyleOrigin closeOrigin = this.closeAnimation.getStyleOrigin();
                    Object openValue = this.openAnimation.getValue();
                    Object closeValue = this.closeAnimation.getValue();
                    try {
                        this.openAnimation.setValue(this.noneEnum);
                        this.closeAnimation.setValue(this.noneEnum);
                        dropHandler(draggedTab, tab, type);
                        event.setDropCompleted(true);
                    } finally {
                        this.openAnimation.applyStyle(openOrigin, openValue);
                        this.closeAnimation.applyStyle(closeOrigin, closeValue);
                    }

                } else {
                    event.setDropCompleted(false);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            event.consume();
        }
    }

    void tabPane_handleDragDone(DragEvent event) {
        Tab tab = DRAGGED_TAB;
        if (tab == null) {
            return;
        }

        efx_dragFinished(tab);
    }

    private void dropHandler(Tab draggedTab, Tab targetTab, DropType dropType) {
        TabPane targetPane = targetTab.getTabPane();
        int oldIndex = draggedTab.getTabPane().getTabs().indexOf(draggedTab);
        draggedTab.getTabPane().getTabs().remove(draggedTab);
        int idx = targetPane.getTabs().indexOf(targetTab);
        if (dropType == DropType.AFTER) {
            if (idx + 1 <= targetPane.getTabs().size()) {
                targetPane.getTabs().add(idx + 1, draggedTab);
            } else {
                targetPane.getTabs().add(draggedTab);
            }
        } else {
            targetPane.getTabs().add(idx, draggedTab);
        }

        fireTabDragged((BrowserTab) draggedTab, oldIndex, targetPane.getTabs().indexOf(draggedTab));
        draggedTab.getTabPane().getSelectionModel().select(draggedTab);
    }

    private void efx_dragFinished(Tab tab) {
        if (marker != null) {
            marker.setVisible(false);
            marker = null;
        }
    }

    public void fireTabDragged(BrowserTab draggedTab, int fromIndex, int toIndex) {
        TabDraggedEvent event = new TabDraggedEvent(draggedTab, fromIndex, toIndex);
        if (onTabDragged.get() != null)
            onTabDragged.get().handle(event);
        fireEvent(event);
    }

    private void doneHandler(Tab draggedTab, Tab targetTab, Bounds bounds, DropType dropType) {
        if (dropType == DropType.NONE) {
            if (marker != null) {
                marker.setVisible(false);
                marker = null;
            }
            return;
        }
        PositionMarker data = new PositionMarker(draggedTab, targetTab, bounds, dropType);
        if (marker == null || !marker.equals(data)) {
            if (marker != null) {
                marker.setVisible(false);
                marker = null;
            }
            marker = handleMarker(data);
        }
    }

    private PositionMarker handleMarker(PositionMarker data) {
        PositionMarker marker = null;
        for (Node n : pane.getChildren()) {
            if (n instanceof PositionMarker) {
                marker = (PositionMarker) n;
            }
        }

        if (marker == null) {
            marker = new PositionMarker(data.draggedTab, data.targetTab, data.bounds, data.dropType);
            marker.setManaged(false);
            pane.getChildren().add(marker);
        } else {
            marker.setVisible(true);
        }

        double w = marker.getBoundsInLocal().getWidth();
        double h = marker.getBoundsInLocal().getHeight();

        double ratio = data.bounds.getHeight() / h;
        ratio += 0.1;
        marker.setScaleX(ratio);
        marker.setScaleY(ratio);

        double wDiff = w / 2;
        double hDiff = (h - h * ratio) / 2;

        if (data.dropType == DropType.AFTER) {
            marker.relocate(data.bounds.getMinX() + data.bounds.getWidth() - wDiff, data.bounds.getMinY() - hDiff);
        } else {
            marker.relocate(data.bounds.getMinX() - wDiff, data.bounds.getMinY() - hDiff);
        }

        marker.bounds = data.bounds;
        marker.dropType = data.dropType;
        marker.draggedTab = data.draggedTab;
        marker.targetTab = data.targetTab;
        return marker;
    }

}
