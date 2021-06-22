package com.example.lab9.controller;

import com.example.lab9.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ActividadWebService {
    @Autowired
    ActividadRepository actividadRepository;

    @ResponseBody
    @GetMapping (value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarActividades(){
        return new ResponseEntity(actividadRepository.findAll(),HttpStatus.OK);
    }
}
