package client;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import shared.Question;


public class ques2Controller {

    @FXML private Label question;
    @FXML private Label answer;
    @FXML private Label opt1;
    @FXML private Label opt2;
    @FXML private Label opt3;
    @FXML private Label opt4;
    @FXML private Label explanation;
    private server.user currentUser;

    public void setUser(server.user u) {
        this.currentUser = u;
    }


    public int num;

    public Question ques;

    public void setQuestion(Question q, int num) {
        this.ques = q;
        this.num = num;

        question.setText(num + ". " + q.question);
        opt1.setText("A) " + q.optionA);
        opt2.setText("B) " + q.optionB);
        opt3.setText("C) " + q.optionC);
        opt4.setText("D) " + q.optionD);
        answer.setText("Correct Answer: " + q.correctOption);
        explanation.setText( "Explanation: " + q.explanation);
    }
}
