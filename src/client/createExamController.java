package client;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.SlideInRight;
import animatefx.animation.SlideInUp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class createExamController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    List<Question> ques = new ArrayList<>();
    List<quesController> controllers = new ArrayList<>();

    @FXML private ComboBox<String> classBox;
    @FXML private ComboBox<String> subBox;
    @FXML private TextField durationBox;
    @FXML private TextField title;

    @FXML private VBox questionBox;


    private server.user currentUser;

    @Override
    public void initialize(URL location , ResourceBundle res) {
        classBox.getItems().addAll("Class 9", "Class 10");
        subBox.getItems().addAll("Physics", "Chemistry");
    }

    public void setUser(server.user u) {
        this.currentUser = u;
    }

    public void load(ActionEvent e) throws IOException {
        String msg;
        msg = "LOAD_QUES," + classBox.getValue() + "," + subBox.getValue();
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
                Question question = new Question(classBox.getValue(), subBox.getValue(),parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                ques.add(question);
            } else continue;

            }

        for (int i = 0; i < ques.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("question.fxml"));
            Parent card = loader.load();
            quesController controller = loader.getController();

            controller.setQuestion(ques.get(i), i + 1, this);
            controllers.add(controller);
            questionBox.getChildren().add(card);
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

    public void submit(ActionEvent e) throws IOException {
        String msg;
        List<Question> selected = new ArrayList<>();

        String idx = "";

        int i = 0;
        for(quesController c : controllers){
            if(c.isSelected()){
                idx += i +",";
                i++;
                selected.add(c.ques);
            }
        }
        msg = "SAVE_EXAM," + classBox.getValue() + "," + subBox.getValue() + "," + title.getText() + "," +durationBox.getText();
        //System.out.println(msg);

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);
            out.writeObject(selected);
            out.writeObject(idx);

            String response = (String) in.readObject();

            //System.out.println(response);
            socket.close();

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void editQuestion(Question q) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Addquestion.fxml"));
                Parent root = loader.load();

                AddquestionController controller = loader.getController();
                controller.setUser(currentUser);
                controller.prefil(q);

                Stage currentStage = (Stage) classBox.getScene().getWindow();

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
                currentStage.setScene(scene);
                currentStage.show();

                new SlideInRight(root).play();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}


