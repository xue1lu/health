import com.jd.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Auther lxy
 * @Date
 */
public class A_ProviderApplication {

    @Test
    public void test() throws IOException {

        //获得容器
        new ClassPathXmlApplicationContext("spring-provider.xml");

        System.in.read();
    }
}
