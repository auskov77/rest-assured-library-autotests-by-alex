package ru.buttonone.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BComments {
    @Id
    private long id;
    @JoinColumn(name = "book_id")
    private long bookId;
    private String nickname;
    private String message;

}
