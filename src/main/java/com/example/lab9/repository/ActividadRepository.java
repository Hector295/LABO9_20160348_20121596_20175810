package com.example.lab9.repository;

import com.example.lab9.entity.Actividades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividades, Integer> {

    List<Actividades> findByIdproyecto(int idproyecto);
}
