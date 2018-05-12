package me.david.sploty4.debugger;

import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.tab.BrowserTab;

import java.util.ArrayList;
import java.util.List;

public class Debugger extends Pane {

    @Getter private final BrowserTab browserTab;
    @Getter @Setter private List<DebugTab> debugTabs = new ArrayList<>();
    @Getter TabPane tabs = new TabPane();

    public Debugger(final BrowserTab browserTab) {
        this.browserTab = browserTab;
        setVisible(false);

        tabs.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        debugTabs.add(new Console());

        debugTabs.forEach(debugTab -> tabs.getTabs().add(debugTab));

        getChildren().add(tabs);
    }

    public DebugTab getDebugTabByClass(Class<? extends DebugTab> clazz) {
        for (final DebugTab tab : debugTabs)
            if (tab.getClass().equals(clazz))
                return tab;

        Sploty.getLogger().warn("Count not find Debugging Tab named: '" + clazz.getSimpleName() + "'");
        return null;
    }

    public void toggle() {
        setVisible(!isVisible());
        if (isVisible()) {
            browserTab.getSplit().getItems().add(1, this);
            browserTab.getSplit().setDividerPosition(1, 0.8);
        } else browserTab.getSplit().getItems().remove(this);
        layout();
        requestLayout();
    }

}
