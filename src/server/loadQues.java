package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class loadQues {

    public String load(String className, String sub) {
        String filename = "src/server/questions/" + className + "_" + sub + ".txt";

        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append("~");
            }
        } catch (IOException e) {
            return "Failed";
        }

        return content.toString();
    }

}
