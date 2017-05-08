package zone.com.java8study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.Collections;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IOActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Stream.of("a","b","c").peek(String::toString).collect(Collectors.toList());
        Stream.of("a","b","c").collect(Collectors.toSet());
        Stream.of("a","b","c").collect(Collectors.toCollection(TreeSet::new));
        BinaryOperator<String> a=(s, s2) -> s+s2;

    }

    private void log() {
        System.out.println("hei");
    }

}
