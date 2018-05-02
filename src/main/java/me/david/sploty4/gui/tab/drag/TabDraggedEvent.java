package me.david.sploty4.gui.tab.drag;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.gui.tab.BrowserTab;

public class TabDraggedEvent extends Event {

    @Getter @Setter private int fromIndex, toIndex;

    public static final EventType<TabDraggedEvent> TAB_DRAGGED = new EventType<>(Event.ANY, "TAB_DRAGGED");

    public TabDraggedEvent(BrowserTab draggedTab, int fromIndex, int toIndex) {
        super(TAB_DRAGGED);

        this.source = draggedTab;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

}
