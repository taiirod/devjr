package com.teste.devjr.util;

import com.teste.devjr.controller.OrderItemController;
import com.teste.devjr.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ProcFile {

    private static final Logger log = LoggerFactory.getLogger(ProcFile.class);

    @Autowired
    private OrderItemController orderItemController;

    @Scheduled(fixedRate = 30000)
    public void process () {

        File file = new File("/pending");
        File[] files = file.listFiles();

        int i = 0;
        if (files != null){
            for (File f: files) {

                String orderDate = f.getName();
                orderDate.split("_*_");
                log.info(orderDate + " " + i++);

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file +"/"+f.getName()));
                    while(br.ready()) {
                        String linha = br.readLine();
                        System.out.println(linha);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
