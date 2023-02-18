package group_id;

import static io.restassured.RestAssured.*;

import groovyjarjarantlr4.v4.analysis.LeftFactoringRuleTransformer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

public class EComerceAPItest {

  public static void main(String[] args) {
    // OVDE SMO naglasili URL i da sve saljemo u JSON formatu
    RequestSpecification req = new RequestSpecBuilder()
      .setBaseUri("https://rahulshettyacademy.com")
      .setContentType(ContentType.JSON)
      .build();

    LoginRequest loginRequest = new LoginRequest(); // kreiram objekat od POJO klase

    loginRequest.setUserEmail("dst1@test.com");
    loginRequest.setUserPassword("123Ses123");

    RequestSpecification reqLogin = given()
      .relaxedHTTPSValidation() // DA NE PROVERAVA SSL SERTIFIKATE
      .log()
      .all()
      .spec(req)
      .body(loginRequest); //GIVEN

    LoginResponse loginResponse = reqLogin
      .when()
      .post("/api/ecom/auth/login")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .as(LoginResponse.class); //WHEN  i kreiram objekat od  LoginResponse POJO

    String token = loginResponse.getToken();
    String userId = loginResponse.getUserId();

    System.out.println(token);
    System.out.println(userId);

    //ADD PRODUCT

    RequestSpecification addProductBaseReq = new RequestSpecBuilder()
      .setBaseUri("https://rahulshettyacademy.com")
      .addHeader("Authorization", token)
      .build();

    RequestSpecification reqAddProduct = given()
      .log()
      .all()
      .spec(addProductBaseReq)
      .param("productName", "Proizvod")
      .param("productAddedBy", userId)
      .param("productCategory", "fashion")
      .param("productSubCategory", "shirts")
      .param("productPrice", "10000")
      .param("productDescription", "Addias Originals")
      .param("productFor", "woman")
      .multiPart(
        "productImage",
        new File("/C:/Users/dsta/OneDrive - NovaLite/Desktop/960x0.jpg")
      ); //dodavanje slike

    String addProductResponse = reqAddProduct
      .when()
      .post("/api/ecom/product/add-product")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .asString();

    JsonPath js = new JsonPath(addProductResponse);
    String productId = js.get("productId");

    //CREATE ORDER

    RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
      .setBaseUri("https://rahulshettyacademy.com")
      .addHeader("Authorization", token)
      .setContentType(ContentType.JSON)
      .build();

    OrderDetail orderDetail = new OrderDetail();
    orderDetail.setCountry("India");
    orderDetail.setProductOrderedId(productId);

    List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
    orderDetailList.add(orderDetail);

    Orders orders = new Orders();

    orders.setOrders(orderDetailList);

    RequestSpecification createOrderReq = given()
      .log()
      .all()
      .spec(createOrderBaseReq)
      .body(orders);

    String responseAddOrder = createOrderReq
      .when()
      .post("/api/ecom/order/create-order")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .asString();

    System.out.println(responseAddOrder);

    // DELETE PRODUCT

    RequestSpecification deleteProdBaseReq = new RequestSpecBuilder()
      .setBaseUri("https://rahulshettyacademy.com")
      .addHeader("Authorization", token)
      .setContentType(ContentType.JSON)
      .build();

    RequestSpecification deleteProdReq = given()
      .log()
      .all()
      .spec(deleteProdBaseReq)
      .pathParam("productId", productId);

    String deleteProdResponse = deleteProdReq
      .when()
      .delete("/api/ecom/product/delete-product/{productId}")
      .then()
      .log()
      .all()
      .extract()
      .response()
      .asString();

    JsonPath js1 = new JsonPath(deleteProdResponse);
    String message = js1.get("message");

    Assert.assertEquals("Product Deleted Successfully", message);
  }
}
