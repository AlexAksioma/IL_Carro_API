package rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;

public class RestAddNewCarTests {
    String token;

    @BeforeClass
    public void initClass(){
        RestAssured.baseURI = "https://ilcarro-backend.herokuapp.com";
        RestAssured.basePath = "v1";
        AuthenticationBodyDto authenticationBodyDto = AuthenticationBodyDto.builder()
                .username("alexmedqwerty@gmail.com")
                .password("Qwerty12345!")
                .build();
        TokenDto tokenDto = given()
                .body(authenticationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .as(TokenDto.class)
                ;
        token =  tokenDto.getAccessToken();
        System.out.println(token);
    }

    @Test
    public void addNewCarPositiveTest(){
        int i = new Random().nextInt(1000)+1000;
        CarDto carDto = CarDto.builder()
                .serialNumber(i+"abc")
                .manufacture("ford")
                .model("focus")
                .year("2000")
                .fuel("gas")
                .seats(4)
                .carClass("qqqq")
                .pricePerDay(123.123)
                .about("gefvsdvsd")
                .city("Haifa")
                .build();
        ResponseMessageDto responseMessageDto = given()
                .header("Authorization",token)
                .body(carDto)
                .contentType(ContentType.JSON)
                .when()
                .post("cars")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .as(ResponseMessageDto.class)
                ;
        System.out.println(responseMessageDto.getMessage());
    }

    @Test
    public void addNewCarNegativeTest_WrongDataCar(){
        int i = new Random().nextInt(1000)+1000;
        CarDto carDto = CarDto.builder()
                //.serialNumber(i+"abc")
                .manufacture("ford")
                .model("focus")
                .year("2000")
                .fuel("gas")
                .seats(4)
                .carClass("qqqq")
                .pricePerDay(123.123)
                .about("gefvsdvsd")
                .city("Haifa")
                .build();
        ErrorMessageDto errorMessageDto = given()
                .header("Authorization",token)
                .body(carDto)
                .contentType(ContentType.JSON)
                .when()
                .post("cars")
                .then()
                .assertThat().statusCode(400)
                .extract()
                .as(ErrorMessageDto.class)
                ;
        System.out.println(errorMessageDto.getMessage().toString());
    }
}
