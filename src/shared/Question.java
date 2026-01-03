package shared;
import java.io.Serializable;


public class Question implements Serializable {

    public String question;
    public String optionA, optionB, optionC, optionD;
    public String correctOption;
    public String cls;
    public String sub;
    public String explanation;


    public Question(String cls, String sub, String ques, String a, String b, String c, String d, String correct, String ex) {

        this.question = ques;
        this.optionA = a;
        this.optionB = b;
        this.optionC = c;
        this.optionD = d;
        this.correctOption = correct;
        this.cls = cls;
        this.sub = sub;
        this.explanation = ex;
    }

    public String makeString() {
        return question + "|" + optionA + "|" + optionB + "|" + optionC + "|" + optionD + "|" + correctOption + "|" + explanation;
    }

    public String makeString2() {
        return question + "@" + optionA + "@" + optionB + "@" + optionC + "@" + optionD + "@" + correctOption + "@"+ "0@0@0@0@Explanation: " + explanation;
    }

}
