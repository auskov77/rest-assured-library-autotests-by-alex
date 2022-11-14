package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    @Query("select a.fio " +
            "from books b " +
            "join books_authors ba on b.id = ba.book_id " +
            "join authors a on ba.author_id = a.id " +
            "where b.id = :id")
    List<Author> getAuthorFromDbByBookId(@Param("id") long id);
}
