package zone.com.java8study.test;

import junit.framework.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * [2017] by Zone
 */

public class EndSymbolTest {

    //    http://www.concretepage.com/java/jdk-8/java-8-stream-findAny-findFirst-limit-max-min-example
    public static void main(String[] args) {
        matchTest();
        maxMinTest();
        findTest();
        collectTest();
        reduceTest();
        forEach();
    }

    private static void forEach() {
        System.out.println("forEach:");
        Stream.of("1", "2", "3", "4").forEach(s -> System.out.print(s + "\t"));
        System.out.println();
    }

    private static void reduceTest() {
        //参数accumulator 累加
        System.out.println("reduce:" + Stream.of("1", "2", "3", "4")
                .reduce((integer, integer2) -> integer + integer2).get());
        //参数 identity初始值
        System.out.println("reduce:" + Stream.of("1", "2", "3", "4")
                .reduce("start:", (integer, integer2) -> integer + integer2));

        //参数 combiner 与parallel并行方法有关，
        //Tips:如果不是并行则不执行此方法！！！
        //accumulator的作用与单独线程的元素组累加;
        //combiner的作用在于合并每个线程的result得到最终结果
        System.out.println("reduce:" + Stream.of("1", "2", "3", "4")
                .parallel()
                .reduce("start:",
                        (integer, integer2) -> {
                            System.out.println("accumulator---->left:" + integer + "\t right:" + integer2);
                            return integer + integer2;
                        },
                        (integer, integer2) -> {
                            System.out.println("combiner--->left:" + integer + "\t right:" + integer2);
                            return integer + "," + integer2;
                        }));
    }

    private static void collectTest() {
        System.out.println("collect:" + Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList()));
        System.out.println("collect:" + Stream.of(1, 2, 3, 4, 5).collect(Collectors.toSet()));
        //todo
        System.out.println("collect:" +
                Stream.of(1, 2, 3)
                        .map(String::valueOf)
                        .parallel()//开启并行才能弹出combiner方法内的日志；
                        .collect(StringBuilder::new, (stringBuilder, s) -> {
                            System.out.println("accumulator:" + s);
                            stringBuilder.append(s);
                        }, (stringBuilder, stringBuilder2) -> {
                            //并行  .parallel()开启才会 弹出此日志
                            System.out.println("combiner stringBuilder1:" + stringBuilder.toString()
                                    + "\t stringBuilder2:" + stringBuilder2.toString());
                            stringBuilder.append(stringBuilder2);
                        }));
    }

    private static void findTest() {
        //findAny 目前和 findFirst一样都是返回第一个元素。。。
        System.out.println("findAny:" + Stream.of(1, 2, 3, 4, 5).findAny().get());
        System.out.println("findFirst:" + Stream.of(1, 2, 3, 4, 5).findFirst().get());
    }

    private static void maxMinTest() {
        //max 返回最大的元素 通过Comparator接口
        System.out.println("max:" + Stream.of(1, 2, 3, 4, 5).max((o1, o2) -> o1 >= o2 ? 1 : -1).get());
        //min 返回最小的元素 通过Comparator接口
        System.out.println("min:" + Stream.of(1, 2, 3, 4, 5).min((o1, o2) -> o1 >= o2 ? 1 : -1).get());
        //Tips:Comparator.comparing(Integer::valueOf) 用起来更爽!
        System.out.println("min-comparing:" + Stream.of(1, 2, 3, 4, 5)
                .min(Comparator.comparing(Integer::valueOf)).get());
    }

    private static void matchTest() {
        //allMatch  所有元素都匹配就return true;
        System.out.println("allMatch:" + Stream.of(1, 2, 3, 4, 5).allMatch(integer -> integer == 1));
        //anyMatch  有一个元素匹配就return true;
        System.out.println("anyMatch:" + Stream.of(1, 2, 3, 4, 5).anyMatch(integer -> integer == 1));
        //noneMatch  没有一个元素匹配就return true;
        System.out.println("noneMatch:" + Stream.of(1, 2, 3, 4, 5).noneMatch(integer -> integer == 1));
    }

}
