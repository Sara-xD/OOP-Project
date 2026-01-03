package client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class questionController {

    @FXML
    private Label Qlabel;
    @FXML
    private RadioButton o1radio;
    @FXML
    private RadioButton o2radio;
    @FXML
    private RadioButton o3radio;
    @FXML
    private RadioButton o4radio;

    private String correct;
    private examPortalController EPC;

    public void setExamPortalController(examPortalController EPC) {
        this.EPC = EPC;
    }

    @FXML
    public void setQuesData(int qindex, String q, String o1, String o2, String o3, String o4, String correct){
        Qlabel.setText((qindex+1)+". "+q);
        o1radio.setText("(A) "+o1);
        o2radio.setText("(B) "+o2);
        o3radio.setText("(C) "+o3);
        o4radio.setText("(D) "+o4);
        this.correct = correct;

        ToggleGroup group = new ToggleGroup();
        o1radio.setToggleGroup(group);
        o2radio.setToggleGroup(group);
        o3radio.setToggleGroup(group);
        o4radio.setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                RadioButton selectedRB = (RadioButton) selected;
                if(selectedRB==o1radio){
                    EPC.setAnswer("A",qindex);
                }
                if(selectedRB==o2radio){
                    EPC.setAnswer("B",qindex);
                }
                if(selectedRB==o3radio){
                    EPC.setAnswer("C",qindex);
                }
                if(selectedRB==o4radio){
                    EPC.setAnswer("D",qindex);
                }
            }

        });
    }
}
