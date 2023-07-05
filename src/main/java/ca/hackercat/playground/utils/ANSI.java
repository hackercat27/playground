package ca.hackercat.playground.utils;

/**
 * Contains predefined ANSI escape sequences.
 */
public class ANSI {
    private ANSI() {}

    // like a real doom engine. you remember from yesterday when you wrote this.

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    
    public static final String FOREGROUND_BLACK = "\u001B[30m";
    public static final String FOREGROUND_RED = "\u001B[31m";
    public static final String FOREGROUND_GREEN = "\u001B[32m";
    public static final String FOREGROUND_YELLOW = "\u001B[33m";
    public static final String FOREGROUND_BLUE = "\u001B[34m";
    public static final String FOREGROUND_MAGENTA = "\u001B[35m";
    public static final String FOREGROUND_CYAN = "\u001B[36m";
    public static final String FOREGROUND_LIGHT_GRAY = "\u001B[37m";
    public static final String FOREGROUND_DARK_GRAY = "\u001B[90m";
    public static final String FOREGROUND_LIGHT_RED = "\u001B[91m";
    public static final String FOREGROUND_LIGHT_GREEN = "\u001B[92m";
    public static final String FOREGROUND_LIGHT_YELLOW = "\u001B[93m";
    public static final String FOREGROUND_LIGHT_BLUE = "\u001B[94m";
    public static final String FOREGROUND_LIGHT_MAGENTA = "\u001B[95m";
    public static final String FOREGROUND_LIGHT_CYAN = "\u001B[96m";
    public static final String FOREGROUND_WHITE = "\u001B[97m";
    
    public static final String BACKGROUND_BLACK = "\u001B[40m";
    public static final String BACKGROUND_RED = "\u001B[41m";
    public static final String BACKGROUND_GREEN = "\u001B[42m";
    public static final String BACKGROUND_YELLOW = "\u001B[43m";
    public static final String BACKGROUND_BLUE = "\u001B[44m";
    public static final String BACKGROUND_MAGENTA = "\u001B[45m";
    public static final String BACKGROUND_CYAN = "\u001B[46m";
    public static final String BACKGROUND_LIGHT_GRAY = "\u001B[47m";
    public static final String BACKGROUND_DARK_GRAY = "\u001B[100m";
    public static final String BACKGROUND_LIGHT_RED = "\u001B[101m";
    public static final String BACKGROUND_LIGHT_GREEN = "\u001B[102m";
    public static final String BACKGROUND_LIGHT_YELLOW = "\u001B[103m";
    public static final String BACKGROUND_LIGHT_BLUE = "\u001B[104m";
    public static final String BACKGROUND_LIGHT_MAGENTA = "\u001B[105m";
    public static final String BACKGROUND_LIGHT_CYAN = "\u001B[106m";
    public static final String BACKGROUND_WHITE = "\u001B[107m";
    
//    public static void main(String[] args) {
//        // tests all ANSI colors with a print instruction.
//
//        System.out.println(
//                BOLD + "bolded" + RESET + "\n" +
//                        UNDERLINE + "underlined" + RESET + "\n" +
//                        FOREGROUND_BLACK + "black fg" + RESET + "\n" +
//                        FOREGROUND_RED + "red fg" + RESET + "\n" +
//                        FOREGROUND_GREEN + "green fg" + RESET + "\n" +
//                        FOREGROUND_YELLOW + "yellow fg" + RESET + "\n" +
//                        FOREGROUND_BLUE + "blue fg" + RESET + "\n" +
//                        FOREGROUND_MAGENTA + "magenta fg" + RESET + "\n" +
//                        FOREGROUND_CYAN + "cyan fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_GRAY + "light gray fg" + RESET + "\n" +
//
//                        FOREGROUND_DARK_GRAY + "dark gray fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_RED + "light red fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_GREEN + "light green fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_YELLOW + "light yellow fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_BLUE + "light blue fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_MAGENTA + "light magenta fg" + RESET + "\n" +
//                        FOREGROUND_LIGHT_CYAN + "light cyan fg" + RESET + "\n" +
//                        FOREGROUND_WHITE + "white fg" + RESET + "\n" +
//
//                        BACKGROUND_BLACK + "black bg" + RESET + "\n" +
//                        BACKGROUND_RED + "red bg" + RESET + "\n" +
//                        BACKGROUND_GREEN + "green bg" + RESET + "\n" +
//                        BACKGROUND_YELLOW + "yellow bg" + RESET + "\n" +
//                        BACKGROUND_BLUE + "blue bg" + RESET + "\n" +
//                        BACKGROUND_MAGENTA + "magenta bg" + RESET + "\n" +
//                        BACKGROUND_CYAN + "cyan bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_GRAY + "light gray bg" + RESET + "\n" +
//
//                        BACKGROUND_DARK_GRAY + "dark_gray bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_RED + "light red bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_GREEN + "light green bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_YELLOW + "light yellow bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_BLUE + "light blue bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_MAGENTA + "light magenta bg" + RESET + "\n" +
//                        BACKGROUND_LIGHT_CYAN + "light cyan bg" + RESET + "\n" +
//                        BACKGROUND_WHITE + "white bg" + RESET + "\n"
//        );
//    }
}
