package Files;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class dinamicJson {

  @Test(dataProvider = "BooksData")
  public void addBook(String isbn, String aisle) {
    RestAssured.baseURI = "http://216.10.245.166";

    String response = given()
      .header("Content-Type", "application/json")
      .body(payload.Addbook(isbn, aisle))
      .when()
      .post("/Library/Addbook.php")
      .then()
      .log()
      .all()
      .assertThat()
      .statusCode(200)
      .extract()
      .response()
      .asString();

    JsonPath js = reusableMethods.rawToJson(response);

    String id = js.get("ID");
    System.out.println(id);
  }

  @DataProvider(name = "BooksData")
  public Object[][] getData() {
    //array - collection of elements
    // multidimensional array - collection of arrays
    return new Object[][] {
      { "abdc", "123" },
      { "defr", "4567" },
      { "yrss", "7886" },
    };
  }
}
