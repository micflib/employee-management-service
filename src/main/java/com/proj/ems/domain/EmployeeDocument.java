package com.proj.ems.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@Document("employees")
public class EmployeeDocument
{
	@Id
	private String uuid;

	@Field(Fields.EMAIL)
	@Indexed(unique = true)
	@NonNull
	private String email;

	@Field(Fields.FULL_NAME)
	@NonNull
	private String fullName;

	@Field(Fields.BIRTHDAY)
	@NonNull
	private String birthday;

	@Field(Fields.HOBBIES)
	@NonNull
	private List<String> hobbies;

	public String getEmailFieldName()
	{
		return Fields.EMAIL;
	}

	private static class Fields
	{
		private static final String EMAIL = "email";
		private static final String FULL_NAME = "name";
		private static final String BIRTHDAY = "birthday";
		private static final String HOBBIES = "hobbies";
	}
}
