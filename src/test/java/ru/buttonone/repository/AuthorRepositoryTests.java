package ru.buttonone.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
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

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeEach
    public void insertTestBookById() throws JsonProcessingException {
        Book expectedBook = new Book(
                TEST_ID_1,
                TEST_A_1,
                TEST_G_1,
                TEST_T_1
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
        long deletedIdBook = bookRepository.getBookIdByBookTitle(TEST_T_1).get(0).getId();
        given()
                .when()
                .delete(API_BOOKS + "/" + deletedIdBook)
                .then()
                .statusCode(STATUS_COD_200);
    }

    @DisplayName(" получить автора по id из БД")
    @Test
    public void shouldHaveCorrectGetAuthorById() {
        long actualIdBook = bookRepository.getBookIdByBookTitle(TEST_T_1).get(0).getId();
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
        String bookAuthors = book.getAuthors();
        String actualFio = authorRepository.getAuthorFromDbByBookId(actualIdBook).get(0).getFio();

        Assertions.assertEquals(bookAuthors, actualFio);
    }
}