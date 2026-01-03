package server;

import shared.Question;

import java.util.List;

public class Exam {
    public String title;
    public String subject;
    public String clsName;
    public String duration;
    List<Question> questions;

    public Exam( String className, String subject, String title,  String duration, List<Question> questions) {
        this.subject = subject;
        this.clsName = className;
        this.duration = duration;
        this.questions = questions;
        this.title = title;

    }
}