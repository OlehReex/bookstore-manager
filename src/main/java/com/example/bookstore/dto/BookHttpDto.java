package com.example.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookHttpDto {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private BigDecimal price;
}
