public enum Option {
    ROCK,
    SCISSORS,
    PAPER;

    public boolean defeats(Option option) {
        return OptionComparator.instance.compare(this, option) > 0;
    }
}
