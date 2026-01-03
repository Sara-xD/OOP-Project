package client;

import java.io.*;
import java.net.*;


public class socketCalls {
    // timeouts for connection and reading
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;


    public Object authCall(String msg) {
        Object status = null;
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {

            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 45454), CONNECTION_TIMEOUT);
            socket.setSoTimeout(READ_TIMEOUT);


            //data exchange
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());


            //authentication message to server
            out.writeObject(msg);
            out.flush();


            //awaiting response
            status = in.readObject();
        }








        //PROPER AND DETAILED EXCEPTION HANDLING


        catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timed out or read timed out: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Network I/O error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error reading object: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        } finally {

            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return status;
    }
}




