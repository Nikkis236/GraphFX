package View.form;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AppForm {
    private AppMenu appMenu;
    private GraphTabPane graphTabPane;
    private GraphToolBar graphToolBar;
    private GraphStatusBar graphStatusBar;

    private VBox vBox;


    public AppForm(Stage stage) {
        graphToolBar = new GraphToolBar();
        graphStatusBar = new GraphStatusBar();
        graphTabPane = new GraphTabPane(graphToolBar, graphStatusBar);
        appMenu = new AppMenu(graphTabPane, stage);

        vBox = new VBox();

        vBox.setStyle("-fx-background-color: #f4f9fd");
        configureVBox();
    }

    public VBox getVBox() {
        return vBox;
    }

    /*
        Configs
     */

    private void configureVBox() {
        vBox.getChildren().addAll(
                appMenu.getMenuBar(),
                graphTabPane.getTabPane(),
                graphToolBar.getToolBar(),
                graphStatusBar.getStatusBar()
        );
    }
}
