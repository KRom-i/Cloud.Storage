package Format;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleFormat {


    public static double getRound(double doubleValue, int places){

        BigDecimal db = new BigDecimal(Double.toString(doubleValue));

        db = db.setScale(places, RoundingMode.HALF_UP);

        return  db.doubleValue();
    }


}
