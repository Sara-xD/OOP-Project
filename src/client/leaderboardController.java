package client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class leaderboardController {


    @FXML
    private Label indexLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Pane scorePane;
    @FXML
    private Pane timePane;


    void setLBdata(int index, int mark, Integer timeRemaining, String name){
        nameLabel.setText(name);
        if(mark!=-2) scoreLabel.setText(String.valueOf(mark));
        else  {
            scoreLabel.setText("Did not Attend");
            scorePane.setPrefWidth(140);
        }


        indexLabel.setText(String.valueOf(index));
        if(timeRemaining!=-3){
            int min = timeRemaining / 60;
            int sec = timeRemaining % 60;
            timeLabel.setText(String.format("%02d : %02d", min, sec));
        }else{
            timeLabel.setText("         -");
        }
    }
}
