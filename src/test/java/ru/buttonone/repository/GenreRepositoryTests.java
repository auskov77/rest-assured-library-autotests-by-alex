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
import static ru.buttonone.SomeApiConstants.*;

@DisplayName("Проверка API тестов методов GenreRepository")
@SpringBootTest
public class GenreRepositoryTests {

    @Autowired
    private GenreRepository genreRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @DisplayName(" получить id жанра по name из БД")
    @Test
    public void shouldHaveCorrectGetGenreById(){
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
        String bookGenre = book.getGenre();
        long actualId = genreRepository.getGenreIdByGenreName(bookGenre).get(0).getId();

        Assertions.assertEquals(1, actualId);
    }
}