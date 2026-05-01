import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultiClient {
    private final Map<String, String> d;
    private final ClientHandler p1;
    private final ClientHandler p2;

    public MultiClient(ClientHandler p1, ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.d = new HashMap<>();
        d.put("rock", "scissors");
        d.put("scissors", "paper");
        d.put("paper", "rock");
    }

    public void runGame() {
        p1.broadcast("Make your move.");
        p2.broadcast("Waiting for player 1 to make a move...");
        String moveP1 = p1.receive();

        p2.broadcast("Make your move.");
        p1.broadcast("Waiting for player 2 to make a move...");

        String moveP2 = p2.receive();


        broadcastAll("Player 1 chose: " + moveP1 + " and Player 2 chose: " + moveP2);

        if (moveP1.equals(moveP2)) {
            broadcastAll("It's a tie..");
        } else if (moveP1.equals(d.get(moveP2))) {
            broadcastAll("Player 1 wins.");
        } else {
            broadcastAll("Player 2 wins.");
        }

        p1.stopClient();
        p2.stopClient();

    }

    public void broadcastAll(String msg) {
        p1.broadcast(msg);
        p2.broadcast(msg);
    }

}
