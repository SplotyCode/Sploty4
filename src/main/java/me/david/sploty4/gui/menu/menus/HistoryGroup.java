package me.david.sploty4.gui.menu.menus;

import javafx.scene.control.ChoiceDialog;
import me.david.sploty4.Sploty;
import me.david.sploty4.features.History;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;

import java.util.ArrayList;
import java.util.Optional;

public class HistoryGroup extends MenuGroup implements History.Listener {

    public HistoryGroup(Window window) {
        super(window, "History");
        Sploty.getInstance().getHistory().addListener(this);
        onUpdate();
    }

    @Override
    public void onUpdate() {
        getItems().clear();
        History history = Sploty.getInstance().getHistory();
        add("Clear Recent History", (event -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Last Hour", "Last Hour", "Last 4 Hours", "Today", "This Week", "Everything");
            dialog.setTitle("Clear Recent History");
            dialog.setHeaderText("Clear Recent History");
            dialog.setContentText("Time frame: ");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                long currentDay = System.currentTimeMillis()/1000/60/60/24;
                if(s.equals("Everything")) history.getHistory().clear();
                else {
                    for (History.HistoryEntry entry : new ArrayList<>(history.getHistory())) {
                        long entryDay = entry.getTime() / 1000 / 60 / 60 / 24;
                        long delay = System.currentTimeMillis() - entry.getTime();
                        switch (s) {
                            case "Last Hour":
                                if (delay < 1000 * 60 * 60)
                                    history.getHistory().remove(entry);
                                break;
                            case "Last 4 Hours":
                                if (delay < 1000 * 60 * 60 * 4)
                                    history.getHistory().remove(entry);
                                break;
                            case "Today":
                                if(currentDay != entryDay)
                                    history.getHistory().remove(entry);
                                break;
                            case "This Week":
                                if(currentDay/7 != entryDay/7)
                                    history.getHistory().remove(entry);
                                break;
                            default: throw new IllegalStateException(s + " was not expected");
                        }
                    }
                }
                history.getListeners().forEach(History.Listener::onUpdate);
            });
        }));
        int i = 0;
        while (!history.getHistory().isEmpty() && i != 15 && history.getHistory().size()-1-i > 0){
            History.HistoryEntry entry = history.getHistory().get(history.getHistory().size()-1-i);
            add(entry.getDisplayname(), event -> getWindow().getTabBar().getCurrentTab().openNew(entry.getUrl(), true));
            i++;
        }
    }
}
