package com.zy.exercise3;

import com.zy.exercise3.Controller.infController;
import com.zy.exercise3.Controller.mainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    public static Stage mainStage;
    public static Stage infoStage;
    public static mainController mainViewController;
    public static infController infViewController;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 710, 450);
        stage.setTitle("Exercise4");
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });

        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("inf-view.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load(), 567, 325);
        Stage infStage = new Stage();
        infStage.setScene(scene2);
        infStage.setResizable(false);
        infoStage = infStage;

        infStage.initOwner(stage);
        infStage.setTitle("Date_and_time");
        mainViewController = fxmlLoader.getController();
        infViewController = fxmlLoader2.getController();

        mainStage.getIcons().setAll(new Image(
                Objects.requireNonNull(HelloApplication.class.getResourceAsStream("icon.png"))));
        infoStage.getIcons().setAll(new Image(
                Objects.requireNonNull(HelloApplication.class.getResourceAsStream("icon.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}