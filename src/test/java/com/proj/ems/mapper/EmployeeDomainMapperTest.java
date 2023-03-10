package com.proj.ems.mapper;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proj.ems.domain.EmployeeDocument;
import com.proj.ems.domain.EmployeeDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeDomainMapperTest
{

	private  EmployeeDomainMapper mapper;

	@BeforeEach
	void setup()
	{
		mapper = new EmployeeDomainMapper();
	}

	@Test
	void shouldReturnNull_whenMappingToDto_givenNull()
	{
		EmployeeDto actualEmployeeDto = mapper.toDto(null);
		assertEquals(null, actualEmployeeDto);
	}

	@Test
	void shouldReturnDto_whenMappingToDto_givenDocument()
	{
		EmployeeDocument employeeDocument = getEmployeeDocument();
		EmployeeDto actualEmployeeDto = mapper.toDto(employeeDocument);
		EmployeeDto expectedEmployeeDto = getEmployeeDto();
		assertEquals(expectedEmployeeDto, actualEmployeeDto);
	}

	@Test
	void shouldReturnNull_whenMappingToDocument_givenNull()
	{
		EmployeeDocument actualEmployeeDocument = mapper.toDocument(null);
		assertEquals(null, actualEmployeeDocument);
	}

	@Test
	void shouldReturnDocument_whenMappingToDocument_givenDto()
	{
		EmployeeDto employeeDto = getEmployeeDto();
		EmployeeDocument actualEmployeeDocument = mapper.toDocument(employeeDto);
		EmployeeDocument expectedEmployeeDocument = getEmployeeDocument();
		assertEquals(expectedEmployeeDocument, actualEmployeeDocument);
	}

	private EmployeeDocument getEmployeeDocument()
	{
		return EmployeeDocument.builder()
			.uuid("1234567890")
			.email("testemail@gmail.com")
			.fullName("Test Name")
			.birthday("2222-11-11")
			.hobbies(new ArrayList<>(Arrays.asList("travel", "music", "movies")))
			.build();
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
