import com.aliyuncs.exceptions.ClientException;
import com.jd.health.utils.SMSUtils;
import org.junit.Test;

/**
 * @Auther lxy
 * @Date
 */
public class TestMessage {
    @Test
    public void test() throws ClientException {
        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,"13598948870","666666");
    }
}
