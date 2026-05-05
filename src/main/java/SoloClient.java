import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoloClient implements Game {
    private final ClientHandler p1;
    private final Map<String, String> d;

    public SoloClient(ClientHandler p1) {
        this.p1 = p1;
        this.d = new HashMap<>();
        d.put("rock", "scissors");
        d.put("scissors", "paper");
        d.put("paper", "rock");
    }

    public void runGame() {
            p1.broadcast("You ever heard of rock paper scissors?");
            Random r = new Random();
            String[] values = {"rock", "scissors", "paper"};

            String inputLine;
            while ((inputLine = p1.receive()) != null) {

                if ("q".equals(inputLine)) {
                    p1.broadcast("Connection closing..");
                    break;
                }

                if (d.get(inputLine) == null) {
                    p1.broadcast("I've never heard of " + inputLine + "...");
                    continue;
                }

                String move = values[r.nextInt(values.length)];
                p1.broadcast("I choose " + move);

                if(inputLine.equals(move)) {
                    p1.broadcast("It's a tie..");
                } else if (inputLine.equals(d.get(move))) {
                    p1.broadcast("I win.");
                } else {
                    p1.broadcast("I lost...");
                }
            }
    }
}
