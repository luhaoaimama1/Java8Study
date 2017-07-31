package zone.com.java8study.rx;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public class RxEmitter<T> implements ObservableOnSubscribe<T>, ObservableEmitter<T> {

    ObservableEmitter<T> e;

    @Override
    public void subscribe(ObservableEmitter<T> e) throws Exception {
        this.e = e;
    }

    @Override
    public void onNext(T value) {
        e.onNext(value);
    }

    @Override
    public void onError(Throwable error) {
        e.onError(error);
    }

    @Override
    public void onComplete() {
        e.onComplete();
    }

    @Override
    public void setDisposable(Disposable d) {
        e.setDisposable(d);
    }

    @Override
    public void setCancellable(Cancellable c) {
        e.setCancellable(c);
    }

    @Override
    public boolean isDisposed() {
        return e.isDisposed();
    }

    @Override
    public ObservableEmitter<T> serialize() {
        return e.serialize();
    }

    @Override
    public boolean tryOnError(Throwable t) {
        return e.tryOnError(t);
    }
}