public class Main {
    public static void main(String[] args) {
        var profile = RunningProfile.ONE_TO_MANY;

        var config = new TaskConfig(1000);

        switch (profile) {
            case ONE_TO_MANY -> new OneToMany(args, config).run();
            case OME_TO_ONE -> new OneToOne(args, config).run();
            case MANY_TO_MANY -> new ManyToMany(args, config).run();
            case MANY_TO_ONE -> new ManyToOne(args, config).run();
        }
    }
}
