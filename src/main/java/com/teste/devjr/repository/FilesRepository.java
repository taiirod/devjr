package com.teste.devjr.repository;

import com.teste.devjr.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Integer> {

    Files findBySkuAndFileName(String sku, String file_name);

    long countAllByFileName(String file);

}
