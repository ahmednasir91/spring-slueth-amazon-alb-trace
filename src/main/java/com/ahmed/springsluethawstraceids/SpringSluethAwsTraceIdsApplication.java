package com.ahmed.springsluethawstraceids;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.log.SleuthSlf4jProperties;
import org.springframework.cloud.sleuth.log.SpanLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringSluethAwsTraceIdsApplication {

	public static final String AMAZON_TRACE_ID = "X-Amzn-Trace-Id";

	public static void main(String[] args) {
		SpringApplication.run(SpringSluethAwsTraceIdsApplication.class, args);
	}

	@Bean
	public HttpSpanExtractor awsTraceHttpSpanExtractor() {
		return new AwsTraceHttpSpanExtractor();
	}

	@Bean
	public HttpSpanInjector awsTraceHttpSpanInjector() {
		return new AwsTraceHttpSpanInjector();
	}

	@Bean
	public SpanLogger awsTraceSpanLogger(SleuthSlf4jProperties sleuthSlf4jProperties) {
		return new AwsTraceSpanLogger(sleuthSlf4jProperties.getNameSkipPattern());
	}

	@RestController
	class TestController {

		private Logger LOG = LoggerFactory.getLogger(getClass());

		@GetMapping
		public void hello() {
			LOG.info("Hello");
		}
	}
}
