package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.Question;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class chatPageController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private server.user currentUser;
    public void setUser(server.user u) {
        this.currentUser = u;
    }

    @FXML private Label topic_label;
    @FXML private TextField chatBox;
    @FXML private ListView<String> listView;

    private forumCard card;


    public void prefil(forumCard card) {

        this.card = card;
        topic_label.setText(card.topic);
        load();

    }
    @FXML
    private VBox questionBox;
    List<Question> ques = new ArrayList<>();

    public void send(ActionEvent e){

        String msg;
        if(Controller.getRole().equalsIgnoreCase("Student")){
            msg = "SEND_MSG" + "|" + card.username + "|"+ Controller.getUnameString() + "|" + card.topic + "|" + chatBox.getText();
        }
        else msg = "SEND_MSG" + "|" + card.username + "|"+ currentUser.username + "|" + card.topic + "|" + chatBox.getText();
        System.out.println(msg);

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);

            String response = (String) in.readObject();
            //System.out.println(response);

            chatBox.clear();
            load();

        } catch (Exception E) {
            E.printStackTrace();
        }

    }

    public void load(){
        listView.getItems().clear();
        String msg = "LOAD_CHAT|" + card.username + "|" + card.topic;

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);

            List<String> response = (List<String>) in.readObject();
                for (String line : response) {
                    listView.getItems().add(line);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
