import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther lxy
 * @Date
 */
public class TestSpringSecurity {
    @Test
    public void testSpringSecurity() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("1234"));
        //$2a$10$kTOJfJkpDlFjCLIM4/UOY.plmYkFcC1KBLLYwKA6xlQMPFkao8LnW
        //解密
        System.out.println(encoder.matches("1234","$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));
        System.out.println(encoder.matches("1234","$2a$10$3xW2nBjwBM3rx1LoYprVsemNri5bvxeOd/QfmO7UDFQhW2HRHLi.C"));
        System.out.println(encoder.matches("1234","$2a$10$zYJRscVUgHX1wqwu90WereuTmIg6h/JGirGG4SWBsZ60wVPCgtF8W"));


    }
}
