package ru.buttonone.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.buttonone.domain.Book;
import java.util.List;
import static io.restassured.RestAssured.given;
import static ru.buttonone.SomeApiConstants.*;

@DisplayName("Проверка API тестов методов BookRepository:")
@SpringBootTest
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeTestMethod
    public void insertTestBookByIdFirstMethod() throws JsonProcessingException {
        Book expectedBook = new Book(
                TEST_ID_3,
                TEST_A_3,
                TEST_G_3,
                TEST_T_3
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

    @BeforeTestMethod
    public void insertTestBookByIdSecondMethod() throws JsonProcessingException {
        Book expectedBook = new Book(
                TEST_ID_4,
                TEST_A_4,
                TEST_G_4,
                TEST_T_4
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
    public void deleteTestBookByIdFirstMethod() {
        if ((bookRepository.getBookIdByBookTitle(TEST_T_3).size()) != 0) {
            var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_T_3).get(0).getId();
            given()
                    .when()
                    .delete(API_BOOKS + "/" + deleteBookId)
                    .then()
                    .statusCode(STATUS_COD_200);
        }
    }

    @AfterEach
    public void deleteTestBookByIdSecondMethod() {
        if ((bookRepository.getBookIdByBookTitle(TEST_T_4).size()) != 0) {
            var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_T_4).get(0).getId();
            given()
                    .when()
                    .delete(API_BOOKS + "/" + deleteBookId)
                    .then()
                    .statusCode(STATUS_COD_200);
        }
    }

    @DisplayName(" после добавления книга появляется в БД")
    @Test
    public void shouldHaveCorrectEntityInDbAfterAddingBook() throws JsonProcessingException {
        insertTestBookByIdFirstMethod();
        Book actualBook = bookRepository.getBooksByTitle(TEST_T_3).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(TEST_A_3, actualBook.getAuthors()),
                () -> Assertions.assertEquals(TEST_G_3, actualBook.getGenre()),
                () -> Assertions.assertEquals(TEST_T_3, actualBook.getTitle())
        );
        deleteTestBookByIdFirstMethod();
    }

    @DisplayName(" получить все книги из БД")
    @Test
    public void shouldHaveCorrectGetAllBooks() throws JsonProcessingException {
        ValidatableResponse validateResponseBeforeAddedBooks = given()
                .when()
                .get(API_BOOKS)
                .then()
                .statusCode(STATUS_COD_200);
        List<Book> bookListBeforeAdded = validateResponseBeforeAddedBooks
                .extract()
                .body()
                .jsonPath().getList("", Book.class);
        long sizeBookInDb = bookListBeforeAdded.size();
        insertTestBookByIdFirstMethod();
        insertTestBookByIdSecondMethod();
        long expectedSize = sizeBookInDb + 2;
        ValidatableResponse validateResponseAfterAddedBooks = given()
                .when()
                .get(API_BOOKS)
                .then()
                .statusCode(STATUS_COD_200);
        List<Book> bookListAfterAdded = validateResponseAfterAddedBooks
                .extract()
                .body()
                .jsonPath().getList("", Book.class);
        long actualSize = bookListAfterAdded.size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @DisplayName(" удалить книгу по id из БД")
    @Test
    public void shouldHaveCorrectDeleteBookById() throws JsonProcessingException {
        insertTestBookByIdFirstMethod();
        var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_T_3).get(0).getId();
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
    public void shouldHaveCorrectGetBookById() throws JsonProcessingException {
        insertTestBookByIdFirstMethod();
        var insertBookId = bookRepository.getBookIdByBookTitle(TEST_T_3).get(0).getId();
        ValidatableResponse validatableResponse =
                given()
                        .header(HEADER)
                        .when()
                        .get(API_BOOKS + "/" + insertBookId)
                        .then()
                        .statusCode(STATUS_COD_200);
        var book = validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", Book.class);

        Assertions.assertEquals(book, new Book(
                bookRepository.getBooksByTitle(TEST_T_3).get(0).getId(),
                TEST_A_3,
                TEST_G_3,
                TEST_T_3));
    }
}
