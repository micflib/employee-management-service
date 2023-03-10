package com.proj.ems.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.proj.ems.domain.EmployeeDto;
import com.proj.ems.service.EmployeeManagementService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeManagementController.class)
class EmployeeManagementControllerIntegrationTest
{
	@Autowired
	private MockMvc mvc;

	@MockBean
	private EmployeeManagementService service;

	@Test
	public void shouldReturnStatusOKAndEmptyJsonArray_whenGetEmployees_givenEmptyEmployeeDTOs() throws Exception
	{
		given(service.getEmployeeList()).willReturn(new ArrayList<>());

		mvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void shouldReturnStatusOKAndJsonArray_whenGetEmployees_givenEmployeeDTOs() throws Exception
	{
		List<EmployeeDto> allEmployees = getEmployees();
		given(service.getEmployeeList()).willReturn(allEmployees);

		mvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].fullName", is(allEmployees.get(0).getFullName())));
	}

	@Test
	public void shouldReturnStatusOKAndJsonObject_whenGetEmployeeByUuid_givenEmployeeDTO() throws Exception
	{
		EmployeeDto dto = getEmployeeDto();
		given(service.getEmployeeByUuid(anyString())).willReturn(dto);

		mvc.perform(get("/employees/"+dto.getUuid()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", aMapWithSize(5)))
			.andExpect(jsonPath("$.fullName", is(dto.getFullName())));
	}

	@Test
	public void shouldReturnStatus401_whenInsertEmployee_givenEmployeeDTO_withoutAuthentication() throws Exception
	{
		EmployeeDto dto = getEmployeeDto();
		given(service.insert(any())).willReturn(dto);

		mvc.perform(put("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getRequestJsonBody(dto)))
			.andExpect(status().is(401));
	}

	@Test
	@WithMockUser(username= "admin", password = "{noop}password", roles = "ADMIN")
	public void shouldReturnStatus400_whenInsertEmployee_givenEmployeeDTOWithInvalidFormat_withAuthentication() throws Exception
	{
		EmployeeDto dto = getEmployeeDtoWithInvalidFormat();
		given(service.insert(any())).willReturn(dto);

		mvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getRequestJsonBody(dto)))
			.andExpect(status().is(400));
	}

	@Test
	@WithMockUser(username= "admin", password = "{noop}password", roles = "ADMIN")
	public void shouldReturnStatusOKAndJsonObject_whenInsertEmployee_givenEmployeeDTO_withAuthentication() throws Exception
	{
		EmployeeDto dto = getEmployeeDto();
		given(service.insert(any())).willReturn(dto);

		mvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getRequestJsonBody(dto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", aMapWithSize(5)))
			.andExpect(jsonPath("$.fullName", is(dto.getFullName())));
	}

	@Test
	@WithMockUser(username= "admin", password = "{noop}password", roles = "ADMIN")
	public void shouldReturnStatusOK_whenUpdateEmployee_givenEmployeeDTO_withAuthentication() throws Exception
	{
		EmployeeDto dto = getEmployeeDto();
		given(service.insert(any())).willReturn(dto);

		mvc.perform(put("/employees/"+dto.getUuid())
				.contentType(MediaType.APPLICATION_JSON)
				.content(getRequestJsonBody(dto)))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username= "admin", password = "{noop}password", roles = "ADMIN")
	public void shouldReturnStatusOK_whenDeleteEmployee_givenUuid_withAuthentication() throws Exception
	{
		EmployeeDto dto = getEmployeeDto();
		given(service.insert(any())).willReturn(dto);

		mvc.perform(delete("/employees/"+dto.getUuid())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	private List<EmployeeDto> getEmployees()
	{
		List<EmployeeDto> dtos = new ArrayList<>();
		dtos.add(getEmployeeDto());
		dtos.add(getEmployeeDto());
		return dtos;
	}

	private String getRequestJsonBody(EmployeeDto dto) throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return  ow.writeValueAsString(dto);
	}
	private EmployeeDto getEmployeeDto()
	{
		return EmployeeDto.builder()
			.uuid("1234567890")
			.email("testemail@gmail.com")
			.fullName("Test Name")
			.birthday("2022-05-22")
			.hobbies(new ArrayList<>(Arrays.asList("travel", "music", "movies")))
			.build();
	}

	private EmployeeDto getEmployeeDtoWithInvalidFormat()
	{
		return EmployeeDto.builder()
			.uuid("1234567890")
			.email("testemail@gmail")
			.fullName("Test Name")
			.birthday("2222-11-11222")
			.hobbies(new ArrayList<>(Arrays.asList("travel", "music", "movies")))
			.build();
	}
}
