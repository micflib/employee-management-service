package com.proj.ems.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.proj.ems.domain.EmployeeDocument;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
class EmployeeRepositoryTest
{
	private static final String UUID = "1234567890";

	@Autowired
	private EmployeeRepository repository;
	@Autowired
	private CacheManager cacheManager;

	@Configuration
	static class Config
	{
		@Bean
		CacheManager cacheManager()
		{
			return new ConcurrentMapCacheManager("Employees");
		}
		@Bean
		EmployeeRepository repository()
		{
			return Mockito.mock(EmployeeRepository.class);
		}
	}

	@BeforeEach
	void setup()
	{
		createEmployeeDocument();
	}

	@Test
	public void testFindByIdIsNotCachedInFirstRequest()
	{
		assertEquals(empty(), getCachedEmployee(UUID));
	}

	@Test
	public void testFindByIdIsCachedInSecondRequest()
	{
		Optional<EmployeeDocument> document = repository.findById(UUID);
		assertEquals(document, getCachedEmployee(UUID));
	}

	@Test
	void testRecordGotEvictedWhenBeingPersisted()
	{
		// 1st findByIdRequest will make the result cached
		Optional<EmployeeDocument> document = repository.findById(UUID);
		assertEquals(document, getCachedEmployee(UUID));

		// Save the same record will remove it from cache
		createEmployeeDocument();
		assertEquals(empty(), getCachedEmployee(UUID));
	}

	private Optional<EmployeeDocument> getCachedEmployee(String id)
	{
		return ofNullable(cacheManager.getCache("Employees")).map(c -> c.get(id, EmployeeDocument.class));
	}

	private void createEmployeeDocument()
	{
		repository.save(getEmployeeDocument());
	}

	private EmployeeDocument getEmployeeDocument()
	{
		return EmployeeDocument.builder()
			.uuid(UUID)
			.email("testemail@gmail.com")
			.fullName("Test Name")
			.birthday("2222-11-11")
			.hobbies(new ArrayList<>(Arrays.asList("travel", "music", "movies")))
			.build();
	}
}
