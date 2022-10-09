package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Query("select a.id from authors a where a.fio = :fio")
    List<Author> getAuthorIdByAuthorFio(@Param("fio") String fio);
}
