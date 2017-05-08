package zone.com.java8study;

import junit.framework.Assert;

import java.util.stream.Stream;

/**
 * [2017] by Zone
 */

public class Test {

    public static void main(String[] args) {
        matchTest();
        System.out.println("max:"+Stream.of(1, 2, 3, 4, 5).max((o1, o2) -> o1>=o2?1:-1).get());
        System.out.println("min:"+Stream.of(1, 2, 3, 4, 5).min((o1, o2) -> o1>=o2?1:-1).get());
    }

    private static void matchTest() {
        //allMatch  所有元素都匹配就return true;
        System.out.println("allMatch:"+Stream.of(1, 2, 3, 4, 5).allMatch(integer -> integer==1));
        //anyMatch  有一个元素匹配就return true;
        System.out.println("anyMatch:"+Stream.of(1, 2, 3, 4, 5).anyMatch(integer -> integer==1));
        //noneMatch  没有一个元素匹配就return true;
        System.out.println("noneMatch:"+Stream.of(1, 2, 3, 4, 5).noneMatch(integer -> integer==1));
    }

}
