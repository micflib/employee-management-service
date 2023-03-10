package com.proj.ems.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.proj.ems.domain.EmployeeDocument;
import com.proj.ems.domain.EmployeeDto;

@Component
public class EmployeeDomainMapper
{
	public EmployeeDocument toDocument(EmployeeDto employeeDto)
	{
		if (employeeDto == null)
		{
			return null;
		}

		return EmployeeDocument.builder()
			.uuid(mapEmptyString(employeeDto.getUuid()))
			.email(mapEmptyString(employeeDto.getEmail()))
			.fullName(mapEmptyString(employeeDto.getFullName()))
			.birthday(mapEmptyString(employeeDto.getBirthday()))
			.hobbies(mapEmptyList(employeeDto.getHobbies()))
			.build();

	}

	public EmployeeDto toDto(EmployeeDocument employeeDocument)
	{
		if (employeeDocument == null)
		{
			return null;
		}

		return EmployeeDto.builder()
			.uuid(mapEmptyString(employeeDocument.getUuid()))
			.email(mapEmptyString(employeeDocument.getEmail()))
			.fullName(mapEmptyString(employeeDocument.getFullName()))
			.birthday(mapEmptyString(employeeDocument.getBirthday()))
			.hobbies(mapEmptyList(employeeDocument.getHobbies()))
			.build();
	}

	private String mapEmptyString(String string)
	{
		return string != null && !string.isEmpty() ? string : null;
	}

	private <T> List<T> mapEmptyList(List<T> list)
	{
		return list != null && !list.isEmpty() ? list : null;
	}
}
