package shared;

import java.io.Serializable;

public class Record implements Serializable {
    public int value1;
    public int value2;
    public String name;

    public Record(int value1, int value2, String name) {
        this.value1 = value1;
        this.value2 = value2;
        this.name = name;
    }

    @Override
    public String toString() {
        return "(" + value1 + ", " + value2 + ", " + name + ")";
    }
}

