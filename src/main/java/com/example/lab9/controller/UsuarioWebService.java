package com.example.lab9.controller;

import com.example.lab9.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Access;
import java.awt.*;

@Controller
public class UsuarioWebService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @ResponseBody
    @GetMapping(value = "/usuario",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity listarUsuarios(){
        return new ResponseEntity(usuarioRepository.findAll(), HttpStatus.OK);
    }

}
