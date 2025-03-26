package com.castores.app.controller;

import com.castores.app.model.Movimiento;

import com.castores.app.repository.MovimientoRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {
    @Autowired
    private MovimientoRepository movimientoRepository;

    @GetMapping
    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.findAll();
    }
}
