package com.teste.devjr.util;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ProcFile {

    private static final Logger log = LoggerFactory.getLogger(ProcFile.class);


    @Scheduled(fixedRate = 30000)
    public void process() {

        File file = new File("/pending");
        File[] files = file.listFiles();

        int i = 0;
        if (files != null) {
            for (File f : files) {

                String orderDate = f.getName();
                log.info(orderDate + " " + i++);

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file + "/" + f.getName()));
                    while (br.ready()) {

                        String linha = br.readLine();
                        String[] l = linha.split("\\|");
                        String a = l[0];
                        String b = l[1];
                        String c = l[2];
                        String d = l[3];
                        String e = l[4];

                        JsonObject order = new JsonObject();
                        order.addProperty("nr", a);
                        order.addProperty("sku", b);
                        order.addProperty("qt", c);
                        order.addProperty("vl", d);
                        order.addProperty("status", e);
                        System.out.println(order);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
