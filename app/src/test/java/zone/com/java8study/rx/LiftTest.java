package zone.com.java8study.rx;

import org.junit.Test;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * [2017] by Zone
 */

public class LiftTest {
    @Test
    public void lift(){
            Observable.just(1,2)
                    //也是代理模式  observer是真正订阅
                    .lift(observer -> new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                            observer.onNext(integer+"?");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    })
                    .subscribe(o -> System.out.println(o));
    }
    @Test
    public void compose(){
        //compose:有多个 Observable ，并且他们都需要应用一组相同的 变换
        ObservableTransformer<Integer, Integer> composer=new ObservableTransformer<Integer, Integer>() {

            @Override
            public ObservableSource<Integer> apply(Observable<Integer> upstream) {
                return upstream
                        .take(5)
                        .filter(integer -> integer%2==0);
            } };

        Observable<Integer> ob1 = Observable.range(0,9);
        Observable<Integer> ob2 = Observable.range(10,9);
        Observable<Integer> ob3 = Observable.range(30,9);
        ob1.compose(composer).subscribe(integer -> System.out.println("ob1:"+integer));
        ob2.compose(composer).subscribe(integer -> System.out.println("ob2:"+integer));
        ob3.compose(composer).subscribe(integer -> System.out.println("ob3:"+integer));
    }
}
