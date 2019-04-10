package com.teste.devjr.util;

import com.google.gson.JsonObject;
import com.teste.devjr.model.Files;
import com.teste.devjr.repository.FilesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class ProcFile {

    private static final Logger log = LoggerFactory.getLogger(ProcFile.class);

    @Autowired
    private FilesRepository filesRepository;


    @Scheduled(fixedRate = 30000)
    public void process() {

        File file = new File("pending");
        File[] files = file.listFiles();

        int i = 0;
        if (files != null) {
            for (File f : files) {
                Arrays.sort(files);

                String orderDate = f.getName();
                log.info(orderDate + " " + i++);

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file + "/" + f.getName()));
                    while (br.ready()) {

                        String linha = br.readLine();
                        String[] l = linha.split("\\|");
                        String nr = l[0];
                        String sku = l[1];
                        String qt = l[2];
                        String vl = l[3];
                        String status = l[4];

                        Files fi = new Files();
                        fi.setFile_name(orderDate);
                        fi.setNr(Integer.valueOf(nr));
                        fi.setSku(sku);
                        fi.setQt(Integer.valueOf(qt));
                        fi.setVl(BigDecimal.valueOf(Double.parseDouble(vl)));
                        fi.setStatus(status);
                        System.out.println(fi.toString());

                        filesRepository.save(fi);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
