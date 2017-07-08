package wickerlabs.com.paystory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Wickerman on 5/17/2017.
 */

public class Backend {
    private static Backend ourInstance;
    private OkHttpClient httpClient;

    public static Backend getInstance() {
        if (ourInstance == null) {
            ourInstance = new Backend();
            return ourInstance;
        } else {
            return ourInstance;
        }
    }

    private Backend() {
        httpClient = new OkHttpClient();
    }

    public void storePayment(String api, Payment payment, final AddCallback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("apiKey", api)
                .add("amount", String.valueOf(payment.getAmountValue()))
                .add("payee",payment.getPayee())
                .add("date",payment.getDate())
                .build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constants.BASE_URL.concat("add"))
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(null,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();

                try {
                JSONObject object = new JSONObject(json);

                String responseMsg = null;

                    responseMsg = object.getString("message");
                    callback.onComplete(responseMsg,null);

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onComplete(null,e);
                }
            }
        });
    }

    public List<Payment> getPayments(String api,final GetCallback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("apiKey", api)
                .build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constants.BASE_URL.concat("get"))
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onComplete(null, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String webResponse = response.body().string();
                List<Payment> paymentsList = new ArrayList<Payment>();

                JSONObject object = null;
                try {
                    object = new JSONObject(webResponse);
                    JSONArray payments = object.getJSONArray("payments");

                    for (int i = 0; i < payments.length(); i++) {
                        JSONObject payObject = payments.getJSONObject(i);

                        String amount = payObject.getString("amount");
                        String payee = payObject.getString("payee");
                        String datePaid = payObject.getString("date_of");

                        Payment payment = new Payment(amount, payee, datePaid);
                        paymentsList.add(payment);
                    }
                    callback.onComplete(paymentsList, null);

                } catch (JSONException e) {
                    callback.onComplete(null,e);
                    e.printStackTrace();
                }
            }
        });

        return new ArrayList<>();
    }
}
