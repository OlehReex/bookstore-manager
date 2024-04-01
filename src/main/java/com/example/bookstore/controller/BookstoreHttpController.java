package com.example.bookstore.controller;

import java.util.List;
import com.example.bookstore.dto.BookCreateHttpDto;
import com.example.bookstore.dto.BookHttpDto;
import com.example.bookstore.dto.BookUpdateHttpDto;
import com.example.bookstore.mapper.BookMapper;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookList;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.Empty;
import com.example.grpc.bookstore.UpdateBookRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.grpc.bookstore.BookstoreServiceGrpc;

@RestController
@RequestMapping(value = "/books")
public class BookstoreHttpController {
    private final BookstoreServiceGrpc.BookstoreServiceBlockingStub bookstoreStub;
    private final BookMapper bookMapper;

    @Autowired
    public BookstoreHttpController(BookstoreServiceGrpc.BookstoreServiceBlockingStub bookstoreStub,
                                   BookMapper bookMapper) {
        this.bookstoreStub = bookstoreStub;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public List<BookHttpDto> getAllBooks() {
        BookList grpcResponse = bookstoreStub.getAllBooks(Empty.newBuilder().build());
        List<BookDto> booksList = grpcResponse.getBooksList();
        return booksList.stream()
                .map(bookMapper::toBookHttpDto)
                .toList();
    }

    @GetMapping("/{id}")
    public BookHttpDto getBookById(@PathVariable("id") String id) {
        BookById bookByIdRequest = BookById.newBuilder().setId(id).build();
        BookDto bookById = bookstoreStub.getBookById(bookByIdRequest);
        return bookMapper.toBookHttpDto(bookById);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookHttpDto createBook(@RequestBody BookCreateHttpDto creatorRequest) {
        BookRequestDto createBookRequest = bookMapper.toBookRequestDto(creatorRequest);
        BookDto bookDto = bookstoreStub.addBook(createBookRequest);
        return bookMapper.toBookHttpDto(bookDto);
    }

    @PutMapping("/{id}")
    public BookHttpDto updateBookById(@PathVariable("id") String id,
                                      @RequestBody BookUpdateHttpDto updateRequest) {
        UpdateBookRequestDto updateBookRequest = bookMapper.toUpdateBookRequest(updateRequest, id);
        BookDto bookDto = bookstoreStub.updateBook(updateBookRequest);
        return bookMapper.toBookHttpDto(bookDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        BookById deleteByIdRequest = BookById.newBuilder().setId(id).build();
        bookstoreStub.deleteBook(deleteByIdRequest);
    }
}
