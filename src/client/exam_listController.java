package client;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.SlideInUp;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class exam_listController{

    private server.user currentUser;
    private Stage stage;
    private Scene scene;

    @FXML
    private HBox examCards;

    List<examCard> cards = new ArrayList<>();
    List<examCardController> controllers = new ArrayList<>();

    public void setUser(server.user u) {
        this.currentUser = u;
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardT.fxml"));

        Parent root = loader.load();
        dashtController controller = loader.getController();
        controller.setUser(currentUser);

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/dashboardT.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new FadeIn(root).play();
    }

    public void load() throws IOException {
        String msg;
        msg = "EXAM_LIST";
        //System.out.println(msg);

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);

            String response = (String) in.readObject();
            show(response);

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void show(String response) throws IOException {
        examCards.getChildren().clear();
        cards.clear();

        String[] lines = response.split("~");
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length == 5){

                examCard card = new examCard(parts[0], parts[1], parts[2], parts[3], parts[4]);
                cards.add(card);
            }
        }

        for (int i = 0; i < cards.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("examCard.fxml"));
            Parent card = loader.load();
            examCardController controller = loader.getController();
            controller.setUser(currentUser);

            controller.setCard(cards.get(i));
            controllers.add(controller);
            examCards.getChildren().add(card);

        }
    }
}
