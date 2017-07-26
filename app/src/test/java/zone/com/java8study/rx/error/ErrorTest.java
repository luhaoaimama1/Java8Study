package zone.com.java8study.rx.error;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import javax.xml.transform.Source;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 * <p>
 * 1. 吞掉这个错误，切换到一个备用的Observable继续发射数据
 * 2. 吞掉这个错误然后发射默认值
 * 3. 吞掉这个错误并立即尝试重启这个Observable
 * 4. 吞掉这个错误，在一些回退间隔后重启这个Observable
 */

public class ErrorTest {
    // retryWhen 需要一个Observable 通过判断 throwableObservable
    // Observable发射一个数据 就重新订阅，
    // 发射的是 onError 通知，它就将这个通知传递给观察者然后终止。
    @Test
    public void retryWhen() {

        System.out.println("\n retryWhen 1:");
        Observable.just(1, "2", 3)
                .cast(Integer.class)
                .retryWhen(throwableObservable -> Observable.timer(400, TimeUnit.MILLISECONDS))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));
        //1,1

        System.out.println("\n retryWhen 2:");
        Observable.just(1, "2", 3)
                .cast(Integer.class)
                .retryWhen(throwableObservable -> Observable.error(new RuntimeException()))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));

        while (true) {
        }
    }

    @Test
    public void retry() {
//        如果原始Observable遇到错误，重新订阅它期望它能正常终止
//        retry 总是传递 onNext 通知给观 察者，由于重新订阅，可能会造成数据项重复
//        无论收到多少次 onError 通知，无参数版本的 retry
        System.out.println("\n retry count:");
        Observable.create(e -> {
            e.onNext(1);
            e.onNext(2);
            e.onError(new Throwable("hehe"));
        })
                .retry(3)
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));

        System.out.println("\n retry by throwable Predicate:");
        Observable.create(e -> {
            e.onNext(1);
            e.onNext(2);
            e.onError(new Throwable("hehe"));
        })
//这个函数返回一个布尔值，如果返回 true ， retry 应该 再次订阅和镜像原始的Observable，
// 如果返回 false ， retry 会将最新的一个 onError 通知 传递给它的观察者。
                .retry(throwable -> throwable.getMessage().equals("hehe1"))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));
    }

    /**
     * 让Observable在遇到错误时开始发射第二个Observable的数据序列。
     * 区别
     * onErrorResumeNext可以处理所有的错误
     * <p>
     * //这个有点垃圾 建议用onErrorResumeNext
     * onExceptionResumeNext只能处理异常。 Throwable 不是一个 Exception ，
     * 它会将错误传递给观察者的 onError 方法，不会使用备用 的Observable。
     */
    @Test
    public void resumeNext() {

        System.out.println("\n onErrorResumeNext:");
        Observable.error(new Throwable("我擦 空啊"))
                .onErrorResumeNext(throwable -> {
                    System.out.println("错误信息：" + throwable.getMessage());
                    return Observable.range(0, 3);
                })
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));

        System.out.println("\n onExceptionResumeNext:");
        Observable.error(new Throwable("我擦 空啊"))
                .onExceptionResumeNext(observer -> Observable.range(0, 3))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));
//
        System.out.println("\n onExceptionResumeNext2:");
        Observable.error(new Exception("我擦 空啊"))
                .onExceptionResumeNext(observer -> Observable.range(0, 3))
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));
    }

    //让Observable遇到错误时发射一个特殊的项并且正常终止
    @Test
    public void onErrorReturn() {

        Observable.error(new Throwable("我擦 空啊"))
                .onErrorReturnItem("hei")
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));


        Observable.error(new Throwable("我擦 空啊"))
                .onErrorReturn(throwable -> {
                    System.out.println("错误信息：" + throwable.getMessage());
                    return throwable;
                })
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));

    }
}
