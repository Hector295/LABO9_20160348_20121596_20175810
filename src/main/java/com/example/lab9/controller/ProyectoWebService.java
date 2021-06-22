package com.example.lab9.controller;

import com.example.lab9.entity.Actividades;
import com.example.lab9.entity.Proyecto;
import com.example.lab9.repository.ActividadRepository;
import com.example.lab9.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
                responseMap.put("proyecto", opt.get());
                responseMap.put("estado", "ok");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "no se encontró el proyecto con id: " + id);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("msg", "El ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity guardarProyecto(
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
                responseMap.put("listaActividades", listaActividad);
                responseMap.put("estado", "ok");
                responseMap.put("proyecto", opt.get());
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "no se encontró el proyecto con id: " + id);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("msg", "El ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/proyecto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity actualizarProyecto(@RequestBody Proyecto proyecto) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (proyecto.getId() > 0) {
            Optional<Proyecto> opt = proyectoRepository.findById(proyecto.getId());
            if (opt.isPresent()) {
                Proyecto proyOpt = opt.get();

                if (proyecto.getNombreproyecto() != null &&
                        !proyOpt.getNombreproyecto().equals(proyecto.getNombreproyecto())) {
                    proyOpt.setNombreproyecto(proyecto.getNombreproyecto());
                }
                if (proyecto.getUsuario_owner() != null &&
                        proyOpt.getUsuario_owner().equals(proyecto.getUsuario_owner())) {
                    proyOpt.setUsuario_owner(proyecto.getUsuario_owner());
                }
                if (proyecto.getId() != 0 &&
                        proyOpt.getId() != proyecto.getId()) {
                    proyOpt.setId(proyecto.getId());
                }
                proyectoRepository.save(proyOpt);
                responseMap.put("estado", "actualizado");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "El id del proyecto a actualizar no existe");
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } else {
            responseMap.put("msg", "Debe enviar un ID");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/proyecto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity borrarProyecto(@PathVariable("id") String idrec) {

        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idrec);
            if (proyectoRepository.existsById(id)) {
                proyectoRepository.deleteById(id);
                responseMap.put("estado", "borrado exitoso");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "no se encontró el proyecto con id: " + id);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            responseMap.put("msg", "El ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity gestionExcepcion(HttpServletRequest request) {

        HashMap<String, Object> responseMap = new HashMap<>();
        if (request.getMethod().equals("POST")) {
            responseMap.put("msg", "Debe enviar un proyecto");
            responseMap.put("estado", "error");
        }
        if (request.getMethod().equals("PUT")) {
            responseMap.put("msg", "Debe enviar un proyecto");
            responseMap.put("estado", "error");
        }
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }




}
