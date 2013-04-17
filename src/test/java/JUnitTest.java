import com.example.photographers.Cache;
import com.example.photographers.Image;
import com.example.photographers.services.CoreLoader;
import org.junit.Test;

import java.util.List;

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
            loader.Load("http://photographers.com.ua/pictures/days/30/?page=" + i, Cache.getInstance());
        }

        List<Image> cache = Cache.getInstance().getCache();
        int iter = 0;
        for (Image image : cache) {
            System.out.println("image = " + image);
        }
    }
}
