package zone.com.java8study.rx;

import org.junit.Test;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * [2017] by Zone
 */

public class Demo {
    @Test
    public void fileTranslate(){
        Observable.fromArray( new File("/Users/fuzhipeng/Documents"))
                .flatMap(file ->Observable.fromArray(file.listFiles()) )
                .filter(file -> file.getName().endsWith(".png"))
                .take(5)
                .map(file ->  file.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(s -> System.out.println(s));
        while (true){}
    }
}
