package zone.com.java8study.rx.over.compose;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//composes 工具类
public class RxComposes {

    public static <T> ObservableTransformer<T, T> applyObservableAsync() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}