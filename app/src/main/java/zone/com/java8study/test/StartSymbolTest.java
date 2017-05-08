package zone.com.java8study.test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import zone.com.java8study.GsonUtils;

/**
 * [2017] by Zone
 */

public class StartSymbolTest {
    public static void main(String[] args) {
        //concat 连接两个流
        System.out.println("map:" +
                GsonUtils.toJson(
                        IntStream
                                .concat(IntStream.range(1, 3), IntStream.range(4, 8))
                                .toArray()));
        //of 把参数变成流
        System.out.println("of:" +
                GsonUtils.toJson(
                        Stream.of(1, 2, 3)
                                .toArray()));
        generate2IterateTest();

        //buidler 一个一个加元素最后build成流
        System.out.println("builder:" +
                Stream.builder()
                        .add(1).add(2).add(3)
                        .build()
                        .collect(Collectors.toList()));

        System.out.println("empty:" + Stream.empty().collect(Collectors.toList()));
    }

    private static void generate2IterateTest() {
        //iterate: while循环
        System.out.println("generate:");
        Stream.generate(() -> "AA")
                .limit(5)//不加这个就死循环了！
                .forEach(s -> System.out.println("generate:" + s));

        //iterate: while循环+累加器
        System.out.println("iterate:");
        //seed 初始值，f 累加器
        Stream.iterate("a", s -> s + "1")
                .limit(5)//不加这个就死循环了！
                .forEach(s -> System.out.println("iterate:" + s));
    }
}
