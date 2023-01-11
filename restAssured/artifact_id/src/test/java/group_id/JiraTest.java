package group_id;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import java.io.File;
import org.testng.Assert;

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
      .relaxedHTTPSValidation() // OVO JE DA ZAOBIDJEMO HTTPS validaciju ->  uvek to dodajem
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

    String expectedMessage = "Hey, how are you?";

    String addCommentResponse = given()
      .pathParam("Key", "10101")
      .log()
      .all()
      .header("Content-Type", "application/json")
      .body(
        "{\r\n        \"body\": \"" +
        expectedMessage +
        "\",\r\n   " +
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
      .statusCode(201)
      .extract()
      .response()
      .asString(); //ovde extraktujem response da bi ga mogao izvuci ID od komentara koji mi treba u tacki 5.

    JsonPath js = new JsonPath(addCommentResponse);
    String commentID = js.getString("id");

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
    //5. Complex Jira Json response to retrieve the added Comment with code logic

    // sada proveravam ID od komentara koje sam dobio iz responsa is tacke 4.

    JsonPath js1 = new JsonPath(issueDetails);
    int commentsCounts = js1.get("fields.comment.comments.size()");

    for (int i = 0; i < commentsCounts; i++) {
      String commentIdIssue = js1
        .get("fields.comment.comments[" + i + "].id")
        .toString();

      if (commentIdIssue.equalsIgnoreCase(commentID)) {
        String message = js1
          .get("fields.comment.comments[" + i + "].body")
          .toString();

        System.out.println(message);

        //TEST NG ASSERTION
        Assert.assertEquals(message, expectedMessage);
      }
    }
  }
}
