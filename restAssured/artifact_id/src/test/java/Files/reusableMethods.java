package Files;

import io.restassured.path.json.JsonPath;

public class reusableMethods {

  public static JsonPath rawToJson(String response) {
    JsonPath jsn1 = new JsonPath(response);
    return jsn1;
  }
}
