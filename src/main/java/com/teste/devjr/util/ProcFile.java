package com.teste.devjr.util;

import com.google.gson.JsonObject;
import com.teste.devjr.controller.FilesController;
import com.teste.devjr.model.Files;
import com.teste.devjr.model.Order;
import com.teste.devjr.model.OrderItem;
import com.teste.devjr.model.Product;
import com.teste.devjr.repository.FilesRepository;
import com.teste.devjr.repository.OrderItemRepository;
import com.teste.devjr.repository.OrderRepository;
import com.teste.devjr.repository.ProductRepository;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class ProcFile {

    private static final Logger log = LoggerFactory.getLogger(ProcFile.class);

    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Scheduled(fixedRate = 30000)
    public void process() {

        File file = new File("pending");
        File[] files = file.listFiles();

        if (files != null) {
            for (File f : files) {
                Arrays.sort(files);

                String orderDate = f.getName();

                try {

                    long exists = filesRepository.countAllByFileName(orderDate);

                    if (exists > 0) {
                        log.info("Arquivo " + orderDate + " já foi inserido no banco!");
                    } else {

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
                            fi.setfileName(orderDate);
                            fi.setNr(Integer.valueOf(nr));
                            fi.setSku(sku);
                            fi.setQt(Integer.valueOf(qt));
                            fi.setVl(BigDecimal.valueOf(Double.parseDouble(vl)));
                            fi.setStatus(status);

                            filesRepository.save(fi);

                        }
                        log.info("Inserindo arquivo " + orderDate + " no banco!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        List<Files> fi = filesRepository.findAll();
        List<Product> products = productRepository.findAll();

        int i = 0;
        for (Files f : fi) {
            String fileName = f.getfileName();
            String regex = "(\\d{14})";
            String[] orderDate = fileName.split(regex);
            System.out.println(orderDate + " " + i++);

            if (f.getStatus().equals("PENDENTE")) {
                for (Product p : products) {
                    if (p.getSku().equals(f.getSku())) {
                        System.out.println("DISPONIVEL");
                        if (p.getQuantityAvailable() > f.getQt()) {
                            System.out.println("TEM DISPONIVEL");


                            LocalDateTime now = LocalDateTime.now();

                            Order order = new Order();
                            order.setOrderDate(now);
                            orderRepository.save(order);


                            OrderItem orderItem = new OrderItem();
                            orderItem.setId_order(order.getId());
                            orderItem.setSku(f.getSku());
                            orderItem.setQuantity(BigDecimal.valueOf(f.getQt()));
                            BigDecimal price = p.getIndustryPrice().multiply(p.getDiscount());
                            orderItem.setPrice(p.getIndustryPrice().subtract(price.divide(new BigDecimal("100"))));

                            orderItemRepository.save(orderItem);


                        } else {
                            System.out.println("QUANTIDADE INDISPONIVEL");
                        }
                    } else {
                    }
                }
            }
        }
    }

}
