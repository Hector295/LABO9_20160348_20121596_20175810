package com.example.lab9.controller;

import com.example.lab9.entity.Actividades;
import com.example.lab9.entity.Proyecto;
import com.example.lab9.repository.ActividadRepository;
import com.example.lab9.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ProyectoWebService {

    @Autowired
    ProyectoRepository proyectoRepository;

    @Autowired
    ActividadRepository actividadRepository;

    @GetMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarProyectos() {
        return new ResponseEntity(proyectoRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/proyecto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity obtenerProyecto(@PathVariable("id") String idrec) {

        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idrec);
            Optional<Proyecto> opt = proyectoRepository.findById(id);
            if (opt.isPresent()) {
                responseMap.put("estado", "ok");
                responseMap.put("proyecto", opt.get());
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "no se encontró el proyecto con id: " + id);
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID debe ser un número");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity guardarProyectos(
            @RequestBody Proyecto proyecto,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseMap = new HashMap<>();

        proyectoRepository.save(proyecto);
        if (fetchId) {
            responseMap.put("id", proyecto.getId());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);

    }

    @GetMapping(value = "/proyectoConActividades/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity obtenerProyectoConActividades(@PathVariable("id") String idStr) {

        HashMap<String, Object> responseMap = new HashMap<>();


        try {
            int id = Integer.parseInt(idStr);
            Optional<Proyecto> opt = proyectoRepository.findById(id);
            List<Actividades> listaActividad= actividadRepository.findByIdproyecto(id);

            if (opt.isPresent() && listaActividad.size()!=0) {
                responseMap.put("estado", "ok");
                responseMap.put("proyecto", opt.get());
                responseMap.put("listaActividades", listaActividad);
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "no se encontró el proyecto con id: " + id);
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "El ID debe ser un número");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }



}
