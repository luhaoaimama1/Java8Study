package zone.com.java8study.test.dil;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * [2017] by Zone
 */

public class IntExtraTest {
    public static void main(String[] args) {

        System.out.println( IntStream.of(1,2,3).average());
        System.out.println( IntStream.of(1,2,3).sum());
        System.out.println( IntStream.of(1,2,3).summaryStatistics());
        System.out.println( IntStream.of(1,2,3).boxed());

        //不包括后边界
        System.out.println("range:"+IntStream.range(1,5).boxed().collect(Collectors.toList()));
        //包括后边界
        System.out.println("range:"+IntStream.rangeClosed(1,5).boxed().collect(Collectors.toList()));

    }
}
