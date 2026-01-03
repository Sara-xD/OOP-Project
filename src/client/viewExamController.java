package client;

import animatefx.animation.FadeIn;
import animatefx.animation.SlideInUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class viewExamController {

    @FXML
    private TextField titleBox;
    @FXML
    private TextField classBox;
    @FXML
    private TextField subjectBox;
    @FXML
    private TextField durationBox;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private server.user currentUser;
    @FXML private VBox questionBox;
    List<Question> ques = new ArrayList<>();
    private examCard card;


    public void setUser(server.user u) {
        this.currentUser = u;
    }

    public void prefil(examCard card) {

        this.card = card;
        titleBox.setText(card.topic);
        classBox.setText(card.cls);
        subjectBox.setText(card.sub);
        durationBox.setText(card.dur);


        String msg;
        msg = "SHOW_EXAM," + card.topic;
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
        questionBox.getChildren().clear();
        ques.clear();

        String[] lines = response.split("~");
        for (String line : lines) {
            //System.out.println(line);
            String[] parts = line.split("\\|");
            if (parts.length == 7){
                Question question = new Question(card.cls, card.sub, parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                ques.add(question);
            } else continue;

        }

        for (int i = 0; i < ques.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("question2.fxml"));
            Parent card = loader.load();
            ques2Controller controller = loader.getController();


            controller.setQuestion(ques.get(i), i + 1);
            questionBox.getChildren().add(card);
        }
    }

    public void back(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("exams_list.fxml"));

        Parent root = loader.load();
        exam_listController controller = loader.getController();
        controller.setUser(currentUser);

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new FadeIn(root).play();

    }


}
