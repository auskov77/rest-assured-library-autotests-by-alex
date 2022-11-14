package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    @Query("select b.id, b.title, a.fio as authors, g.name as genre " +
            "from books b " +
            "join books_authors ba on b.id = ba.book_id " +
            "join authors a on ba.author_id = a.id " +
            "join genres g on b.genre_id  = g.id " +
            "where title = :title")
    List<Book> getBooksByTitle(@Param("title") String title);

    @Query("select * from books b where b.id = :id")
    List<Book> getBooksById(@Param("id") long id);

    @Query("select b.id from books b where b.title = :title")
    List<Book> getBookIdByBookTitle(@Param("title") String title);
}
