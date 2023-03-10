package com.proj.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.ems.domain.EmployeeEvent;

@Service
public class EmployeeEventService
{
	private final KafkaTemplate<String, EmployeeEvent> kafkaTemplate;
	private final String topicName;

	@Autowired
	public EmployeeEventService(KafkaTemplate<String, EmployeeEvent> kafkaTemplate, @Value("${spring.kafka.topic-name}") String topicName)
	{
		this.kafkaTemplate = kafkaTemplate;
		this.topicName = topicName;
	}

	public void send(EmployeeEvent producer)
	{
		this.kafkaTemplate.send(topicName, producer);
	}

	public void send(String methodType, Object data)
	{
		String message;
		try
		{
			message = new ObjectMapper().writeValueAsString(data);
		}
		catch (JsonProcessingException e)
		{
			throw new RuntimeException(e);
		}
		EmployeeEvent producer = new EmployeeEvent(methodType, message);
		send(producer);
	}
}
