package client;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("landing.fxml"));
        Scene scene = new Scene(root);
        String css = getClass().getResource("/landing.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/app_iconwithoutbg.png"))
        );

        primaryStage.setScene(scene);
        primaryStage.show();

        new FadeIn(root).play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
