package zone.com.java8study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.stream.Stream;
public class LambdaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void main(String[] args) {
        boolean result = Stream.of(1, 2, 3, 4, 5).allMatch(integer -> integer==1);
        System.out.println(result);
    }

    private void log() {
        System.out.println("hei");
    }
}
