package rewards.batch.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

//	TODO 01: Check the content of this configuration class.
//	It imports the Spring Batch infrastructure configuration class and the job configuration.
//	You will do most of your work in the BatchJobConfig, and the related test class.
@Configuration
@Import({BatchExecutionConfig.class, BatchJobConfig.class})
public class SystemTestConfig {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.addScript("classpath:org/springframework/batch/core/schema-hsqldb.sql")
					  .addScript("classpath:db-schema.sql")
					  .addScript("classpath:db-test-data.sql")
					  .build();
	}

}
