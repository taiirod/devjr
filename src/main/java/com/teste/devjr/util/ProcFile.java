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
import java.io.*;
import java.math.BigDecimal;
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

    @Autowired
    private DownAndUpFile downAndUpFile;

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
                        log.info("File " + fileName + " already in database!");
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
                        log.info("Inserting file " + fileName + " in database.");
                    }

                    processOrder(fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void processOrder(String fileName) throws IOException {
        List<Product> products = productRepository.findAll();
        List<Files> filesByFileName = filesRepository.findAllByFileName(fileName);

        LocalDateTime orderDate = getOrderDate(fileName);

        Order order = new Order();
        order.setOrderDate(orderDate);

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

                        int priceCompare = price.compareTo(byFilename.getVl());

                        if (p.getQuantityAvailable() < orderItem.getQuantity()) {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("QUANTIDADE_INSUFICIENTE");
                            filesRepository.save(setStatus);
                        } else if (priceCompare < 0) {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("VALOR_ACIMA");
                            filesRepository.save(setStatus);
                        } else {
                            orderItemRepository.saveAndFlush(orderItem);

                            setStatus.setStatus("ATENDIDO");
                            filesRepository.save(setStatus);
                        }


                        Product subtractProd = productRepository.findBySku(orderItem.getSku());
                        subtractProd.setQuantityAvailable(subtractProd.getQuantityAvailable() - orderItem.getQuantity());
                        productRepository.save(subtractProd);

                        createFileAndUpload(order, byFilename);
                    }
                }
            }
        }
    }

    private void createFileAndUpload(Order order, Files byFilename) throws IOException {
        List<OrderItem> orderItems = orderItemRepository.findAllByIdOrder(order.getId());


        boolean processed = new File("processed").mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter("processed" + "/" + byFilename.getfileName()));
        for (OrderItem oi : orderItems) {
            Files file = filesRepository.findBySkuAndFileName(oi.getSku(), byFilename.getfileName());

            writer.write(
                    oi.getId_order() + "|"
                            + oi.getSku() + "|"
                            + oi.getQuantity() + "|"
                            + oi.getPrice() + "|"
                            + file.getStatus());
            writer.newLine();
            writer.flush();
        }
    }

    private LocalDateTime getOrderDate(String fileName) {
        String parseDate = "";

        String year = fileName.substring(2, 6);
        String month = fileName.substring(6, 8);
        String day = fileName.substring(8, 10);
        String hour = fileName.substring(10, 12);
        String minutes = fileName.substring(12, 14);
        String seconds = fileName.substring(14, 16);


        parseDate = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(parseDate, formatter);
    }
}

