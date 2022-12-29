package group_id;

import static io.restassured.RestAssured.*; // OVO MORA RUCNO DA SE IMPORTUJE
import static org.hamcrest.Matchers.*; // i ovo

import Files.payload;
import Files.reusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.Assert;

public class Basics {

  public static void main(String[] args) throws IOException {
    RestAssured.baseURI = "https://rahulshettyacademy.com";

    // vezbanje =>  1) ADD new place, 2) Update place with new adress, 3)Get place to validate if new adress is present in reponse

    //          1)  ADD NEW PLACE
    String response = given()
      .log()
      .all()
      .queryParam("key", "qaclick123")
      .header("Content-Type", "application/json")
      //.body(payload.AddPlace()) // ovde smo izbacili JSON u drugi fajl 'payload.java' i importovali ga

      //ovde cemo direkto koristiti JSON file
      //logika je sledeca->  pretvaramo content fajla u Byte-> pretvaramo Bytes u STRING
      .body(
        new String(
          Files.readAllBytes(
            Paths.get(
              "D:\\cy\\restAssured\\artifact_id\\src\\test\\java\\Files\\addPlace.json"
            )
          )
        )
      )
      .when()
      .post("maps/api/place/add/json")
      .then()
      .assertThat()
      .statusCode(200)
      .body("scope", equalTo("APP"))
      .header("Server", "Apache/2.4.41 (Ubuntu)")
      .extract()
      .response()
      .asString();

    System.out.println(response);

    JsonPath jsn = new JsonPath(response); // ova klasa uzima String kao input i konvertuje ga u JSON i parsuje ga

    String placeId = jsn.getString("place_id");

    System.out.println(placeId);
    //  2)  UPDATE PLACE with NEW adress

    String newAddress = "Upoja IV upojevica br. U";

    given()
      .log()
      .all()
      .queryParam("key", "qaclick123")
      .header("Content-Type", "application/json")
      .body(
        "{\r\n   \r\n      \"place_id\": \"" +
        placeId +
        "\",\r\n      \"address\": \"" +
        newAddress +
        "\",\r\n      \"key\": \"qaclick123\"\r\n     \r\n  }"
      )
      .when()
      .put("maps/api/place/update/json")
      .then()
      .assertThat()
      .log()
      .all()
      .statusCode(200)
      .body("msg", equalTo("Address successfully updated"));

    // GET place to validate change of adress

    String getPlaceResponse = given()
      .log()
      .all()
      .queryParam("key", "qaclick123")
      .queryParam("place_id", placeId)
      .when()
      .get("maps/api/place/get/json")
      .then()
      .assertThat()
      .log()
      .all()
      .statusCode(200)
      .extract()
      .response()
      .asString();

    JsonPath jsn1 = reusableMethods.rawToJson(getPlaceResponse); //optimizacija kreiranjem posebnog fajla 'reusableMethods.java'
    String actualAddress = jsn1.getString("address");

    System.out.println(actualAddress);
    // testNG assertions
    //  Assert.assertEquals(actualAddress, newAddress);
  }
}
