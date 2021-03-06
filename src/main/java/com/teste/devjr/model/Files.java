package com.teste.devjr.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "files")
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fileName")
    private String fileName;

    private int nr;

    private String sku;

    private int qt;

    private BigDecimal vl;

    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfileName() {
        return fileName;
    }

    public void setfileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQt() {
        return qt;
    }

    public void setQt(int qt) {
        this.qt = qt;
    }

    public BigDecimal getVl() {
        return vl;
    }

    public void setVl(BigDecimal vl) {
        this.vl = vl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
