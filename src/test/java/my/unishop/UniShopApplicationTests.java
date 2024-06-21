package my.unishop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(classes = UniShopApplicationTests.class)
@TestPropertySource(locations = "classpath:application-test.yml")
class UniShopApplicationTests {

	@Test
	void contextLoads() {
	}

}
