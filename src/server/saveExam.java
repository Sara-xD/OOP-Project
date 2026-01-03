package server;
import shared.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class saveExam {

    public void save(Exam exam, String idx) {
        String filePath = "src/server/exams/" + exam.title + ".txt";
        int count = exam.questions.size();
        //add_to_student(exam);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(exam.clsName + "|" + exam.subject + "|" + exam.title + "|" + exam.duration + "~");
            save_list(exam.clsName + "|" + exam.subject + "|" + exam.title + "|" + exam.duration + "|" + count + "~");
            for (Question q : exam.questions) {
                writer.write(q.makeString() +"~");
            }

            add_to_student(exam, idx);
            add_to_all();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public void save_list(String s){
        String filePath = "src/server/exams/examName_list.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.append(s);

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public void add_to_student(Exam exam, String idxString) {
        String filePath = "src/server/examList.txt";

        String[] parts = idxString.split(",");
        List<String> indices = new ArrayList<>();
        for (String s : parts) {
            if (!s.isEmpty()) indices.add(s);
        }

        int total = exam.questions.size();
        int selected = indices.size();

        StringBuilder line = new StringBuilder();
        line.append(exam.clsName.replace("Class ", "")).append(",")
                .append(exam.subject).append(",")
                .append(exam.title).append(",")
                .append(exam.duration).append(",")
                .append(total).append(",")
                .append(selected);

        for (String index : indices) {
            line.append(",").append(index);
        }

        line.append("~");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add_to_all() {

        String line = "~-1~-2~-3";
        String filePath = "src/server/answersheets";

        File dir = new File(filePath);
        File[] files = dir.listFiles();

        if(files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                        writer.write(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}

