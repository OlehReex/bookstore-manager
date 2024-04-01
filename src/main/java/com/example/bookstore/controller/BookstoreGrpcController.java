package com.example.bookstore.controller;

import java.util.List;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.DataValidationService;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookList;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.BookstoreServiceGrpc;
import com.example.grpc.bookstore.Empty;
import com.example.grpc.bookstore.UpdateBookRequestDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@GrpcService
public class BookstoreGrpcController extends BookstoreServiceGrpc.BookstoreServiceImplBase{
    private final BookService bookService;
    private final DataValidationService validationService;

    @Override
    public void getAllBooks(Empty request, StreamObserver<BookList> responseObserver) {
        List<BookDto> bookList = bookService.getAllBooks();
        BookList.Builder builder = BookList.newBuilder();
        builder.addAllBooks(bookList);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBookById(BookById request, StreamObserver<BookDto> responseObserver) {
        validationService.isIdValid(request);
        BookDto bookDto = bookService.getBookById(request);
        responseObserver.onNext(bookDto);
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(BookRequestDto request, StreamObserver<BookDto> responseObserver) {
        validationService.isBookValid(request);
        BookDto bookDto = bookService.addBook(request);
        responseObserver.onNext(bookDto);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequestDto request, StreamObserver<BookDto> responseObserver) {
        validationService.isUpdatedDataValid(request);
        BookDto bookDto = bookService.updateBook(request);
        responseObserver.onNext(bookDto);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(BookById request, StreamObserver<Empty> responseObserver) {
        validationService.isIdValid(request);
        bookService.deleteBook(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
