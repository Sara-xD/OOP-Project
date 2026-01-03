package client;

import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class forumController {


    private server.user currentUser;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private VBox forumCards;

    List<forumCard> cards = new ArrayList<>();
    List<forumBoxController> controllers = new ArrayList<>();

    public void setUser(server.user u) {
        this.currentUser = u;

        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(ActionEvent e) throws IOException {
        System.out.println("currentUser in forum: " + currentUser);
        System.out.println("currentUser role: " + (currentUser != null ? currentUser.role : "NULL"));
        if(currentUser.role.equalsIgnoreCase("Teacher")) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardT.fxml"));

            Parent root = loader.load();
            dashtController controller = loader.getController();
            controller.setUser(this.currentUser);

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/dashboardT.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            new FadeIn(root).play();
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));

            Parent root = loader.load();
            studentDashController controller = loader.getController();
            controller.setUser(this.currentUser);

            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            new FadeIn(root).play();
        }
    }

    public void post(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/post.fxml"));
        Parent root = loader.load();
        postController controller = loader.getController();
        controller.setUser(currentUser);
        controller.setCntrl(this);

        Stage postStage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        postStage.setScene(scene);
        postStage.show();
    }

    public void load() throws IOException {

        String msg;
        msg = "LOAD_FORUM";
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
        forumCards.getChildren().clear();
        cards.clear();

        String[] lines = response.split("~");
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length != 0){

                forumCard card = new forumCard(parts[0], parts[1]);
                cards.add(card);
            }
        }

        for (int i = 0; i < cards.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("forumBox.fxml"));
            Parent card = loader.load();
            forumBoxController controller = loader.getController();
            controller.setUser(currentUser);

            controller.setCard(cards.get(i));
            controllers.add(controller);
            forumCards.getChildren().add(card);

        }
    }

}
