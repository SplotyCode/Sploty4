package me.david.sploty4.gui.tab;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;

import java.util.ArrayList;

public class TabContextMenu extends ContextMenu {

    private BrowserTab tab;

    public TabContextMenu(BrowserTab tab) {
        this.tab = tab;
        add("Close", () -> tab.getList().getTabs().remove(tab));
        add("Close Others", () -> {
            for(Tab ta : new ArrayList<>(tab.getList().getTabs()))
                if(ta != tab)
                    tab.getList().getTabs().remove(ta);
        });
        add("Reload", tab::reload);
        add("Reload All", () -> {
            for(Tab ta : tab.getList().getTabs())
                ((BrowserTab) ta).reload();
        });
    }

    private void add(String title, Runnable runnable){
        MenuItem item = new MenuItem(title);
        item.setOnAction((event -> runnable.run()));
        getItems().add(item);
    }

    public BrowserTab getTab() {
        return tab;
    }
}
