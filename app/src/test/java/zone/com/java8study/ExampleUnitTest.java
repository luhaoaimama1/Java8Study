package zone.com.java8study;

import android.os.Looper;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void bt_hello() {
        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("onError");
                emitter.onError(new Throwable("HEIHEI"));
                System.out.println("onError");
                emitter.onError(new Throwable("HEIHEI"));
                System.out.println("emit 2");
                emitter.onNext(2);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });

    }
}