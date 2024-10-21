import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = sc.nextLine();
                if (!messageToSend.isEmpty()) {
                    bufferedWriter.write(username + " : " + messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
            sc.close(); // Close the Scanner when done
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        if (msgFromGroupChat != null) {
                            System.out.println(msgFromGroupChat);
                        } else {
                            // Connection was lost
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username for the Group Chat :>");
        String username = sc.nextLine();
        
        Socket socket = null;
        try {
            socket = new Socket("localhost", 1234);
        } catch (IOException e) {
            System.out.println("Failed to connect to the server.");
            return; // Exit if connection failed
        }

        Client client = new Client(socket, username);
        client.listenForMessage(); // Start listening for messages
        client.sendMessage();      // Start sending messages
    }
}
