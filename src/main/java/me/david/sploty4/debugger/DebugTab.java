package me.david.sploty4.debugger;

import javafx.scene.control.Tab;
import lombok.Getter;

public class DebugTab extends Tab {

    @Getter private String name;

    public DebugTab(String name) {
        super(name);
        setClosable(false);
        this.name = name;
    }
}
