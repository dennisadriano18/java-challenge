# AXA Take Home Coding Exam

Applicant: Dennis R. Adriano

Description: This is a webservice application which performs the basic CRUD functions using Springboot and Java 8. 

NOTE: YOU NEED TO GENERATE A VALID ACCESS TOKEN TO ACCESS ANY OF THE SERVICE ENDPOINTS!!!

## Features Added

1. Added Swagger Implementation for documentation.
2. Added JUnit Tests.
3. Added Spring Security thru JWT Authentication.
4. Added JWT Endpoint to create token.
5. Added lomboc library.
6. Fixed endpoints request protocol.
7. Removed redundant methods, codes. 

## Swagger Implementation

```bash
http://localhost:8080/swagger-ui/index.html
```

## How To Use

NOTE: YOU NEED TO GENERATE A VALID ACCESS TOKEN TO ACCESS ANY OF THE SERVICE ENDPOINTS!!!

** Start the spring application.

** Generate an access token by creating a post request to:

```bash
http://localhost:8080/api/v1/authenticate
```
** With the json below:

```python
{
    "username": "admin",
    "password": "password"
}
```
** An access token will be generated:

```python
**This is only a sample
{
    "jwtToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwOTgxNjU5MSwiZXhwIjoxNzA5ODUyNTkxfQ.wf9uylDP8wDXy9GU90jU2ZC9IvTldpYi6fzW46T-HmL6AKB4gogkOQjExNmgol4JQu5i4E3G1jxNgM1hcp9AEw"
}
```

** Copy the [jwtToken] value and add this as a BEARER token to all your request to any of the endpoints below:

```bash
http://localhost:8080/api/v1/employees
http://localhost:8080/api/v1/employees/add
http://localhost:8080/api/v1/employees/updateById/{employeeId}
http://localhost:8080/api/v1/employees/deleteById/{employeeId}
```
** Please refer to the swagger-ui for the required request format and parameters for each of the service endpoints. 


## Comments

If I have more time, I would have added the following features:

1. Added more test cases for the junit test.
2. Applied Roles filters in accessing endpoints to add security. 
3. Used better encoding for username and password. 
4. removed any hard coded value and moved to properties file.

## Experiences in Java

1. I have been using Java for more or less 10 years.
2. I was able to use different frameworks like Struts, Spring, JSF. 
3. I was able to use different databases like MySQL, MSSQL, Postgres, DB2, Oracle etc. 
4. Thru the years, I have been switching with different work roles, from Frontend, to Backend to Production Support. 
5. For the past 2 years, I have been using Nodejs, React and Java. 
6. I have performed different testing procedures such as, unit testing, load testing, regression testing and performance testing.
