package stepDefinitions;

import static io.restassured.RestAssured.*; //OVO MORA RUCNO DA SE IMPORTUJE (ZA GIVEN)
import static org.junit.Assert.assertEquals;

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

  @Given("^Add Place Payload$")
  public void add_place_payload() throws IOException {
    res = given().spec(requestSpecification()).body(data.addPlacePayload()); //odvajamo body}
  }

  @When("^user calls \"([^\"]*)\" with Post http request$")
  public void user_calls_something_with_post_http_request(String strArg1) {
    // kreiranje RESPONSA

    resSpec =
      new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
    response =
      res
        .when()
        .post("/maps/api/place/add/json")
        .then()
        .spec(resSpec)
        .extract()
        .response();
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
    String resp = response.asString();
    JsonPath js = new JsonPath(resp);

    assertEquals(js.get(keyValue).toString(), expectedValue);
  }
}
