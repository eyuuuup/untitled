import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultiClient {
    private final ClientHandler p1;
    private final ClientHandler p2;

    public MultiClient(ClientHandler p1, ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void runGame() throws IOException {
        broadcast("Make your move.");
        String[] moves = receive();
        System.out.println(Arrays.toString(moves));

        Map<String, String> d = new HashMap<>();
        d.put("rock", "scissors");
        d.put("scissors", "paper");
        d.put("paper", "rock");
        String moveP1 = moves[0];
        String moveP2 = moves[1];

        broadcast("Player 1 chose: " + moveP1 + " and Player 2 chose: " + moveP2);

        if (moveP1.equals(moveP2)) {
            broadcast("It's a tie..");
        } else if (moveP1.equals(d.get(moveP2))) {
            broadcast("Player 1 wins.");
        } else {
            broadcast("Player 2 wins.");
        }

        p1.stopClient();
        p2.stopClient();

    }

    public void broadcast(String msg) {
        p1.out.println(msg);
        p2.out.println(msg);
    }

    public String[] receive() throws IOException {
        String[] moves = new String[2];
        moves[0] = p1.getMove();
        moves[1] = p2.getMove();

        return moves;
    }
}
