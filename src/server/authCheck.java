package server;

import java.io.*;
import java.util.HashMap;


public class authCheck {

    HashMap<String, user> users = new HashMap<>();

    public void load(){

        try(BufferedReader fin = new BufferedReader(new FileReader("src/server/users.txt"))){

            String line;
            while((line = fin.readLine()) != null){
                String[] arr = line.split(",");
                if(arr.length == 4){
                    String role = arr[0];
                    String name = arr[1];
                    String username = arr[2];
                    String password = arr[3];

                    user u = new user(role, name, username, password);
                    users.put(username, u);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public user login(String username, String password){
        user u = users.get(username);
        System.out.println("Looking up user: " + username);
        System.out.println("User found? " + (u != null));
        //System.out.println(u.password);
        System.out.println("Password match? " + (u != null && u.password.equals(password)));

        if(u != null && u.password.trim().equals(password.trim())){
            return u;
        }

        else return null;
    }

    public user signup(String username, String password, String role , String name){

        if(users.containsKey(username)){
            return null;
        }

        user u = new user(role, name, username, password);
        users.put(username, u);
        addUser(u);

        return u;
    }

    public void addUser(user u){
        try(BufferedWriter fout = new BufferedWriter(new FileWriter("src/server/users.txt", true))){

            fout.write(u.role + "," + u.name + "," + u.username + "," + u.password);
            fout.newLine();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
