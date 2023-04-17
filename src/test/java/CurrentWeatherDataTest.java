import io.qameta.allure.AllureId;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CurrentWeatherDataTest extends BaseTest{
    RequestSpecification request;
    ResponseSpecification responseGet;

    @BeforeEach
    public void setRequest(){
        request = RestAssured.given();

        responseGet = RestAssured.expect().statusCode(200);
    }

   @Test
   @AllureId("1")
    public void shouldGetWeather(){
      Response response = request
              .param("q","Moscow")
              .param("id","2172797")
              .param("lat","35")
              .param("lon","139")
              .param("zip","95050")
              .param("appid", AUTH_KEY)
              .get("/weather");
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
       @AllureId("2")
       public void shouldGetWeatherWithCityUnknow() {   //Введено рандомное название города.
        Response response = request
                       .param("q","rtrr")
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
    @AllureId("3") //// Не смогла разобраться почему не совпадает bode с json из файла. ниже аналогичная работа с файлом., но в файле простой json  работает
    public void shouldGetWeatherJson() throws FileNotFoundException {
        File file = new File("src/test/resources/weather.json");
        FileReader reader  = new FileReader(file);
        Scanner sc = new Scanner(reader);
        StringBuilder json = new StringBuilder();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine());

        }
        JSONObject jsonObj = new JSONObject(json.toString());
        Response response = request
                .param("q","London")
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .log().body()
                .assertThat()
                .body(equalTo(jsonObj.toString()));

    }
    @Test
    @AllureId("4")
    public void shouldNotParamGet() throws FileNotFoundException { /// в запросе не заполнены необходимые поля
        File file = new File("src/test/resources/1.json");
        FileReader reader = new FileReader(file);
        Scanner sc = new Scanner(reader);
        StringBuilder json = new StringBuilder();
        while (sc.hasNextLine()) {
            json.append(sc.nextLine());
        }

        JSONObject jsonObj = new JSONObject(json.toString());
        Response response = request
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .log().body()
                .assertThat()
                .body(equalTo(jsonObj.toString()));
    }
    @Test
    @AllureId("5")
    public void shouldGetShema() {
        Response response = request
                .param("q","London")
                .param("id", "2172797")
                .param("lat", "35")
                .param("lon", "139")
                .param("zip", "95050")
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .assertThat()
                .log().body()
                .body(matchesJsonSchemaInClasspath("ShemaJson.json"));
    }
    @Test
    @AllureId("6")
    public void shouldGetWeatherNameCity() {

        Response response = request
                .param("q","London")
                .param("id", "2172797")
                .param("lat", "35")
                .param("lon", "139")
                .param("zip", "95050")
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .spec(responseGet)
                .assertThat()
                .log().body()
                .body("name",equalTo("London"));
    }
    @Test
    @AllureId("7")
    public void shouldGetWeatherNegativeCityID() {

        Response response = request
                .param("id", "====")
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .assertThat()
                .log().body()
                .body("cod",equalTo("400"),"message", equalTo("==== is not a city ID"));
    }
    @Test
    @AllureId("8") /// Тест падает с ошибкой. Но через swagger_если задать город русскими буквами, то отрабатывает.
    public void shouldGetWeatherNameRus() {
        Response response = request
                .param("q","Лондон")
                .param("appid", AUTH_KEY)
                .get("/weather");
        response
                .then()
                .spec(responseGet)
                .assertThat()
                .log().body()
                .body("name",equalTo("London"));
    }
    @Test
    @AllureId("9")
    public void shouldGetWeatherXML() {

        Response response = request
                .param("q", "France")
                .param("mode","xml")
                .param("appid", AUTH_KEY)
                .get("/weather");
        assertThat(response).extracting(
                Response::getContentType).isEqualTo("application/xml; charset=utf-8");
    }
    }