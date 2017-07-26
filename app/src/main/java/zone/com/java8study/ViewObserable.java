package zone.com.java8study;

import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * [2017] by Zone
 */

public class ViewObserable extends Observable {
    private final View v;

    public ViewObserable(View v) {
        this.v=v;
    }

    @Override
    protected void subscribeActual(Observer observer) {
        //Observable。sub订阅的时候 会被他包装下；
        // 在这里可以订阅
        v.setOnClickListener(v1 ->{
            System.out.println(1);
            observer.onNext(0);
        });
    }
}
