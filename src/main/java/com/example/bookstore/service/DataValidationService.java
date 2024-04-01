package com.example.bookstore.service;

import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.UpdateBookRequestDto;

public interface DataValidationService {

    void isBookValid(BookRequestDto requestDto);

    void isIdValid(BookById requestId);

    void isUpdatedDataValid(UpdateBookRequestDto requestDto);
}
