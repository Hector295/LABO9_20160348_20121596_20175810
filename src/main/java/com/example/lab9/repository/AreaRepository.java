package com.example.lab9.repository;

import com.example.lab9.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AreaRepository extends JpaRepository<Area,Integer> {

}