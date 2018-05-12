package cpubug.debugger;

import javafx.scene.control.Tab;
import lombok.Getter;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.tab.BrowserTab;

public class DebugTab extends Tab {

    @Getter private String name;
    @Getter private final Window window;
    @Getter private final BrowserTab tab;

    public DebugTab(String name, final Window window, final BrowserTab tab) {
        super(name);
        setClosable(false);
        this.name = name;
        this.window = window;
        this.tab = tab;
    }
}
