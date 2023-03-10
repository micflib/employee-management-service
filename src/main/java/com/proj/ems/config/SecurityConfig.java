package com.proj.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	private static final String[] AUTH_WHITELIST = { "/swagger-ui/*" };
	private static final String ACCESS_ROLE = "ADMIN";

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.httpBasic()
			.and().authorizeRequests()
			.antMatchers(HttpMethod.POST).hasRole(ACCESS_ROLE)
			.antMatchers(HttpMethod.DELETE).hasRole(ACCESS_ROLE)
			.antMatchers(HttpMethod.PUT).hasRole(ACCESS_ROLE)
			.antMatchers(HttpMethod.PATCH).hasRole(ACCESS_ROLE)
			.and().csrf().disable();
	}

	@Override
	public void configure(WebSecurity webSecurity)
	{
		webSecurity.ignoring().antMatchers(AUTH_WHITELIST);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication().withUser("admin").password("{noop}password").roles(ACCESS_ROLE);
	}
}
