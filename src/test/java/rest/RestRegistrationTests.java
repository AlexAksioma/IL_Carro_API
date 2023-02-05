package rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ErrorMessageDto;
import dto.RegistrationBodyDto;
import dto.TokenDto;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RestRegistrationTests {
    @BeforeClass
    public void initClass(){
        RestAssured.baseURI = "https://ilcarro-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void registrationPositiveTest(){
        int i = new Random().nextInt(1000)+1000;
        RegistrationBodyDto registrationBodyDto = RegistrationBodyDto.builder()
                .firstName("Alex"+i)
                .lastName("ZXc"+i)
                .username("QwerttyZX"+i+"@gmail.com")
                .password(i+"Qwerty!_")
                .build()
                ;
         TokenDto tokenDto = given()
                .body(registrationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .as(TokenDto.class)
                ;
        System.out.println(tokenDto.toString());
    }

    @Test
    public void registrationNegativeTest_WrongEmail(){
        int i = new Random().nextInt(1000)+1000;
        RegistrationBodyDto registrationBodyDto = RegistrationBodyDto.builder()
                .firstName("Alex"+i)
                .lastName("ZXc"+i)
                .username("QwerttyZX"+i+"gmail.com")
                .password(i+"Qwerty!_")
                .build()
                ;
        ErrorMessageDto errorMessageDto = given()
                .body(registrationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error", containsString("Bad Request"))
                .extract()
                .as(ErrorMessageDto.class)
                ;
        System.out.println(errorMessageDto.getMessage().toString());
    }

    @Test
    public void registrationNegativeTest_DuplicateUser(){
        int i = new Random().nextInt(1000)+1000;
        RegistrationBodyDto registrationBodyDto = RegistrationBodyDto.builder()
                .firstName("Alex"+i)
                .lastName("ZXc"+i)
                .username("QwerttyZX"+i+"@gmail.com")
                .password(i+"Qwerty!_")
                .build()
                ;

        given()
                .body(registrationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                ;
        ErrorMessageDto errorMessageDto = given()
                .body(registrationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(409)
                .assertThat().body("message", containsString("User already exists"))
                .extract()
                .as(ErrorMessageDto.class)
                ;
        System.out.println(errorMessageDto.toString());
    }
}
