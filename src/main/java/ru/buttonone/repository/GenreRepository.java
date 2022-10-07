package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    @Query("select g.id  from genres g where g.name = :name")
    List<Genre> getGenreIdByGenreName(@Param("name") String name);
}
