package client;
import animatefx.animation.FadeIn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;


public class examController {


    @FXML
    private Label gradeLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label topicLabel;
    @FXML
    private Label marksLabel;
    @FXML
    private Label qnoLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button examButton;
    @FXML
    private Pane timePane;
//    @FXML
//    private Button attendButton;


    private int index;


    private Stage stage;
    private Scene scene;
    private Parent root;


//    public examController(String grade, String sub, String topic, String marks, String qno, String time){
//        this.grade = grade;
//        this.sub = sub;
//        this.topic = topic;
//        this.marks = marks;
//        this.qno = qno;
//        this.time = time;
//        gradeLabel.setText("Class: "+grade);
//        subjectLabel.setText(sub);
//        topicLabel.setText(topic);
//        marksLabel.setText("Marks: "+marks);
//        qnoLabel.setText("No. of Questions: "+qno);
//        timeLabel.setText("Time: "+time+" minutes");
//        System.out.println("ppppppp");
//    }


    @FXML
    public void setExamData(int index,String grade, String sub, String topic, String marks, String gottenMarks, String remainingTime, String qno, String time){
        this.index = index;
        gradeLabel.setText("Class: "+grade);
        subjectLabel.setText(sub);
        topicLabel.setText(topic);
        if(gottenMarks.equals("-2")) marksLabel.setText("Marks: "+marks);
        else marksLabel.setText("Marks: "+gottenMarks+" / "+marks);
        qnoLabel.setText("No. of Questions: "+qno);
        timeLabel.setText("Time: "+time+" minutes");
        if(studentDashController.getViewMode().equals("viewResult")) examButton.setText("View Result");
        if(studentDashController.getViewMode().equals("leaderBoard")) examButton.setText("View Scoreboard");
        if(!studentDashController.getViewMode().equals("takeExam")) {
            qnoLabel.setText("Time: " + time + " minutes");
            if (remainingTime.equals("-3")) {
                timeLabel.setText("Not attended");
            } else {
                int min = Integer.parseInt(remainingTime) / 60;
                int sec = Integer.parseInt(remainingTime) % 60;
                timeLabel.setText("Submitted " + min + " min " + sec + " seconds early");
                timePane.setPrefWidth(244);


            }
        }
    }


    public void attendButton(javafx.event.ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("examPortal.fxml"));
        root = loader.load();
        examPortalController portalController = loader.getController();
        portalController.setExamIndex(index);
        portalController.seteForTimeUp(e);
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
