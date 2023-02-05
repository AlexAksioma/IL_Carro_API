package rest;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthenticationBodyDto;
import dto.ErrorMessageDto;
import dto.TokenDto;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RestLoginTests {
    String token;

    @BeforeClass
    public void initClass(){
        RestAssured.baseURI = "https://ilcarro-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void LoginPositiveTest(){
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
    }

    @Test
    public void LoginNegativeTest_WrongEmail(){
        AuthenticationBodyDto authenticationBodyDto = AuthenticationBodyDto.builder()
                .username("alexmedqwertygmail.com")
                .password("Qwerty12345!")
                .build();
        ErrorMessageDto errorMessageDto = given()
                .body(authenticationBodyDto)
                .contentType(ContentType.JSON)
                .when()
                .post("user/login/usernamepassword")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message", containsString("Login or Password incorrect"))
                .assertThat().body("error", containsString("Unauthorized"))
                .extract()
                .as(ErrorMessageDto.class)
                ;
        System.out.println(errorMessageDto.getError());
        System.out.println(errorMessageDto.getMessage().toString());
    }
}
