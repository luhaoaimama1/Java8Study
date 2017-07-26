package zone.com.java8study.rx.connectableObservable;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;

/**
 * [2017] by Zone
 * 可连接的Observable在 被订阅时并不开始发射数据，只有在它的 connect() 被调用时才开始
 * 用这种方法，你可以 等所有的潜在订阅者都订阅了这个Observable之后才开始发射数据。
 * 即使没有任何订阅者订阅它，你也可以使用 connect 让他发射
 * <p>
 * Observable.publish()将一个Observable转换为一个可连接的Observable
 * ConnectableObservable.connect()指示一个可连接的Observable开始发射数据.
 * <p>
 * refCount  操作符把从一个可连接的Observable连接和断开的过程自动化了
 * 就像reply的感觉式样 每次订阅 都对单个订阅的重复播放一边
 * <p>
 * Observable share方法就是publish().refCount()的连用
 * <p>
 * ConnectableObservable之前使用Replay 操作符
 * 总是发射完整的数据序列给任何未来的观察者，即使对于connect() 之后的订阅者也是一样
 */

public class ConnectableTest {


    @Test
    public void replay() {
        //todo demo无效！
        ConnectableObservable<Integer> co = Observable.just(1, 2, 3)
                //保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅
                .replay(3)
                .publish();//publish转成ConnectableObservable
        co.connect();//此时开始发射数据 不同与 refCount 只发送一次
        co.subscribe(integer -> System.out.println("订阅1：" + integer));
        co.subscribe(integer -> System.out.println("订阅2：" + integer));

        co.subscribe(integer -> System.out.println("订阅3：" + integer));
    }

    //skip系列是 从某一个位置到结束
    @Test
    public void connect() {
        ConnectableObservable<Integer> co = Observable.just(1, 2, 3)
                .publish();//publish转成ConnectableObservable

        co.subscribe(integer -> System.out.println("订阅1：" + integer));
        co.subscribe(integer -> System.out.println("订阅2：" + integer));
        co.subscribe(integer -> System.out.println("订阅3：" + integer));
        co.connect();//此时开始发射数据 不同与 refCount 只发送一次
    }

    @Test
    public void publish() {
        ConnectableObservable<Integer> co = Observable.just(1, 2, 3)
                .publish();

        co.subscribe(integer -> System.out.println("订阅1：" + integer));
        co.subscribe(integer -> System.out.println("订阅2：" + integer));
        co.subscribe(integer -> System.out.println("订阅3：" + integer));
        co.connect();//此时开始发射数据
    }

    @Test
    public void publish2() {
        Observable<Integer> co = Observable.just(1, 2, 3)
                .publish()
                //类似于reply  跟时间线有关  订阅开始就开始发送
                .refCount();

        co.subscribe(integer -> System.out.println("订阅1：" + integer));
        co.subscribe(integer -> System.out.println("订阅2：" + integer));

        Observable.timer(300, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> {
                    co.subscribe(integer -> System.out.println("订阅3：" + integer));
                }).blockingSubscribe();

    }
}
