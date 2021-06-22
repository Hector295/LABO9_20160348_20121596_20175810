package com.example.lab9.repository;


import com.example.lab9.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,String> {
     Usuario findByCorreo(String correo);
}
