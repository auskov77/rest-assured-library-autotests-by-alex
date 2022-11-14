package ru.buttonone.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import ru.buttonone.domain.BComments;
import ru.buttonone.domain.Book;

import static io.restassured.RestAssured.given;
import static ru.buttonone.SomeApiConstants.*;

@DisplayName("Проверка API тестов методов B_commentsRepository")
@SpringBootTest
public class BCommentsRepositoryTests {

    @Autowired
    private BCommentsRepository b_commentsRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @BeforeTestMethod
    public void insertTestBook() throws JsonProcessingException {
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
                .log().all()
                .statusCode(STATUS_COD_200);
    }

    @AfterEach
    public void deleteTestBookById() {
        if ((bookRepository.getBookIdByBookTitle(TEST_T_1).size()) != 0) {
            var deleteBookId = bookRepository.getBookIdByBookTitle(TEST_T_1).get(0).getId();
            given()
                    .when()
                    .delete(API_BOOKS + "/" + deleteBookId)
                    .then()
                    .statusCode(STATUS_COD_200);
        }
    }

    @BeforeTestMethod
    public void insertTestCommentById() throws JsonProcessingException {
        long insertTestBookId = bookRepository.getBooksByTitle(TEST_T_1).get(0).getId();
        System.out.println("insertTestBookId = " + insertTestBookId);
        BComments expectedCommentBook = new BComments(
                TEST_COMMENT_ID,
                insertTestBookId,
                TEST_COMMENT_NICKNAME,
                TEST_COMMENT_MESSAGE
        );
        System.out.println(API_BOOKS + "/" + insertTestBookId + API_COMMENTS);
        String jsonExpectedBook = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(expectedCommentBook);
        System.out.println("jsonExpectedBook = " + jsonExpectedBook);
        given()
                .header(HEADER)
                .body(jsonExpectedBook)
                .when()
                .post(API_BOOKS + "/" + insertTestBookId + API_COMMENTS)
                .then()
                .statusCode(STATUS_COD_200);
    }

    @DisplayName(" получить комментарии по id из БД")
    @Test
    public void shouldHaveCorrectGetCommentsById() throws JsonProcessingException {
        insertTestBook();
        insertTestCommentById();
        long insertTestBookId = bookRepository.getBooksByTitle(TEST_T_1).get(0).getId();
        long insertTestBookCommentId = b_commentsRepository.getCommentsByNickname(TEST_COMMENT_NICKNAME).get(0).getId();
        ValidatableResponse validatableResponse = given()
                .when()
                .get(API_BOOKS + "/" + insertTestBookId + API_COMMENTS)
                .then()
                .log().all()
                .statusCode(STATUS_COD_200);
        validatableResponse
                .extract()
                .body()
                .jsonPath()
                .getObject("", BComments[].class);
        BComments actualB_comments = b_commentsRepository.getCommentsByNickname(TEST_COMMENT_NICKNAME).get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(insertTestBookCommentId, actualB_comments.getId()),
                () -> Assertions.assertEquals(insertTestBookId, actualB_comments.getBookId()),
                () -> Assertions.assertEquals(TEST_COMMENT_NICKNAME, actualB_comments.getNickname()),
                () -> Assertions.assertEquals(TEST_COMMENT_MESSAGE, actualB_comments.getMessage())
        );
    }
}