package com.example.bookstore.unit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.bookstore.controller.BookstoreGrpcController;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.mapper.impl.BookMapperImpl;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.service.DataValidationService;
import com.example.bookstore.service.impl.BookServiceImpl;
import com.example.grpc.bookstore.BookById;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookList;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookServiceImpl bookService;

    @Mock
    private DataValidationService validationService;

    @Mock
    private StreamObserver<BookList> boolListObserver;

    @Mock
    private StreamObserver<BookDto> bookDtoObserver;

    @InjectMocks
    private BookstoreGrpcController grpcController;

    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllBooks() {
        String firstBookId = "11aaaa11-2b22-333c-d444-0e55ee5e55e5";
        String secondBookId = "22aaaa22-1b11-777c-d888-0e99ee9e99e9";

        BookDto firstBookDto = createMockBookDto(firstBookId);
        BookDto secondBookDto = createMockBookDto(secondBookId);
        Book firstBook = createNewBook(firstBookId);
        Book secondBook = createNewBook(secondBookId);
        List<BookDto> expectedDtoList = List.of(firstBookDto, secondBookDto);
        List<Book>  expectedList = List.of(firstBook, secondBook);

        when(bookService.getAllBooks()).thenReturn(expectedDtoList);
        when(bookRepository.findAll()).thenReturn(expectedList);

        grpcController.getAllBooks(Empty.getDefaultInstance(), boolListObserver);

        ArgumentCaptor<BookList> captor = ArgumentCaptor.forClass(BookList.class);
        verify(boolListObserver).onNext(captor.capture());

        BookList actualBookList = captor.getValue();
        List<BookDto> allBooks = actualBookList.getBooksList();

        assertEquals(expectedDtoList.size(), actualBookList.getBooksList().size());
        verify(boolListObserver).onCompleted();
        assertEquals(firstBookId, allBooks.get(0).getId());
        assertEquals(secondBookId, allBooks.get(1).getId());
    }

    @Test
    public void testAddBook() {
        String bookId = "11aaaa11-2b22-333c-d444-0e55ee5e55e5";
        BookRequestDto requestDto = createMockBookRequest();
        Book book = createNewBook(bookId);

        doNothing().when(validationService).isBookValid(requestDto);
        when(bookService.addBook(requestDto)).thenReturn(createMockBookDto(bookId));
        when(bookRepository.save(book)).thenReturn(book);

        grpcController.addBook(requestDto, bookDtoObserver);

        ArgumentCaptor<BookDto> captor = ArgumentCaptor.forClass(BookDto.class);
        verify(bookDtoObserver).onNext(captor.capture());

        BookDto bookDto = captor.getValue();

        verify(bookDtoObserver).onNext(any());
        verify(bookDtoObserver).onCompleted();
        assertEquals(book.getId().toString(), bookDto.getId());
        assertEquals(book.getAuthor(), bookDto.getAuthor());
    }

    @Test
    public void testGetBookById() {
        String bookId = "11aaaa11-2b22-333c-d444-e55ee5e55e55";
        BookDto expectedBookDto = createMockBookDto(bookId);
        Book expectedBook = createNewBook(bookId);
        BookById bookByIdRequest = createMoockById(bookId);

        doNothing().when(validationService).isIdValid(bookByIdRequest);
        when(bookRepository.findById(UUID.fromString(bookId))).thenReturn(Optional.of(expectedBook));
        when(bookService.getBookById(createMockBookById(bookId))).thenReturn(expectedBookDto);

        grpcController.getBookById(bookByIdRequest, bookDtoObserver);

        ArgumentCaptor<BookDto> captor = ArgumentCaptor.forClass(BookDto.class);
        verify(bookDtoObserver).onNext(captor.capture());

        BookDto dtoBookById = bookService.getBookById(createMockBookById(bookId));

        verify(bookDtoObserver).onNext(any());
        verify(bookDtoObserver).onCompleted();
        assertEquals(bookId, dtoBookById.getId());
        assertEquals(expectedBookDto.getAuthor(), dtoBookById.getAuthor());
    }

    private BookById createMoockById(String bookId) {
        return BookById.newBuilder()
                .setId(bookId)
                .build();
    }

    private BookDto createMockBookDto(String id) {
        return BookDto.newBuilder()
                .setId(id)
                .setTitle("Example Title")
                .setAuthor("Example Author")
                .setIsbn("12")
                .setPrice(34.00f)
                .setQuantity(10)
                .build();
    }

    private BookRequestDto createMockBookRequest() {
        return BookRequestDto.newBuilder()
                .setTitle("Example Title")
                .setAuthor("Example Author")
                .setIsbn("12")
                .setPrice(34.00f)
                .setQuantity(10)
                .build();
    }

    private BookById createMockBookById(String id) {
        return BookById.newBuilder()
                .setId(id)
                .build();
    }

    private Book createNewBook(String bookId) {
        Book book = new Book();
        book.setId(UUID.fromString(bookId));
        book.setTitle("Example Title");
        book.setAuthor("Example Author");
        book.setIsbn("12");
        book.setPrice(BigDecimal.valueOf(34.00f));
        book.setQuantity(BigDecimal.valueOf(10));
        return book;
    }
}
