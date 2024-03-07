package jp.co.axa.apidemo.controllers;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.ApiDemoApplication;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiDemoApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public static final String URL_TEMPLATE = "/api/v1/employees/";
	
	private static Employee employee;
	
	private static EmployeeService employeeService;
	
	@Before
	public void setup() throws Exception {
		employeeService = new EmployeeServiceImpl();
		employee = new Employee();
	}
	
	@Test
	public void testCreateEmployee_returnOkResponse() {
		String newEmployeeString = "{\n"
				+ "   \n"
				+ "    \"name\":\"Math Murdock\", \n"
				+ "    \"salary\": 20000,\n"
				+ "    \"department\": \"Math\"\n"
				+ "    \n"
				+ "}";
		try {
			mockMvc.perform(post(URL_TEMPLATE+"add")
		            .contentType(MediaType.APPLICATION_JSON).content(newEmployeeString))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.name", is("Math Murdock")))
		            .andExpect(jsonPath("$.salary", is(20000)))
		            .andExpect(jsonPath("$.department", is("Math")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateEmployee_returnBadResponse() {
		String newEmployeeString = "\n"
				+ "   \n"
				+ "    \"name\":\"Math Murdock\", \n"
				+ "    \"salary\": 20000,\n"
				+ "    \"department\": \"TEST\"\n"
				+ "    \n";
		try {
			mockMvc.perform(post(URL_TEMPLATE+"add")
		            .contentType(MediaType.APPLICATION_JSON).content(newEmployeeString))
		            .andExpect(status().is(400));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	@Test
	public void testUpdateEmployee_returnOkResponse() {
		String addEmployeeString = "{\n"
				+ "   \n"
				+ "    \"name\":\"Math Murdock\", \n"
				+ "    \"salary\": 20000,\n"
				+ "    \"department\": \"Math\"\n"
				+ "    \n"
				+ "}";
		
		String updatedEmployeeString = "{\n"
				+ "   \n"
				+ "    \"name\":\"Mathias Murdock\", \n"
				+ "    \"salary\": 30000,\n"
				+ "    \"department\": \"Math\"\n"
				+ "    \n"
				+ "}";
		try {
			// Create Employee before update
			mockMvc.perform(post(URL_TEMPLATE+"add")
		            .contentType(MediaType.APPLICATION_JSON).content(addEmployeeString))
		            .andExpect(status().is(200));

			// Update created employee
			mockMvc.perform(put(URL_TEMPLATE+"updateById/{employeeId}", 1)
		            .contentType(MediaType.APPLICATION_JSON).content(updatedEmployeeString))
					.andExpect(status().is(200));
		    
			// Check if update took effect
			mockMvc.perform(get(URL_TEMPLATE+"{employeeId}", 1))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.id", is(1)))
					.andExpect(jsonPath("$.name", is("Mathias Murdock")))
					.andExpect(jsonPath("$.salary", is(30000)))
					.andExpect(jsonPath("$.department", is("Math")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateEmployee_returnBadResponse() {
		
		String updatedEmployeeString = "{\n"
				+ "   \n"
				+ "    \"name\":\"Mathias Murdock\", \n"
				+ "    \"salary\": 30000,\n"
				+ "    \"department\": \"Math\"\n"
				+ "    \n"
				+ "}";
		try {

			// Update non-existing employee
			mockMvc.perform(put(URL_TEMPLATE+"updateById/{employeeId}", 9999)
		            .contentType(MediaType.APPLICATION_JSON).content(updatedEmployeeString))
					.andExpect(status().isNotFound());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
    public void testDeleteEmployee_returnBadResponse() {
		try {
			// Delete non-existing employee
			mockMvc.perform(delete(URL_TEMPLATE+"deleteById/{employeeId}", 9999))
				.andExpect(status().isNotFound());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
    }


}
