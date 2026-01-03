package client;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.SlideInUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import shared.examResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class analyzeController implements Initializable {

    private server.user currentUser;

    private Stage stage;
    private Scene scene;

    public void setUser(server.user u) {

        this.currentUser = u;
        counter counter = new counter();
        int examNum = counter.count("exam") -1;
        int stunum = counter.count("student") -1;
        examnum.setText("" + examNum);
        stnum.setText("" + stunum);
    }

    @FXML private TableView<examResult> table;

    @FXML
    private TableColumn<examResult, String> exam_name;
    @FXML
    private TableColumn<examResult, String> subject;
    @FXML
    private TableColumn<examResult, String> name;
    @FXML
    private TableColumn<examResult, Integer> total;
    @FXML
    private TableColumn<examResult, Integer> marks;
    @FXML
    private TableColumn<examResult, Integer> rank;
    @FXML
    private TextField search_box;

    @FXML private Label stnum;
    @FXML private Label examnum;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exam_name.setCellValueFactory(new PropertyValueFactory<>("examName"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        name.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));
        marks.setCellValueFactory(new PropertyValueFactory<>("marks"));
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
    }

    public void search(ActionEvent e) {

        String msg = "SEARCH";
        try {
            Socket socket = new Socket("127.0.0.1", 45454);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(msg);
            String response = (String) in.readObject();

            filter(response);
            socket.close();

        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void filter(String s){

        String search = search_box.getText().trim();

        String [] datas = s.split("~");
        List<examResult> results = new ArrayList<>();

        for (String result : datas) {
            String[] parts = result.split(",");
            if (parts.length != 5) continue;

            String exam = parts[0];
            if (exam.equalsIgnoreCase(search)) {
                String subject = parts[1];
                String name = parts[2];
                int total = Integer.parseInt(parts[3]);
                int marks = Integer.parseInt(parts[4]);

                results.add(new examResult(exam, subject, name, total, marks, 0));
            }
        }

        results.sort((a, b) -> Integer.compare(b.getMarks(), a.getMarks()));

        for (int i = 0; i < results.size(); i++) {
            examResult result = results.get(i);
            results.set(i, new examResult(
                    result.getExamName(),
                    result.getSubject(),
                    result.getStudentName(),
                    result.getTotal(),
                    result.getMarks(),
                    i + 1
            ));
        }

        table.getItems().setAll(results);


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

}
