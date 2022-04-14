package cs.event.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import cs.event.data.entity.LogEvent;

/**
 * Deserializer class for LogEvent
 *
 */
public class LogEventDeserializer extends StdDeserializer<LogEvent> {
	public LogEventDeserializer(Class<?> t) {
		super(t);
	}

	public LogEventDeserializer() {
		this(null);
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Parse log line to Object LogEvent
	 */
	@Override
	public LogEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		String id = node.get("id").asText();
		String state = node.get("state").asText();
		long timestamp = node.get("timestamp").asLong();
		String type = node.has("type") ? node.get("type").asText() : "";
		String hostName = node.has("host") ? node.get("host").asText() : "";
		LogEvent logEvent = new LogEvent();

		logEvent.setLogId(id);
		logEvent.setState(state);
		logEvent.setLogTimestamp(timestamp);
		logEvent.setLogType(type);
		logEvent.setHostName(hostName);
		return logEvent;
	}

}
