package com.example.demo;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@SpringBootApplication
public class CloudwatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudwatchApplication.class, args);
	}

	@Bean
	public MeterRegistry meterRegistry() {

		CloudWatchConfig cloudWatchConfig = new CloudWatchConfig() {
			@Override
			public String get(String s) {
				return null;
			}

			@Override
			public String namespace() {
				return "SpringBootApp";
			}
		};
		return new CloudWatchMeterRegistry(
				cloudWatchConfig,
				Clock.SYSTEM,
				CloudWatchAsyncClient.create());

	}


}
