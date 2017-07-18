package zone.com.java8study.java8.collectors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import zone.com.java8study.utils.GsonUtils;

/**
 * [2017] by Zone
 */

public class SymbolTest {
    public static void main(String[] args) {
        //counting 同list.size()
        log("counting:" + Stream.of(1, 2, 3, 4).collect(Collectors.counting()));
        //maxBy
        log("maxBy:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.maxBy(Comparator.comparing(o -> o))).get());
        //minBy
        log("minBy:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.minBy(Comparator.comparing(o -> o))).get());


        //averagingInt计算平均值
        log("averagingInt:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.averagingInt(value -> value)));

        //summingInt 求和
        log("summingInt:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.summingInt(value -> value)));

        //summarizingInt 计算平均值
        log("summarizingInt:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.summarizingInt(value -> value)));

        //mapping 原来的流转换成新流:方形过渡到圆形
        log("mapping:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.mapping(o -> "新元素：" + String.valueOf(o)
                        , Collectors.toList())));

        //collectingAndThen 收集之后装起来
        //downstream :Collector,finisher 把downStream收集起来；
        log("collectingAndThen:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.collectingAndThen(Collectors.toList(),
                        integers -> integers.toArray())));

        //partitioningBy groupingBy 功能的简化,分组类型仅仅有两种 true false;
        log("partitioningBy:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.partitioningBy(o -> o % 2 == 0)));

        groupingByTest();
        joining();
        toCollection();
        reduceingTest();
    }

    private static void reduceingTest() {
        //reducing 累加器
        log("reducing:" + Stream.of("1", "2", "3").collect(
                Collectors.reducing((o, o2) -> o + o2)).get());
        // reducing
        log("reducing:" + Stream.of("1", "2", "3").collect(
                Collectors.reducing("start:", (o, o2) -> o + o2)));

        // reducing
        // identity 初始
        // mapper 累加之前流元素处理
        // op 累加和 与 流元素 的合并
        log("reducing:" + Stream.of("1", "2", "3").collect(
                Collectors.reducing("start:",
                        o -> {
                            System.out.println("mapper:" + o);
                            return o;
                        },
                        (u, u2) -> {
                            System.out.println("op u:" + u + "\t u2:" + u2);
                            return u + u2;
                        })));
    }

    private static void groupingByTest() {
        //groupingBy 类型分组
        log("groupingBy:" + Stream.of(
                new Student("I", "1"),
                new Student("love", "2"),
                new Student("you?", "3")
        ).collect(Collectors.groupingBy(Student::getStyle)));

        //mapFactory:用一个集合去装载进来的流元素；仅仅初始化一次；
        //downstream:Collector 为了继续操作
        log("groupingBy:" + Stream.of(
                new Student("I", "1"),
                new Student("love", "2"),
                new Student("you?", "3")
        ).collect(Collectors.groupingBy(Student::getStyle,
                () -> new HashMap<>(),
                Collectors.toList())));
    }

    private static void joining() {
        //joining 原来的流转换成新流:方形过渡到圆形
        log("joining:" + Stream.of("1", "2", "3", "4").collect(
                Collectors.joining()));
        log("joining:" + Stream.of("1", "2", "3", "4").collect(
                Collectors.joining(";")));
        log("joining:" + Stream.of("1", "2", "3", "4").collect(
                Collectors.joining(";", "start-", "-end")));
    }

    private static void toCollection() {
        //toCollection -->collectionFactory:用一个集合去装载进来的流元素；仅仅初始化一次；
        log("toCollection:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toCollection(ArrayList::new)));
        log("toCollection:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toCollection(() -> {
                    System.out.println("初始化");
                    return new ArrayList<>();
                })
        ));

        //toList 用list去收集Array/Linked等会选优
        log("toList:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toList()));
        //toSet 用list去收集Hash/Tree等会选优
        log("toSet:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toSet()));


        //toMap
        // keyMapper：从流元素中提取对应的key
        // valueMapper：从流元素中提取对应的value
        // Function.identity() 恒等式 ；就是原来是啥 结果就是啥
        log("toMap:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toMap(o -> o, integer -> integer)));

        log("toMap:" + Stream.of(1, 2, 3, 4).collect(
                Collectors.toMap(o -> o + 10, Function.identity())));
    }

    public static void log(String str) {
        System.out.println(str);
    }

    public static class Student {
        private String name;
        private String style;

        public Student(String name, String style) {
            this.name = name;
            this.style = style;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        @Override
        public String toString() {
            return GsonUtils.toJson(this);
        }
    }
}
