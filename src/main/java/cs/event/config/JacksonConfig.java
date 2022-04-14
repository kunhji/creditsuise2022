package cs.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cs.event.data.entity.LogEvent;
import cs.event.service.LogEventDeserializer;

@Configuration
public class JacksonConfig {

	@Bean
	public Module logEventModule() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(LogEvent.class, new LogEventDeserializer());
		return module;
	}
}
