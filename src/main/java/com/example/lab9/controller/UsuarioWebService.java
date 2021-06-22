package com.example.lab9.controller;

import com.example.lab9.entity.Actividades;
import com.example.lab9.entity.Usuario;
import com.example.lab9.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class UsuarioWebService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = "/usuario",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarUsuarios(){
        return new ResponseEntity(usuarioRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity guardarActividades(
            @RequestBody Usuario usuario,
            @RequestParam(value = "fetchCorreo", required = false) boolean fetchCorreo) {

        HashMap<String, Object> responseMap = new HashMap<>();


        if (fetchCorreo) {
            responseMap.put("estado", "creado");
            responseMap.put("correo", usuario.getCorreo());
        }

        usuarioRepository.save(usuario);
        responseMap.put("estado", "creado");
        return new ResponseEntity(responseMap, HttpStatus.CREATED);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity gestionExcepcion(HttpServletRequest request){
        HashMap<String,Object> responseMap = new HashMap<>();
        if (request.getMethod().equals("POST")){
            responseMap.put("msg", "Debe enviar un usuario");
            responseMap.put("estado", "error");
        }

        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity actualizarProducto(@RequestBody Usuario usuario) {

        HashMap<String, Object> responseMap = new HashMap<>();

        if (usuario.getCorreo()!=null) {
            Optional<Usuario> opt = Optional.ofNullable(usuarioRepository.findByCorreo(usuario.getCorreo()));
            if (opt.isPresent()) {
                usuarioRepository.save(usuario);
                responseMap.put("estado", "actualizado");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("msg", "El correo del usuario a actulizar no existe");
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        } else {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un usuario");
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(value = "/usuario/{correo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity borrarProducto(@PathVariable("correo") String correo) {

        HashMap<String, Object> responseMap = new HashMap<>();
        if (correo!=null){
            if (usuarioRepository.existsById(correo)){
                usuarioRepository.deleteById(correo);
                responseMap.put("estado", "borrado exitoso");
                return new ResponseEntity(responseMap, HttpStatus.OK);
            }else {
                responseMap.put("msg", "No se encontró el usuario con correo: " + correo);
                responseMap.put("estado", "error");
                return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
    }


}
