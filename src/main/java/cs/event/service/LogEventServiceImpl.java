package cs.event.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cs.event.data.entity.LogEvent;
import cs.event.data.repository.LogEventRepository;
import cs.event.model.AlertType;
import cs.event.model.LogEventStatus;

/**
 * Service class for Log event processor
 *
 */
@Component
public class LogEventServiceImpl implements LogEventService {

	final static Logger logger = LoggerFactory.getLogger(LogEventServiceImpl.class);

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	LogEventRepository logEventRepository;

	@Override
	public void readAndPublishLogEvents(String path) {
		try {
			logger.info("Reading log events from file {}", path);

			// Read file
			// .lines - support for Large files is provided using Files.lines in Java8.
			// .parallel - improves performance by parallelising reading

			Files.lines(Paths.get(path)).parallel().filter(StringUtils::hasText).forEach(eventPublisher::publishEvent);
			logger.info("Log events from log file {} published for processing.", path);

		} catch (IOException e) {
			logger.error("Error occurred while reading from path : {} , error : {}  ", path, e);
			System.exit(1); // Cannot proceed as file cannot be read or published
		}
	}

	@Override
	public void processLogEvent(LogEvent log) {

		// Check if the log entry with the id already exists.
		LogEvent logEventFromDb = logEventRepository.findByLogId(log.getLogId());

		if (logEventFromDb != null) {
			// Log event found in DB
			logger.debug("Log event found for log id {},  updating DB entry", log);
			if (LogEventStatus.STARTED.name().equals(log.getState())) {
				// entry in DB is in FINISHED status.
				long finishedTimestamp = logEventFromDb.getLogTimestamp();
				setDurationAndAlert(log, finishedTimestamp, log.getLogTimestamp());
				logEventRepository.save(log);
			} else if (LogEventStatus.FINISHED.name().equals(log.getState())) {
				// entry in DB is in STARTED status
				long startedTimestamp = logEventFromDb.getLogTimestamp();
				setDurationAndAlert(log, log.getLogTimestamp(), startedTimestamp);
				logEventRepository.save(log);
			}
			logger.info("COMPLETED : - Log event found in db for log id {},  updated DB", log);
		} else {
			// Log event NOT found in DB
			// write to db if event does not already exist
			logger.debug("No event found in db for log {} , hence writing to DB", log);
			logEventRepository.save(log);
			logger.info("Log entry persisted in DB {}", log);
		}
	}

	/**
	 * Get the start and end timestamp and set duration and alert
	 * 
	 * @param log           - log event to set the duration and alert
	 * @param fromTimeStamp - first timestamp from db
	 * @param toTimeStamp   - new timestamp from log
	 */
	public void setDurationAndAlert(LogEvent log, Long finishedTimeStamp, Long startedTimeStamp) {
		long duration = finishedTimeStamp - startedTimeStamp;
		log.setDuration(duration);

		// alert if long event take longer than 4ms
		// The number of ms (4 ms) should be a "Configurable parameter"

		log.setAlert((duration > 4) ? AlertType.Y.name() : AlertType.N.name());
		log.setState(LogEventStatus.COMPLETED.name());
	}

}
