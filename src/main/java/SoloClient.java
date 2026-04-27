import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoloClient {
    private final BufferedReader in ;
    private final PrintWriter out;

    public SoloClient(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void runGame() {
        try {


            Map<String, String> d = new HashMap<>();
            d.put("rock", "scissors");
            d.put("scissors", "paper");
            d.put("paper", "rock");

            out.println("You ever heard of rock paper scissors?");
            Random r = new Random();
            String[] values = {"rock", "scissors", "paper"};


            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if ("q".equals(inputLine)) {
                    out.println("Connection closing..");
                    break;
                }

                if (d.get(inputLine) == null) {
                    out.println("I've never heard of " + inputLine + "...");
                    continue;
                }

                String move = values[r.nextInt(values.length)];
                out.println("I choose " + move);

                if(inputLine.equals(move)) {
                    out.println("It's a tie..");
                } else if (inputLine.equals(d.get(move))) {
                    out.println("I win.");
                } else {
                    out.println("I lost...");
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
