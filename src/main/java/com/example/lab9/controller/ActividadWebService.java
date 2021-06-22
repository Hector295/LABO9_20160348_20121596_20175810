package com.example.lab9.controller;

import com.example.lab9.entity.Actividades;
import com.example.lab9.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
public class ActividadWebService {
    @Autowired
    ActividadRepository actividadRepository;

    @GetMapping (value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarActividades(){
        return new ResponseEntity(actividadRepository.findAll(),HttpStatus.OK);
    }

    @PostMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity guardarProducto(
            @RequestBody Actividades actividades,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseMap = new HashMap<>();

        actividadRepository.save(actividades);
        if (fetchId) {
            responseMap.put("id", actividades.getIdactividad());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);

    }


}
