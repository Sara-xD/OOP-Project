package server;

import shared.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class editQues {

    public void edit(Question oldq, Question newq) {
        String filePath = "src/server/questions/" + newq.cls + "_" + newq.sub + ".txt";
        List<String> lines = new ArrayList<>();

        String old = oldq.makeString();
        String updated = newq.makeString();



        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(old)) {
                    lines.add(updated);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
