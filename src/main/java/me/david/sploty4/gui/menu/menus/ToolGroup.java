package me.david.sploty4.gui.menu.menus;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;

public class ToolGroup extends MenuGroup {

    public ToolGroup(Window window) {
        super(window, "Tool");
        add("View-Source", event -> {
            //todo else send notification
            if(!window.getCurrentTab().isViewSource())
                window.getCurrentTab().openNew("view-source:" + window.getCurrentTab().getCurrentUrl(), false);
        }, new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));

        add("Debugger", event -> window.getCurrentTab().getDebugger().toggle(),
                new KeyCodeCombination(KeyCode.F12));
    }
}
