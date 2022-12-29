package group_id;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import java.io.File;
import javax.print.DocFlavor.STRING;

public class JiraTest {

  public static void main(String[] args) {
    RestAssured.baseURI = "http://localhost:8080";

    //1) Login to JIRA to create Session using LOGIN API

    // create Session

    SessionFilter session = new SessionFilter(); // ovo koristim uvemsto JsonPath-a

    // Obzirom da smo ceo 'response' ubacili u String, postoje dva nacina kako da dodjem do SessionId-ja
    // I nacin bi bio preko JsonPath klase   =>     JsonPath jsn = new JsonPath(response)
    //  II nacin je ovde primenjen preko   SessionFilter-a  =>  slusa kada se kreira nova sesija i hvata podatke

    String response = given()
      .header("Content-Type", "application/json")
      .pathParam("Key", "10101")
      .log()
      .all()
      .body(
        "{\r\n    \"username\": \"fred\",\r\n    " +
        "      \"password\": \"freds_password\"\r\n      }"
      )
      .log()
      .all()
      .filter(session) // OVO JE TRENUTNA SESIJA koju posle svuda koristim
      .when()
      .post("/rest/auth/1/session")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .asString();

    // 2. Adding a coment to existing issue in JIRA    using   ADD COMMENT API
    // a da bi do mogao, treba mi session ID, zato koristim FILTER SESSION

    given()
      .pathParam("Key", "10101")
      .log()
      .all()
      .header("Content-Type", "application/json")
      .body(
        "{\r\n        \"body\": \"This is mine first comment\",\r\n   " +
        "     \"visibility\": {\r\n            \"type\": \"role\",\r\n     " +
        "       \"value\": \"Administrators\"\r\n        }\r\n    }"
      )
      .filter(session) //evo je ovde
      .when()
      .post("/rest/api/2/issue/{Key}/comment")
      .then()
      .log()
      .all()
      .assertThat()
      .statusCode(201);
    // 3. Add attachment to existing issue using ADD ATTACHMENT

    // primer kako se salje atachment u JIRI   =>            curl -D- -u admin:admin -X POST -H "X-Atlassian-Token: no-check" -F "file=@myfile.txt" http://myhost/rest/api/2/issue/TEST-123/attachments

    given()
      .header("X-Atlassian-Token", "no-check")
      .filter(session)
      .pathParam("Key", "10101")
      .header("Content-Type", "multipart/form-data")
      .multiPart("file", new File("jira.txt"))
      .when()
      .post("/rest/api/2/issue/{Key}/attachments")
      .then()
      .log()
      .all()
      .assertThat()
      .statusCode(200);

    //  4. Get issue using GET ISSUE API

    String issueDetails = given()
      .filter(session)
      .pathParam("Key", "10101") // sa ovim dobijamo ceo response vezano za taj issue
      .queryParam("fields", "comment") // ovo je ako hocu da izvucem samo komentare
      .when()
      .get("/rest/api/2/issue/{Key}")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .asString();
  }
}
