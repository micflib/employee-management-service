package com.proj.ems.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.proj.ems.domain.EmployeeDocument;
import com.proj.ems.domain.EmployeeDto;
import com.proj.ems.mapper.EmployeeDomainMapper;
import com.proj.ems.repository.EmployeeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
class EmployeeManagementServiceTest
{
	private static final String UUID = "1234567890";
	@InjectMocks
	private EmployeeManagementService managementService;
	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private EmployeeDomainMapper mapper;
	@Mock
	private EmployeeEventService eventService;

	@Configuration
	static class Config
	{
		@Bean
		EmployeeRepository repository()
		{
			return Mockito.mock(EmployeeRepository.class);
		}
	}

	@Test
	void shouldReturnEmployeeDtoList_whenGetEmployeeList()
	{
		when(employeeRepository.findAll()).thenReturn(getDocuments());
		when(mapper.toDto(any())).thenReturn(getEmployeeDto());
		List<EmployeeDto> list = managementService.getEmployeeList();
		assertEquals(getEmployees(), list);
	}

	@Test
	void shouldReturnEmployeeDto_whenGetEmployeeByUuid_givenString()
	{
		when(employeeRepository.findById(any())).thenReturn(Optional.ofNullable(getEmployeeDocument()));
		when(mapper.toDto(any())).thenReturn(getEmployeeDto());
		EmployeeDto actualEmployeeDto = managementService.getEmployeeByUuid(UUID);
		assertEquals(getEmployeeDto(), actualEmployeeDto);
	}

	@Test
	void shouldReturnInsertedEmployeeDto_whenInsert_givenEmployeeDto()
	{
		when(employeeRepository.save(any())).thenReturn(getEmployeeDocument());
		when(mapper.toDocument(any())).thenReturn(getEmployeeDocument());
		when(mapper.toDto(any())).thenReturn(getEmployeeDto());

		EmployeeDto actualEmployeeDto = managementService.insert(getEmployeeDto());
		assertEquals(getEmployeeDto(), actualEmployeeDto);
		verify(employeeRepository, times(1)).save(any());
		verify(eventService, times(1)).send(any(), any());
	}

	@Test
	void shouldBeSuccess_whenUpdate_givenEmployeeDtoAndId()
	{
		when(mapper.toDocument(any())).thenReturn(getEmployeeDocument());
		when(employeeRepository.findById(any())).thenReturn(Optional.ofNullable(getEmployeeDocument()));
		managementService.update(getEmployeeDto(), UUID);
		verify(employeeRepository, times(1)).findById(any());
		verify(employeeRepository, times(1)).save(any());
		verify(eventService, times(1)).send(any(), any());
	}

	@Test
	void shouldBeSuccess_whenDelete_givenId()
	{
		managementService.delete(UUID);
		verify(employeeRepository, times(1)).deleteById(any());
		verify(eventService, times(1)).send(any(), any());
	}

	private Iterable<EmployeeDocument> getDocuments()
	{
		List<EmployeeDocument> documents = new ArrayList<>();
		documents.add(getEmployeeDocument());
		documents.add(getEmployeeDocument());
		return documents;
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

	private List<EmployeeDto> getEmployees()
	{
		List<EmployeeDto> dtos = new ArrayList<>();
		dtos.add(getEmployeeDto());
		dtos.add(getEmployeeDto());
		return dtos;
	}

	private EmployeeDto getEmployeeDto()
	{
		return EmployeeDto.builder()
			.uuid("1234567890")
			.email("testemail@gmail.com")
			.fullName("Test Name")
			.birthday("2222-11-11")
			.hobbies(new ArrayList<>(Arrays.asList("travel", "music", "movies")))
			.build();
	}
}
