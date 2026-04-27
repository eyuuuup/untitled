import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientHandler extends Thread {
    public final BufferedReader in;
    public final PrintWriter out;
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
                    SoloClient game = new SoloClient(in, out);
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

    public String getMove() throws IOException {
        String inputLine;
        out.println("rock paper or scissors");
        List<String> values = Arrays.asList("rock", "scissors", "paper");
        while ((inputLine = in.readLine()) != null) {

            if (values.contains(inputLine)) {
                return inputLine;
            } else {
                out.println("I've never heard of " + inputLine + "...");;
            }
        }
        return inputLine;
    }


    public void stopClient() {
        try {
            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
