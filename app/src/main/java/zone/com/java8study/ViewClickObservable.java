//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package zone.com.java8study;

import android.view.View;
import android.view.View.OnClickListener;
import com.jakewharton.rxbinding2.internal.Notification;
import com.jakewharton.rxbinding2.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

//todo 1为了view不被发送而泄漏 采取Observable<Object>
final class ViewClickObservable extends Observable<Object> {
    private final View view;

    ViewClickObservable(View view) {
        this.view = view;
    }

// todo   可以看成像一种代理机制，不设置就是直接返回原对象，可以忽略，最后调用subscribeActual（）
//    observer中包含了 原observer 这个observer发数据，就用原observer发数据
//    订阅者同样是代理。
    //支触发一次
    protected void subscribeActual(Observer<? super Object> observer) {
//        if(Preconditions.checkMainThread(observer)) {
            //todo 2这里是通过observer 发射数据
            ViewClickObservable.Listener listener = new ViewClickObservable.Listener(this.view, observer);
            //todo 3 退出监听的订阅 因为想要知道什么时候 onDispose 从而接触click监听
            observer.onSubscribe(listener);
            this.view.setOnClickListener(listener);
//        }
    }

    static final class Listener extends MainThreadDisposable implements OnClickListener {
        private final View view;
        private final Observer<? super Object> observer;

        Listener(View view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        public void onClick(View v) {
            if(!this.isDisposed()) {
                this.observer.onNext(Notification.INSTANCE);
            }

        }

        protected void onDispose() {
            this.view.setOnClickListener((OnClickListener)null);
        }
    }
}
