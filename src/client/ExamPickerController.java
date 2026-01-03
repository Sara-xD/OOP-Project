package client;

import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ExamPickerController implements Initializable{

    @FXML
    private ChoiceBox<String> classChoiceBox;

    @FXML
    private ChoiceBox<String> subChoiceBox;

    @FXML
    private VBox examContainer;

    @FXML
    private AnchorPane innerAnchorPane;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private String[] classes = {"Class 9", "Class 10"};
    private String[] subjects = {"Physics", "Math", "Chemistry"};
    private String[] exams;
    private String grade, sub;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        innerAnchorPane.prefHeightProperty().bind(examContainer.heightProperty());
//      System.out.println("classChoiceBox = " + classChoiceBox);
//      System.out.println("subChoiceBox = " + subChoiceBox);
        classChoiceBox.getItems().addAll(classes);
        classChoiceBox.setOnAction(this::setGrade);
        subChoiceBox.getItems().addAll(subjects);
        subChoiceBox.setOnAction(this::setSub);


//        Scanner scanner = null;
//        try {
//            scanner = new Scanner(new File("src/client/examList.txt"));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        String examList = scanner.nextLine();

        reloadExamPicker();
    }
//        for (int i = 0; i < exams.length; i++) {
//            try {
//                //Node examNode = FXMLLoader.load(getClass().getResource("exam.fxml"));
//                String[] argus = exams[i].split(",");
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("exam.fxml"));
//                loader.setControllerFactory(param ->
//                        new examController(argus[0], argus[1], argus[2], argus[3], argus[4], argus[5])
//                );
//                Node examNode = loader.load();
////                examController eC = new examController(argus[0],argus[1],argus[2],argus[3],argus[4],argus[5]);
////                loader.setControllerFactory(param -> eC );
//                //System.out.println(argus[0]+argus[1]+argus[2]+argus[3]+argus[4]+argus[5]);
//                //Node examNode = loader.load();
//                examContainer.getChildren().add(examNode);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < exams.length; i++) {
//            try {
//                String[] argus = exams[i].split(",");
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("exam.fxml"));
//                loader.setControllerFactory(param -> {
//                    System.out.println("Constructing examController!"); // <-- should print
//                    return new examController(argus[0], argus[1], argus[2], argus[3], argus[4], argus[5]);
//                });
//                System.out.println("before");
//                Node examNode = loader.load();
//                System.out.println("after");
//                examContainer.getChildren().add(examNode);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    public void reloadExamPicker(){
        String examList = null;
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("EXAMLIST");

            examList = (String) in.readObject();

            //System.out.println(examList);
            socket.close();

        } catch (Exception E) {
            E.printStackTrace();
        }

        exams = examList.split("~");

        examContainer.getChildren().clear();

        for (int i = 0; i < exams.length; i++) {
            try {
                String[] argus = exams[i].split(",");

                if(grade!=null){
                    if(!grade.equals("Class "+argus[0])){
                        continue;
                    }
                }

                if(sub!=null){
                    if(!argus[1].equals(sub)){
                        continue;
                    }
                }

                if(true){
                    String student_answersheets=null;
//                    BufferedReader reader = null;
//                    try {
//                        reader = new BufferedReader(new FileReader("src/client/"+ Controller.unameString+".txt"));
//                        student_answersheets = reader.readLine();
//                        reader.close();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
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
                    System.out.println(student_answersheets + "track");

                    String[] student_answersheets_strings = student_answersheets.split("~");
                    if((studentDashController.getViewMode().equals("takeExam")&&student_answersheets_strings[9+3*i].equals("-1"))||studentDashController.getViewMode().equals("viewResult")&&!student_answersheets_strings[9+3*i].equals("-1")||studentDashController.getViewMode().equals("leaderBoard")){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("exam.fxml"));
                        Node examNode = loader.load();
                        examController controller = loader.getController();
                        if (studentDashController.getViewMode().equals("takeExam")) controller.setExamData(i,argus[0], argus[1], argus[2], argus[3], "-2", "-3", argus[4], argus[5]);
                        else controller.setExamData(i,argus[0], argus[1], argus[2], argus[3],student_answersheets_strings[9+3*i+1] , student_answersheets_strings[9+3*i+2] , argus[4], argus[5]);
                        examContainer.getChildren().add(examNode);
                        System.out.println(i+argus[2]);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGrade(ActionEvent actionEvent){
        grade = classChoiceBox.getValue();
        reloadExamPicker();
    }
    public void setSub(ActionEvent actionEvent){
        sub = subChoiceBox.getValue();
        reloadExamPicker();
    }

    public void back(ActionEvent e){
        try {
            root = FXMLLoader.load(getClass().getResource("studentDashboard.fxml"));
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
