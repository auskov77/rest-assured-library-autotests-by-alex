package ru.buttonone.repository;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.buttonone.domain.Book;
import static ru.buttonone.SomeApiConstants.*;

import static io.restassured.RestAssured.given;

@DisplayName("Проверка API тестов методов AuthorRepository")
@SpringBootTest
public class AuthorRepositoryTests {

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DisplayName(" получить id автора по fio из БД")
    @Test
    public void shouldHaveCorrectGetAuthorById(){
        ValidatableResponse validatableResponse = given()
                .when()
                .get(API_BOOKS + "/" + CORRECT_ID_1)
                .then()
                .statusCode(STATUS_COD_200);

        Book book = validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", Book.class);
        String bookAuthors = book.getAuthors();
        long actualId = authorRepository.getAuthorIdByAuthorFio(bookAuthors).get(0).getId();

        Assertions.assertEquals(1, actualId);
    }

}