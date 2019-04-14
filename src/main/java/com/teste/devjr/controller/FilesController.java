package com.teste.devjr.controller;

import com.teste.devjr.model.Files;
import com.teste.devjr.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private FilesRepository filesRepository;


    @GetMapping
    private List<Files> all() {
        return filesRepository.findAll();
    }

    @PostMapping
    private void add(@RequestBody Files files) {
        filesRepository.save(files);
    }

}
