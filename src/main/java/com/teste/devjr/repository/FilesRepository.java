package com.teste.devjr.repository;

import com.teste.devjr.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Integer> {

    //List<Files> findAllByFileName();

    long countAllByFileName(String file);

/*
    1|20757|36|3.56|PENDENTE
2|28137|37|7.73|PENDENTE
3|30171|39|5.13|PENDENTE
4|28092|39|4.20|PENDENTE
5|24074|41|8.12|PENDENTE
6|31634|41|13.94|PENDENTE
7|27466|42|2.61|PENDENTE
8|20481|42|4.77|PENDENTE
9|25729|45|1.13|PENDENTE
10|25721|45|1.13|PENDENTE
11|25722|45|1.13|PENDENTE
12|25723|45|1.13|PENDENTE
13|25724|45|1.13|PENDENTE
14|25725|45|1.13|PENDENTE
*/

}
