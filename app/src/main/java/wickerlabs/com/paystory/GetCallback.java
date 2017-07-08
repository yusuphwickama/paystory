package wickerlabs.com.paystory;

import java.util.List;

/**
 * Created by Wickerman on 5/17/2017.
 */

public interface GetCallback {

    void onComplete(List<Payment> payments, Exception e);

}
