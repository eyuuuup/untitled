import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Server {
    private final Queue<ClientHandler> matchmaking = new LinkedList<>();

    public void start(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                new ClientHandler(socket.accept(), this).start();
                System.out.println("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void joinQueue(ClientHandler client) throws IOException {
        matchmaking.add(client);

        if (matchmaking.size() >= 2) {
            ClientHandler p1 = matchmaking.poll();
            ClientHandler p2 = matchmaking.poll();

            p1.out.println("Found match.");
            p2.out.println("Found match.");

            System.out.println("Match found");
            MultiClient game = new MultiClient(p1, p2);
            game.runGame();
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}
