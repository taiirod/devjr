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
import javax.persistence.PrePersist;
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
    @PrePersist
    public void process() {

        File file = new File("pending");
        File[] files = file.listFiles();
        String fileName = "";

        if (files != null) {
            for (File f : files) {
                Arrays.sort(files);

                fileName = f.getName();

                try {

                    long exists = filesRepository.countAllByFileName(fileName);

                    if (exists > 0) {
                        log.info("Arquivo " + fileName + " já foi inserido no banco!");
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
                            fi.setfileName(fileName);
                            fi.setNr(Integer.valueOf(nr));
                            fi.setSku(sku);
                            fi.setQt(Integer.valueOf(qt));
                            fi.setVl(BigDecimal.valueOf(Double.parseDouble(vl)));
                            fi.setStatus(status);

                            filesRepository.save(fi);

                        }
                        log.info("Inserindo arquivo " + fileName + " no banco!");
                    }

                    List<Product> products = productRepository.findAll();
                    List<Files> filesByFileName = filesRepository.findAllByFileName(fileName);
                    List<Files> fn = filesRepository.findAll();

                    LocalDateTime now = LocalDateTime.now();

                    Order order = new Order();
                    order.setOrderDate(now);

                    for (Files byfilename : filesByFileName) {
                        if (byfilename.getStatus().equals("PENDENTE")) {


                            orderRepository.saveAndFlush(order);

                            for (Product p : products) {


                                if (p.getSku().equals(byfilename.getSku())) {
                                    System.out.println(byfilename.toString());

                                    OrderItem orderItem = new OrderItem();
                                    orderItem.setId_order(order.getId());

                                    orderItem.setSku(p.getSku());
                                    orderItem.setQuantity(byfilename.getQt());
                                    BigDecimal priceCalc = p.getIndustryPrice().multiply(p.getDiscount());
                                    BigDecimal price = p.getIndustryPrice().subtract(priceCalc.divide(new BigDecimal("100")));
                                    orderItem.setPrice(price.multiply(BigDecimal.valueOf(orderItem.getQuantity())));

                                    orderItemRepository.saveAndFlush(orderItem);

                                    Files processed = filesRepository.findBySkuAndFileName(orderItem.getSku(), byfilename.getfileName());
                                    processed.setStatus("ATENDIDO");
                                    filesRepository.save(processed);

                                        /*

                                        int priceIsBigger = price.compareTo(byfilename.getVl());

                                        orderItemRepository.saveAndFlush(orderItem);

                                        Files processed = filesRepository.findBySkuAndFileName(orderItem.getSku(), byfilename.getfileName());
                                        processed.setStatus("ATENDIDO");
                                        filesRepository.save(processed);

                                        Product prod = productRepository.findBySku(orderItem.getSku());
                                        prod.setQuantityAvailable(prod.getQuantityAvailable() - orderItem.getQuantity());
                                        productRepository.save(prod);*//*
                                     *//*if (priceIsBigger < 0) {
                                        } else {
                                            Files processed = filesRepository.findBySkuAndFileName(orderItem.getSku(), byfilename.getfileName());
                                            processed.setStatus("VALOR_ACIMA");
                                            filesRepository.save(processed);

                                        }*//*
                                    } else {
                                        Files processed = filesRepository.findBySkuAndFileName(byfilename.getSku(), byfilename.getfileName());
                                        processed.setStatus("QUANTIDADE_INSUFICIENTE");
                                        filesRepository.save(processed);
                                    }

                                } else {
                                    Files processed = filesRepository.findBySkuAndFileName(byfilename.getSku(), byfilename.getfileName());
                                    processed.setStatus("NAO_DISPONIVEL");
                                    filesRepository.save(processed);
                                }*/
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

