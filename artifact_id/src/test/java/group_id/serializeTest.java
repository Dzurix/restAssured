package group_id;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;  //ovo mora da se importuje posebno
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.Location;
import pojo.addPlace;

public class serializeTest {
	
	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		addPlace p = new addPlace();
		
		
		p.setAccuracy(50);
		p.setAddress("vase pusibrka 16");
		p.setLanguage("Serbian");
		p.setPhone_number("+381650650652");
		p.setWebsite("https://assess.rs");
		p.setName("ses");
		
		//setovanje tipova
		List <String> myList = new ArrayList <String>();
		myList.add("ses12");
		myList.add("ses123");
		p.setTypes(myList);
		
		
		//setovanje lokacije
		Location loc = new Location();		
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		p.setLocation(loc);
		
		
		
		Response res = given().queryParam("key", "qaclick123").body(p)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response();
		
		
		String responseString =  res.asString();
		System.out.println(responseString);
		
	}



}
