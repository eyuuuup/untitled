import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class ClientHandler {
    private static final String USER_INPUT_LINE_QUIT = "q";
    private static final String USER_INPUT_LINE_AGREE = "y";
    private static final String USER_INPUT_LINE_DISAGREE = "n";
    private static final String USER_INPUT_LINE_WRONG_CHOICE = "That's not a command...";

    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    private final Server server;
    public User user;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.server = server;
    }

    public void intake() {
        try {
            String username;

            out.println("Register username");
            while ((username = in.readLine()) != null) {
                out.println("Is " + username + " correct? (y/n)");

                String choice;

                while ((choice = in.readLine()) != null) {
                    if (USER_INPUT_LINE_AGREE.equals(choice)) {
                        user = new User(username);
                        handleClient();
                    } else if (USER_INPUT_LINE_DISAGREE.equals(choice)) {
                        intake();
                    } else {
                        out.println(USER_INPUT_LINE_WRONG_CHOICE);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void handleClient() {
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
                    out.println(USER_INPUT_LINE_WRONG_CHOICE);
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public void stopClient() {
        try {
            out.println("Your wins: " + user.wins + " and losses: " + user.losses);
            socket.close();
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void broadcast(String message) {
        out.println(message);
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "Failed to receive message.";
        }

    }

    public Option receiveMove() {
        Option userOption = null;
        try {
            String userInputLine;
            out.println("Make your move.");
            while ((userInputLine = in.readLine()) != null) {

                if (USER_INPUT_LINE_QUIT.equals(userInputLine)) {
                    out.println("Connection closing..");
                    stopClient();
                }

                Optional<Option> userOptionOptional = OptionParser.parseFromUserInputLine(userInputLine);

                if (userOptionOptional.isEmpty()) {
                    out.println("I've never heard of " + userInputLine + "...");
                    continue;
                }
                userOption = userOptionOptional.get();
    
                return userOption;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return userOption;
    }

    public Socket getSocket() {
        return socket;
    }
}
