import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {
    private final BlockingQueue<ClientHandler> matchmaking = new LinkedBlockingQueue<>();
    private final ExecutorService executorServiceAcceptor = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceMatchmaker = Executors.newSingleThreadExecutor();
    private final ExecutorService executorServiceGameHandling = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);

    public void start(int port) {
        executorServiceAcceptor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server is listening on port " + port);

                while (true) {
                    Socket socket = serverSocket.accept();
                    executorServiceGameHandling.submit(() -> {
                        try {
                            new ClientHandler(socket, this).handleClient();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    System.out.println("Client connected");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        executorServiceMatchmaker.submit(() -> {
            try {
                while (true) {
                    ClientHandler clientHandler1 = matchmaking.take();
                    ClientHandler clientHandler2 = matchmaking.take();

                    clientHandler1.broadcast("Found match against " + clientHandler1.getSocket().getPort());
                    clientHandler2.broadcast("Found match against " + clientHandler2.getSocket().getPort());
                    System.out.println("Match found for " + clientHandler1.getSocket().getPort() + " and " + clientHandler2.getSocket().getPort());
                    executorServiceGameHandling.submit(() -> {
                        MultiClient game = new MultiClient(clientHandler1, clientHandler2, executorServiceGameHandling);
                        game.runGame();
                    });
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void joinQueue(ClientHandler client) throws IOException {
        matchmaking.add(client);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}
