package client;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;



public class studentDashController implements Initializable{

    @FXML
    private BarChart<String, Number> chart;

    private static final ThreadLocal<String> viewMode = new ThreadLocal<>();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label nameLabel1;

    private server.user currentUser;
    public void setUser(server.user u) {

        this.currentUser = u;


    }

    public static String getViewMode(){
        return viewMode.get();
    }

    public void takeExam(ActionEvent e) throws IOException {
        //updateExamInfo();
        viewMode.set("takeExam");
        root = FXMLLoader.load(getClass().getResource("ExamPicker.fxml"));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        new FadeInRight(root).play();
    }

    public void viewResult(ActionEvent e) throws IOException {
        //updateExamInfo();
        viewMode.set("viewResult");
        root = FXMLLoader.load(getClass().getResource("ExamPicker.fxml"));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        new FadeInRight(root).play();
    }

    public void leaderBoard(ActionEvent e) throws IOException {
        //updateExamInfo();
        viewMode.set("leaderBoard");
        root = FXMLLoader.load(getClass().getResource("ExamPicker.fxml"));
        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        new FadeInRight(root).play();
    }

    public void forum(ActionEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/forum.fxml"));
        Parent root = loader.load();
        forumController controller = loader.getController();
        controller.setUser(currentUser);
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/exam.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        new FadeInRight(root).play();

    }

    public void logout(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/landing.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/landing.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        new FadeInLeft(root).play();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        String uname = Controller.getUnameString();
        nameLabel1.setText(uname);

        String markslist = null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("MARKSLIST@");
            markslist = (String) in.readObject();
            socket.close();
        } catch (Exception E) {
            E.printStackTrace();
        }

        Stack<Double> stack = new Stack<>();
        Stack<String> titles = new Stack<>();

        String[] markslists = markslist.split("~");
        for( String entry : markslists){
            String[] parts = entry.split(",");
            if(parts[2].equals(uname)){
                stack.push(Double.parseDouble(parts[4])/Double.parseDouble(parts[3]));
                titles.push(parts[0]);
            }
        }

        //String[] categories = {"a", "b", "c", "d", "e", "f", "g", "h"};
//        for(int i=0; i<5; i++){
//            if(titles.isEmpty()){
//                categories[i] = "";
//            }
//            else{
//                categories[i] = titles.pop();
//            }
//        }


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 1; i < 6; i++) {
            double percent = stack.isEmpty() ? 0 : stack.pop() * 100;
            series.getData().add(new XYChart.Data<>(String.valueOf(i), percent));
        }
        chart.getData().clear();
        chart.getData().add(series);
    }


}
