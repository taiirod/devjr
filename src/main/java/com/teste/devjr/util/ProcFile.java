package com.teste.devjr.util;

import com.teste.devjr.model.Files;
import com.teste.devjr.model.Order;
import com.teste.devjr.model.OrderItem;
import com.teste.devjr.model.Product;
import com.teste.devjr.repository.FilesRepository;
import com.teste.devjr.repository.OrderItemRepository;
import com.teste.devjr.repository.OrderRepository;
import com.teste.devjr.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

                    processOrder(fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void processOrder(String fileName) {
        List<Product> products = productRepository.findAll();
        List<Files> filesByFileName = filesRepository.findAllByFileName(fileName);

        LocalDateTime now = LocalDateTime.now();

        Order order = new Order();
        order.setOrderDate(now);

        for (Files byFilename : filesByFileName) {
            if (byFilename.getStatus().equals("PENDENTE")) {


                for (Product p : products) {
                    if (p.getSku().equals(byFilename.getSku())) {


                        orderRepository.saveAndFlush(order);

                        OrderItem orderItem = new OrderItem();
                        orderItem.setId_order(order.getId());

                        orderItem.setSku(p.getSku());
                        orderItem.setQuantity(byFilename.getQt());
                        BigDecimal priceCalc = p.getIndustryPrice().multiply(p.getDiscount());
                        BigDecimal price = p.getIndustryPrice().subtract(priceCalc.divide(new BigDecimal("100")));
                        orderItem.setPrice(price.multiply(BigDecimal.valueOf(orderItem.getQuantity())));

                        Files setStatus = filesRepository.findBySkuAndFileName(orderItem.getSku(), byFilename.getfileName());

                        int priceCompare =  price.compareTo(byFilename.getVl());

                        if (p.getQuantityAvailable() < orderItem.getQuantity()) {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("QUANTIDADE_INSUFICIENTE");
                            filesRepository.save(setStatus);
                        } else if (priceCompare > 0) {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("VALOR_ACIMA");
                            filesRepository.save(setStatus);
                        } else {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("ATENDIDO");
                            filesRepository.save(setStatus);
                        }



                        /*Product subtractProd = productRepository.findBySku(orderItem.getSku());
                        subtractProd.setQuantityAvailable(subtractProd.getQuantityAvailable() - orderItem.getQuantity());*/

                    }
                }
            }
        }
    }
}

