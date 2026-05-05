import java.util.Comparator;

public class OptionComparator implements Comparator<Option> {
    public static final OptionComparator instance = new OptionComparator();

    private OptionComparator() {
    }

    @Override
    public int compare(Option o1, Option o2) {
        return switch (o1) {
            case Option.ROCK -> switch (o2) {
                case Option.ROCK -> 0;
                case Option.SCISSORS -> 1;
                case Option.PAPER -> -1;
            };
            case Option.SCISSORS -> switch (o2) {
                case Option.ROCK -> -1;
                case Option.SCISSORS -> 0;
                case Option.PAPER -> 1;
            };
            case Option.PAPER -> switch (o2) {
                case Option.ROCK -> 1;
                case Option.SCISSORS -> -1;
                case Option.PAPER -> 0;
            };
        };
    }
}
