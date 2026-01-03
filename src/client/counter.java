package client;

import java.io.File;
import java.io.FilenameFilter;

public class counter {
     public int count(String s) {

        int count = 0;
        File folder;

        if (s.equals("exam")) {
            folder = new File("src/server/exams");
        }
        else {
            folder = new File("src/server/answersheets/");
        }
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        count++;
                    }
                }
            }
        return count;
        }
    }
