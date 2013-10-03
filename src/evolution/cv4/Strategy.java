package evolution.cv4;

public abstract class Strategy {

    public enum Move {
        COOPERATE("C"),
        DECEIVE("D");

        private String label;

        Move(String s) {
            this.label = s;
        }

        public String getLabel() {
            return label;
        }
    }

    public abstract Move nextMove();

    public abstract void reward(Result res);

    public abstract String getName();

    public abstract String authorName();

    public abstract void reset();

}
