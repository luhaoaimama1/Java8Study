package zone.com.java8study.collectors;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * [2017] by Zone
 */

public class StringCollector implements Collector<String, StringBuilder, String> {
    @Override
    public Supplier<StringBuilder> supplier() {
        return () -> new StringBuilder();
    }

    @Override
    public BiConsumer<StringBuilder, String> accumulator() {
        return (stringBuilder, s) -> {
            System.out.println("accumulator:"+s);
            stringBuilder.append(s+",");
        };
    }

    @Override
    public BinaryOperator<StringBuilder> combiner() {
        //todo 未被使用 ？？？
        return (stringBuilder, stringBuilder2) -> {
            System.out.println("combiner stringBuilder1:"+stringBuilder.toString()
                    +"\t stringBuilder2:"+stringBuilder2.toString());
            return stringBuilder.append(stringBuilder2);
        };
    }

    @Override
    public Function<StringBuilder, String> finisher() {
        return StringBuilder::toString;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(
                EnumSet.of(
                        //特征支持并发特性
                        // 开启后 即使是 .parallel()。combiner方法被忽略；
//                        Characteristics.CONCURRENT ,
                        //todo ？？？
                         Characteristics.UNORDERED
                        //特征开启后 忽略finisher方法 正确的说此时的finisher方法其实是indentity函数；
//                        ,Characteristics.IDENTITY_FINISH
                ));
    }

    public static void main(String[] args) {
        System.out.println(
                Stream.of(1, 2, 3, 4)
                        .map(String::valueOf)
//                        .parallel()
                        .collect(new StringCollector()));
    }
}
