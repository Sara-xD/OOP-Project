package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class forumBoxController {

    public forumCard card;
    @FXML
    private Label topic_label;

    @FXML
    private Label post_label;

    private server.user currentUser;
    List<forumBoxController> controllers = new ArrayList<>();

    public void setUser(server.user u) {
        this.currentUser = u;
    }

    public void setCard(forumCard card) {
        this.card = card;
        topic_label.setText(card.topic);
        post_label.setText(card.username);

    }

    public void view(ActionEvent e) {

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("chat_page.fxml"));
            Parent root = loader.load();

            chatPageController controller = loader.getController();
            controller.setUser(currentUser);
            controller.prefil(card);
            controller.setUser(currentUser);

            Stage chatStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
            chatStage.setScene(scene);
            chatStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
