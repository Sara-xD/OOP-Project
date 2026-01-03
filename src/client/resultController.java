package client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class resultController {
    @FXML
    private Label Qlabel;
    @FXML
    private Label o1label;
    @FXML
    private Label o2label;
    @FXML
    private Label o3label;
    @FXML
    private Label o4label;


    @FXML
    private Label o1perc;
    @FXML
    private Label o2perc;
    @FXML
    private Label o3perc;
    @FXML
    private Label o4perc;


    @FXML
    private Label Qstatus;
    @FXML
    private Label o1status;
    @FXML
    private Label o2status;
    @FXML
    private Label o3status;
    @FXML
    private Label o4status;


    @FXML
    private Pane QstatusPane;
    @FXML
    private Pane o1statusPane;
    @FXML
    private Pane o2statusPane;
    @FXML
    private Pane o3statusPane;
    @FXML
    private Pane o4statusPane;


    @FXML
    private Pane o1filler;
    @FXML
    private Pane o2filler;
    @FXML
    private Pane o3filler;
    @FXML
    private Pane o4filler;


    @FXML
    private Label explanationLabel;


    private String correct;
    private int examIndex;


    public void setExamIndex(int examIndex){
        this.examIndex = examIndex;
    }


    @FXML
    public void setQuesData(int qindex, String q, String o1, String o2, String o3, String o4, String correct, String o1num, String o2num, String o3num, String o4num, String explanation){
        Qlabel.setText(qindex+". "+q);
        o1label.setText("(A) "+o1);
        o2label.setText("(B) "+o2);
        o3label.setText("(C) "+o3);
        o4label.setText("(D) "+o4);
        this.correct = correct;




        String student_answersheets = null;
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader("src/client/"+ Controller.unameString+".txt"));
//            student_answersheets = reader.readLine();
//            reader.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //System.out.println(student_answersheets);


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


        String[] student_answersheets_strings = student_answersheets.split("~");


        String answerString = String.valueOf(student_answersheets_strings[9+3*examIndex].charAt(qindex));


        if(correct.equals(answerString)){
            Qstatus.setText("Correct");
            QstatusPane.getStyleClass().setAll("pane-lightgreen-strongborder");
        }
        else if(answerString.equals("O")){
            Qstatus.setText("Not Answered");
            QstatusPane.getStyleClass().setAll("pane-mistyblue-strongborder");
        }
        else{
            Qstatus.setText("Incorrect");
            QstatusPane.getStyleClass().setAll("pane-rose-strongborder");
        }




        if(correct.equals("A")){
            o1status.setText("Correct Answer");
            o1statusPane.getStyleClass().setAll("pane-lightgreen-strongborder");
        }
        if(correct.equals("B")){
            o2status.setText("Correct Answer");
            o2statusPane.getStyleClass().setAll("pane-lightgreen-strongborder");
        }
        if(correct.equals("C")){
            o3status.setText("Correct Answer");
            o3statusPane.getStyleClass().setAll("pane-lightgreen-strongborder");
        }
        if(correct.equals("D")){
            o4status.setText("Correct Answer");
            o4statusPane.getStyleClass().setAll("pane-lightgreen-strongborder");
        }


        if(answerString.equals("A")){
            o1status.setText("Your Answer");
            if(!Qstatus.getText().equals("Correct")){
                o1statusPane.getStyleClass().setAll("pane-rose-strongborder");
            }
        }
        if(answerString.equals("B")){
            o2status.setText("Your Answer");
            if(!Qstatus.getText().equals("Correct")){
                o2statusPane.getStyleClass().setAll("pane-rose-strongborder");
            }
        }
        if(answerString.equals("C")){
            o3status.setText("Your Answer");
            if(!Qstatus.getText().equals("Correct")){
                o3statusPane.getStyleClass().setAll("pane-rose-strongborder");
            }
        }
        if(answerString.equals("D")){
            o4status.setText("Your Answer");
            if(!Qstatus.getText().equals("Correct")){
                o4statusPane.getStyleClass().setAll("pane-rose-strongborder");
            }
        }
        explanationLabel.setText(explanation);


//        System.out.println(o1num);
//        System.out.println(o2num);
//        System.out.println(o3num);
//        System.out.println(o4num);


        int o1number = Integer.parseInt(o1num);
        int o2number = Integer.parseInt(o2num);
        int o3number = Integer.parseInt(o3num);
        int o4number = Integer.parseInt(o4num);


        int sum = o1number+o2number+o3number+o4number;


        if(sum!=0) {
            o1perc.setText(String.format("%.2f%%", (o1number * 100.0 / (double) sum)));
            o2perc.setText(String.format("%.2f%%", (o2number * 100.0 / (double) sum)));
            o3perc.setText(String.format("%.2f%%", (o3number * 100.0 / (double) sum)));
            o4perc.setText(String.format("%.2f%%", (o4number * 100.0 / (double) sum)));


            if(o1number!=0){
                o1filler.getStyleClass().setAll("poll-percentage");
                o1filler.setPrefWidth(o1number * 920 / (double) sum);
            }
            if(o2number!=0){
                o2filler.getStyleClass().setAll("poll-percentage");
                o2filler.setPrefWidth(o2number * 920 / (double) sum);
            }
            if(o3number!=0){
                o3filler.getStyleClass().setAll("poll-percentage");
                o3filler.setPrefWidth(o3number * 920 / (double) sum);
            }
            if(o4number!=0){
                o4filler.getStyleClass().setAll("poll-percentage");
                o4filler.setPrefWidth(o4number * 920 / (double) sum);
            }


        } else{
            o1perc.setText("0%");
            o2perc.setText("0%");
            o3perc.setText("0%");
            o4perc.setText("0%");
        }


    }
}
