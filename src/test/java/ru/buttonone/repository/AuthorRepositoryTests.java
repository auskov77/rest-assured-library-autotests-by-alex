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

import static io.restassured.RestAssured.given;

@DisplayName("Проверка API тестов методов AuthorRepository")
@SpringBootTest
class AuthorRepositoryTests {
    public static final String BASE_URL = "http://localhost:8080";
    public static final int STATUS_COD_200 = 200;
    public static final String API_BOOKS = "/api/books";
    public static final int CORRECT_ID_1 = 1;

    @Autowired
    AuthorRepository authorRepository;

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


        Assertions.assertEquals(2, actualId);
    }

}