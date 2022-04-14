package cs.event.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import cs.event.data.entity.LogEvent;

@RunWith(MockitoJUnitRunner.class)
public class LogEventDeserializerTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private JsonParser jsonParser;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private JsonNode jsonNode;

	@InjectMocks
	private LogEventDeserializer deserializer;

	@Before
	public void initMocks() throws IOException {
		when(jsonParser.getCodec().readTree(jsonParser)).thenReturn(jsonNode);

		when(jsonNode.get("id").asText()).thenReturn("id");
		when(jsonNode.get("state").asText()).thenReturn("state");
		when(jsonNode.get("timestamp").asLong()).thenReturn(3L);
	}

	@Test
	public void testDeserializeLogEventWhenApplicationLogEventNotExists() throws IOException {
		when(jsonNode.has("type")).thenReturn(false);
		when(jsonNode.has("host")).thenReturn(false);
		LogEvent logEvent = deserializer.deserialize(jsonParser, null);

		assertEquals("id", logEvent.getLogId());
		assertEquals("state", logEvent.getState());
		assertEquals(3L, logEvent.getLogTimestamp());
		assertEquals("", logEvent.getLogType());
		assertEquals("", logEvent.getHostName());
	}

	@Test
	public void testDeserializeLogEventWhenApplicationLogEventExists() throws IOException {
		when(jsonNode.has("type")).thenReturn(true);
		when(jsonNode.has("host")).thenReturn(true);

		when(jsonNode.get("type").asText()).thenReturn("type");
		when(jsonNode.get("host").asText()).thenReturn("host");

		LogEvent logEvent = deserializer.deserialize(jsonParser, null);

		assertEquals("id", logEvent.getLogId());
		assertEquals("state", logEvent.getState());
		assertEquals(3L, logEvent.getLogTimestamp());
		assertEquals("type", logEvent.getLogType());
		assertEquals("host", logEvent.getHostName());
	}

}
