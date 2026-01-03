package client;

import animatefx.animation.FadeInLeft;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {

    private static final ThreadLocal<String> role = new ThreadLocal<>();
    private static final ThreadLocal<String> unameString = new ThreadLocal<>();
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static String getRole(){
        return role.get();
    }
    public static String getUnameString(){
        return unameString.get();
    }
    public static void setUnameString(String s){
        unameString.set(s);
    }
    public static void setRole(String s){role.set(s);}

    @FXML
    private Label welcomeLabel;

    private final String text = "Kindly choose your role\nto proceed";
    private int idx = 0;

    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                idx = 0;
                welcomeLabel.setText("");

                PauseTransition pause = new PauseTransition(Duration.millis(100));
                pause.setOnFinished(new javafx.event.EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        typeWriterEffect();
                    }
                });
                pause.play();
            }
        });
    }


    private void typeWriterEffect() {
        welcomeLabel.setText("");
        Timeline timeline = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(50), new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (idx < text.length()) {
                    welcomeLabel.setText(welcomeLabel.getText() + text.charAt(idx));
                    idx++;
                }
            }
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(text.length());
        timeline.play();
    }

    public void Teacher(ActionEvent e)throws IOException {


        //System.out.println("Teacher");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        authController controller = loader.getController();
        controller.setOption("Teacher");
        setRole("Teacher");
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/auth.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new FadeInLeft(root).play();
    }

    public void Student(ActionEvent e)throws IOException {
        //System.out.println("Student");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        authController controller = loader.getController();
        controller.setOption("Student");
        setRole("Student");
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/auth.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new FadeInLeft(root).play();
    }
}