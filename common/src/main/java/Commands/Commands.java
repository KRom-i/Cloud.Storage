package Commands;

public class Commands {

    public static final String AUTH = "/auth";
    public static final String CHECK = "/check";
    public static final String USER = "/us";
    public static final String ADMIN = "/adm";
    public static final String ADD = "/add";

    public static final String LOAD = "/load";
    public static final String UP = "/up";
    public static final String DOWN = "/down";
    public static final String PACK = "/pack";
    public static final String END_PACK = "/endPack";
    public static final String INCOMING = "/incoming";
    public static final String EXIST = "EXIST";

    public static final String CURRENT_DIRECTORY = "/cd";
    public static final String DIRECTORY_HOME = "/home";

    public static final String FILE = "/file";
    public static final String LIST = "/list";
    public static final String CREATE_FILE = "/creFil";
    public static final String CREATE_DIR = "/creDir";
    public static final String DELETE = "/del";
    public static final String RENAME = "/ren";
    public static final String COPY = "/copy";
    public static final String SEARCH = "/search";

    public static final String ZIP = "/zip";
    public static final String UNZIP = "/unzip";

    public static final String ROOT = "/root";

    public static final String INFO = "/info";
    public static final String SET = "/set";
    public static final String GET = "/get";

    public static final String NICK = "/nick";
    public static final String SIZE = "/size";

    public static final String EXIT = "/exit";

    public static final String ERROR = "/err";

    public static final String REPLACE = "/replace";

    public static final String[] DELIMITER = {":", "!", "~", "@"};




    public static String BUILD(int numDelimiter, String... commands){

        if (commands.length == 0) return null ;

        if (numDelimiter < 0 || numDelimiter > DELIMITER.length - 1) return  null;

        String command = "";

        for(int i = 0; i < commands.length; i++) {
             command += commands[i];
             if (i != commands.length - 1) command += DELIMITER[numDelimiter];
        }

        return command;
    }





}
