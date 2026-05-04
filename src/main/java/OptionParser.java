import java.util.Arrays;
import java.util.Optional;

public class OptionParser {
    private OptionParser() {
    }

    public static Optional<Option> parseFromUserInputLine(String userInputLine) {
        return Arrays.stream(Option.values())
                .filter(option -> option.name().equalsIgnoreCase(userInputLine))
                .findFirst();
    }
}
