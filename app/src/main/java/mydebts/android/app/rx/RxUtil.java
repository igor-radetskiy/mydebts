package mydebts.android.app.rx;

import javax.inject.Inject;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    @Inject
    public RxUtil() {}

    public <T> SingleTransformer<T, T> singleSchedulersTransformer() {
        return single -> single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
