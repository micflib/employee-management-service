package com.proj.ems.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.proj.ems.domain.EmployeeEvent;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@Configuration
public class EmployeeEventConfig
{
	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	private static final Integer RETRIES = 3;

	public Map<String, Object> producerConfiguration()
	{
		Map<String, Object> producerProperties = new HashMap<>();
		producerProperties.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		producerProperties.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		producerProperties.put(RETRIES_CONFIG, RETRIES);
		return producerProperties;
	}

	@Bean
	public KafkaTemplate<String, EmployeeEvent> kafkaTemplate()
	{
		Map<String, Object> producerConfiguration = producerConfiguration();
		DefaultKafkaProducerFactory<String, EmployeeEvent> producerFactory = new DefaultKafkaProducerFactory<>(producerConfiguration);
		return new KafkaTemplate<>(producerFactory);
	}
}
