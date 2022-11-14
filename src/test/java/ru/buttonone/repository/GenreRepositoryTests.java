package ru.buttonone.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
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

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    public void insertTestBookById() throws JsonProcessingException {
        Book expectedBook = new Book(
                TEST_ID_2,
                TEST_A_2,
                TEST_G_2,
                TEST_T_2
        );
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook);
        given()
                .header(HEADER)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_COD_200);
    }

    @AfterEach
    public void deleteTestBookById() {
        long deletedIdBook = bookRepository.getBookIdByBookTitle(TEST_T_2).get(0).getId();
        given()
                .when()
                .delete(API_BOOKS + "/" + deletedIdBook)
                .then()
                .statusCode(STATUS_COD_200);
    }

    @DisplayName(" получить жанр по id из БД")
    @Test
    public void shouldHaveCorrectGetGenreById(){
        long actualIdBook = bookRepository.getBookIdByBookTitle(TEST_T_2).get(0).getId();
                ValidatableResponse validatableResponse = given()
                .when()
                .get(API_BOOKS + "/" + actualIdBook)
                .then()
                .statusCode(STATUS_COD_200);
        Book book = validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", Book.class);
        String bookGenre = book.getGenre();
        String actualName = genreRepository.getGenreFromDbByBookId(actualIdBook).get(0).getName();

        Assertions.assertEquals(bookGenre, actualName);
    }
}