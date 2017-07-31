package zone.com.java8study.rx.over;

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

}
