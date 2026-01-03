package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class postController {

    private forumController cntrl;

    public void setCntrl(forumController fc) {
        this.cntrl = fc;
    }

    private server.user currentUser;
    public void setUser(server.user u) {
        this.currentUser = u;
    }

    @FXML
    private TextArea topic_box;
    @FXML
    private TextField ques_box;

    public void submit(ActionEvent e) throws IOException {
        String msg;

        if(Controller.getRole().equalsIgnoreCase("Student")){
            msg = "FORUM_ADD" + "|" + Controller.getUnameString() + "|" + topic_box.getText() + "|" + ques_box.getText();
        }
        else msg = "FORUM_ADD" + "|" + currentUser.username + "|" + topic_box.getText() + "|" + ques_box.getText();
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);

            String response = (String) in.readObject();

            //System.out.println(response);

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();

            cntrl.load();

        } catch (Exception E) {
            E.printStackTrace();
        }

    }

}
