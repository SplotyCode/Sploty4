package me.david.sploty4.gui.menu;

import javafx.scene.control.MenuBar;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.menus.FileGroup;
import me.david.sploty4.gui.menu.menus.HelpGroup;
import me.david.sploty4.gui.menu.menus.HistoryGroup;
import me.david.sploty4.gui.menu.menus.ViewGroup;

public class MenuManager extends MenuBar{

    public MenuManager(Window window){
        getMenus().addAll(new FileGroup(window), new ViewGroup(window), new HistoryGroup(window), new HelpGroup(window));
    }

}
