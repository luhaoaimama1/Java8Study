package zone.com.java8study.rx.over.combine;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 */

public class CombineTest {


    @Test
//    和flatMap很像，将Observable发射的数据变换为Observables集合，
// 当原始Observable发射一个新的数据（Observable）时，它将取消订阅前一个Observable。
    public void switchMap() {
        //结果是10,20,30,300 没有出现10 100 20 200 30 300 是因为发送的时候接触订阅了；

        Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(3)
                .doOnNext(aLong -> System.out.println())
                .switchMap(aLong -> Observable.intervalRange(aLong * 10, 3,
                        0, 300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread()))
                .subscribe(aLong -> System.out.print(aLong + "\t"));

//        期待结果
//        0	1
//        10	11
//        20	21	22
//        而不是
//        0     1   2
//        10	11  12
//        20	21  22


        while (true) {
        }
    }


    @Test
    public void startWith() {
//   RxJava的startWith()是concat()的对应部分。正如concat()向发射数据的Observable追加数据那样
// ，在Observable开始发射他们的数据之前， startWith()通过传递一个参数来先发射一个数据序列
        Observable.just("old")
                .startWith("Start")
                .startWith("Start2")
                .startWith(Observable.just("Other Observable"))
                .startWith(Arrays.asList("from Iterable"))
                .startWithArray("from Array", "from Array2")
                .subscribe(s -> System.out.println(s));
    }


    /**
     * 根据时间线 合并
     * merge的为静态方法  合并多个observer
     * mergeWith则是public方法
     */
    @Test
    public void merge() {
        Observable<Long> ob1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread());

        Observable<Long> ob2 = Observable.interval(50, TimeUnit.MILLISECONDS)
                .take(3)
                .map(aLong -> aLong + 10)
                .subscribeOn(Schedulers.newThread());
        Observable.merge(ob1, ob2)
                .subscribe(o -> System.out.print(o + "\t"));

        System.out.println();
        ob1.mergeWith(ob2)
                .subscribe(o -> System.out.print(o + "\t"));
        while (true) {
        }
    }

    /**
     * 学习了Zip的基本用法, 那么它在Android有什么用呢, 其实很多场景都可以用到Zip. 举个例子.
     * 比如一个界面需要展示用户的一些信息, 而这些信息分别要从两个服务器接口中获取,
     * <p>
     * 只有当原始的Observable中的每一个都发射了 一条数据时 zip 才发射数据。
     * <p>
     * 静态方法 zip最多可接受九个obserables
     * zipWith 则总是接受两个 非静态方法
     */
    @Test
    public void zip() {
//        因为在同一线程导致，有先后顺序 既然，水管既Observable执行有先后顺序
        System.out.println("\n zip:");
        Observable<Long> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread());
        Observable<Long> observable2 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());
        Observable.zip(observable1, observable2, (aLong, aLong2) -> {
            System.out.print("aLong:" + aLong + "\t aLong2:" + aLong2);
            return aLong + aLong2;
        }).subscribe(o -> System.out.println("===>" + o + "\t"));


//        observable1.zipWith( observable2, (aLong, aLong2) -> {
//            System.out.print("aLong:" + aLong + "\t aLong2:" + aLong2+"\t");
//            return aLong + aLong2;
//        }).subscribe(o -> System.out.println("===>" + o + "\t"));
        while (true) {
        }
    }

    /**
     * 类似zip但不同
     * 当原始Observables的任何一个发射了一条数据时，
     * <p>
     * CombineLatest 使用一 个函数结合它们最近发射的数据，然后发射这个函数的返回值。
     * 它接受二到九个Observable作为参数，或者单 个Observables列表作为参数
     */
    @Test
    public void combineLatest() {
        System.out.println("\n combineLatest:");
        Observable<Long> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());
        Observable<Long> observable2 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(5)
                .subscribeOn(Schedulers.newThread());
        Observable.combineLatest(observable1, observable2, (aLong, aLong2) -> {
            System.out.print("aLong:" + aLong + "\t aLong2:" + aLong2 + "\t");
            return aLong + aLong2;
        }).subscribe(o -> System.out.println("===>" + o + "\t"));
        while (true) {
        }

    }


    /**
     * 类似zip  但是只 在单个原始Observable发射了一条数据时才发射数据
     * todo 但是注意 如果没有合并元素 既辅助Observable一次都没发射的时候 是不发射数据的
     */
    @Test
    public void withLatestFrom() {
        System.out.println("\n zip:");
        Observable<Long> observable2 = Observable.interval(150, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread())
                .withLatestFrom(observable2, (aLong, aLong2) -> {
                    System.out.print("aLong:" + aLong + "\t aLong2:" + aLong2 + "\t");
                    return aLong + aLong2;
                })
                .subscribe(o -> System.out.println("===>" + o + "\t"));
        while (true) {
        }

    }

    <T> Function<Integer, Observable<T>> just(final Observable<T> observable) {
        return new Function<Integer, Observable<T>>() {
            @Override
            public Observable<T> apply(Integer t1) {
                return observable;
            }
        };
    }

    /**
     * 通过重接的时间线 ，右侧与左侧的所有结果进行合并；
     * leftEnd 左侧窗口时间线
     * rightEnd 右侧窗口时间线
     * // FIXME: 2017/7/24  demo无效  具体不懂  groupJoin也就不管了
     */
    @Test
    public void join() {
        Observable.intervalRange(10, 4, 0, 300, TimeUnit.MILLISECONDS)
                .join(Observable.interval(100, TimeUnit.MILLISECONDS)
                                .take(7)
                        , aLong -> {
                            System.out.println("开始收集："+aLong);
                            return Observable.just(aLong);
                        }
                        , aLong -> Observable.timer(200, TimeUnit.MILLISECONDS)
                        , (aLong, aLong2) -> {
                            System.out.print("aLong:" + aLong + "\t aLong2:" + aLong2 + "\t");
                            return aLong + aLong2;
                        }
                )
                .subscribe(aLong -> System.out.println(aLong));

        while (true) {
        }
    }
}
