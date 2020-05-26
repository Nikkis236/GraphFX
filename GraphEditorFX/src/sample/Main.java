package sample;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import View.form.AppForm;


public class Main extends Application {
    public static final double MAIN_FORM_HEIGHT = 768;
    public static final double MAIN_FORM_WIDTH = 1366;


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graph Editor");
        primaryStage.setScene(new Scene(new AppForm(primaryStage).getVBox()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
