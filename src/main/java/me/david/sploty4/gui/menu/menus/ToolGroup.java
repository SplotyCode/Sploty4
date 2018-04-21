package me.david.sploty4.gui.menu.menus;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;

public class ToolGroup extends MenuGroup {

    public ToolGroup(Window window) {
        super(window, "Tool");
        add("View-Source", (event) -> {
            if(!window.getCurrentTab().getCurrentUrl().startsWith("view-source:"))
                window.getCurrentTab().openNew("view-source:" + window.getCurrentTab().getCurrentUrl(), false);
        }, new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
    }
}
