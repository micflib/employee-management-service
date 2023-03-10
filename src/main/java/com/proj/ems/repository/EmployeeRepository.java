package com.proj.ems.repository;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.proj.ems.domain.EmployeeDocument;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeDocument, String>
{
	@CacheEvict(cacheNames = "Employees", key = "#entity.uuid")
	EmployeeDocument save(EmployeeDocument entity);

	@CacheEvict(cacheNames = "Employees", key = "#uuid")
	void deleteById(String uuid);

	@Cacheable(cacheNames = "Employees", key = "#uuid", unless="#result == null")
	Optional<EmployeeDocument> findById(String uuid);

	Iterable<EmployeeDocument> findAll();
}
