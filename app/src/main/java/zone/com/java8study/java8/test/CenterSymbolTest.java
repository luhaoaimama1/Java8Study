package zone.com.java8study.java8.test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import zone.com.java8study.utils.GsonUtils;

/**
 * [2017] by Zone
 */

public class CenterSymbolTest {
    public static void main(String[] args) {

        //filter 筛选
        System.out.println("filter:" +
                Stream.of(1, 2, 3, 4)
                        .filter(integer -> integer > 3)
                        .collect(Collectors.toList()));
        //peek 查看流元素
        System.out.println("peek:" +
                Stream.of(1, 2, 3, 4)
                        .peek(integer -> System.out.println(integer))
                        .collect(Collectors.toList()));

        //distinct 去掉重复元素
        System.out.println("distinct:" +
                Stream.of(1, 2, 2, 4)
                        .distinct()
                        .collect(Collectors.toList()));

        //skip 扔掉前面n个元素
        System.out.println("distinct:" +
                Stream.of(1, 2, 3, 4)
                        .skip(2)
                        .collect(Collectors.toList()));


        //limit 返回不超过跟定长度n的流
        System.out.println("limit:" +
                Stream.of("1", "2", "3", "4")
                        .limit(2)
                        .collect(Collectors.toList()));

        //map 原来的流转换成新流:方形过渡到圆形
        System.out.println("map:" +
                Stream.of("1", "2", "3", "4")
                        .map(s -> "新元素"+s)
                        .collect(Collectors.toList()));

        flatMapTest();
        sortTest();
        //map 原来的流转换成新流:方形过渡到圆形
        System.out.println("unordered:" +
                Stream.of(7,1, 2, 3, 4)
                        .unordered()
                        .peek(integer -> System.out.println(integer))
                        .collect(Collectors.toList()));
    }

    private static void sortTest() {
        //sorted 排序
        System.out.println("sorted:" +
                Stream.of( "3", "1", "2","4")
                        .sorted()
                        .collect(Collectors.toList()));

        //sorted 排序
        System.out.println("sorted:" +
                Stream.of( "3", "1", "2","4")
                        .sorted(Comparator.comparing(t -> t))
                        .collect(Collectors.toList()));
    }

    private static void flatMapTest() {
        //flatMap 合并流
        long[][] data = {{1L,2L},{3L,4L},{5L,6L}};
        LongStream ls1 = Arrays.stream(data).flatMapToLong(row -> Arrays.stream(row));
        System.out.println("flatMap:"+ GsonUtils.toJson(ls1.toArray()));

        //flatMap：正确的使用姿势 ,合并流
        int[] l1 = {4,8,9};
        IntDemoPerson p1 = new IntDemoPerson("Ram", l1);
        int[] l2 = {2,7,8};
        IntDemoPerson p2 = new IntDemoPerson("Shyam", l2);
        List<IntDemoPerson> list = Arrays.asList(p1,p2);

        IntStream is2 = list.stream().flatMapToInt(row -> Arrays.stream(row.getLuckyNum()));
        System.out.println("flatMap:"+GsonUtils.toJson(is2.toArray()));
    }

    static  class IntDemoPerson {
        private String name;
        private int[] luckyNum;
        public IntDemoPerson(String name, int[] luckyNum){
            this.name = name;
            this.luckyNum = luckyNum;
        }
        public String getName() {
            return name;
        }
        public int[] getLuckyNum() {
            return luckyNum;
        }
    }

}
