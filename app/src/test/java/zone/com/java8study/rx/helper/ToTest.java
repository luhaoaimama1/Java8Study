package zone.com.java8study.rx.helper;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;

/**
 * [2017] by Zone
 */

public class ToTest {
    @Test
    public void toList2SortList2Map2Flowable2Future() {
        System.out.println("/n toList");
        //阻塞订阅版本
        Observable.just(1, 2, 3)
                .toList().blockingGet()
                .forEach(aLong -> System.out.println(aLong));


        System.out.println("/n toSortedList");
        Observable.just(5, 2, 3)
                .toSortedList()
                .blockingGet()
                .forEach(integer -> System.out.println(integer));


        System.out.println("/n toFuture");
//        这个操作符将Observable转换为一个返 回单个数据项的 Future   带有返回值的任务
//          如果原始Observable发射多个数据项， Future 会收到1个 IllegalArgumentException
//        如果原始Observable没有发射任何数据， Future 会收到一 个 NoSuchElementException
//
//      如果你想将发射多个数据项的Observable转换为 Future ，
//       可以这样 用: myObservable.toList().toFuture() 。

        try {
            Observable.just(1, 2, 3)
                    .toList()//转换成Single<List<T>> 这样就变成一个数据了
                    .toFuture()
                    .get()
                    .forEach(integer -> System.out.println(integer));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("/n toMap");
        Map<String, Integer> map = Observable.just(5, 2, 3)
//                .toMap(integer -> integer + "_")
                //key 就是5_,value就是5+10   mapSupplier map提供者
                .toMap(integer -> integer + "_"
                        , integer -> integer + 10
                        , () -> new HashMap<>())
                .blockingGet();
        System.out.println(map.toString());



    }

    @Test
    public void blockingSubscribe() {
        //阻塞订阅版本
        Observable.just(1, 2, 3)
                .blockingSubscribe(integer -> System.out.println(integer));
    }

    @Test
    public void blockingForEach() {
//        对BlockingObservable发射的每一项数据调用一个方法，会阻塞直到Observable完成。
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    if (aLong == 10)
                        throw new RuntimeException();
                }).onErrorReturnItem(-1L)
                .blockingForEach(aLong -> System.out.println(aLong));
    }

    @Test
    public void blockingIterable() {
        //阻塞成迭代器
        Observable.just(1, 2, 3)
                .blockingIterable()
//                .blockingIterable(5);
                .forEach(aLong -> System.out.println("aLong:" + aLong));

        //阻塞成迭代器  blockingNext?不是很懂啊
        Observable.just(1, 2, 3)
                .blockingNext()
                .forEach(aLong -> System.out.println("blockingNext:" + aLong));
        while (true) {
        }
    }

    @Test
    public void blockingNext() {

    }

    @Test
    public void blockingFirst2Last() {
        Long first = Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    if (aLong == 10)
                        throw new RuntimeException();
                }).onErrorReturnItem(-1L)
                .blockingFirst();

        System.out.println("first:" + first);

        System.out.println("first defaultItem:" +
                Observable.empty()
                        //默认值版本
                        .blockingFirst(-1));


        Long last = Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    if (aLong == 10)
                        throw new RuntimeException();
                }).onErrorReturnItem(-1L)
                .blockingLast();
        System.out.println("last:" + last);
        System.out.println("last defaultItem:" + Observable.empty()
                //默认值版本
                .blockingLast(-2));
    }


    @Test
    public void blockingMostRecent() {
        // 无用 blockingMostRecent返回一个总是返回Observable最近发射的数据的Iterable 类似于while的感觉
        Iterable<Long> c = Observable.interval(100, TimeUnit.MILLISECONDS)
                .doOnNext(aLong -> {
                    if (aLong == 10)
                        throw new RuntimeException();
                }).onErrorReturnItem(-1L)
                .blockingMostRecent(-3L);
        for (Long aLong : c) {
            System.out.println("aLong:" + aLong);
        }
    }

    @Test
    public void blockingSingle() {
//blockingSingle
//        终止时只发射了一个值，返回那个值
        //   empty   无默认值 报错， 默认值的话显示默认值
        //   多个值的话  有无默认值都报错
        System.out.println("emit 1 value:" + Observable.just(1).blockingSingle());

        System.out.println("default empty single:" + Observable.empty().blockingSingle(-1));
        System.out.println("default emit 1 value:" + Observable.just(1).blockingSingle(-1));


        try {
            System.out.println("empty single:" + Observable.empty().blockingSingle());
            System.out.println("emit many value:" + Observable.just(1, 2).blockingSingle());


            System.out.println("default emit many value:" + Observable.just(1, 2)
                    .blockingSingle(-1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void blockingLatest() {
        List<Long> list = null;
        try {
            list = Observable.interval(100, TimeUnit.MILLISECONDS)
                    .doOnNext(aLong -> {
                        if (aLong == 10)
                            throw new RuntimeException();
                    }).onErrorReturnItem(-1L)
                    .toList().toFuture().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (Long aLong : list) {
            System.out.println("aLong:" + aLong);
        }
    }
}
