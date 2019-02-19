# OfferDataManagement

This is a "microservice" application that provide REST API for Create, Read, List and Delete Offers for Product according to specify Functional Business Rules.

# Technology Stack

 - Spring Boot 2.1.3
 - Spring MVC
 - OpenAPI-Generator
 - SpringfoxSwagger2
 - Spring Data JPA
 - Hibernate
 - H2 in memory database
 - mapstruct
 - Maven
 - MockMvc 
 - Spring Scheduling
 
# API-First development using openapi-generator

[OpenAPI-Generator]() is configured for this application. You can generate API code from the `src/main/resources/openapi/OfferDataManagement.yaml` definition file by running:

```bash
mvn generate-sources
```


# Quick start
Below all the commands to clone, build and run the project with Maven and Java 8 JDK:

	- git clone https://github.com/giovannizarola/offerdatamanagement.git
	
	- cd offerdatamanagement
	
	- mvn clean install
	
	- java -jar target/offerdatamanagement-1.0.jar
	
	- the embedded servlet container starts at http://localhost:8080
	
	- swagger-ui is available at http://localhost:8080/swagger-ui.htm
	
	- H2 console available at http://localhost:8080/h2-console
	
# Running a POST Offer

	- URL is http://localhost:8080/v1/offers   
	
JSON examples to POST a movie
```
{  
  "description": "string",
  "offer_code": "00001",
  "offer_price": "100",
  "product_code": "001",
  "start_date": "2019-02-18",
  "end_date": "2019-02-25",
  "status": "ACTIVE"
}
```

To create a new Offer respect the following rule:
1) start_date must be today
2) end_date must be bigger then start_date
3) offer_code must be unique
4) is not possible to have more then one offer active for the same product
5) a job runs every day at 3:00AM and updates the status of the offers to EXPIRE which have end_date equals to yesterday


For the other operations open the swagger-ui

 
	