package server;

import shared.Question;
import shared.Record;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;


public class server {

    private ServerSocket ss;

    private static final Object auth = new Object();
    private static final Object questions = new Object();
    //private static final Object questions2 = new Object();
    private static final Object exam = new Object();
    private static final Object marks = new Object();
    //private static final Object users = new Object();
    private static final Object files = new Object();

    private final authCheck checker;
    private final saveQues saver;
    private final loadQues loader;
    private final editQues editor;
    private final saveExam examSaver;




    server(){

        checker = new authCheck();
        saver = new saveQues();
        loader = new loadQues();
        editor = new editQues();
        examSaver = new saveExam();

        try {

            synchronized (auth) {
                checker.load();
            }
            ss = new ServerSocket(45454);
            System.out.println("Server started");

            while(true){
                Socket clientSocket = ss.accept();
                System.out.println("Client connected");

                Thread client = new Thread(new driver(clientSocket));
                client.start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class driver implements Runnable{
        private Socket clientSocket;

        public driver(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try{
                serve(clientSocket);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void serve(Socket clientSocket)throws IOException, ClassNotFoundException {

        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            String msg = (String) in.readObject();
            String[] arr = msg.split(",");

            //String s;
//            saveQues saver = new saveQues();
//            loadQues loader = new loadQues();
//            editQues editor = new editQues();
//            saveExam examSaver = new saveExam();

            if (arr[0].equals("LOGIN") && arr.length == 3) {

                synchronized (auth) {
                    user u = checker.login(arr[1], arr[2]);

                    if (u != null) {
                        out.writeObject(u);
                    }

                    else {
                        out.writeObject("Invalid username or password");

                    }
                }

            } else if (arr[0].equals("SIGNUP") && arr.length == 5) {

                synchronized (auth) {
                    user u = checker.signup(arr[1], arr[2], arr[3], arr[4]);
                    if (u != null) {
                        out.writeObject(u);

                    }
                    else {
                        out.writeObject("username taken");
                    }
                }

            } else if (msg.startsWith("ADDQUESTION")) {

                synchronized (questions) {
                    String [] ques = msg.split("\\|");
                    Question q = new Question(ques[1],ques[2],ques[3],ques[4],ques[5],ques[6],ques[7],ques[8],ques[9]);

                    saver.save(q);
                    out.writeObject("Question added");

                }

            } else if (msg.startsWith("EDITQUESTION")) {

                synchronized (questions) {
                    String [] ques = msg.split("\\|");
                    Question oldq = new Question(ques[1],ques[2],ques[3],ques[4],ques[5],ques[6],ques[7],ques[8],ques[9]);
                    Question newq = new Question(ques[10],ques[11],ques[12],ques[13],ques[14],ques[15],ques[16],ques[17],ques[18]);

                    editor.edit(oldq, newq);
                    out.writeObject("Question edited");
                }


            } else if (msg.startsWith("SAVE_EXAM,")) {

                synchronized (exam) {
                    String [] parts = msg.split(",");
                    List<Question> selecteds = (List<Question>) in.readObject();
                    Exam ex = new Exam(parts[1], parts[2], parts[3], parts[4], selecteds);
                    String idx = in.readObject().toString();

                    examSaver.save(ex, idx);
                    out.writeObject("Exam added");
                }


            } else if (msg.startsWith("LOAD_QUES")){

                synchronized (questions) {
                    String [] req = msg.split(",");
                    String res = loader.load(req[1], req[2]);

                    if (res != null) {
                        out.writeObject(res);
                    }
                    else {
                        out.writeObject("Invalid question");
                    }
                }


            }else if (msg.startsWith("EXAM_LIST")) {

                synchronized (exam) {
                    String filePath = "src/server/exams/examName_list.txt";
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    out.writeObject(sb.toString());
                }


            }else if (msg.startsWith("SHOW_EXAM")) {

                synchronized (exam) {
                    String [] req = msg.split(",");

                    String filePath = "src/server/exams/" + req[1] + ".txt";

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                        String line = br.readLine();
                        int idx = line.indexOf('~');
                        String result = line.substring(idx + 1);
                        out.writeObject(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }else if (msg.startsWith("SEARCH")) {

                synchronized (marks) {
                    String filePath = "src/server/marksList.txt";
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

                        String line = br.readLine();
                        out.writeObject(line);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }else if (msg.startsWith("FORUM_ADD")) {

                synchronized (files) {
                    String filePath = "src/server/forum.txt";
                    String [] req = msg.split("\\|");

                    try (BufferedWriter br = new BufferedWriter(new FileWriter(filePath,true))) {

                        br.write(req[1] + "|" + req[2] + "|" + req[3]);
                        br.newLine();

                        out.writeObject("Forum add done");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else if (msg.startsWith("LOAD_FORUM")) {

                synchronized (files) {
                    String filePath = "src/server/forum.txt";

                    StringBuilder content = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            content.append(line);
                            content.append("~");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out.writeObject(content.toString());
                }

            }else if (msg.startsWith("SEND_MSG")) {

                synchronized (files) {
                    String filePath = "src/server/forum.txt";
                    List<String> updated = new ArrayList<>();

                    String [] req = msg.split("\\|");
                    String search = req[1] + "|" + req[3];
                    String add = "|" + req[2] +": "+ req[4];

                    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.startsWith(search)) {
                                line += add;
                            }
                            updated.add(line);
                        }
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                        for (String line : updated) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }

                    out.writeObject("updated");
                }

            }else if (msg.startsWith("LOAD_CHAT")) {

                synchronized (files) {

                    String[] req = msg.split("\\|");
                    String username = req[1];
                    String topic = req[2];

                    List<String> response = new ArrayList<>();

                    try (BufferedReader reader = new BufferedReader(new FileReader("src/server/forum.txt"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split("\\|");
                            if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(topic)) {
                                response.add("Question: " + parts[2]);
                                for (int i = 3; i < parts.length; i++) {
                                    response.add(parts[i]);
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    out.writeObject(response);
                }

            }else if(msg.startsWith("EXAMLIST")){

                synchronized (files) {
                    Scanner scanner = null;
                    String s;
                    try {
                        scanner = new Scanner(new File("src/server/examList.txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    s = scanner.nextLine();
                    out.writeObject(s);
                }

            }else if(msg.startsWith("STUDENT_ANSWERSHEET")){

                synchronized (exam) {
                    String[] arrAnsSheet = msg.split("#");
                    String s;
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(new File("src/server/answersheets/"+arrAnsSheet[1]+".txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    s = scanner.nextLine();
                    out.writeObject(s);
                }

            } else if(msg.startsWith("QUESLIST")){

                synchronized (questions) {
                    String[] arrQuesList = msg.split("#");
                    Scanner scanner = null;
                    String s;
                    try {
                        scanner = new Scanner(new File("src/server/student_ques/"+arrQuesList[1]+".txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    s = scanner.nextLine();
                    out.writeObject(s);
                }

            } else if(msg.startsWith("UPDATE_ANSWERSHEET")){

                synchronized (exam) {
                    String[] arrUpdateAnsSheet = msg.split("#");
                    String s;
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter("src/server/answersheets/"+ arrUpdateAnsSheet[2]+".txt");
                        writer.write(arrUpdateAnsSheet[1]);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    s = "updated answer sheet";
                    out.writeObject(s);
                }

            }  else if(msg.startsWith("CREATE_ANSWERSHEET")){

                synchronized (exam) {
                    String[] arrUpdateAnsSheet = msg.split("#");
                    String s;
                    FileWriter writer = null;
                    System.out.println(arrUpdateAnsSheet[1]+"jsdkd");
                    try {
                        writer = new FileWriter("src/server/answersheets/"+ arrUpdateAnsSheet[1]+".txt");
                        writer.write("0~0~0~0~0~0~0~0~0");
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    s = "updated answer sheet";
                    out.writeObject(s);
                }

            }else if(msg.startsWith("UQF")){

                synchronized (questions){
                    String[] arrUpdateQuesFile = msg.split("#");
                    String s;
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter("src/server/student_ques/"+arrUpdateQuesFile[2]+".txt");
                        writer.write(arrUpdateQuesFile[1]);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    s = "updated question file";
                    out.writeObject(s);
                }

            } else if(msg.startsWith("LEADERBOARD")){

                synchronized (auth) {
                    String[] leaderboardInfo = msg.split("#");
                    int examIndex = Integer.parseInt(leaderboardInfo[1]);
                    List<shared.Record> list = new ArrayList<>();

                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(new File("src/server/users.txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    while(scanner.hasNextLine()){
                        String userData = "";
                        userData = scanner.nextLine();
                        String[] userDatum = userData.split(",");
                        if(userDatum[0].equals("Student")){
                            Scanner scanner2 = null;
                            try {
                                scanner2 = new Scanner(new File("src/server/answersheets/"+userDatum[2]+".txt"));
                            } catch (FileNotFoundException e){
                                throw new RuntimeException(e);
                            }
                            String ansSheet = scanner2.nextLine();
                            String[] ansSheets = ansSheet.split("~");
                            Integer mark = Integer.parseInt(ansSheets[9+3*examIndex+1]);
                            Integer timeRemaining = Integer.parseInt(ansSheets[9+3*examIndex+2]);
                            list.add(new Record(mark, timeRemaining, userDatum[2]));
                        }
                    }

                    list.sort((a, b) -> {
                        int cmp1 = Integer.compare(b.value1, a.value1);
                        if (cmp1 != 0) return cmp1;
                        return Integer.compare(b.value2, a.value2);
                    });
                    out.writeObject(list);
                    //System.out.println("leader");
                }

            } else if(msg.startsWith("POSITION")){

                synchronized (auth) {
                    String s;
                    String[] data = msg.split("#");
                    int examIndex = Integer.parseInt(data[1]);
                    int refMark = Integer.parseInt(data[2]);
                    int refTime = Integer.parseInt(data[3]);
                    int place = 1;
                    int attendee = 0;

                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(new File("src/server/users.txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    while(scanner.hasNextLine()){
                        String userData = "";
                        userData = scanner.nextLine();
                        String[] userDatum = userData.split(",");
                        if(userDatum[0].equals("Student")){
                            Scanner scanner2 = null;
                            try {
                                scanner2 = new Scanner(new File("src/server/answersheets/"+userDatum[2]+".txt"));
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            String ansSheet = scanner2.nextLine();
                            String[] ansSheets = ansSheet.split("~");
                            int mark = Integer.parseInt(ansSheets[9+3*examIndex+1]);
                            int timeRemaining = Integer.parseInt(ansSheets[9+3*examIndex+2]);
                            if(mark != -2) attendee++;
                            if(mark>refMark) place++;
                            if(mark==refMark){

                            }
                        }
                    }

                    s = place+ "@" + attendee;
                    out.writeObject(s);
                    System.out.println("this is s: "+s);
                }

            } else if(msg.startsWith("MARKSLIST")){

                synchronized (marks) {
                    String s;
                    Scanner scanner = null;
                    try {
                        scanner = new Scanner(new File("src/server/markslist.txt"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    s = scanner.nextLine();
                    out.writeObject(s);
                }

            } else if(msg.startsWith("UPDATE_MARKSLIST")){

                synchronized (marks) {
                    String[] arrUpdateAnsSheet = msg.split("@");
                    String s;
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter("src/server/marksList.txt");
                        writer.write(arrUpdateAnsSheet[1]);
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    s = "updated answer sheet";
                    out.writeObject(s);
                }

            }
            else {
                out.writeObject("Failed");
            }


            clientSocket.close();

        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
    }
    }


    public static void main(String[] args) {
        server server = new server();
    }

}
