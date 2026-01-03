package server;
import java.io.Serializable;

public class user implements Serializable {
    public String role , name , username, password;

    user(String role, String name, String username, String password) {
        this.role = role;
        this.name = name;
        this.username = username;
        this.password = password;

    }
}
