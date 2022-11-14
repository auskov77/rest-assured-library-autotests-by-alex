package ru.buttonone;

import io.restassured.http.Header;

public enum SomeApiConstants {
    ;
    public static final String BASE_URL = "http://localhost:8081";
    public static final Header HEADER = new Header("Content-Type", "application/json");
    public static final int STATUS_COD_200 = 200;
    public static final String API_BOOKS = "/api/books";
    public static final String API_COMMENTS = "/comments";
    public static final String API_BOOKS_ADD = "/api/books/add";
    public static final String TEST_A_1 = "test_a1";
    public static final String TEST_G_1 = "test_g1";
    public static final String TEST_T_1 = "test_t1";
    public static final long TEST_ID_1 = 1;
    public static final String TEST_A_2 = "test_a2";
    public static final String TEST_G_2 = "test_g2";
    public static final String TEST_T_2 = "test_t2";
    public static final long TEST_ID_2 = 2;
    public static final String TEST_A_3 = "test_a3";
    public static final String TEST_G_3 = "test_g3";
    public static final String TEST_T_3 = "test_t3";
    public static final long TEST_ID_3 = 3;
    public static final String TEST_A_4 = "test_a4";
    public static final String TEST_G_4 = "test_g4";
    public static final String TEST_T_4 = "test_t4";
    public static final long TEST_ID_4 = 4;
    public static final String TEST_COMMENT_NICKNAME = "test_nick1";
    public static final String TEST_COMMENT_MESSAGE = "test_m1";
    public static final long TEST_COMMENT_ID = 1;
}
