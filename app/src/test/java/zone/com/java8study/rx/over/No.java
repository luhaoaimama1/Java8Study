package zone.com.java8study.rx.over;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.functions.BooleanSupplier;

/**
 * [2017] by Zone
 */

public class No {


    @Test
    public void repeat() {
        //    repeat 方法。它不是创建一个Observable，而是重复发射原始
// Observable的数据序列，这个序列或者是无限的，或者通过 repeat(n) 指定重复次数
        Observable.just("Love", "For", "You!")
                .repeat(3)//重复三次
                .subscribe(s -> System.out.println(s));


        System.out.println("-----repeatUntil-----");

        //repeatUntil getAsBoolean 如果返回 true则不repeat false则repeat
        //主要用于动态控制
        Observable.just("Love", "For", "You!")
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        System.out.println("getAsBoolean");
                        count++;
                        if (count == 3)
                            return true;
                        else
                            return false;
                    }
                }).subscribe(s -> System.out.println(s));


        System.out.println("-----repeatWhen -----");
        /**
         * repeatWhen 暂时看不出来有啥用
         */
    }

    int count;
}
