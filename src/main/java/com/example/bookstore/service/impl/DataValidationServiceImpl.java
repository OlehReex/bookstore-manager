package com.example.bookstore.service.impl;

import java.util.regex.Pattern;
import com.example.bookstore.exception.InvalidDataException;
import com.example.bookstore.service.DataValidationService;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.UpdateBookRequestDto;
import org.springframework.stereotype.Service;

@Service
public class DataValidationServiceImpl implements DataValidationService {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^\\d{2}-\\d{3}-\\d{3}-\\d{2}$");

    @Override
    public void isBookValid(BookRequestDto requestDto) {
        if (requestDto.getAuthor().isEmpty()
                || requestDto.getPrice() <= 0
                || requestDto.getQuantity() < 0
                || requestDto.getTitle().isEmpty()
                || !isIsbnValid(requestDto.getIsbn())) {
            throw new InvalidDataException("Invalid data");
        }
    }

    @Override
    public void isIdValid(BookById requestId) {
        if (requestId.getId().isEmpty()) {
            throw new InvalidDataException("Invalid book id");
        }
    }

    @Override
    public void isUpdatedDataValid(UpdateBookRequestDto requestDto) {
        if (requestDto.getAuthor().isEmpty()
                || requestDto.getPrice() <= 0
                || requestDto.getQuantity() < 0
                || requestDto.getTitle().isEmpty()
                || !isIsbnValid(requestDto.getIsbn())) {
            throw new InvalidDataException("Invalid update data");
        }
    }

    private boolean isIsbnValid(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }
}
