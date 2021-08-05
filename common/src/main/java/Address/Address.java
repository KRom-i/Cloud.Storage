package Address;

public class Address {

//    Аутентификация
    public final static int AUTH = 0;

    public final static int LOG = 1;
    public final static int EXIT = 2;


//  Работа с файлами
    public final static int FILE = 1;

    public final static int DIR_DOWN = 0;
    public final static int DIR_UP = 1;
    public final static int DIR_SET = 2;
    public final static int DIR_HOME = 3;
    public final static int CREATE_FILE = 4;
    public final static int CREATE_DIR = 5;
    public final static int DELETE = 6;
    public final static int RENAME = 7;
    public final static int COPY = 8;
    public final static int SEARCH = 9;
    public final static int ZIP = 10;
    public final static int UNZIP = 11;
    public final static int REFRESH = 12;

//  Загрузка файла
    public final static int LOAD = 2;

    public final static int PACK_DOWN = 0;
    public final static int DATA_DOWN = 1;
    public final static  int DOWN_END = 5;

    public final static int PACK_UP = 2;
    public final static int DATA_UP = 3;
    public final static int UP_END = 4;




//    Ошибки
    public final static int ERROR = 3;
    public final static int SIZE = 0;
    public final static int NOT_FOUND = 1;




}
