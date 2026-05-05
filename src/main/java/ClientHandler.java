import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    private final Server server;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.server = server;
    }

    public void run() {
        try {
            String inputLine;
            out.println("(S)ingle-player game or (M)ulti-player game?");
            while ((inputLine = in.readLine()) != null) {

                if (inputLine.equals("M") ) {
                    out.println("Searching for a match...");
                    server.joinQueue(this);
                    break;

                } else if (inputLine.equals("S")) {
                    SoloClient game = new SoloClient(this);
                    game.runGame();
                    stopClient();
                } else {
                    out.println("That's not a command...");
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public void stopClient() {
        try {
            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String message) {
        out.println(message);
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to receive message.";
        }

    }

    public Socket getSocket() {
        return socket;
    }
}
