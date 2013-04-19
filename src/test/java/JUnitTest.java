import com.example.photographers.services.Consumer;
import com.example.photographers.services.CoreLoader;
import org.junit.Test;

/**
 * User: patronus
 */
public class JUnitTest {

    @Test
    public void testLoading() {
        CoreLoader loader = new CoreLoader();
        int i = 0;
        for (; i <= 100; i++) {
            System.out.println("i = " + i);
            loader.load("http://photographers.com.ua/pictures/days/30/?page=" + i, new Consumer(null));
        }



    }
}
