package wickerlabs.com.paystory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView paymentList;
    private PaymentAdapter paymentAdapter;
    private List<Payment> payments;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,AddPayment.class));
            }
        });

        payments = new ArrayList<>();
        paymentList = (ListView) findViewById(R.id.paymentList);

        paymentAdapter = new PaymentAdapter(this,R.layout.payment_item,payments);

        getPayments();

    }

    private void getPayments(){
        dialog = ProgressDialog.show(MainActivity.this,"Please wait","Loading payments...",true,false);
        paymentAdapter.clear();
        payments.clear();

        payments = Backend.getInstance().getPayments("wickerman", new GetCallback() {
            @Override
            public void onComplete(final List<Payment> payments, final Exception e) {
                if(e == null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            paymentAdapter.addAll(payments);
                            dialog.dismiss();
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        paymentList.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            getPayments();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
