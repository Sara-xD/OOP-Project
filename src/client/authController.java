package client;

import animatefx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;


public class authController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TextField uname;
    @FXML private PasswordField pass;
    @FXML private TextField name;
    @FXML private Label mode;


    private String option ;
    private int signUpflag = 0;



    @FXML
    private Pane authPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));


        authPane.setEffect(shadow);

    }

    public void setOption(String option) {
        this.option = option;
    }

    public void login(ActionEvent e)throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/auth.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        signUpflag = 0;

        new SlideInLeft(root).play();
    }

    public void signup(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Parent root = loader.load();

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/auth.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        authController controller = loader.getController();
        controller.setOption(this.option);
        controller.setMode("signup");
        new SlideInRight(root).play();


    }

    public void submit(ActionEvent e){
        String msg;
        String page = mode.getText();
        Controller.setUnameString(uname.getText());
        System.out.println(Controller.getUnameString()+"client");
        if(mode.getText().equals("login")){
           msg = "LOGIN," + uname.getText() + "," + pass.getText();
        }
        else{signUpflag = 1;
            msg = "SIGNUP," + uname.getText() + "," + pass.getText() + "," + option + "," + name.getText();
        }

        try{
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);
            Object status = in.readObject();

            if (status instanceof server.user) {
                server.user u = (server.user) status;

                next(e, u);
            } else if (status instanceof String) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
                showStatus((String) status);
            }

        } catch (Exception E) {
            E.printStackTrace();
        }


    }

    public void showStatus(String status){
        if(status.startsWith("Done")){
            String page = mode.getText();
            if(mode.getText().equals("login")){
                System.out.println("Login successful");
            }
            else System.out.println("Signup Successful");
        }

        else System.out.println(status);

    }

    public void next(ActionEvent e, server.user u) throws IOException {
        if (u.role.equalsIgnoreCase("Teacher")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardT.fxml"));
            Parent root = loader.load();

            dashtController controller = loader.getController();
            controller.setUser(u);

            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/dashboardT.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

            new FadeIn(root).play();

        }

        else if (u.role.equalsIgnoreCase("Student")) {
            if(signUpflag==1){
                updateExamInfo();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));
            Parent root = loader.load();

            studentDashController controller = loader.getController();
            controller.setUser(u);

            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            new FadeInLeft(root).play();
        }


    }

    public void updateExamInfo(){
        String examList = null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("EXAMLIST");

            examList = (String) in.readObject();
            socket.close();
            //System.out.println(examList);

        } catch (Exception E) {
            E.printStackTrace();
        }

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("CREATE_ANSWERSHEET#"+Controller.getUnameString());
            socket.close();;
        } catch (Exception E) {
            E.printStackTrace();
        }

        String student_answersheets=null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("STUDENT_ANSWERSHEET#"+Controller.getUnameString());

            student_answersheets = (String) in.readObject();

            //System.out.println(examList);
            socket.close();

        } catch (Exception E) {
            E.printStackTrace();
        }

        String[] exams = examList.split("~");
        String[] answersheets = student_answersheets.split("~");

        int examNeeded = exams.length - (answersheets.length-9)/3;

       // System.out.println(exams.length+" "+examNeeded);

        for(int i = 0; i<examNeeded; i++){
            student_answersheets+="~-1~-2~-3";
        }

        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("UPDATE_ANSWERSHEET#"+student_answersheets+"#"+Controller.getUnameString());
            socket.close();;
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void setMode(String modeText) {
        mode.setText(modeText);
    }

}


