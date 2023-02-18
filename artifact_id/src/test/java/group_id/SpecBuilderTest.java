package group_id;

import static io.restassured.RestAssured.given; //ovo mora da se importuje posebno

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.ArrayList;
import java.util.List;
import pojo.Location;
import pojo.addPlace;

public class SpecBuilderTest {

  public static void main(String[] args) {
    RestAssured.baseURI = "https://rahulshettyacademy.com";

    addPlace p = new addPlace();

    p.setAccuracy(50);
    p.setAddress("vase pusibrka 16");
    p.setLanguage("Serbian");
    p.setPhone_number("+381650650652");
    p.setWebsite("https://assess.rs");
    p.setName("ses");

    //setovanje tipova
    List<String> myList = new ArrayList<String>();
    myList.add("ses12");
    myList.add("ses123");
    p.setTypes(myList);

    //setovanje lokacije
    Location loc = new Location();
    loc.setLat(-38.383494);
    loc.setLng(33.427362);
    p.setLocation(loc);

    // kreiranje REQUEST
    RequestSpecification req = new RequestSpecBuilder()
      .setBaseUri("https://rahulshettyacademy.com")
      .addQueryParam("key", "qaclick123")
      .setContentType(ContentType.JSON)
      .build();

    // kreiranje RESPONSA
    ResponseSpecification resSpec = new ResponseSpecBuilder()
      .expectStatusCode(200)
      .expectContentType(ContentType.JSON)
      .build();

    RequestSpecification res = given().spec(req).body(p); //odvajamo body

    Response response = res
      .when()
      .post("/maps/api/place/add/json")
      .then()
      .spec(resSpec)
      .extract()
      .response();

    String responseString = response.asString();
    System.out.println(responseString);
  }
}
