package com.teste.devjr;

import com.teste.devjr.util.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class DevjrApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevjrApplication.class, args);
    }

}


