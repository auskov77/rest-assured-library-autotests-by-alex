package ru.buttonone.repository;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.domain.BComments;

import static io.restassured.RestAssured.given;
import static ru.buttonone.SomeApiConstants.*;

@DisplayName("Проверка API тестов методов B_commentsRepository")
@SpringBootTest
public class BCommentsRepositoryTests {

    @Autowired
    private BCommentsRepository b_commentsRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DisplayName(" получить комментарии по id из БД")
    @Test
    public void shouldHaveCorrectGetCommentsById(){
        ValidatableResponse validatableResponse = given()
                .when()
                .get(API_BOOKS + "/" + CORRECT_ID_1 + "/comments")
                .then()
                .log().all()
                .statusCode(STATUS_COD_200);
        validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", BComments[].class);
        BComments actualB_comments = b_commentsRepository.getNicknameAndMessageByB_commentsById(CORRECT_ID_1).get(0);

        Assertions.assertAll(
                ()-> Assertions.assertEquals(1, actualB_comments.getId()),
                ()-> Assertions.assertEquals("1", actualB_comments.getBookId()),
                ()-> Assertions.assertEquals("nick1", actualB_comments.getNickname()),
                ()-> Assertions.assertEquals("m1", actualB_comments.getMessage())
        );
    }
}