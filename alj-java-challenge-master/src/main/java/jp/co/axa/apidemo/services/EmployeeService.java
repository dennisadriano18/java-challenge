package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import javassist.NotFoundException;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public Employee saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId) throws ResponseStatusException;

    public Employee updateEmployee(Employee employee, Long id) throws ResponseStatusException;
}