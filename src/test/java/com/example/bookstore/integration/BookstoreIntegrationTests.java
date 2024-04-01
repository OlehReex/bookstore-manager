package com.example.bookstore.integration;

import java.util.Optional;

import org.testng.annotations.BeforeSuite;
import com.example.bookstore.BookstoreManageApplication;
import org.springframework.boot.SpringApplication;
import org.apache.commons.dbcp.BasicDataSource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testng.annotations.Test;
import static org.citrusframework.actions.EchoAction.Builder.echo;
import static org.citrusframework.actions.ExecuteSQLQueryAction.Builder.query;
import static org.citrusframework.http.actions.HttpActionBuilder.http;
import static org.junit.Assert.assertEquals;

@Sql(scripts = {"classpath:database/add-books-to-database.sql"})
@Sql(scripts = {"classpath:database/clean-database.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Test
public class BookstoreIntegrationTests extends TestNGCitrusSpringSupport {
    private final String CONTENT_TYPE_JSON = "application/json";
    private final String CLIENT_ID = "httpClient";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BasicDataSource dataSource;

//    @BeforeSuite
//    public void setUp() {
//        applicationContext = SpringApplication.run(BookstoreManageApplication.class);
//    }

    @CitrusTest
    public void getAllBooks_returnsListDto() {
        String queryGetAllBooks = "SELECT * FROM books";
        String startEchoMessage = "Requesting all books...";
        String finishEchoMessage = "All books received successfully!";

        run(echo(startEchoMessage));

        run(http().client(CLIENT_ID)
                .send()
                .get("/books")
                .message()
                .contentType(CONTENT_TYPE_JSON)
                .fork(true)
        );

        Optional<Integer> optionalCount = Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM books", Integer.class));
        int actualCount = optionalCount.orElse(0);
        assertEquals(2, actualCount);

        run(query()
                .dataSource(dataSource)
                .statement(queryGetAllBooks)
                .validate("title", "Clean Code", "Thinking in Java")
                .validate("author", "Robert Martin", "Bruce Eckel")
        );

        run(http().client(CLIENT_ID)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(CONTENT_TYPE_JSON)
        );

        run(echo(finishEchoMessage));
    }

    @CitrusTest
    public void getBook_byValidId_returnsDto() {
        String queryByBookId = "SELECT * FROM books WHERE id = 'f52c591f-03be-4782-92b1-20b87858654d'";
        String getRequestByBookId = "/books/f52c591f-03be-4782-92b1-20b87858654d";
        String startEchoMessage = "Requesting book by id...";
        String finishEchoMessage = "Book received successfully!";

        run(echo(startEchoMessage));

        run(http().client(CLIENT_ID)
                .send()
                .get(getRequestByBookId)
                .message()
                .contentType(CONTENT_TYPE_JSON)
                .fork(true)
        );

        run(query()
                .dataSource(dataSource)
                .statement(queryByBookId)
                .validate("title", "Thinking in Java")
                .validate("author", "Bruce Eckel")
        );

        run(http().client(CLIENT_ID)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(CONTENT_TYPE_JSON)
        );

        run(echo(finishEchoMessage));
    }
//    @CitrusTest
//    public void createBook_validRequestDto_returnsDto() {
//        String queryCreateBook = "INSERT INTO books (id, title, author, isbn, quantity, " +
//                "price, is_deleted) VALUES ('07a10969-595f-4222-8381-2990fb0ee051', 'Fight Club', 'Chuck Palanek', '00-000-000-03', 15, 99.90, false)";
//        String queryGetAllBooks = "SELECT * FROM books";
//        String postRequest = "/books";
//        String startEchoMessage = "Creating book...";
//        String finishEchoMessage = "Book created successfully!";
//
//        run(echo(startEchoMessage));
//
//        cleanDatabase();
//
//        run(http().client(CLIENT_ID)
//                .send()
//                .post("/books")
//                .message()
//                .body("{\n" +
//                        "  \"id\": \"07a10969-595f-4222-8381-2990fb0ee051\",\n" +
//                        "  \"title\": \"Fight Club\",\n" +
//                        "  \"author\": \"Chuck Palanek\",\n" +
//                        "  \"isbn\": \"00-000-000-16\",\n" +
//                        "  \"quantity\": 15,\n" +
//                        "  \"price\": 99.90,\n" +
//                        "  \"is_deleted\": false\n" +
//                        "}")
//                .contentType(CONTENT_TYPE_JSON)
//        );
//
////        createBook();
//
//
//        Optional<Integer> optionalCount = Optional.ofNullable(jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM books", Integer.class));
//        int actualCount = optionalCount.orElse(0);
//        assertEquals(3, actualCount);
//
//        run(query()
//                .dataSource(dataSource)
//                .statement(queryGetAllBooks)
//                .validate("title",  "Clean Code", "Thinking in Java", "Fight Club")
//                .validate("author", "Robert Martin", "Bruce Eckel", "Chuck Palanek")
//        );
//
//        run(echo(finishEchoMessage));
//    }
}
