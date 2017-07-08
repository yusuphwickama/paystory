package wickerlabs.com.paystory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;


public class Utils {

    public static String moneyFormatter(String amount){

        double moneyAmount = Double.valueOf(amount);

        return new DecimalFormat("#,###").format(Double.valueOf(moneyAmount));

    }


}
