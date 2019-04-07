package com.teste.devjr.controller;

import com.teste.devjr.model.Files;
import com.teste.devjr.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FilesRepository filesRepository;


    @GetMapping
    private List<Files> all () {
        return filesRepository.findAll();
    }

    @PostMapping
    private void add (@RequestBody Files files) {
        filesRepository.save(files);
    }

}
