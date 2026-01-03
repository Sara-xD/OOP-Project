package client;

import animatefx.animation.FadeInLeft;
import animatefx.animation.SlideInUp;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class dashtController {

    @FXML private HBox examCardContainer;
    @FXML private Label nameLabel;
    @FXML private Label stnum;
    @FXML private Label examnum;

    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;

    private server.user currentUser;



    public void setUser(server.user u) {

        this.currentUser = u;
        nameLabel.setText("Hello " + u.name);
        counter counter = new counter();
        int examNum = counter.count("exam") -1;
        int stunum = counter.count("student") -1;
        examnum.setText("" + examNum);
        stnum.setText("" + stunum);

    }

    public void CreateExam(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/createExam.fxml"));
        Parent root = loader.load();

        createExamController controller = loader.getController();
        controller.setUser(this.currentUser);

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new SlideInUp(root).play();

    }

    public void AddQuestion(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/Addquestion.fxml"));
        Parent root = loader.load();
        AddquestionController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new SlideInUp(root).play();
    }

    public void ViewExams(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/exams_list.fxml"));
        Parent root = loader.load();
        exam_listController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new SlideInUp(root).play();

    }

    public void analyze(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/analyze.fxml"));
        Parent root = loader.load();
        analyzeController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new SlideInUp(root).play();

    }

    public void logout(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/landing.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/landing.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void forum(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/forum.fxml"));
        Parent root = loader.load();
        forumController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();


    }



}
