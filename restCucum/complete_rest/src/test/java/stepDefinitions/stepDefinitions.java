package stepDefinitions;

import static io.restassured.RestAssured.*; //OVO MORA RUCNO DA SE IMPORTUJE (ZA GIVEN)
import static org.junit.Assert.assertEquals;

import Resources.APIresources;
import Resources.TestDataBuild;
import Resources.Utils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pojo.Location;
import pojo.addPlace;

public class stepDefinitions extends Utils {

  RequestSpecification res;
  ResponseSpecification resSpec;
  Response response;
  TestDataBuild data = new TestDataBuild(); //kreiranje objekta od klase
 

  @Given("^Add Place Payload with \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
  public void add_place_payload_with_something_something_something(
    String name,
    String language,
    String address
  ) throws Throwable {
    res =
      given()
        .spec(requestSpecification())
        .body(data.addPlacePayload(name, language, address)); //odvajamo body}
  }

  @When("user calls {string} with {string} http request")
  public void user_calls_with_http_request(String resource, String method) {
       
	  
	  //constructor will be called with value of resource which you pass 
	APIresources resourceAPI =   APIresources.valueOf(resource); // OVO JE ENUM klasa
	System.out.println(resourceAPI.getResource());
	
    // kreiranje RESPONSA

    resSpec =
      new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
    
    if(method.equalsIgnoreCase("POST")) 
    response =
      res
        .when()
        .post(resourceAPI.getResource()); //OVDE JE SVRHA RADA SA ENUM klasama
    else if(method.equalsIgnoreCase("GET"))
    response =
    res
      .when()
      .get(resourceAPI.getResource());
    
    }

  @Then("^the API call got success with status code 200$")
  public void the_api_call_got_success_with_status_code_200() {
    assertEquals(response.getStatusCode(), 200);
  }

  @And("^\"([^\"]*)\" in response body is \"([^\"]*)\"$")
  public void something_in_response_body_is_something(
    String keyValue,
    String expectedValue
  ) {
   


    assertEquals(getJsonPath(response,keyValue), expectedValue);
  }
  
  @Then("verify place_Id created maps to {string} ising {string}")
  public void verify_place_id_created_maps_to_ising(String expectedName, String resource) throws IOException {
      
	  //requestSpec
	String place_id = getJsonPath(response,"place_id");
	  res.given().spec(requestSpecification()).queryParam("place_id", place_id);
	  user_calls_with_http_request(resource,"GET");
	  
	  String actualName = getJsonPath(response,"name");
	  
	  assertEquals(actualName, expectedName);
	  
	  
  }
}
