package zone.com.java8study.rx.over.helper;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 */

public class HelperTest {

    @Test
    public void Timestamp() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
               .take(3)
                //它将一个发射T类型数据的Observable转换为一个发射类型 为
                // Timestamped<T> 的数据的Observable，每一项都包含数据的原始发射时间
                .timestamp()
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===> throwable")
                        , () -> System.out.println("===> complete")
                        , disposable -> System.out.println("===> 订阅"));
        while (true) {
        }
    }

    @Test
    public void timeout() {

        System.out.println("\ntimeout:");
        Observable.interval(100, TimeUnit.MILLISECONDS)
//        过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，它会发一个错误
                .timeout(50, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>timeout throwable")
                        , () -> System.out.println("===>timeout complete")
                        , disposable -> System.out.println("===>timeout 订阅"));


        System.out.println("\ntimeout2:");
        Observable<Integer> other;
        Observable.empty()
                // 过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，他会用备用Observable 发送数据，本身的会发送一个compelte
                .timeout(50, TimeUnit.MILLISECONDS, other = Observable.just(2, 3, 4))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>timeout2 throwable")
                        , () -> System.out.println("===>timeout2 complete")
                        , disposable -> System.out.println("===>timeout2 订阅"));
        other.subscribe(o -> System.out.println("k ===>" + o + "\t"));
        while (true) {
        }
    }

    @Test
    public void timeInterval() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
//         把发送的数据 转化为  相邻发送数据的时间间隔实体
                .timeInterval()
//                .timeInterval(Schedulers.newThread())
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete")
                        , disposable -> System.out.println("===>订阅"));
        while (true) {
        }
    }

    @Test
    public void subscribe() {
        Observable.range(0, 3)
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete")
                        , disposable -> System.out.println("===>订阅"))
                .isDisposed();//解除订阅

        Observable.range(0, 3)
                //subscribe的简化版本  没啥用
                .forEach(o -> System.out.println("===>" + o + "\t"));

    }

    @Test
    public void ObserveOn2subscribeOn() {
        Observable.range(0, 3)
//               指定Observable自身在哪个调度器上执行
                .subscribeOn(Schedulers.newThread())
                //  指定一个观察者在哪个调度器上观察这个Observable
                //todo 注意 遇到错误 会立即处理而不是等待下游还没观察的数据
                //todo 既onError 通知会跳到(并吞掉)原始Observable发射的数据项前面
                .observeOn(Schedulers.newThread())
                .subscribe(o -> System.out.print("===>" + o + "\t"));
    }

    @Test
    public void serialize() {
        //保证上游下游同一线程 ，防止不同线程下 onError 通知会跳到(并吞掉)原始Observable发射的数据项前面的错误行为
        Observable.range(0, 3)
                .serialize()
                .subscribe(o -> System.out.print("===>" + o + "\t"));
    }

    @Test
    public void materialize2dematerialize() {

        Observable.range(0, 3)
                //将Observable转换成一个通知列表。
                .materialize()
                //与上面的作用相反，将通知逆转回一个Observable
                .dematerialize()
                .subscribe(o -> System.out.print("===>" + o + "\t"));
    }

    @Test
    public void do2() {
        System.out.println("\ndoOnEach:");
//       注册一个回调，它产生的Observable每发射一项数据就会调用它一 次
        Observable.range(0, 3)
                .doOnEach(integerNotification -> System.out.print("doOnEach:"+integerNotification.getValue()))
                .subscribe(o -> System.out.print("===>" + o + "\t\n "));


        System.out.println("\ndoOnNext:");
//        类似doOnEach 不是接受一个 Notification 参 数，而是接受发射的数据项。
        Observable.range(0, 3)
                .doOnNext(integer -> {
                    if (integer == 2)
                        throw new Error("O__O");
                    System.out.print(integer);
                })
                .subscribe(o -> System.out.print("===>" + o + "\t")
                        , throwable -> System.out.print("===>throwable")
                        , () -> System.out.print("===>complete"));

        System.out.println("\ndoOnSubscribe:");
//注册一个动作，在观察者订阅时使用
        Observable.range(0, 3)
                .doOnSubscribe(disposable -> System.out.print("开始订阅"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));

//注册一个动作，在观察者OnComplete时使用
        System.out.println("\ndoOnComplete:");
        Observable.range(0, 3)
                .doOnComplete(() -> System.out.print("doOnComplete"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));

//注册一个动作，在观察者doOnError时使用
        System.out.println("\ndoOnError:");
        Observable.error(new Throwable("?"))
                .doOnError(throwable -> System.out.print("throwable"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));

//注册一个动作，Observable终止之前会被调用，无论是正 常还是异常终止。
        System.out.println("\ndoOnTerminate:");
        Observable.range(0, 3)
                .doOnTerminate(() -> System.out.print("\t doOnTerminate"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));

//注册一个动作，当它产生的Observable终止之后会被调用，无论是正常还 是异常终止。在doOnTerminate之后执行
        System.out.println("\ndoFinally:");
        Observable.range(0, 3)
                .doFinally(() -> System.out.print("\t doFinally"))
                .doOnTerminate(() -> System.out.print("\t  doOnTerminate"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));


//当观察者取消订阅它生成的Observable它就会被调
        System.out.println("\ndoOnDispose:");
        Disposable ab = Observable.interval(1, TimeUnit.SECONDS)
                .take(3)
                .doOnDispose(() -> System.out.println("解除订阅"))
                .subscribe(o -> System.out.print("===>" + o + "\t"));
        ab.dispose();

        while (true) {
        }
    }


    @Test
    public void delay() {
//        延迟一段指定的时间在发射
//todo  delay 不会平移 onError 通知，它会立即将这个通知传递给订阅者，同时丢弃任何待 发射的 onNext 通知。
//todo 然而它会平移一个 onCompleted 通知
//        Observable.range(0, 3)
//                .delay(1400, TimeUnit.MILLISECONDS)
//                .subscribe(o -> System.out.println("===>" + o + "\t"));


//        delaySubscription 让你你可以延迟订阅原始Observable。
        // FIXME: 2017/7/24 delaySubscription demo不成立  等待中
        Observable.just(1)
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete")
                        , disposable -> System.out.println("===>订阅"));
        while (true) {
        }
    }

}
