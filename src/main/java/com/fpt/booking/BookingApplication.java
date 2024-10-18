package com.fpt.booking;

import com.fpt.booking.config.AppProperties;
import com.fpt.booking.config.FileStorageProperties;
import com.fpt.booking.services.FileUploadService;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({FileStorageProperties.class,  AppProperties.class})
@SpringBootApplication
public class BookingApplication implements CommandLineRunner {

    @Resource
    private FileUploadService fileUploadService;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileUploadService.init();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
