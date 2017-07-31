package zone.com.java8study.rx.over;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * [2017] by Zone
 *
 * Observable 可观察的 也可以说 上游；
 * 经过测试
 * isDisposed: 是否和 下游 既订阅者/观察者 接触注册关系；
 * observe中Disposable:  可以主动 dispose 解除订阅关系；
 *
 * ObservableEmitter :中onComplete 或者 onError 走任何一个 都会 主动解除订阅关系；
 * 如果解除订阅关系以后在 发射 onError 则会 报错;而发射onComplete则不会。
 * 注意解除订阅关系 还是可以发射
 *
 * todo  建议你在传递给 create 方法的函数中检查观察者的 isUnsubscribed 状态，
 * todo  以便在没有观察者的时候，让你的Observable停止发射数据或者做昂贵的运算。
 *
 *如果有多个Disposable 该怎么办呢, RxJava中已经内置了一个容器CompositeDisposable,
 * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中,
 * 在退出的时候, 调用CompositeDisposable.clear() 即可切断所有的水管.
 *
 * CompositeDisposable compositeDisposable=new CompositeDisposable();
 *   compositeDisposable.add(d);
 *   compositeDisposable.clear();
 */
public class EmitterTest {

    @Test
    public void complete() {
        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("onComplete");
                emitter.onComplete();
                System.out.println("onComplete");
                emitter.onComplete();
                System.out.println("emit 2");
                emitter.onNext(2);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });
    }



    @Test
    public void complete2error() {
        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emitter.isDisposed: "+emitter.isDisposed());

                System.out.println("onError");
                emitter.onError(new Throwable("hei"));
                System.out.println("onComplete");
                emitter.onComplete();
                System.out.println("emitter.isDisposed: "+emitter.isDisposed());


                System.out.println("emit 2");
                emitter.onNext(2);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });

    }
    @Test
    public void error() {
        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("onError");
                emitter.onError(new Throwable("HEIHEI"));
                System.out.println("onError");
                emitter.onError(new Throwable("HEIHEI"));
                System.out.println("emit 2");
                emitter.onNext(2);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });

    }


    @Test
    public void disposableTest() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("emit 1");
                emitter.onNext(1);
                System.out.println("emit 2");
                emitter.onNext(2);
                System.out.println("emit complete");
                emitter.onComplete();
                System.out.println("emit 3");
                emitter.onNext(3);
                emitter.onError(new Throwable("O__O "));
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable mDisposable;
            private int i;

            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(Integer value) {
                System.out.println("onNext: " + value);
                i++;
                if (i == 2) {
                    System.out.println("dispose");
                    mDisposable.dispose();
                    System.out.println("isDisposed : " + mDisposable.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }

            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
    }


    @Test
    public void disposableCollectionTest() {
        CompositeDisposable compositeDisposable=new CompositeDisposable();
        final ObservableEmitter[] emitter1 = new ObservableEmitter[2];
        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter1[0] =emitter;
                emitter.onNext(1);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });

        //创建一个上游 Observable：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter1[1] =emitter;
                emitter.onNext(1);
            }
        }).subscribe(//建立连接
                new Observer<Integer>() { //创建一个下游 Observer
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        System.out.println("subscribe");
                    }

                    @Override
                    public void onNext(Integer value) {
                        System.out.println("onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("complete");
                    }
                });

        compositeDisposable.clear();
        System.out.println("0 isDisposed:"+emitter1[0].isDisposed());
        System.out.println("1 isDisposed:"+emitter1[1].isDisposed());
    }


}
