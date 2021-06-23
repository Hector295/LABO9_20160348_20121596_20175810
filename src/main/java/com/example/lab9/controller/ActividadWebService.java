package com.example.lab9.controller;

import com.example.lab9.entity.Actividades;
import com.example.lab9.entity.Proyecto;
import com.example.lab9.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;

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
    public ResponseEntity guardarActividad(
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

    @PutMapping(value = "/actividad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity actualizarActividad(@RequestBody Actividades actividades) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (actividades.getIdactividad() > 0) {
            Optional<Actividades> opt = actividadRepository.findById(actividades.getIdactividad());
            if (opt.isPresent()) {
                Actividades actOpt = opt.get();

                if (actividades.getNombreactividad() != null &&
                        !actOpt.getNombreactividad().equals(actividades.getNombreactividad())) {
                    actOpt.setNombreactividad(actividades.getNombreactividad());
                }
                if (actividades.getUsuario_owner() != null &&
                        actOpt.getUsuario_owner().equals(actividades.getUsuario_owner())) {
                    actOpt.setUsuario_owner(actividades.getUsuario_owner());
                }
                if (actividades.getIdactividad() != 0 &&
                        actOpt.getIdactividad() != actividades.getIdactividad()) {
                    actOpt.setIdactividad(actividades.getIdactividad());
                }
                if (actividades.getIdproyecto() != 0 &&
                        actOpt.getIdproyecto() != actividades.getIdproyecto()) {
                    actOpt.setIdproyecto(actividades.getIdproyecto());
                }
                if (!actOpt.getDescripcion().equals(actividades.getDescripcion())) {
                    actOpt.setDescripcion(actividades.getDescripcion());
                }
                if (actividades.getPeso() != null &&
                        actOpt.getPeso().equals(actividades.getPeso())) {
                    actOpt.setPeso(actividades.getPeso());
                }

                actividadRepository.save(actOpt);
                responseMap.put("estado", "actualizado");
                return new ResponseEntity(responseMap, HttpStatus.OK);

            } else {
                responseMap.put("msg", "El id de la actividad a actualizar no existe");
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }

        } else {
            responseMap.put("msg", "Debe enviar un ID");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/actividad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity borrarProyecto(@PathVariable("id") String idrec) {

        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int id = Integer.parseInt(idrec);

            if (actividadRepository.existsById(id)) {
                actividadRepository.deleteById(id);
                responseMap.put("estado", "borrado exitoso");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "no se encontró la actividad con id: " + id);
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
            responseMap.put("msg", "Debe enviar una actividad");
            responseMap.put("estado", "error");
        }
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);

    }

}
