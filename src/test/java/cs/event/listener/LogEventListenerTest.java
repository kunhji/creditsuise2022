package cs.event.listener;

import static org.mockito.Mockito.times;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import cs.event.data.entity.LogEvent;
import cs.event.service.LogEventServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(args = { "--arg1=test-path" })
public class LogEventListenerTest {

	@Autowired
	private ApplicationEventPublisher publisher;

	@MockBean
	private LogEventServiceImpl logEventService;

	@Test
	public void testLogEvent() {
		LogEvent logEvent = new LogEvent();
		publisher.publishEvent(logEvent);

		// verify that your method in you
		Mockito.verify(logEventService, times(1)).processLogEvent(logEvent);
	}

}
