package com.example.bookstore.service;

import java.util.List;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.UpdateBookRequestDto;

public interface BookService {

    List<BookDto> getAllBooks();

    BookDto addBook(BookRequestDto requestDto);

    void deleteBook(BookById requestId);

    BookDto updateBook(UpdateBookRequestDto requestDto);

    BookDto getBookById(BookById requestId);
}
