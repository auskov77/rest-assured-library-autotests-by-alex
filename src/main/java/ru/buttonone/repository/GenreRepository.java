package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    @Query("select g.name " +
            "from books b " +
            "join genres g on b.genre_id = g.id " +
            "where b.id = :id")
    List<Genre> getGenreFromDbByBookId(@Param("id") long id);
}
