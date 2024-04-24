package com.universityApi.test.university_api_tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UniversityApiTests {
	@BeforeClass
	public void setup() {
		baseURI = "http://127.0.0.1:4010";
	}

	// API Key
	private static final String API_KEY = "f3c84cbb-1f9a-4b87-bb5b-2d1691b24e1e";

	@Test
	public void getUniversity() {
		given().header("api_key", API_KEY).queryParam("universityName", "University of Toronto").when()
				.get("/university").then().log().all().statusCode(200)
				.body("UniversityName", equalTo("University of Toronto"));
	}

	@Test
	public void createNewUniversity() {
		given().header("api_key", API_KEY).contentType("application/json").body(
				"{ \"UniversityName\": \"University of Waterloo\", \"UniversityLocation\": \"Waterloo, Ontario\", \"UniversityFounded\": 2023 }")
				.when().post("/university").then().statusCode(201);
	}

	@Test
	public void deleteUniversity() {
		given().header("api_key", API_KEY).queryParam("universityName", "University of Toronto").when()
				.delete("/university").then().statusCode(204).log().all();
	}


	@Test
	public void getUniversityById() {
		given().header("api_key", API_KEY).pathParam("universityID", 978).when().get("/university/{universityID}")
				.then().statusCode(200).body("UniversityID", equalTo(978));
	}

	@Test
	public void updateUniversityById() {
		given().header("api_key", API_KEY).pathParam("universityID", 978).contentType("application/json").body(
				"{ \"UniversityName\": \"University of California\", \"UniversityLocation\": \"California\", \"UniversityFounded\": 1977 }")
				.when().put("/university/{universityID}").then().statusCode(201);
	}

	
	@Test
	public void getAllUniversities() {
		given().header("api_key", API_KEY).when().get("/universities").then().statusCode(200);
	}

	
	@Test
	public void getUniversityInvalidRequest() {
		given().header("api_key", API_KEY).when().get("/university").then().statusCode(422).body("message",
				equalTo("Missing parameter: universityName"));
	}

	@Test
	public void getUniversitiesUnauthorized() {
		given()
				
				.when().get("/universities").then().statusCode(401);
	}

	
	@Test
	public void testMissingAuth() {
		given()
				// Sending a request with incorrect API Key
				.header("api_key", "123sss").when().get("/university").then().statusCode(404);
	}

	
	@Test
	public void testUrlPathError() {
		given().header("api_key", API_KEY).when().get("/university/abcd").then().log().all().statusCode(404);
	}

	@Test
	public void testUnencodedParameter() {
		given().header("api_key", API_KEY).queryParam("universityName", "University of Toronto") 
				.when().get("/university").then().log().all().statusCode(200);
	}

}
