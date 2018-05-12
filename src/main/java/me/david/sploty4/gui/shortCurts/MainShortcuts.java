package me.david.sploty4.gui.shortCurts;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import me.david.sploty4.gui.Window;

public class MainShortcuts {

    private Window window;

    public MainShortcuts(Window window){
        this.window = window;
        add(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN), () -> window.getTabBar().getTabs().remove(window.getTabBar().getCurrentTab()));
        add(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN), () -> window.getTabBar().getCurrentTab().getUrlBar().requestFocus());
        add(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), () -> window.getTabBar().getCurrentTab().reload());
        add(new KeyCodeCombination(KeyCode.F5), () -> window.getTabBar().getCurrentTab().reload());
        window.getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            KeyCodeCombination keyCode = new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_DOWN);
            if (keyCode.match(keyEvent)) {
                keyEvent.consume();
                if (keyEvent.isShiftDown()) window.getTabBar().getSelectionModel().selectPrevious();
                else window.getTabBar().getSelectionModel().selectNext();
            }
        });
    }

    private void add(KeyCombination key, Runnable runnable){
        window.getScene().addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if(key.match(keyEvent)) {
                runnable.run();
                keyEvent.consume();
            }
        });
    }
}
