package wickerlabs.com.paystory;

/**
 * Created by Wickerman on 5/17/2017.
 */

public class Payment {

    private String amount, payee, date;

    public Payment(String amount, String payee, String date) {
        this.amount = amount;
        this.payee = payee;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public int getAmountValue(){
        return Integer.valueOf(amount);
    }

    public String getPayee() {
        return payee;
    }

    public String getDate() {
        return date;
    }
}
