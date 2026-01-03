package shared;

public class examResult {
    private String examName;
    private String subject;
    private String studentName;
    private int total;
    private int marks;
    private int rank;

    public examResult(String exname, String sub, String stname, int total, int marks, int rank) {
        this.examName = exname;
        this.subject = sub;
        this.studentName = stname;
        this.total = total;
        this.marks = marks;
        this.rank = rank;
    }

    public String getExamName() {
        return examName;
    }

    public String getSubject() {
        return subject;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getTotal() {
        return total;
    }

    public int getMarks() {
        return marks;
    }

    public int getRank() {
        return rank;
    }
}
