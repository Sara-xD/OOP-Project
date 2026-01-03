package client;
import animatefx.animation.FadeIn;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.Record;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;


public class examPortalController {


    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private VBox qsContainer;


    @FXML
    private AnchorPane innerAnchorPane;


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
    private Label timeLabel1;
    @FXML
    private Label scoreLabel;
    //    @FXML
//    private Pane timePane1;
    @FXML
    private Pane scorePane1;


    private int examIndex;
    private String grade;
    private String sub;
    private String topic;
    private String marks;
    private String qno;
    private String time;
    private String score;
    private Button submitButton;
    private ActionEvent eForTimeUp;






//    public void setExamData(int examIndex,String grade,String sub,String topic, String marks,String qno,String time){
//        this.examIndex = examIndex;
//        this.grade = grade;
//        this.sub = sub;
//        this.topic = topic;
//        this.marks =marks;
//        this.qno = qno;
//        this.time = time;
//    }


    private String quesFile;
    private String student_answersheets;
    private String[] questions;
    private String[] argus;
    private String[] answers;
    private String[] student_answersheets_strings;
    private Timeline timer;
    private int timeRemaining = 0;


    public void seteForTimeUp(ActionEvent eForTimeUp) {
        this.eForTimeUp = eForTimeUp;
    }


    public void startCountdown(int[] seconds) {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(seconds[0] > 1){
                seconds[0]--;
                timeRemaining--;
                int min = seconds[0] / 60;
                int sec = seconds[0] % 60;
                timeLabel.setText(String.format("%02d : %02d", min, sec));
            } else{
                submitButton.fire();
            }
        }));
        timer.setCycleCount(seconds[0]);
        timer.play();
    }






    public void init(){


        innerAnchorPane.prefHeightProperty().bind(qsContainer.heightProperty());


//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(new File("src/client/examList.txt"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        String examList = scanner.nextLine();


        student_answersheets=null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("STUDENT_ANSWERSHEET#"+Controller.getUnameString());
            student_answersheets = (String) in.readObject();
            socket.close();
        } catch (Exception E) {
            E.printStackTrace();
        }


        student_answersheets_strings = student_answersheets.split("~");


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


        String[] positionData = null;
        if(!studentDashController.getViewMode().equals("takeExam")) {
            String positionString = null;
            try {
                Socket socket = new Socket("127.0.0.1", 45454);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("POSITION#"+examIndex+"#"+student_answersheets_strings[9+3*examIndex+1]+"#"+student_answersheets_strings[9+3*examIndex+2]);
                positionString = (String) in.readObject();
                socket.close();


            } catch (Exception E) {
                E.printStackTrace();
            }
            positionData = positionString.split("@");
        }




        String[] exams = examList.split("~");
        argus = exams[examIndex].split(",");
        grade = argus[0];
        sub = argus[1];
        topic = argus[2];
        marks = argus[3];
        qno = argus[4];
        time = argus[5];


        topicLabel.setText(topic);
        gradeLabel.setText("Class: "+grade);
        subjectLabel.setText("Subject: "+sub);
        marksLabel.setText("Total marks: "+ marks);
        qnoLabel.setText("No. of questions: "+qno);




        if(!studentDashController.getViewMode().equals("takeExam")){
            scoreLabel.setText("Score: "+student_answersheets_strings[9+3*examIndex+1]+" / "+marks);
            if (marks.equals("-2")) timeLabel.setText("Not Attended");
            else {
                String suffix = "";
                if(Integer.parseInt(positionData[0])%10==1) suffix+= "st";




                else if(Integer.parseInt(positionData[0])%10==2) suffix+= "nd";
                else if(Integer.parseInt(positionData[0])%10==3) suffix+= "rd";
                else suffix += "th";
                timeLabel.setText(positionData[0]+suffix);
                int min = Integer.parseInt(student_answersheets_strings[9+3*examIndex+2]) / 60;
                int sec = Integer.parseInt(student_answersheets_strings[9+3*examIndex+2]) % 60;
                marksLabel.setText("Time: " + time + " minutes\n");
                scorePane1.setVisible(true);
                scorePane1.setManaged(true);
//                timeLabel1.setText("Submitted "+ min + " min " + sec + " seconds early");
//                timePane1.getStyleClass().setAll("pane-blue");
                scorePane1.getStyleClass().setAll("pane-class_sub_ques_mark_score");
            }
        } else{
            timeLabel.setText(String.format("%02d : 00", Integer.parseInt(time)));
            int[] timesec = {Integer.parseInt(time)*60};
            timeRemaining = Integer.parseInt(time)*60;
            startCountdown(timesec);
            scorePane1.setVisible(false);
            scorePane1.setManaged(false);
        }




