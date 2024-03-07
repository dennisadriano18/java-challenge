package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
    @Autowired
    private EmployeeRepository employeeRepository;

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    // Use cache in case same employeeId is searched
    @Cacheable(cacheNames="employees", key = "#employeeId")
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        if(!optEmp.isPresent()) {
        	logger.warn("EmployeeServiceImpl>getEmployee> employeeId {} not found", employeeId);
        	return null;
        }
        return optEmp.get();
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    // Delete from cache
    @CacheEvict(value = "employees", key = "#employeeId")
    public void deleteEmployee(Long employeeId) throws ResponseStatusException{
    	Employee empFromRecord = getEmployee(employeeId);
    	if(empFromRecord == null) {
    		throw new ResponseStatusException(
    	      		  HttpStatus.NOT_FOUND, "Unable to locate employee with id: " + employeeId
    	      		);
    	}
    	employeeRepository.deleteById(employeeId);
    }

    // Delete from cache
    @CacheEvict(value = "employees", key = "#id")
    public Employee updateEmployee(Employee employee, Long id) throws ResponseStatusException {
    	Employee empFromRecord = getEmployee(id);
        if(empFromRecord != null){
            empFromRecord.setDepartment(employee.getDepartment());
            empFromRecord.setName(employee.getName());
            empFromRecord.setSalary(employee.getSalary());
            return employeeRepository.save(empFromRecord);
        }
        logger.warn("EmployeeServiceImpl>updateEmployee> employeeId {} not found", id);
        throw new ResponseStatusException(
        		  HttpStatus.NOT_FOUND, "Unable to locate employee with id: " + id
        		);
    }
}