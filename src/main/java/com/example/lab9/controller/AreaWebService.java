package com.example.lab9.controller;
import com.example.lab9.entity.Area;
import com.example.lab9.entity.Usuario;
import com.example.lab9.repository.AreaRepository;
import com.example.lab9.repository.UsuarioRepository;
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
public class AreaWebService {

    @Autowired
    AreaRepository areaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;


    @GetMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listaArea() {
        return new ResponseEntity(areaRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/area/{id}")
    public ResponseEntity obtenerAreaPorID(@PathVariable("id") String iDarea) {
        HashMap<String, Object> responseMap = new HashMap<>();

        try {
            int idarea = Integer.parseInt(iDarea);
            Optional<Area> areaOpt = areaRepository.findById(idarea);
            if (areaOpt.isPresent()) {
                Area area = areaOpt.get();
                responseMap.put("area", area);
                responseMap.put("estado", "ok");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "No se encontró el area con id: " + idarea);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            responseMap.put("msg", "El  ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/areaConUsuarios/{id}")
    public ResponseEntity AreaConUsuarios(@PathVariable("id") String iDarea){
        HashMap<String, Object> responseMap = new HashMap<>();
        HashMap<String, Object> responseSubMap = new HashMap<>();

        try{
            int idarea = Integer.parseInt(iDarea);

            List<Usuario> usuarios = usuarioRepository.findByIdarea(idarea);
            Optional<Area> areaOpt = areaRepository.findById(idarea);
            if(areaOpt.isPresent()){
                Area area = areaOpt.get();

                responseSubMap.put("listaUsuarios",usuarios);
                responseSubMap.put("idarea",area.getIdarea());
                responseSubMap.put("nombrearea",area.getNombrearea());

                responseMap.put("area",responseSubMap);
                responseMap.put("estado", "ok");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            }else{
                responseMap.put("msg", "no se encontró el area con id: " + idarea);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        }catch (NumberFormatException e){
            responseMap.put("msg", "el  ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "/area", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveArea(@RequestBody Area area,
                                      @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseMap = new HashMap<>();

        areaRepository.save(area);
        if (fetchId) {
            responseMap.put("id", area.getIdarea());
        }
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);
    }

    @PutMapping(value = "/area",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity editArea(@RequestBody Area area){
        HashMap<String, Object> responseMap = new HashMap<>();
        Optional<Area> areaOpt = areaRepository.findById(area.getIdarea());

        if(areaOpt.isPresent()){
            area = areaOpt.get();
            areaRepository.save(area);
            responseMap.put("estado",area.getNombrearea()+" Actualizado");
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }else{
            responseMap.put("estado","error");
            responseMap.put("msg","El area no existe");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/area/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteArea(@PathVariable("id") String iDarea){
        HashMap<String, Object> responseMap = new HashMap<>();
        try{
            int idarea = Integer.parseInt(iDarea);
            Optional<Area> areaOpt = areaRepository.findById(idarea);
            if(areaOpt.isPresent()){
                areaRepository.deleteById(idarea);
                responseMap.put("estado","borrado existoso");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            }else{
                responseMap.put("estado","error");
                responseMap.put("msg","El area no existe");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        }catch (NumberFormatException e){
            responseMap.put("msg", "el  ID debe ser un número");
            responseMap.put("estado", "error");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity gestionException(HttpServletRequest request) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            responseMap.put("msg", "Debe enviar un area");
            responseMap.put("estado", "error");
        }
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }

}