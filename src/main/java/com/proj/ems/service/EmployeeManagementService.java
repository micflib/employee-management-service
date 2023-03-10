package com.proj.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.ems.domain.EmployeeDocument;
import com.proj.ems.domain.EmployeeDto;
import com.proj.ems.mapper.EmployeeDomainMapper;
import com.proj.ems.repository.EmployeeRepository;

@Service
public class EmployeeManagementService
{
	private final EmployeeRepository repository;
	private final EmployeeEventService eventService;
	private final EmployeeDomainMapper mapper;

	@Autowired
	public EmployeeManagementService(EmployeeRepository repository, EmployeeEventService eventService, EmployeeDomainMapper mapper)
	{
		this.repository = repository;
		this.eventService = eventService;
		this.mapper = mapper;
	}

	public List<EmployeeDto> getEmployeeList()
	{
		List<EmployeeDto> dtoList = new ArrayList<>();
		repository.findAll().forEach(employeeDocument -> {
			dtoList.add(mapper.toDto(employeeDocument));
		});
		return dtoList;
	}

	public EmployeeDto getEmployeeByUuid(String uuid)
	{
		return mapper.toDto(repository.findById(uuid).get());
	}

	public EmployeeDto insert(EmployeeDto employeeDto)
	{
		EmployeeDocument employeeDocument = mapper.toDocument(employeeDto);
		employeeDocument = employeeDocument.toBuilder().uuid(null).build();
		employeeDocument = repository.save(employeeDocument);
		eventService.send(new Object() {}.getClass().getEnclosingMethod().getName(), employeeDocument);
		return mapper.toDto(employeeDocument);
	}

	public void update(EmployeeDto employeeDto, String uuid)
	{
		EmployeeDocument employeeDocument = mapper.toDocument(employeeDto);
		repository.findById(uuid).get();
		employeeDocument = employeeDocument.toBuilder().uuid(uuid).build();
		repository.save(employeeDocument);
		eventService.send(new Object() {}.getClass().getEnclosingMethod().getName(), employeeDocument);
	}

	public void delete(String uuid)
	{
		repository.deleteById(uuid);
		eventService.send(new Object() {}.getClass().getEnclosingMethod().getName(), uuid);
	}
}
