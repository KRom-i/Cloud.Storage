package DataTransferTools;

public class BinarySystem {

    private final static int bitInteger = 32;

    /**
     ( Integer =  123456 ) бинарный сдвиг вправо:

     24 разряда  (i = 0)  biCode[i] = 123456 >> (32 - ( (0 + 1) * 8)) =  [ 0 ]
     16 разрядов  (i = 1)  biCode[i] = 123456 >> (32 - ( (1 + 1) * 8)) = [ 0 ]
     8 разярядов    (i = 2)  biCode[i] = 123456 >> (32 - ( (2 + 1) * 8)) = [ 47 ]
     0 разрядов      (i = 3)  biCode[i] = 123456 >> (32 - ( (3 + 1) * 8)) = [ 57 ]

     Результат 4 байта (32 бита)

     byte[4] { 0, 0, 47, 57}
     */

    public static byte[] integerEncoder (int length) {

        byte[] biCode = new byte[bitInteger / 8];

        for (int i = 0; i < biCode.length; i++) {
            biCode[i] = (byte) (length >> (bitInteger - ( (i + 1) * 8 )));
        }

        return biCode;

    }

    /**
     *
     ( byte[4] { 0, 0, 47, 57} ) бинарный сдвиг влево:

     24 разряда   (i = 0)  int shift = (4 - 1 - 0) * 8;   = 24
     16 разрядов   (i = 1)  int shift = (4 - 1 - 1) * 8;   = 26
     8 разярядов    (i = 2)  int shift = (4 - 1 - 2) * 8;   = 8
     0 разрядов      (i = 3)  int shift = (4 - 1 - 3) * 8;   = 0

     value 0 = 0
     value 1 = 0
     value 2 = 12288
     value 3 = 57

     Результат Integer =  123456  ( сумма value (0 - 3) )

     */

    public static int integerDecoder (byte[] b) {

        int numberBytes = bitInteger / 8;

        int value = 0;

        for (int i = 0; i < numberBytes; i++) {

            int shift = (numberBytes - 1 - i) * 8;

            value += (b[i] & 0x000000FF) << shift;
        }
        return value;

    }

}
