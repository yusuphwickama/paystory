package wickerlabs.com.paystory;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.text.DateFormat;
import java.util.Date;

public class AddPayment extends AppCompatActivity {

    private Date globDate;
    EditText dateTxt;
    EditText name;
    EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        amount = (EditText) findViewById(R.id.amountEdit);
        name = (EditText) findViewById(R.id.payeeEditTxt);
        dateTxt = (EditText) findViewById(R.id.mDate);

        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTxt = (EditText) v;
                new SingleDateAndTimePickerDialog.Builder(AddPayment.this)
                        .displayHours(false)
                        .displayMinutes(false)
                        .bottomSheet()
                        .title("Date of payment")
                        .titleTextColor(getResources().getColor(R.color.colorPrimaryDark))
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                DateFormat dateFormat = DateFormat.getDateInstance();

                                dateTxt.setText(dateFormat.format(date));
                            }
                        })
                        .display();
            }
        });

        Button submitbtn = (Button)findViewById(R.id.submitBtn);



        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amountStr = amount.getText().toString();
                String nameStr = name.getText().toString();
                String dateStr = dateTxt.getText().toString();

                if(amountStr.trim().length() > 3 && nameStr.trim().length() > 5 && dateStr.trim().length() > 5){

                    final ProgressDialog progressDialog = ProgressDialog.show(AddPayment.this,"Please wait","Sending information", true, false);

                    Payment payment = new Payment(amountStr,nameStr,dateStr);

                    Backend.getInstance().storePayment("wickerman", payment, new AddCallback() {
                        @Override
                        public void onComplete(final String response, final Exception e) {
                            if(e == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddPayment.this,response,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                                AddPayment.this.finish();
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddPayment.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(AddPayment.this,"Invalid input fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
