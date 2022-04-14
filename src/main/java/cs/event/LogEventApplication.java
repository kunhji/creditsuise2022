package cs.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cs.event.service.LogEventService;

/**
 * Boot starter class for log event processor application
 *
 */
@SpringBootApplication
public class LogEventApplication implements CommandLineRunner {

	@Autowired
	LogEventService logEventService;

	public static void main(String[] args) {
		SpringApplication.run(LogEventApplication.class, args);
	}

	@Override
	public void run(String... args) {

		if (args == null || args.length != 1) {
			throw new IllegalArgumentException("Path of log file expected as input argument.");
		}
		logEventService.readAndPublishLogEvents(args[0]);

	}

}