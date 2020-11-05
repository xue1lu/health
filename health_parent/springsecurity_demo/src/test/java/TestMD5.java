import com.jd.health.utils.MD5Utils;
import org.junit.Test;

/**
 * @Auther lxy
 * @Date
 */
public class TestMD5 {
    @Test
    public void test() {
        System.out.println(MD5Utils.md5("1234xiaowang"));
        System.out.println(MD5Utils.md5("1234xiaoli"));
    }
}
