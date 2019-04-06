package com.teste.devjr;

import com.teste.devjr.util.FTPUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@SpringBootApplication
public class DevjrApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevjrApplication.class, args);


        FTPUtil ftpUtil = new FTPUtil();

        ftpUtil.download();

    }

}


