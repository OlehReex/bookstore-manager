package com.example.bookstore.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookCreateHttpDto {
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private BigDecimal price;
}
