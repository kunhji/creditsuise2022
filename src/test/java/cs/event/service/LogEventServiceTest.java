package cs.event.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import cs.event.data.entity.LogEvent;
import cs.event.data.repository.LogEventRepository;
import cs.event.model.AlertType;
import cs.event.model.LogEventStatus;

@RunWith(MockitoJUnitRunner.class)
public class LogEventServiceTest {

	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Mock
	private LogEventRepository logEventRepository;

	@InjectMocks
	private LogEventServiceImpl logEventService;

	@Test
	public void testReadAndPublishLogEvents() {
		String path = ClassLoader.getSystemResource("test-log-event.txt").getPath();
		logEventService.readAndPublishLogEvents(path);

		ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(eventPublisher, Mockito.times(6)).publishEvent(eventCaptor.capture());
	}

	@Test
	public void testProcessLogEventWhenNoEntryInDb() {

		LogEvent startEvent = new LogEvent();
		startEvent.setLogId("logIdStart");
		startEvent.setState(LogEventStatus.STARTED.name());
		startEvent.setLogTimestamp(1491377495213L);

		LogEvent startEventExpected = new LogEvent();
		startEventExpected.setLogId("logIdStart");
		startEventExpected.setState(LogEventStatus.STARTED.name());
		startEventExpected.setLogTimestamp(1491377495213L);
		startEventExpected.setAlert("N");

		Mockito.when(logEventRepository.findByLogId(Mockito.anyString())).thenReturn(null);

		Mockito.when(logEventRepository.save(startEvent)).thenReturn(startEventExpected);
		logEventService.processLogEvent(startEvent);

		Mockito.verify(logEventRepository, Mockito.times(1)).findByLogId("logIdStart");

		Mockito.verify(logEventRepository, Mockito.times(1)).save(Mockito.any());

	}

	@Test
	public void testProcessLogEventWhenEntryInDb() {

		LogEvent startEvent = new LogEvent();
		startEvent.setLogId("logIdStart");
		startEvent.setState(LogEventStatus.STARTED.name());
		startEvent.setLogTimestamp(1491377495213L);

		LogEvent startEventExpected = new LogEvent();
		startEventExpected.setLogId("logIdStart");
		startEventExpected.setState(LogEventStatus.STARTED.name());
		startEventExpected.setLogTimestamp(1491377495213L);
		startEventExpected.setAlert("N");

		Mockito.when(logEventRepository.findByLogId(Mockito.anyString())).thenReturn(startEvent);

		Mockito.when(logEventRepository.save(startEvent)).thenReturn(startEventExpected);
		logEventService.processLogEvent(startEvent);

		Mockito.verify(logEventRepository, Mockito.times(1)).findByLogId("logIdStart");

		Mockito.verify(logEventRepository, Mockito.times(1)).save(Mockito.any());

	}

	@Test
	public void testProcessLogEventWhenFirstEntryIsFinishedInDb() {

		LogEvent firstEvent = new LogEvent();
		firstEvent.setLogId("logIdFinished");
		firstEvent.setState(LogEventStatus.FINISHED.name());
		firstEvent.setLogTimestamp(1491377495213L);

		LogEvent firstEventExpected = new LogEvent();
		firstEventExpected.setLogId("logIdFinished");
		firstEventExpected.setState(LogEventStatus.FINISHED.name());
		firstEventExpected.setLogTimestamp(1491377495213L);
		firstEventExpected.setAlert("N");

		Mockito.when(logEventRepository.findByLogId(Mockito.anyString())).thenReturn(firstEvent);

		Mockito.when(logEventRepository.save(firstEvent)).thenReturn(firstEventExpected);
		logEventService.processLogEvent(firstEvent);

		Mockito.verify(logEventRepository, Mockito.times(1)).findByLogId("logIdFinished");

		Mockito.verify(logEventRepository, Mockito.times(1)).save(Mockito.any());

	}

	@Test
	public void testSetDurationAndAlertWhenDurationLessThan4s() {

		LogEvent startEvent = new LogEvent();
		startEvent.setLogId("logIdStart");
		startEvent.setState(LogEventStatus.STARTED.name());
		startEvent.setLogTimestamp(1491377495213L);

		logEventService.setDurationAndAlert(startEvent, 1491377495216L, 1491377495213L);

		assertEquals(3, startEvent.getDuration());
		assertEquals(AlertType.N.name(), startEvent.getAlert());
		assertEquals(LogEventStatus.COMPLETED.name(), startEvent.getState());

	}

	@Test
	public void testSetDurationAndAlertWhenDurationGreaterThan4s() {

		LogEvent startEvent = new LogEvent();
		startEvent.setLogId("logIdStart");
		startEvent.setState(LogEventStatus.STARTED.name());
		startEvent.setLogTimestamp(1491377495213L);

		logEventService.setDurationAndAlert(startEvent, 1491377495218L, 1491377495213L);

		assertEquals(5, startEvent.getDuration());
		assertEquals(AlertType.Y.name(), startEvent.getAlert());
		assertEquals(LogEventStatus.COMPLETED.name(), startEvent.getState());

	}
}
