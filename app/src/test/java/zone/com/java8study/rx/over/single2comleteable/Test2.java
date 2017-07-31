package zone.com.java8study.rx.over.single2comleteable;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * [2017] by Zone
 * 理解RxJava中的Single和Completable
 * http://developer.51cto.com/art/201703/535298.htm
 */

public class Test2 {

    //   Single，其实这个网络请求并不是一个连续事件流，你只会发起一次 Get 请求返回数据并且只收到一个事件。
//    我们都知道这种情况下 onComplete 会紧跟着 onNext 被调用，那为什么不把它们合二为一呢？
    @Test
    public void single() {
        Single.just("Amit")
                .subscribe(s -> System.out.println(s)
                        , throwable -> System.out.println("异常"));
    }


    //通过 PUT 请求更新数据 我只关心 onComplete 事件。
    //    使用 Completable 时我们忽略 onNext 事件，只处理 onComplete 和 onError 事件
    @Test
    public void completable() {
        Completable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(() -> System.out.println("成功")
                        , throwable -> System.out.println("异常"));
    }


    //Flowable是RxJava2.x中新增的类，专门用于应对背压（Backpressure）问题，但这并不是RxJava2.x中新引入的概念。
    // 所谓背压，即生产者的速度大于消费者的速度带来的问题，比如在Android中常见的点击事件，点击过快则会造成点击两次的效果
    @Test
    public void flowable() {
        Flowable.create(e -> {
            for (int i = 0; i < 10000; i++) {
                e.onNext(i);
            }
            e.onComplete();
        }, BackpressureStrategy.DROP) //指定背压处理策略，抛出异常
                .subscribe(integer -> {
                    System.out.println(integer);
                    Thread.sleep(1000);
                }, throwable -> System.out.println("异常"));
    }
}
