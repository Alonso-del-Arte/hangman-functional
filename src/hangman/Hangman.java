package hangman;

public class Hangman {

    private static boolean playWithGUI = false;

    private static String[] processCommandLineOptions(String[] args) {
        String[] processedArgs = new String[args.length];
        int index = 0;
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "-g":
                case "-graphical":
                case "-gui":
                    playWithGUI = true;
                    processedArgs[index++] = "-g";
                    break;
                case "-text":
                case "-txt":
                    playWithGUI = false;
                    processedArgs[index++] = "-t";
                    break;
                default:
                    System.err.println("Command line option " + arg
                            + " not recognized");
            }
        }
        return processedArgs;
    }

    public static void main(String[] args) {
        String[] procArgs = processCommandLineOptions(args);
        if (playWithGUI) {
            ui.graphical.Game.main(procArgs);
        } else {
            ui.text.Game.main(procArgs);
        }
    }

}
