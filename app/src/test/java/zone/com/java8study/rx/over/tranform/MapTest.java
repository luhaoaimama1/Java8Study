package zone.com.java8study.rx.over.tranform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 * <p>
 * map:原来的流转换成新流:方形过渡到圆形
 * FlatMap:将一个发送事件的上游Observable变换为多个发送事件的Observables，
 * 然后将它们发射的事件合并后放进一个单独的Observable里.
 * <p>
 * FlatMap 将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并 后放进一个单独的Observable
 * <p>
 * todo flatMap并不保证事件的顺序 原因 对这些Observables发射的数据做的是合并( merge )操作，因此它们可能是交 错的。
 * todo 如果需要保证顺序则需要使用concatMap.
 * <p>
 * RxJavaActivity 研究了顺序的问题
 */

public class MapTest {


    @Test
    public void map() {
        Observable.just(1, 2)
                .map(integer -> "This is result " + integer)
                .subscribe(s -> System.out.println(s));
    }

    @Test
    public void flatMap() {
//        注意:FlatMap 对这些Observables发射的数据做的是合并( merge )操作，因此它们可能是交 错的。


        Observable.just(1, 2, 3)
                .flatMap(integer -> Observable.range(integer * 10, 2)
                        , (a, b) -> {
                            //a ： 原始数据的 just(1,2,3) 中的值
                            //b ： 代表 flatMap后合并发送的数据的值
                            System.out.print("\n a:" + a + "\t b:" + b);
                            //return flatMap发送的值 ，经过处理后 而发送的值
                            return a + b;
                        })
                .subscribe(s -> System.out.print("\t" + s));

//combiner 用来合并 的
// maxConcurrency ：这个参数设置 flatMap 从原来的 Observable映射Observables的最大同时订阅数
// 当达到这个限制时，它会等待其中一个终止 然后再订阅另一个。  测不出来

// delayErrors 不懂  干啥的？测不出来

        Observable.just(1, 2)
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer v) throws Exception {
                        return Observable.range(0, v * 2);
                    }
                }, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer a, Integer b) throws Exception {
                        //a ： 原始数据的 just(1,2,3) 中的值
                        //b ： 代表 flatMap后合并发送的数据的值
                        System.out.println("a:" + a + "\t b:" + b);
                        //return flatMap发送的值 ，经过处理后 而发送的值
                        return a + b;
                    }
                }, false, 3)
                .subscribe(integer -> System.out.println(integer));

        while (true) {
        }
    }


    @Test
    //groupBy 通过apply的值当做key 进行分组
    //groupBy后生成的 GroupedObservable .subscibe 订阅每个group  每个group.subscibe在订阅 则是最后的结果
    //todo 注意一旦订阅会缓存每一组 可以使用take(0)丢弃不必要的缓存 fiter之类的  防止内存泄漏

    //todo 如果你取消订阅一个 GroupedObservable ，那个Observable将会终止。如果之后原始的
    // todo Observable又发射了一个与这个Observable的Key匹配的数据， groupBy 将会为这个Key创建 一个新的 GroupedObservable 。
    public void groupBy() {
        Observable.range(0, 10)
                .groupBy(integer -> integer % 2, integer -> "(" + integer + ")")
                .subscribe(group -> {
                    group.subscribe(integer -> System.out.println(
                            "key:" + group.getKey() + "==>value:" + integer));
                });


        Observable<String> source = Observable.fromIterable(Arrays.asList(
                "  foo",
                " FoO ",
                "baR  ",
                "foO ",
                " Baz   ",
                "  qux ",
                "   bar",
                " BAR  ",
                "FOO ",
                "baz  ",
                " bAZ ",
                "    fOo    "
        ));


        //变种 valueSelector  就是把对应存储的value做转换
        /*
         * foo FoO foO FOO fOo
         * baR bar BAR
         * Baz baz bAZ
         * qux
         *
         */
        Function<String, String> keysel = new Function<String, String>() {
            @Override
            public String apply(String t1) {
                return t1.trim().toLowerCase();
            }
        };
        Function<String, String> valuesel = new Function<String, String>() {
            @Override
            public String apply(String t1) {
                return t1 + "__" + t1;
            }
        };

        source.groupBy(keysel, valuesel).subscribe(group -> {
            group.subscribe(integer -> System.out.println("key:" + group.getKey() + "==>" + integer));
        });
        while (true) {
        }
    }

    @Test
//    在发射之前强制将Observable发射的所有数据转换为指定类型
    public void cast() {
        Observable.just(1, 2, "string")
                .cast(Integer.class)//订阅之后才能发横强转
                .subscribe(integer -> System.out.println(integer)
                        , throwable -> System.out.println(throwable.getMessage()));

    }

    @Test
//     与reduce很像，对Observable发射的每一项数据应用一个函数，然后按顺序依次发射每一个值。
//     这个操作符在某些情况下被叫做 accumulator 累加器。
// TODO 图非常好  记得看
    public void scan() {
//        Observable.just(1, 2, 5, 4)
//                .scan((a, b) -> {
//                    //b 是just元数据的值
//                    //a 则是最后应用scan 发送的值
//                    System.out.format("a:%d\tb:%d\n", a, b);
//                    return a * b;
//                })
//                .subscribe(integer -> System.out.println(integer));

        Observable.just(1, 4, 2)
                //7是用来 对于第一次的 a的值
                .scan(7, (a, b) -> {
                    //b 是just元数据的值
                    //a 则是最后应用scan 发送的值
                    System.out.format("a:%d * b:%d\n", a, b);
                    return a * b;
                })
                .subscribe(integer -> System.out.println("===>：" + integer));
    }

    @Test
    /*
     * Example using window operator -> It periodically
     * subdivide items from an Observable into
     * Observable windows and emit these windows rather than
     * emitting the items one at a time
     *
     * 依照此范例
     * 每三秒收集,Observable在此时间内发送的值。组装成 Observable。最后发送出去。
     */
    public void window() {
        Observable.interval(1, TimeUnit.SECONDS).take(7)
                //返回值  Observable<Observable<T>> 即代表 发送Observable<T>
                .window(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(integerObservable -> {
                    System.out.println(integerObservable);
                    integerObservable.subscribe(integer -> System.out.println(integerObservable+"===>"+integer));
                });
        while (true) {
        }
    }

    @Test
    public void concatMap() {

        Observable.just(1, 2, 3)
                .concatMap(integer -> Observable.range(integer * 10, 2))
                .subscribe(s -> System.out.print("\t" + s));

//        concatMap:I am value 1
//        concatMap:I am value 1
//        concatMap:I am value 1
//        concatMap:I am value 2
//        concatMap:I am value 2
//        concatMap:I am value 2
//        concatMap:I am value 3
//        concatMap:I am value 3
//        concatMap:I am value 3
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("concatMap:I am value " + integer);
                }
                return Observable.fromIterable(list).delay(5, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
        delayResult(5000);
    }

    private void delayResult(long millis) {
        //todo 必须进行延时 不然执行完毕 就不管以后的内容了 执行之后的结果了
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

