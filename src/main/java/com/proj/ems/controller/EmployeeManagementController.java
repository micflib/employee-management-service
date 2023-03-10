package com.proj.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proj.ems.domain.EmployeeDto;
import com.proj.ems.service.EmployeeManagementService;

@RestController
@RequestMapping("/employees")
public class EmployeeManagementController
{
	private static final String SUCCESS_MESSAGE = "Operation successful.";
	private final EmployeeManagementService service;

	@Autowired
	public EmployeeManagementController(EmployeeManagementService service)
	{
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() //JsonArray
	{
		List<EmployeeDto> employees = service.getEmployeeList();
		return ResponseEntity.ok().body(employees);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeByUuid(@PathVariable String id)
	{
		EmployeeDto employeeDto = service.getEmployeeByUuid(id);
		return ResponseEntity.ok().body(employeeDto);
	}

	@PostMapping
	public ResponseEntity<EmployeeDto> createEmployee(@Validated @RequestBody EmployeeDto request)
	{
		EmployeeDto employeeDto = service.insert(request);
		return ResponseEntity.ok().body(employeeDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateEmployee(@Validated @RequestBody EmployeeDto request, @PathVariable String id)
	{
		service.update(request, id);
		return ResponseEntity.ok().body(SUCCESS_MESSAGE);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable String id)
	{
		service.delete(id);
		return ResponseEntity.ok().body(SUCCESS_MESSAGE);
	}
}
