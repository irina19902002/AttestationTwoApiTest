import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    static Faker faker;
    static final String AUTH_KEY = "a3d9f38cc5a0b4906112af7d6958debc";

    @BeforeAll
    public static void setBaseURI() {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5";
        Faker faker = new Faker();
    }
}
