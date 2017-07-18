package zone.com.java8study;

import android.os.Looper;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
        Observable<String> observable = Observable.just("hello", "rxjava");
//        observable.map(t -> t + "Zone").subscribe(o -> System.out.println(o));
        observable
                .observeOn(Schedulers.newThread())
                .map(t -> t + "Zone")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    System.out.println("UI线程："+(Looper.myLooper() != Looper.getMainLooper()));
                    System.out.println(o);
                });

//        Observable.just("Hello, world!")
//                .map(s -> s.hashCode())
//                .map(s -> String.valueOf(s))
//                .subscribe(i -> System.out.println(i));
//        String[] ulrs = new String[]{"url1", "url2", "url3"};
//        Observable.just("Hello","world!")
//                .filter(s->s=="Hello")
//                .flatMap(s-> Observable.from(ulrs).map(a->a+" "+s))
//                .take(2)
//                .subscribe(a-> System.out.println(a)).isUnsubscribed();


    }
}