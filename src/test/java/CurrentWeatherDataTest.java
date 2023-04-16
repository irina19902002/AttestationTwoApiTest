import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.codehaus.groovy.control.io.FileReaderSource;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CurrentWeatherDataTest extends BaseTest{
    RequestSpecification request;

    @BeforeEach
    public void setRequest(){
        request = RestAssured.given();
    }

   @Test
    public void shouldGetWeather(){
      Response response = request
              .param("q","Moscow")
              .param("id","2172797")
              .param("lat","35")
              .param("lon","139")
              .param("zip","95050")
              .param("appid", AUTH_KEY)
              .get("/weather");
       //Assertions.assertEquals(200,response.statusCode());
       assertThat(response)
               .extracting(
                       Response::getContentType,
                       Response::getStatusCode
               ).containsExactly(
                       "application/json; charset=utf-8",
               200
               );
   }

       @Test
       public void shouldGetWeatherWithCityUnknow() {   //Введено рандомное название города. Который не соответсвует коды страны
               Response response = request
                       .param("q","rtrt")
                       .param("id", "2172797")
                       .param("lat", "35")
                       .param("lon", "139")
                       .param("zip", "95050")
                       .param("appid", AUTH_KEY)
                       .get("/weather");
               response.then()
                       .statusCode(404)
                       .body("cod", equalTo("404"),
                   "message", equalTo("city not found"));


           }
    @Test
    public void shouldGetWeatherWithCityUnknow111() {
        File file = new File("src/test/resources/1.json");
        FileReader reader  = new FileReader("src/test/resources/1.json");
        Scanner sc = new Scanner(reader);
        StringBuilder json = new StringBuilder();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine());
        }

        JSONObject jsonObj = new JSONObject(json.toString());
        when()
                .get("/weather",null,"2172797","35","139","95050", AUTH_KEY )
                .then()
                .log().body()
                .assertThat()
                .body(equalTo(jsonObj.toString()));
    }
       }