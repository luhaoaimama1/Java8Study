package zone.com.java8study.rx.over.tranform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 * 发射[1-10]
 * buffer count 2 skip 1,结果 [1,2]  [2,3] [3,4] 3=2*1+1
 * buffer count 2 skip 2,结果 [1,2]  [3,4] [5,6] 5=2*2+1
 * buffer count 2 skip 3,结果 [1,2]  [4,5] [7,8] 7=2*3+1;
 * count 代表缓存的数量
 * skip则代表 每次初始偏移量  每次真正的起始值=fistValue+skip*skipCount;
 * <p>
 * bufferSupplier 为缓存装载的容器；
 * <p>
 * onNext 里的泛型是List
 * <p>
 * <p>
 * <p>
 * For exmaple2:
 * Observable.just("one", "two", "three", "four", "five")
 * .buffer(3, 1)
 * .subscribe(integers -> System.out.println(integers));
 * <p>
 * <p>
 * 3 means,  it takes max of three from its start index and create list
 * 1 means, it jumps one step every time
 * so the it gives the following list
 * 1 - one, two, three
 * 2 - two, three, four
 * 3 - three, four, five
 * 4 - four, five
 * 5 - five
 */
public class BufferTest {

//    Observable<U> buffer(Callable<? extends ObservableSource<B>> boundarySupplier, Callable<U> bufferSupplier


    @Test
//        每当 bufferOpenings 发射了一个数据时，它就 创建一个新的 List
//        开始收集原始Observable的数据，并将 bufferOpenings 传递
//        给 closingSelector 函数。这个函数返回一个Observable。
//        buffer 监视这个Observable，当 它检测到一个来自这个Observable的数据时，
//        就关闭 List 并且发射它自己的数据(之前的那 个List)。
//        然后重复这个过程:开始组装一个新 的 List
//   todo 这个缓存可以是 不连续的！  因为 这个有两个事件 个开始事件 和结束事件 。结束事件后 不发生开始事件则导致不连续！
    public void openings2closer() {

        System.out.println("openings:");
        Consumer<Long> longConsumer = aLong -> System.out.println("开始创建 bufferSupplier");
        Consumer<Long> longConsumer2 = aLong -> System.out.println("结束收集");
        Observable.interval(500, TimeUnit.MILLISECONDS).take(7)
//                .doOnNext(aLong -> System.out.println("原始发射物：" + aLong))
                .buffer(Observable.interval(2, TimeUnit.SECONDS)
                                .startWith(-1L)//为了刚开始就发射一次
                                .take(2)//多余的我就不创建了
                                .doOnNext(longConsumer)
                        , aLong -> Observable.timer(3, TimeUnit.SECONDS)
                                .doOnNext(longConsumer2)
                        , () -> new ArrayList<>())
                .subscribe(integers -> System.out.println("buffer发射物" + integers));


        while (true) {

        }
    }


    @Test
    //  boundarySupplier  每当这个Observable发射了一个 值，
    // 它就创建一个新的 List 开始收集来自原始Observable的数据并发射原来的 List
    //todo 这个缓存是连续的, 因为发送一个值代表上个缓存的发送 和这个缓存的创建
    //FIXME: 注意 如果不发送事件缓存 存满了 会自动发送出去的
    public void boundarySupplier() {

        Observable.interval(500, TimeUnit.MILLISECONDS).take(7)
                .buffer(() -> Observable.timer(2, TimeUnit.SECONDS)
                                .doOnNext(aLong -> System.out.println("开始创建 bufferSupplier"))
                        , () -> new ArrayList<Object>())
                .subscribe(integers -> System.out.println(integers));
        while (true) {
        }

    }

    @Test
    public void bufferQuick2() {
//
        /**
         *
         * 内部最终实现只要懂了这个参数就可以了
         * ObservableBufferTimed<T, U> (this, timespan, timeskip, unit,
         *  scheduler, bufferSupplier, Integer.MAX_VALUE, false)
         *
         * timeskip 配合unit为  收集器 创建后的跳过的时间
         * 然后收集的时间为 timespan配合unit 收集来自原始 Observable的数据
         * count 收集个数
         * scheduler 执行的线程
         * bufferSupplier 为缓存装载的容器；
         * restartTimerOnMaxSize 当前缓存到达的时候是否重启 具体不懂！
         */
//        当前变体

        Observable.range(1, 100)
                .buffer(1, TimeUnit.MILLISECONDS, Schedulers.single())//count Integer.MaxValue
                .subscribe(integers -> System.out.println(integers));

        Observable.range(1, 100)
                .buffer(1, TimeUnit.MILLISECONDS, Schedulers.single(), 10)
                .subscribe(integers -> System.out.println(integers));

        Observable.range(1, 100)
                .buffer(1, TimeUnit.MILLISECONDS, Schedulers.single(),
                        10, Functions.<Integer>createArrayList(16), true)
                .subscribe(integers -> System.out.println(integers));


//        当前变体2

        Observable.range(1, 100)
                .buffer(1, 2, TimeUnit.MILLISECONDS)
                .subscribe(integers -> System.out.println(integers));

        Observable.range(1, 100)
                .buffer(1, 2, TimeUnit.MILLISECONDS, Schedulers.single())
                .subscribe(integers -> System.out.println(integers));
//
        Observable.interval(500, TimeUnit.MILLISECONDS).take(7)
                .buffer(3, 2, TimeUnit.SECONDS, Schedulers.single(),
                        Functions.createArrayList(16))
                .subscribe(integers -> System.out.println(integers));

        while (true) {
        }

    }

    @Test
    public void bufferQuick() {

        Observable.range(1, 10)
                .buffer(2)//skip 默认和count一样
                .subscribe(integers -> System.out.println(integers));

        Observable.range(1, 10)
                .buffer(2, () -> new ArrayList<>())
                .subscribe(integers -> System.out.println(integers));

        Observable.range(1, 10)
                .buffer(2, 1, () -> new ArrayList<>())//有默认的装载器
                .subscribe(integers -> System.out.println(integers));
    }

    public void bufferSkipSupplier() {
        System.out.println("skip 1");
        Observable.range(1, 10)
                .buffer(2, 1, new Callable<List<Integer>>() {
                    int calls;

                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<Integer>();
                    }
                }).subscribe(integers -> System.out.println(integers)
                , throwable -> System.out.println(throwable.getMessage()));
        System.out.println("skip 2");
        Observable.range(1, 10)
                .buffer(2, 2, new Callable<List<Integer>>() {
                    int calls;

                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<Integer>();
                    }
                }).subscribe(integers -> System.out.println(integers)
                , throwable -> System.out.println(throwable.getMessage()));

        System.out.println("skip 3");
        Observable.range(1, 10)
                .buffer(2, 3, new Callable<List<Integer>>() {
                    int calls;

                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<Integer>();
                    }
                }).subscribe(integers -> System.out.println(integers)
                , throwable -> System.out.println(throwable.getMessage()));
    }
}
