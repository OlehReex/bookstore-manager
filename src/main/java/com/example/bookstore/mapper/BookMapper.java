package com.example.bookstore.mapper;

import java.util.UUID;
import com.example.bookstore.dto.BookCreateHttpDto;
import com.example.bookstore.dto.BookHttpDto;
import com.example.bookstore.dto.BookUpdateHttpDto;
import com.example.bookstore.model.Book;
import com.example.grpc.bookstore.BookDto;
import com.example.grpc.bookstore.BookRequestDto;
import com.example.grpc.bookstore.UpdateBookRequestDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface BookMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "mapUUIDToString")
    BookDto toDto(Book book);

    Book toBook(BookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    void updateBook(UpdateBookRequestDto updatedRequestDto, @MappingTarget Book bookToUpdate);

    BookHttpDto toBookHttpDto(BookDto grpcDto);

    BookRequestDto toBookRequestDto(BookCreateHttpDto creatorHttpDto);

    @Mapping(target = "id", source = "stringId", qualifiedByName = "toUUID")
    UpdateBookRequestDto toUpdateBookRequest(BookUpdateHttpDto updateHttpDto, String stringId);

    @Named("mapUUIDToString")
    default String mapUUIDToString(UUID id) {
        return id.toString();
    }

    @Named("toUUID")
    default UUID toUUID(String id) {
        return UUID.fromString(id);
    }
}
