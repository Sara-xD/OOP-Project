package client;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.SlideInUp;
import animatefx.animation.SlideOutDown;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import javafx.event.ActionEvent;
import javafx.stage.Stage;
import shared.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AddquestionController implements Initializable {


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private ComboBox<String> classBox;
    @FXML private ComboBox<String> subBox;
    @FXML private ComboBox<String> optionBox;
    @FXML private TextField question;
    @FXML private TextField optionA;
    @FXML private TextField optionB;
    @FXML private TextField optionC;
    @FXML private TextField optionD;
    @FXML private TextField explanation;

    private server.user currentUser;

    public Question old;
    public Question updated;
    public boolean editMode = false;

    public void setUser(server.user u) {
        this.currentUser = u;
    }

    @Override
    public void initialize(URL location , ResourceBundle res) {
        //System.out.println("check ques" + question);
        classBox.getItems().addAll("Class 9", "Class 10");
        subBox.getItems().addAll("Physics", "Chemistry");
        optionBox.getItems().addAll("A", "B", "C", "D");
    }

    public void submit(ActionEvent e) throws IOException {
        String msg;

        if(editMode) {
            updated = new Question(classBox.getValue(), subBox.getValue(), question.getText(), optionA.getText(), optionB.getText(), optionC.getText(),
                    optionD.getText(), optionBox.getValue(), explanation.getText()
            );

            msg = "EDITQUESTION|" + classBox.getValue() + "|" + subBox.getValue() + "|" + old.makeString() + "|" + classBox.getValue() + "|" + subBox.getValue() + "|" + updated.makeString();

        }else {
            msg = "ADDQUESTION|" + classBox.getValue() + "|" + subBox.getValue() + "|" + question.getText() + "|" + optionA.getText() + "|" + optionB.getText() + "|" + optionC.getText() + "|" + optionD.getText() + "|" + optionBox.getValue() + "|" + explanation.getText();
            back(e);
            //System.out.println(msg);
        }
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);

            String response = (String) in.readObject();

            //System.out.println(response);

        } catch (Exception E) {
            E.printStackTrace();
        }

        if(editMode) {
            createExamPage(e);
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

    public void createExamPage(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/createExam.fxml"));
        Parent root = loader.load();

        createExamController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        new SlideInUp(root).play();

    }

    public void prefil(Question q) {
        editMode = true;
        old = q;

        classBox.setValue(q.cls);
        subBox.setValue(q.sub);
        question.setText(q.question);
        optionA.setText(q.optionA);
        optionB.setText(q.optionB);
        optionC.setText(q.optionC);
        optionD.setText(q.optionD);
        optionBox.setValue(q.correctOption);
        explanation.setText(q.explanation);
    }



}
