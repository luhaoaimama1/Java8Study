package zone.com.java8study.rx.over.condition;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * [2017] by Zone
 */

public class ConditionTest {

    //take系列是 从开始 到某一个位置
    @Test
    public void takeUntil() {
        System.out.println("\n takeUntil");
//      当第二个Observable发射了一项数据或者终止时，丢弃原始Observable发射的任何数据
        Observable.intervalRange(30, 20, 500, 100, TimeUnit.MILLISECONDS)
                .takeUntil(Observable.timer(1000, TimeUnit.MILLISECONDS))
                .doOnNext(integer -> System.out.println(integer))
                .doOnComplete(() -> System.out.println("Complete"))
                //此时用这个主要是 测试环境 有执行时间 所以用阻塞比较好
                .blockingSubscribe();


        //条件成立之后，之后就是包括条件成立的数据，丢弃原始Observable发射的任何数据
        System.out.println("\ntakeUntil2:");
        Observable.just(2,3,4,5)
                .takeUntil(integer ->  integer<=4)
                .subscribe(o -> System.out.print(o + "\t"));//2,3,4

        System.out.println("\ntakeWhile:");
        //发射Observable发射的数据，直到一个指定的条件不成立
        Observable.just(2,3,4,5)
                .takeWhile(integer ->integer<=4 )
                .subscribe(o -> System.out.print(o + "\t"));//2,3
        System.out.println("\n over");
    }


    //skip系列是 从某一个位置到结束
    @Test
    public void skipUntil() {
        System.out.println("\nskipUntil:");
//        SkipUntil 订阅原始的Observable，但是忽略它的发射物，
//       直到第二个Observable发射了一 项数据那一刻，它开始发射原始Observable。
        Observable.intervalRange(30, 20, 500, 100, TimeUnit.MILLISECONDS)
                .skipUntil(Observable.timer(1000, TimeUnit.MILLISECONDS))
                .doOnNext(integer -> System.out.println(integer))
                //此时用这个主要是 测试环境 有执行时间 所以用阻塞比较好
                .blockingSubscribe();


        System.out.println("\nskipWhile:");
        //       丢弃Observable发射的数据，直到一个指定的条件不成立
        Observable.just(1,2,3,4)
                //从2开始 因为2条件不成立
                .skipWhile(aLong -> aLong==1)
                .doOnNext(integer -> System.out.println(integer))
                //此时用这个主要是 测试环境 有执行时间 所以用阻塞比较好
                .blockingSubscribe();
    }

    @Test
    public void sequenceEqual() {
        System.out.println("\nsequenceEqual");
//        用于判断两个Observable发射的数据是否相同（数据，发射顺序，终止状态）
        Observable.sequenceEqual(
                Observable.just(2, 3, 4)
                , Observable.just(2, 3, 4))
                .subscribe((aBoolean, throwable) -> System.out.println(aBoolean));

//        它还有一个版本接受第三个参数，可以传递一个函数用于比较两个数据项是否相同。
        System.out.println("\nsequenceEqual2");
        Observable.sequenceEqual(
                Observable.just(2, 3, 4)
                , Observable.just(2, 3, 4)
                , (integer, integer2) -> integer + 1 == integer2)
                .subscribe((aBoolean, throwable) -> System.out.println(aBoolean));
    }

    @Test
    public void contains() {
//        判定一个Observable是否发射一个特定的值
        Observable.just(2, 3, 4)
                .contains(2)
                .subscribe((aBoolean, throwable) -> System.out.println(aBoolean));

    }

    @Test
    public void all() {
        //判定是否Observable发射的所有数据都满足某个条件
        Observable.just(2, 3, 4)
                .all(integer -> integer > 3)
                .subscribe((aBoolean, throwable) -> System.out.println(aBoolean));
    }


    @Test
    public void switch2default_IfEmpty() {
        Observable.empty()
//        switchIfEmpty： 如果原始Observable正常终止后仍然没有发射任何数据，就使用备用的Observable
                .switchIfEmpty(Observable.just(2, 3, 4))
                .subscribe(o -> System.out.println("===>" + o + "\t")); //2,3,4

        Observable.empty()
//        如果原始Observable正常终止后仍然没有发射任何数据，就发射一个默认值,内部调用的switchIfEmpty。
                .defaultIfEmpty(1)
                .subscribe(o -> System.out.println("===>" + o + "\t")); //1
    }


    @Test
    public void amb() {
//        给定多个Observable，只让第一个发射数据的Observable发射全部数据

//        用法1  静态多个
        Observable.ambArray(
                Observable.intervalRange(0, 3, 200, 100, TimeUnit.MILLISECONDS)
                , Observable.intervalRange(10, 3, 300, 100, TimeUnit.MILLISECONDS)
                , Observable.intervalRange(20, 3, 100, 100, TimeUnit.MILLISECONDS)
        )
                .doOnComplete(() -> System.out.println("Complete"))
                .subscribe(aLong -> System.out.println(aLong));

        //   ambWith   非静态一个
        Observable.intervalRange(0, 3, 200, 100, TimeUnit.MILLISECONDS)
                .ambWith(Observable.intervalRange(10, 3, 300, 100, TimeUnit.MILLISECONDS))
                .doOnComplete(() -> System.out.println("Complete"))
                .subscribe(aLong -> System.out.println(aLong));


        while (true) {
        }
    }
}
