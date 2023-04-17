import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
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
    public void shouldGetWeatherWithCityUnknow111() throws FileNotFoundException {
        File file = new File("src/test/resources/weather.json");
        FileReader reader  = new FileReader(file);
        Scanner sc = new Scanner(reader);
        StringBuilder json = new StringBuilder();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine());

        }
        System.out.println(json);
       // System.out.println(json.toString());
        JSONObject jsonObj = new JSONObject(json.toString());
        System.out.println(jsonObj);
        when()
                .get("https://api.openweathermap.org/data/2.5/weather?q=London&id=2172797&lat=35&lon=139&zip=95050&units=standard&lang=en&mode=json&appid=a3d9f38cc5a0b4906112af7d6958debc")
                .then()
                .log().body()
                .assertThat()
                .body(equalTo(jsonObj.toString()));

    }
    }
