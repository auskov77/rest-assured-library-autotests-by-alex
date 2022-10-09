package ru.buttonone.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BComments {
    private long id;
    private String bookId;
    private String nickname;
    private String message;

}
