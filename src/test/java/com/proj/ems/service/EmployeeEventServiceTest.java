package com.proj.ems.service;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import com.proj.ems.domain.EmployeeEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeEventServiceTest
{
	@Test
	void shouldAbleToSendEventToCorrectTopic_whenSendingEvent_givenMethodAndData()
	{
		KafkaTemplate<String, EmployeeEvent> kafkaTemplate = mock(KafkaTemplate.class);
		EmployeeEventService service = new EmployeeEventService(kafkaTemplate, "topic");
		when(kafkaTemplate.send(anyString(), any(EmployeeEvent.class))).thenReturn(null);

		service.send("test", null);

		verify(kafkaTemplate, times(1)).send(anyString(), any(EmployeeEvent.class));
	}

	@Test
	public void shouldAbleToSendEventToCorrectTopic_whenSendingEvent_givenEvent()
	{
		KafkaTemplate<String, EmployeeEvent> kafkaTemplate = mock(KafkaTemplate.class);
		EmployeeEventService service = new EmployeeEventService(kafkaTemplate, "topic");
		when(kafkaTemplate.send(anyString(), any(EmployeeEvent.class))).thenReturn(null);

		EmployeeEvent notification = new EmployeeEvent("test", null);
		service.send(notification);

		verify(kafkaTemplate, times(1)).send("topic", notification);
	}
}
