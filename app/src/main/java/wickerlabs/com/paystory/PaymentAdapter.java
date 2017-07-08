package wickerlabs.com.paystory;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Wickerman on 5/17/2017.
 */

public class PaymentAdapter extends ArrayAdapter<Payment> {

    private int resourceId;
    private List<Payment> paymentList;
    private Context context;


    public PaymentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Payment> objects) {
        super(context,resource,objects);
        this.context = context;
        this.paymentList = objects;
        this.resourceId = resource;
    }


    @Override
    public int getCount() {
        return paymentList.size();
    }

    @Nullable
    @Override
    public Payment getItem(int position) {
        return paymentList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(resourceId,parent,false);

        TextView amount = (TextView) view.findViewById(R.id.amountTxt);
        TextView payee = (TextView) view.findViewById(R.id.payeeTxt);
        TextView date = (TextView) view.findViewById(R.id.dateOfTxt);

        Payment payment = getItem(position);

        String paidby = payment.getPayee();
        String amountPaid = payment.getAmount();
        String datePaid = payment.getDate();

        payee.setText(paidby);

        amount.setText(Utils.moneyFormatter(amountPaid).concat("/="));
        date.setText(datePaid);


        return view;
    }
}
