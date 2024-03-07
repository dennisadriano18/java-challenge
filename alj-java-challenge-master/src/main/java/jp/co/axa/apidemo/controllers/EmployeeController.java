package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.JwtRequest;
import jp.co.axa.apidemo.entities.JwtResponse;
import jp.co.axa.apidemo.services.CustomUserDetailsService;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.util.JWTUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JWTUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
    	logger.info("EmployeeController>getEmployees>retrieving employees");
        List<Employee> employees = employeeService.retrieveEmployees();
        logger.info("EmployeeController>getEmployees>employees count: {}", employees.size());
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
    	logger.info("EmployeeController>getEmployee>employeeId: {}", employeeId);
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping(value = "/employees/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Employee saveEmployee(@Valid @RequestBody Employee employee){
    	logger.info("EmployeeController>saveEmployee>add Employee");
        return employeeService.saveEmployee(employee);
    }

    @DeleteMapping("/employees/deleteById/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
    	logger.info("EmployeeController>deleteEmployee>delete employeeId: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        logger.info("EmployeeController>deleteEmployee>employee successfully deleted");
    }

    @PutMapping("/employees/updateById/{employeeId}")
    public Employee updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
    	logger.info("EmployeeController>updateEmployee>update employeeId: "+employeeId);
    	return employeeService.updateEmployee(employee, employeeId);
       
    }
    
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials", e);
        }

        final UserDetails userDetails
                = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }

}
