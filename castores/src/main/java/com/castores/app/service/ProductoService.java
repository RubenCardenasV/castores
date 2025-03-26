package com.castores.app.service;

import com.castores.app.model.Producto;
import com.castores.app.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }

    public List<Producto> activos() {
        return repository.findByEstatus(1);
    }

    public List<Producto> inactivos() {
        return repository.findByEstatus(0);
    }
}
