import java.util.Optional;
import java.util.Random;

public class SoloClient implements Game {
    private static final String USER_INPUT_LINE_QUIT = "q";

    private final ClientHandler p1;

    public SoloClient(ClientHandler p1) {
        this.p1 = p1;
    }

    public void runGame() {
        p1.broadcast("You ever heard of rock paper scissors?");
        Random r = new Random();

        String userInputLine;
        while ((userInputLine = p1.receive()) != null) {

            if (USER_INPUT_LINE_QUIT.equals(userInputLine)) {
                p1.broadcast("Connection closing..");
                break;
            }

            Optional<Option> userOptionOptional = OptionParser.parseFromUserInputLine(userInputLine);
            if (userOptionOptional.isEmpty()) {
                p1.broadcast("I've never heard of " + userInputLine + "...");
                continue;
            }

            Option userOption = userOptionOptional.get();

            Option cpuOption = Option.values()[r.nextInt(Option.values().length)];
            p1.broadcast("I choose " + cpuOption);

            if (cpuOption.equals(userOption)) {
                p1.broadcast("It's a tie..");
            } else if (cpuOption.defeats(userOption)) {
                p1.broadcast("I win.");
            } else {
                p1.broadcast("I lost...");
            }
        }
    }
}
