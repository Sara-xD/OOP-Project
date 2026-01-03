package server;

import shared.Question;

import java.io.FileWriter;
import java.io.IOException;

public class saveQues {

    public void save(Question q){
        String filePath = "src/server/questions/" + q.cls + "_" + q.sub + ".txt";
        save_to_student(q);
        try {

            FileWriter w = new FileWriter(filePath, true);
            w.write(q.makeString() + "\n");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save_to_student(Question q){
        String filePath = "src/server/student_ques/" + q.cls + "_" + q.sub + ".txt";

        try {
            FileWriter w = new FileWriter(filePath, true);
            w.write(q.makeString2() + "~");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
