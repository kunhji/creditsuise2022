package cs.event.service;

import cs.event.data.entity.LogEvent;

public interface LogEventService {

	/**
	 * Read each line of from the file and publish and event for each line in
	 * parallel to be processed further
	 * 
	 * @param path - file path
	 */
	public void readAndPublishLogEvents(String path);

	/**
	 * Process each log event. Process log event and flag any long events that take
	 * longer than 4ms
	 * 
	 * @param path - file path
	 */
	public void processLogEvent(LogEvent log);

}
