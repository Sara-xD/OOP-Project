package client;

import animatefx.animation.FadeInRight;
import animatefx.animation.SlideInUp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class examCardController {

    @FXML private Label title_label;
    @FXML private Label topic_label;
    @FXML private Label class_label;
    @FXML private Label sub_label;
    @FXML private Label duration_label;
    @FXML private Label mark_label;
    private server.user currentUser;

    public examCard card;

    public void setUser(server.user u) {
        this.currentUser = u;
    }


    public void setCard(examCard card) {
        this.card = card;
        title_label.setText(card.topic);
        topic_label.setText(card.topic);
        class_label.setText(card.cls);
        sub_label.setText(card.sub);
        duration_label.setText(card.dur);
        mark_label.setText(card.mark);
    }

    public void view(ActionEvent e) {

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewExam.fxml"));
            Parent root = loader.load();

            viewExamController controller = loader.getController();
            controller.setUser(currentUser);
            controller.prefil(card);
            controller.setUser(currentUser);

            Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            new FadeInRight(root).play();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
