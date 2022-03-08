package ex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class GqlDemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	JdbcTemplate template;
	
	@Test
	public void restaurantCount() {
		Integer count = template.queryForObject("select count(*) from T_RESTAURANT", Integer.class);
		System.out.println( String.format("there are %s restaurants", count));
	}
	
	@Test
	public void accountCount() {
		Integer count = template.queryForObject("select count(*) from T_ACCOUNT", Integer.class);
		System.out.println( String.format("there are %s accounts", count));
	}

}
