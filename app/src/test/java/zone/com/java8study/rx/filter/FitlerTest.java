package zone.com.java8study.rx.filter;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 */

public class FitlerTest {

    @Test
    public void take() {

        System.out.println("take:");
//        只发射前面的N项数据 然后发射完成通 知，忽略剩余的数据。
        Observable.range(0,10)
                .take(3)
                .subscribe(o -> System.out.print(o + "\t"));
        System.out.println("\ntake2:");
//       发射Observable开始的那段时间发射 的数据，
        Observable.range(0,10)
                .take(100, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("\ntakeLast:");
//        只发射前面的N项数据 然后发射完成通 知，忽略剩余的数据。
        Observable.range(0,10)
                .takeLast(3)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("\ntakeLast2:");
//       发射在原始Observable的生命周 期内最后一段时间内发射的数据
        Observable.range(0,10)
                .takeLast(100, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("\ntakeUntil:");
        Observable.just(2,3,4,5)
                //发送complete的结束条件 当然发送结束之前也会包括这个值
                .takeUntil(integer ->  integer<=4)
                .subscribe(o -> System.out.print(o + "\t"));//2,3,4

        System.out.println("\ntakeWhile:");
        Observable.just(2,3,4,5)
                //当不满足这个条件 会发送结束 不会包括这个值
                .takeWhile(integer ->integer<=4 )
                .subscribe(o -> System.out.print(o + "\t"));//2,3
        System.out.println("\n over");
    }

    @Test
    public void skip2skipLast() {
        //丢弃Observable发射的前N项数据
        Observable.range(0,10)
                .skip(3)
                .subscribe(o -> System.out.print(o + "\t"));

        //变体 丢弃原始Observable开始的那段时间发 射的数据
        Observable.range(0,10)
                .skip(100, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.print(o + "\t"));

//        丢弃Observable发射的后N项数据
        Observable.range(0,10)
                .skipLast(3)
                .subscribe(o -> System.out.print(o + "\t"));

        //变体  丢弃在原始Observable的生命周 期内最后一段时间内发射的数据
        Observable.range(0,10)
                .skipLast(100, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.print(o + "\t"));

    }
    @Test
    public void sample() {
        /**
         * sample==throttleLast 周期采样后 发送最后的数据
         * throttleFirst  周期采样 的第一条数据 发送；
         * todo 注意 如果是已经被发送过的 则不会继续发送
         */
        Observable.create(e -> {
            e.onNext("onNext 0");
            Thread.sleep(100);
            e.onNext("onNext 1");
            Thread.sleep(50);
            e.onNext("onNext 2");
            Thread.sleep(70);
            e.onNext("onNext 3");
            Thread.sleep(200);
            e.onNext("onNext 4");
            e.onNext("onNext 5");
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .sample(200, TimeUnit.MILLISECONDS,Schedulers.newThread())
//                .throttleLast(200, TimeUnit.MILLISECONDS,Schedulers.newThread())
//                .throttleFirst(200, TimeUnit.MILLISECONDS,Schedulers.newThread())
                .subscribe(o -> System.out.print(o + "\t"));
        while (true){}

    }

    @Test
    public void IgnoreElements() {
//        如果你不关心一个Observable发射的数据，但是希望在它完成时或遇到错误终止时收到通 知
        Observable.range(0, 10)
                .ignoreElements()
                .subscribe(() -> System.out.println("complete")
                        , throwable -> System.out.println("throwable"));
    }

    @Test
    public void first2Last() {
        //原理:elementAt(0) 没啥用
        Observable.range(0, 10)
                //如果元数据没有发送  则有发送默认值
                .first(-1)
                .subscribe(o -> System.out.print(o + "\t"));

        Observable.range(0, 10)
                //如果元数据没有发送  则有发送默认值
                .last(-1)
                .subscribe(o -> System.out.print(o + "\t"));

        //用 take去实现
        Observable.range(0, 10)
                //如果元数据没有发送  则有发送默认值
                .take(1)
                .subscribe(o -> System.out.print(o + "\t"));

        Observable.range(0, 10)
                //如果元数据没有发送  则有发送默认值
                .takeLast(1)
                .subscribe(o -> System.out.print(o + "\t"));
    }

    @Test
    public void filter() {
        System.out.println("filter:");
        Observable.range(0, 10)
                //过滤掉false的元素
                .filter(integer -> integer % 2 == 0)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("ofType:");
        Observable.just(0, "what?", 1, "String", 3)
                //ofType 是 filter 操作符的一个特殊形式。它过滤一个Observable只返回指定类型的数据。
                .ofType(String.class)
                .subscribe(o -> System.out.print(o + "\t"));
    }

    @Test
    public void elementAt() {
//        只发射第N项数据
        System.out.println("elementAt:");
        Observable.range(0, 10)
                .elementAt(0)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("elementAt2:");
        Observable.range(0, 10)
//        如果索引值大于数据 项数，它会发射一个默认值(通过额外的参数指定)，而不是抛出异常。
//    但是如果你传递一 个负数索引值，它仍然会抛出一个 IndexOutOfBoundsException 异常。
                .elementAt(100, -100)
                .subscribe(o -> System.out.print(o + "\t"));
    }

    @Test
    public void distinct() {
        System.out.println("distinct1:");
        Observable.just(1, 2, 1, 2, 3)
                .distinct()
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println("\ndistinct2:");
        Observable.just(1, 2, 1, 2, 3)
                //这个函数根据原始Observable发射的数据项产生一个 Key，
                // 然后，比较这些Key而不是数据本身，来判定两个数据是否是不同的
                .distinct(integer -> Math.random())
                .subscribe(o -> System.out.print(o + "\t"));

//       distinctUntilChanged:它只判定一个数据和它的直接前驱是 否是不同的。
        System.out.println("\ndistinctUntilChanged:");
        Observable.just(1, 1, 2, 2, 1, 2, 3)
                .distinctUntilChanged()
                .subscribe(o -> System.out.print(o + "\t"));

//       distinctUntilChanged:它只判定一个数据和它的直接前驱是 否是不同的。
        System.out.println("\ndistinctUntilChanged2:");
        Observable.just(1, 1, 2, 2, 1, 2, 3)
                //distinct(Func1) 一样，根据一个函数产生的Key判定两个相邻的数据项是不是不同的
                .distinctUntilChanged(integer -> Math.random())
                .subscribe(o -> System.out.print(o + "\t"));

    }

    @Test
    public void throttleWithTimeout2debounce() {
/**
 * 操作符会过滤掉发射速率过快的数据项
 *   throttleWithTimeout/debounce： 含义相同
 *   如果发送数据后 指定时间内没有新数据的话 。则发送这条
 *   如果有新数据 则从新 做上面的逻辑。继续验证 是否发送此条
 */
        Observable.create(e -> {
            e.onNext("onNext 0");
            Thread.sleep(100);
            e.onNext("onNext 1");
            Thread.sleep(230);
            e.onNext("onNext 2");
            Thread.sleep(300);
            e.onNext("onNext 3");
            Thread.sleep(400);
            e.onNext("onNext 4");
            Thread.sleep(500);
            e.onNext("onNext 5");
            e.onNext("onNext 6");
        })
//                .throttleWithTimeout(330, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(o -> System.out.println(o));//结果 3 4 6

        while (true) {
        }
    }

}
