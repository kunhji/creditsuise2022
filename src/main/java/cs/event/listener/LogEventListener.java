package cs.event.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import cs.event.data.entity.LogEvent;
import cs.event.service.LogEventService;

/**
 * Listener class for log event processor. listens to two events.
 * 
 * 1 - json string for parsing 2 - log event to persist to DB.
 *
 */
@Component
public class LogEventListener {

	private final static Logger logger = LoggerFactory.getLogger(LogEventListener.class);

	@Autowired
	ObjectMapper mapper;

	@Autowired
	LogEventService service;

	/**
	 * Listen to a log event string and convert to LogEvent
	 * 
	 * @param jsonStrLogEvent - json string from log file to parse
	 * @return LogEvent - deserilazed LogEvent object
	 */
	@EventListener
	public LogEvent parseLogEntry(String jsonStrLogEvent) {
		try {
			logger.debug("Log event json string : {} received. Attempting parsing ...", jsonStrLogEvent);
			LogEvent logEvent = mapper.readValue(jsonStrLogEvent, LogEvent.class);
			logger.debug("Log event json string : {} is transformed to LogEvent : {}", jsonStrLogEvent, logEvent);
			return logEvent;

		} catch (IOException e) {
			// In case of exception in parsing log event, nothing more can be done. Log and
			// skip further processsing

			logger.error("Error occured while parsing  Log event json string : {} , error : {} ", jsonStrLogEvent, e);
			return null;
		}
	}

	/**
	 * Listen to LogEvent object and process the event ie, check the duration for a
	 * event and save to DB
	 * 
	 * @param logEvent - LogEvent to be processed
	 */
	@EventListener
	public void processLogEvent(LogEvent logEvent) {
		try {
			logger.info("Log event received with id {}. Attempting persistence ...", logEvent.getLogId());
			service.processLogEvent(logEvent);
			logger.debug("Log event with id {} saved to DB", logEvent.getLogId());

		} catch (Exception e) {
			logger.error("Error occured while saving to database, error : {} ", e);

			// Exception while persisting log to DB, in this current impl - this entry will
			// be lost. It is recommended to create a re-processing queue for guaranteed
			// processing
			// avoiding loss of msgs.

		}
	}
}
