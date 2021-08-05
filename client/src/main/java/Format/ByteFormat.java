package Format;

public class ByteFormat {

    public static String getStr(long bytes){

        long b = bytes;

        double kb = (double) bytes / 1024;

        double mb = kb / 1024;

        double gb = mb / 1024;


        if (gb > 1){
            return DoubleFormat.getRound(gb, 2) + " ГБ";

        } else if (mb > 1){
            return DoubleFormat.getRound(mb, 2) + " МБ";

        } else if (kb > 1){
            return DoubleFormat.getRound(kb, 2) + " КБ";

        }  else {
            return b + " байт";

        }

    }

}
