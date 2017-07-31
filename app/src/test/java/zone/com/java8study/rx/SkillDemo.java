package zone.com.java8study.rx;

import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * [2017] by Zone
 */

public class SkillDemo {


    @Test
    public void netBehaviorSubject() {

        //为什么用BehaviorSubject   因为BehaviorSubject会发送离订阅最近的上一个值，没有上一个值的时候会发送默认值
        //这样 网络请求的时候我就可以延迟订阅 ,这里考虑两种情况
        //情况1 如果网络请求得到后在订阅 一样可以得到值。
        //情况2 如果订阅后在得到网络请求的值当然也没问题了
        BehaviorSubject<Object> cache = BehaviorSubject.create();
        Observable.timer(1,TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(cache);

        //可以想象成上面是方法  这里是方法被调用
        cache.subscribe(o -> System.out.println(o));

//        todo 反例 这里是得不到值的  为啥 因为他发送值之后 bs才订阅的！
        //Subjects 是可以被 subscribe订阅
        PublishSubject bs = PublishSubject.create();
        Observable.just(1,2,3)
                .subscribe(bs);
        bs.subscribe(o -> System.out.println("反例:"+o));

        while (true){}
    }

    @Test
    public void methodGenericTypes() {
        Observable.just(1, "2", 3)
                .cast(Integer.class)
                .retryWhen(throwableObservable -> {
                    return throwableObservable.switchMap(throwable -> {
                        if (throwable instanceof IllegalArgumentException)
                            return Observable.just(throwable);
                        //todo  方法泛型 如果我不写<Object> 则会报错
                        return Observable.<Object>error(throwable);
                        //这个报错！！！
//                        return Observable.error(throwable);
                    });
                })
                .subscribe(o -> System.out.println("===>" + o + "\t")
                        , throwable -> System.out.println("===>throwable")
                        , () -> System.out.println("===>complete"));

    }

    @Test
    public void emitterEasy() {
        //创建一个Observable 可以直接发送的 原因 获取rx内部方法需要final很恶心 所以...
        RxEmitter<Integer> emitter = new RxEmitter();
        Observable.create(emitter)
                .subscribe(integer -> System.out.println(integer));
        emitter.onNext(1);
        emitter.onNext(2);
    }

    @Test
    public void mapDepth() {
        //有些服务几口设计，返回数据外层会包裹一些额外信息,可以使用map()吧外层格式剥掉
        Observable.just(1)
                .map(integer -> new Integer[]{1, 2, 3})
                .subscribe(integers -> System.out.println(integers));
    }

    @Test
    public void fileTranslate() {
        Observable.fromArray(new File("/Users/fuzhipeng/Documents"))
                .flatMap(file -> Observable.fromArray(file.listFiles()))
                .filter(file -> file.getName().endsWith(".png"))
                .take(5)
                .map(file -> file.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(s -> System.out.println(s));
        while (true) {
        }
    }

    @Test
    public void flatZip() {
        Observable.fromArray(new File("/Users/fuzhipeng/Documents"))
                .flatMap(file -> Observable.fromArray(file.listFiles()))
                //比较经典的 就是Observable.just(file) 吧 file一个元素转成 observer从而进行zip合并的难题解决了
                .flatMap(file ->
                        Observable.zip(Observable.just(file)
                                , Observable.timer(1, TimeUnit.SECONDS)
                                , (file1, aLong) -> file1))
                .filter(file -> file.getName().endsWith(".png"))
                .take(5)
                .map(file -> file.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(s -> System.out.println(s));
        while (true) {
        }
    }
}
