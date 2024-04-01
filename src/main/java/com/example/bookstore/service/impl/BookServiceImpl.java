package com.example.bookstore.service.impl;

import java.util.List;
import java.util.UUID;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.BookService;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.UpdateBookRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        return bookList.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto addBook(BookRequestDto requestDto) {
        Book book = bookMapper.toBook(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public void deleteBook(BookById requestId) {
        UUID uuid = UUID.fromString(requestId.getId());
        Book book = bookRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Book with id "
                        + requestId.getId() + " doesn't exist"));
        bookRepository.delete(book);
    }

    @Override
    public BookDto updateBook(UpdateBookRequestDto requestDto) {
        UUID uuid = UUID.fromString(requestDto.getId());
        Book bookToUpdate = bookRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id "
                        + requestDto.getId()));
        bookMapper.updateBook(requestDto, bookToUpdate);
        bookRepository.save(bookToUpdate);
        return bookMapper.toDto(bookToUpdate);
    }

    @Override
    public BookDto getBookById(BookById requestId) {
        UUID uuid = UUID.fromString(requestId.getId());
        return bookRepository.findById(uuid)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id "
                        + requestId.getId()));
    }
}
