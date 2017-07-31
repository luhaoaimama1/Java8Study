package zone.com.java8study.rx;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by amitshekhar on 06/02/17.
 *
 */
public class RxBus {

    public RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }


    @Test
    public void why(){
        RxBus bus=new RxBus();
        bus.toObservable().subscribe(o -> System.out.println(o));
        //为什么 rxbus用  publish呢 因为publish可以直接发射 而不需要 发射器的引用！
        //Subject 都可以直接发射不需要 发射器的引用 和     Observable.create()不同
        bus.send("a");


    }

}
