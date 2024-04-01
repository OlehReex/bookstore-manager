package com.example.bookstore.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookUpdateHttpDto {
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private BigDecimal price;
}
