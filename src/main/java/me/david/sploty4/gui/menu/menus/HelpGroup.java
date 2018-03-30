package me.david.sploty4.gui.menu.menus;

import me.david.sploty4.gui.InformationGui;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;

public class HelpGroup extends MenuGroup {

    public HelpGroup(Window window) {
        super(window, "Help");
        add("About", event -> new InformationGui(window));
    }
}