//        quesFile = "src/server/Class "+argus[0]+"_"+argus[1]+".txt";
//        Scanner scanner2 = null;
//        try {
//            scanner2 = new Scanner(new File(quesFile));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        String quesList = scanner2.nextLine();
        String quesList = null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


            out.writeObject("QUESLIST#"+"Class "+argus[0]+"_"+argus[1]);


            quesList = (String) in.readObject();


            //System.out.println(examList);
            socket.close();
        } catch (Exception E) {
            E.printStackTrace();
        }


        questions = quesList.split("~");
        qsContainer.setAlignment(Pos.CENTER);


//        System.out.println("printing questions");
//        for(String question: questions){
//            System.out.println(question);
//        }jhgdfdjkfjk
        if (studentDashController.getViewMode().equals("leaderBoard")){
            List<Record> list = null;
            try {
                Socket socket = new Socket("127.0.0.1", 45454);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());


                String msg = "LEADERBOARD#"+examIndex;
                out.writeObject(msg);


                list = (List<Record>) in.readObject();
                socket.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            int leaderBoardIndex = 1;
            for(var record : list ){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("leaderboard.fxml"));
                    Node quesNode = loader.load();
                    leaderboardController controller = loader.getController();
                    controller.setLBdata(leaderBoardIndex, record.value1, record.value2, record.name);
                    qsContainer.getChildren().add(quesNode);
                    leaderBoardIndex++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            submitButton = new Button("Back");
            submitButton.setPrefWidth(120);
            submitButton.setPrefHeight(40);
            VBox.setMargin(submitButton, new Insets(32, 0, 0, 0));
            submitButton.getStyleClass().setAll("button5");
            submitButton.setAlignment(Pos.CENTER);
            qsContainer.getChildren().add(submitButton);
            submitButton.setOnAction(e -> {
                back(e);
            });


        }


        else{
            for (int i = 6; i < argus.length; i++) {


                try {
                    String[] qparts = questions[Integer.parseInt(argus[i])].split("@");


//                System.out.println("printing questions");
//                for(String qpart: qparts){
//                    System.out.println(qpart);
//                }


                    if(studentDashController.getViewMode().equals("takeExam")){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("question1.fxml"));
                        Node quesNode = loader.load();
                        questionController controller = loader.getController();
                        controller.setExamPortalController(this);
                        controller.setQuesData(i-6,qparts[0], qparts[1], qparts[2], qparts[3], qparts[4], qparts[5]);
                        qsContainer.getChildren().add(quesNode);
                    }
                    else if (studentDashController.getViewMode().equals("viewResult")){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("result.fxml"));
                        Node quesNode = loader.load();
                        resultController controller = loader.getController();
                        controller.setExamIndex(examIndex);
                        controller.setQuesData(i-6,qparts[0], qparts[1], qparts[2], qparts[3], qparts[4], qparts[5], qparts[6], qparts[7], qparts[8], qparts[9], qparts[10]);
                        qsContainer.getChildren().add(quesNode);
                    }
                    else {}


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            answers = new String[Integer.parseInt(qno)];
            for(int i=0; i<Integer.parseInt(qno); i++){
                answers[i] = "O";
            }


            submitButton = new Button("Submit");
            submitButton.setPrefWidth(120);
            submitButton.setPrefHeight(40);
            VBox.setMargin(submitButton, new Insets(32, 0, 0, 0));
            submitButton.getStyleClass().setAll("button5");
            submitButton.setAlignment(Pos.CENTER);


            if(studentDashController.getViewMode().equals("takeExam")){
                qsContainer.getChildren().add(submitButton);


                submitButton.setOnAction(e -> {
                    submit(e);
                });
            }
            else{
                qsContainer.getChildren().add(submitButton);
                submitButton.setText("Back");


                submitButton.setOnAction(e -> {
                    back(e);
                });
            }


        }
    }


    public void setAnswer(String ans, int qind){
        answers[qind] = ans;
    }


    public void setExamIndex(int examIndex){
        this.examIndex = examIndex;
        init();
    }


    public void submit(ActionEvent e){
        timeRemaining--;
        timer.stop();
        String choicesheet = "";
        int gottenMark = 0;


        for(int i=0; i< argus.length-6; i++){
            String[] qparts = questions[Integer.parseInt(argus[6+i])].split("@");
            if(qparts[5].equals(answers[i])){
                gottenMark++;
            }
            choicesheet+=answers[i];
        }
//        String answersheet = "ANSWERSHEET"+Controller.unameString+","+String.valueOf(examIndex)+","+choicesheet;
//        System.out.println(answersheet);
//
//        try {
//            Socket socket = new Socket("127.0.0.1", 45454);
//            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//
//            out.writeObject(answersheet);
//
//            String response = (String) in.readObject();
//
//            System.out.println(response);
//
//        } catch (Exception E) {
//            E.printStackTrace();
//        }


//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader("src/client/"+ Controller.unameString+".txt"));
//            student_answersheets = reader.readLine();
//            reader.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(student_answersheets);


        String updated_answersheets="";
        for(int i=0; i<9+3*examIndex; i++){
            updated_answersheets+=student_answersheets_strings[i]+"~";
        }


        updated_answersheets+= choicesheet+'~'+gottenMark+'~'+timeRemaining;




        for(int i = 9+3*examIndex+3; i<student_answersheets_strings.length; i++){
            updated_answersheets+="~"+student_answersheets_strings[i];
        }


//        FileWriter writer = null;
//        try {
//            writer = new FileWriter("src/client/"+ Controller.unameString+".txt");
//            writer.write(updated_answersheets);
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("UPDATE_ANSWERSHEET#"+updated_answersheets+"#"+Controller.getUnameString());
            socket.close();;
        } catch (Exception E) {
            E.printStackTrace();
        }


        //updating markslist


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


        markslist += topic + "," + sub + "," + Controller.getUnameString() + "," + marks + "," + gottenMark + "~";


        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("UPDATE_MARKSLIST@"+markslist);
            socket.close();
        } catch (Exception E) {
            E.printStackTrace();
        }




        //updating ques option percentage trackers


        for(int i=0; i<choicesheet.length(); i++){
            char ans = choicesheet.charAt(i);
            if(ans-'O'!=0){
                String[] qparts = questions[Integer.parseInt(argus[6+i])].split("@");
                int choice = ans - 'A';
                String updatedQuestion = "";


                for(int j=0; j<6+choice; j++){
                    updatedQuestion+=qparts[j]+"@";
                }
                updatedQuestion+=Integer.parseInt(qparts[6+choice])+1;
                for(int j=6+choice+1; j<11; j++){
                    updatedQuestion+="@"+qparts[j];
                }


                questions[Integer.parseInt(argus[6+i])] = updatedQuestion;
            }
        }
        String updatedQuesFile = "";
        for(var question : questions){
            updatedQuesFile += question + "~";
        }
        System.out.println(updatedQuesFile);


        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("UQF#"+updatedQuesFile+"#"+"Class "+grade+"_"+sub);
            socket.close();
        } catch (Exception E) {
            E.printStackTrace();
        }


        try {
            root = FXMLLoader.load(getClass().getResource("StudentDashboard.fxml"));
            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            new FadeIn(root).play();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }


    }


    public void back(ActionEvent e){
        try {
            root = FXMLLoader.load(getClass().getResource("ExamPicker.fxml"));
            stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/studentdashboard.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/panes.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/buttons.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/labels.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            new FadeIn(root).play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }






}
