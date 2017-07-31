package zone.com.java8study.rx.over;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * [2017] by Zone
 *
 * 所有Subject 都可以直接发射不需要 发射器的引用 和 Observable.create()不同
 */
public class Subjects {

    @Test
    public void subjectIsSubscribe() {
        //Subjects 是可以被 subscribe订阅
        PublishSubject bs = PublishSubject.create();
        bs.subscribe(o -> System.out.println("1:"+o));
        Observable.just(1,2,3)
                .subscribe(bs);
    }

    @Test
    public void AsyncSubject() {
//        简单的说使用AsyncSubject无论输入多少参数，永远只输出最后一个参数。
//        但是如果因为发生了错误而终止，AsyncSubject将不会发射任何数据，只是简单的向前传递这个错误通知。
        AsyncSubject<Integer> source = AsyncSubject.create();

        source.subscribe(o -> System.out.println("1:"+o)); // it will emit only 4 and onComplete

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        /*
         * it will emit 4 and onComplete for second observer also.
         */
        source.subscribe(o -> System.out.println("2:"+o));

        source.onNext(4);
        source.onComplete();
    }

    @Test
    public void behaviorSubject() {
//     BehaviorSubject会发送离订阅最近的上一个值，没有上一个值的时候会发送默认值。看图。

        BehaviorSubject<Integer> source = BehaviorSubject.create();
        //默认值版本
//        BehaviorSubject<Integer> source = BehaviorSubject.createDefault(-1);

        source.subscribe(o -> System.out.println("1:"+o)); // it will get 1, 2, 3, 4 and onComplete

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        /*
         * it will emit 3(last emitted), 4 and onComplete for second observer also.
         */
        source.subscribe(o -> System.out.println("2:"+o));

        source.onNext(4);
        source.onComplete();
    }
    @Test
    public void publishSubject() {
//     PublishSubject 可以说是最正常的Subject，从那里订阅就从那里开始发送数据。
        PublishSubject bs = PublishSubject.create();

        bs.subscribe(o -> System.out.println("1:"+o));
        bs.onNext(1);
        bs.onNext(2);
        bs.subscribe(o -> System.out.println("2:"+o));
        bs.onNext(3);
        bs.onComplete();
        bs.subscribe(o -> System.out.println("3:"+o));
    }

    @Test
    public void replaySubject() {
//        无论何时订阅，都会将所有历史订阅内容全部发出。
        ReplaySubject bs = ReplaySubject.create();

        bs.subscribe(o -> System.out.println("1:"+o));
// 无论何时订阅都会收到1，2，3
        bs.onNext(1);
        bs.onNext(2);
        bs.onNext(3);
        bs.onComplete();

        bs.subscribe(o -> System.out.println("2:"+o));

    }
}
