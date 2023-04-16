import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class ApplicationTests {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5";
}
