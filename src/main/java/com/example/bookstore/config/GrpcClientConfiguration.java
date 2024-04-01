package com.example.bookstore.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.grpc.bookstore.BookstoreServiceGrpc;

@Configuration
public class GrpcClientConfiguration {

    @Bean
    public BookstoreServiceGrpc.BookstoreServiceBlockingStub bookstoreStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        return BookstoreServiceGrpc.newBlockingStub(channel);
    }
}
