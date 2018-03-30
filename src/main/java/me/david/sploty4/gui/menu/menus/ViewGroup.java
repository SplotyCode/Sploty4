package me.david.sploty4.gui.menu.menus;

import javafx.application.Application;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import me.david.sploty4.Sploty;
import me.david.sploty4.gui.Window;
import me.david.sploty4.gui.menu.MenuGroup;

public class ViewGroup extends MenuGroup {

    public ViewGroup(Window window) {
        super(window, "View");
        addToggle("Fullscreen", false, (observableValue, aBoolean, t1) -> window.getStage().setFullScreen(observableValue.getValue()));
        Menu menu = new Menu("Design");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioMenuItem caspian = new RadioMenuItem("Caspian");
        RadioMenuItem modena = new RadioMenuItem("Modena(Default)");
        modena.setSelected(true);
        modena.setToggleGroup(toggleGroup);
        caspian.setToggleGroup(toggleGroup);
        menu.getItems().addAll(modena, caspian);
        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> Application.setUserAgentStylesheet(modena.isSelected()?Application.STYLESHEET_MODENA:Application.STYLESHEET_CASPIAN));
        getItems().add(menu);
    }
}
