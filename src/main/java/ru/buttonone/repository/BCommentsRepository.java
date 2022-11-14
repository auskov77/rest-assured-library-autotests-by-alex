package ru.buttonone.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.buttonone.domain.BComments;

import java.util.List;

public interface BCommentsRepository extends CrudRepository<BComments, Long> {
    @Query("select bc.id, bc.book_id, bc.nickname, bc.message from b_comments bc where bc.nickname = :nickname")
    List<BComments> getCommentsByNickname(@Param("nickname") String nickname);
}
