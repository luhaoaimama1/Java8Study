package zone.com.java8study.rx.over.start;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 */
public class StartSymbolTest {


    @Test
    //        一个形式正确的有限Observable必须尝试调用观察者的onCompleted正好一次或者它的
    //   onError正好一次，而且此后不能再调用观察者的任何其它方法

//    建议你在传递给 create 方法的函数中检查观察者的 订阅 状态，以便在没有观察者
//    的时候，让你的Observable停止发射数据或者做昂贵的运算
    public void create() {
        //创建一个上游 Observable：
        Observable.create(e -> {
            e.onNext("Love");
            e.onNext("For");
            e.onNext("You!");
            e.onComplete();
        }).subscribe(s -> System.out.println(s));
    }

    //    Just类似于From，但是From会将数组或Iterable的数据取出然后逐个发射，
// 而Just只是简单的 原样发射，将数组或Iterable当做单个数据。
//    它接受一至九个参数
    @Test
    public void just() {
        Observable.just("Love", "For", "You!")
                .subscribe(s -> System.out.println(s));
    }


    @Test
    public void range() {
        //    一个是范围的起始值，
        // 一个是范 围的数据的数目。0不发送 ，负数 异常
        Observable.range(5, 100)
                .subscribe(s -> System.out.println(s));


        /**
         * 核心的  同intervale 不过start 和count由自己设定！
         */
        Observable.intervalRange(5, 100, 3000, 100,
                TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(s -> System.out.println(s));
        while (true) {
        }
    }


    @Test
//    直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
    public void defer() {
        Observable.defer(() -> Observable.just("Love", "For", "You!"))
                .subscribe(s -> System.out.println(s));
    }


    @Test
    public void timer() {

//   在延迟一段给定的时间后发射一个简单的数字0。
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribe(s -> System.out.println(s));
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(s -> System.out.println(s));
        while (true) {
        }
    }


    @Test
//  这三个操作符生成的Observable行为非常特殊和受限。测试的时候很有用
    //也相当于不需要 发射器的引用 就间接发射了
    public void empty2Never2Throw() {

//      创建一个不发射任何数据但是正常终止的Observable
        Observable.empty();
//        创建一个不发射数据也不终止的Observable
        Observable.never();
//        创建一个不发射数据以一个错误终止的Observable
        Observable.error(new Throwable("hei"));
    }

    @Test
    public void interval() {
        System.out.println("开始");
//        initialDelay  第一次发送0的延迟时间
        //period 以后的美每次间隔 这个值一旦设定后是不可变化的  所以 count方法无效的！
        //Scheduler 默认不在任何特定的调度器上执行。有一个变体可以通过可选参数指定Scheduler。
        int[] s = new int[]{0};
        Observable.interval(3000, 100 + count(s), TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(s2 -> System.out.println(s2));


        /**
         * 核心的  同intervale 不过start 和count由自己设定！
         */
        Observable.intervalRange(5, 100, 3000, 100,
                TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(s2 -> System.out.println(s2));
        while (true) {

        }
    }

    private int count(int[] s) {
        int result = s[0] * 1000;
        s[0] = s[0] + 1;
        return result;
    }

    @Test
    public void from() {
//        todo 注意是 当一个订阅者订阅时
        Integer[] items = {0, 1, 2, 3, 4, 5};
        Observable.fromArray(items).subscribe(
                integer -> System.out.println(integer));

        System.out.println("------fromCallable------");
        /**
         *  todo 注意是 当一个订阅者订阅时，它执行这 个Callable并发射Callable的返回值 只能返回一个值 Callable
         */
        Observable.fromCallable(() -> Arrays.asList("hello", "gaga"))
                .subscribe(strings -> System.out.println(strings));


        System.out.println("------fromIterable------");
        Observable.fromIterable(Arrays.<String>asList("one", "two", "three"))
                .subscribe(integer -> System.out.println(integer));


        System.out.println("------fromFuture------");
//  于Future，它会发射Future.get()方法返回的单个数据。  相当于有返回值的Runnable被执行；
//  from 方法有一个可接受两个可选参 数的版本，分别指定超时时长和时间单位。如果过了指定的时长Future还没有返回一个值，这 个Observable会发射错误通知并终止。
//  from 默认不在任何特定的调度器上执行。然而你可以将Scheduler作为可选的第二个参数传 递给Observable，它会在那个调度器上管理这个Future
        Observable.fromFuture(Observable.never().toFuture(),
                100, TimeUnit.MILLISECONDS, Schedulers.io())
                .test()
                .awaitDone(5, TimeUnit.SECONDS)
                .assertFailure(TimeoutException.class);

        Observable.fromFuture(Observable.just(1).toFuture())
                .doOnComplete(() -> System.out.println("complete"))
                .subscribe();

        while (true) {
        }
    }

}
