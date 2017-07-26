package zone.com.java8study.rx;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 * subscribeOn() 指定的是上游发送事件的线程   既Observable
 * observeOn() 指定的是下游接收事件的线程   既Observe
 * <p>
 * <p>
 * AndroidSchedulers.mainThread() 则是RxAndroid中独有的
 * 调用一次observeOn() 线程便会切换一次。subscribeOn同理！
 * <p>
 * <p>
 * <p>
 * <p>
 * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
 * 读写数据库,文件，
 * <p>
 * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
 * <p>
 * Schedulers.newThread() 代表一个常规的新线程
 * AndroidSchedulers.mainThread() 代表Android的主线程
 *
 * 变换会受到上面的   observeOn 的线程影响
 * 接受者   subscribeOn 的线程影响
 */
public class ThreadTest {
    @Test
    public void demoFinal() {
//     变换会受到上面的   observeOn 的线程影响
//     接受者   subscribeOn 的线程影响
        Observable.range(0, 1)
                .subscribeOn(Schedulers.newThread())
                .map(integer -> {
                    System.out.println("1:" + Thread.currentThread().getName());
                    return integer;
                })
                .observeOn(Schedulers.newThread())
                .map(integer -> {
                    System.out.println("2:" + Thread.currentThread().getName());
                    return integer;
                })
                .subscribeOn(Schedulers.newThread())
                .map(integer -> {
                    System.out.println("3:" + Thread.currentThread().getName());
                    return integer;
                })
                .observeOn(Schedulers.newThread())
                .map(integer -> {
                    System.out.println("4:" + Thread.currentThread().getName());
                    return integer;
                })
                .subscribe(integer ->
                        System.out.println("subscribe1:" + Thread.currentThread().getName()));

    }
}
