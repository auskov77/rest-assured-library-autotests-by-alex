package ru.buttonone.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.buttonone.domain.Book;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

@DisplayName("Проверка API тестов методов BookRepository:")
@SpringBootTest
public class BookRepositoryTests {

    public static final String BASE_URL = "http://localhost:8080";
    public static final int STATUS_COD_200 = 200;
    public static final String API_BOOKS_ADD = "/api/books/add";
    public static final String TEST_GET_A_1 = "test_get_a1";
    public static final String TEST_GET_G_1 = "test_get_g1";
    public static final String TEST_GET_T_1 = "test_get_t1";
    public static final long TEST_ID_1 = 1;
    public static final String TEST_GET_A_2 = "test_get_a2";
    public static final String TEST_GET_G_2 = "test_get_g2";
    public static final String TEST_GET_T_2 = "test_get_t2";
    public static final long TEST_ID_2 = 2;
    public static final String API_BOOKS = "/api/books";

    @Autowired
    BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeTestMethod
    public void insertBookById_2() throws JsonProcessingException {
        Book expectedBook2 = new Book(
                TEST_ID_2,
                TEST_GET_A_2,
                TEST_GET_G_2,
                TEST_GET_T_2);
        String jsonExpectedBook2 = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook2);

        given()
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook2)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_COD_200);
    }

    @AfterTestMethod
    public void deleteTestBookById() {
        var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_GET_T_1).get(0).getId();

        given()
                .when()
                .delete(API_BOOKS + "/" + deleteBookId)
                .then()
                .statusCode(STATUS_COD_200);

    }

    @DisplayName(" после добавления книга появляется в БД")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBook() throws JsonProcessingException {
        Book expectedBook1 = new Book(
                TEST_ID_1,
                TEST_GET_A_1,
                TEST_GET_G_1,
                TEST_GET_T_1);
        String jsonExpectedBook1 = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedBook1);

        given()
                .header(new Header("Content-Type", "application/json"))
                .body(jsonExpectedBook1)
                .when()
                .post(API_BOOKS_ADD)
                .then()
                .statusCode(STATUS_COD_200);

        Book actualBook = bookRepository.getBooksByTitle(TEST_GET_T_1).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(TEST_GET_A_1, actualBook.getAuthors()),
                () -> Assertions.assertEquals(TEST_GET_G_1, actualBook.getGenre()),
                () -> Assertions.assertEquals(TEST_GET_T_1, actualBook.getTitle())
        );
    }

    @DisplayName(" получить все книги из БД")
    @Test
    public void shouldHaveCorrectGetAllBooks() {
        ValidatableResponse validatableResponse = given()
                .when()
                .get(API_BOOKS)
                .then()
                .statusCode(STATUS_COD_200);

        List<Book> bookList = validatableResponse
                .extract()
                .body()
                .jsonPath().getList("", Book.class);

        assertThat(bookList, Matchers.contains(
                new Book(bookRepository.getBookIdByBookTitle("t1").get(0).getId(), "a1", "g1", "t1"),
                new Book(bookRepository.getBookIdByBookTitle(TEST_GET_T_1).get(0).getId(), TEST_GET_A_1, TEST_GET_G_1, TEST_GET_T_1)
        ));
        deleteTestBookById();
    }

    @DisplayName(" удалить книгу по id из БД")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertBookById_2();

        var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_GET_T_2).get(0).getId();

        given()
                .when()
                .delete(API_BOOKS + "/" + deleteBookId)
                .then()
                .statusCode(STATUS_COD_200);

        List<Book> booksById = bookRepository.getBooksById(deleteBookId);

        Assertions.assertEquals("[]", booksById.toString());
    }

    @DisplayName(" получить книгу по id из БД")
    @Test
    public void shouldHaveCorrectGetBookById() {

        ValidatableResponse validatableResponse =
                given()
                        .header(new Header("Content-Type", "application/json"))
                        .when()
                        .get(API_BOOKS + "/" + TEST_ID_1)
                        .then()
                        .statusCode(STATUS_COD_200);

        var book = validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", Book.class);

        Assertions.assertEquals(book, new Book(1, "a1", "g1", "t1"));
    }
}
