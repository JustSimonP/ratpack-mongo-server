import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketListener {

    private static ServerSocket server;
    private static int port = 5555;

    public SocketListener(){
        try {
            server = new ServerSocket(port);

            while(true){
                System.out.println("Waiting for the client request");

                Socket socket = server.accept();
                var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                reader.lines().forEach(System.out::println);

                socket.close();
                reader.close();

            }
            //close the ServerSocket object
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
