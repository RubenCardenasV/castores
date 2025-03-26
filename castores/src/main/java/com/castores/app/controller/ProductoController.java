package com.castores.app.controller;

import com.castores.app.model.Movimiento;
import com.castores.app.model.Producto;
import com.castores.app.model.TipoMovimiento;
import com.castores.app.model.Usuario;
import com.castores.app.repository.MovimientoRepository;
import com.castores.app.repository.TipoMovimientoRepository;
import com.castores.app.service.ProductoService;
import com.castores.app.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;


    @GetMapping("/todos")
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(service.listar());
    }
    

    @GetMapping("/activos")
    public List<Producto> activos() {
        return service.activos();
    }

    @GetMapping("/inactivos")
    public List<Producto> inactivos() {
        return service.inactivos();
    }

    //Agregar producto nuevo (ya lo tienes con POST /api/productos)
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        producto.setCantidad(0);
        producto.setEstatus(1);
        return service.guardar(producto);
    }

    //Aumentar inventario (Entrada de productos)
    @PutMapping("/entrada/{id}")
    public ResponseEntity<?> entradaInventario(
        @PathVariable Long id,
        @RequestParam int cantidad,
        HttpServletRequest request) {        
        
            Optional<Producto> op = service.buscarPorId(id);
        if (op.isPresent()) {
            Producto producto = op.get();
            if (producto.getCantidad() + cantidad < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No puedes reducir la cantidad por debajo de 0");
            }            
            producto.setCantidad(producto.getCantidad() + cantidad);

                
                // Obtener usuario desde el atributo del request (correo)
                String correo = (String) request.getAttribute("correo");
                Usuario usuario = usuarioService.buscarPorCorreo(correo); // Debes tener este método
                TipoMovimiento tipo = tipoMovimientoRepository.findById(1).orElseThrow(); // Entrada = 1

                Movimiento movimiento = new Movimiento();
                movimiento.setProducto(producto);
                movimiento.setCantidad(cantidad);
                movimiento.setUsuario(usuario);
                movimiento.setTipo(tipo);

                movimientoRepository.save(movimiento);



            return ResponseEntity.ok(service.guardar(producto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    @PutMapping("/salida/{id}")
public ResponseEntity<?> salidaInventario
(@PathVariable Long id,
@RequestParam int cantidad,
HttpServletRequest request) {
    Optional<Producto> op = service.buscarPorId(id);
    if (op.isPresent()) {
        Producto producto = op.get();

        // validar que el producto este activo
        if (producto.getEstatus() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No se puede hacer salida de un producto inactivo");
        }
        
        // Validar que la cantidad no sea mayor a la existente
        if (cantidad > producto.getCantidad()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No puedes sacar más productos de los que hay en inventario");
        }

        // Actualiza la cantidad
        producto.setCantidad(producto.getCantidad() - cantidad);

                        // Obtener usuario desde el atributo del request (correo)
                        String correo = (String) request.getAttribute("correo");
                        Usuario usuario = usuarioService.buscarPorCorreo(correo); // Debes tener este método
                        TipoMovimiento tipo = tipoMovimientoRepository.findById(2).orElseThrow(); // Entrada = 1
        
                        Movimiento movimiento = new Movimiento();
                        movimiento.setProducto(producto);
                        movimiento.setCantidad(cantidad);
                        movimiento.setUsuario(usuario);
                        movimiento.setTipo(tipo);
        
                        movimientoRepository.save(movimiento);
        return ResponseEntity.ok(service.guardar(producto));
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
}


    //Dar de baja (actualiza estatus a 0 sin eliminar)
    @PutMapping("/baja/{id}")
    public ResponseEntity<?> darDeBaja(@PathVariable Long id) {
        Optional<Producto> op = service.buscarPorId(id);
        if (op.isPresent()) {
            Producto producto = op.get();
            producto.setEstatus(0); // inactivo
            return ResponseEntity.ok(service.guardar(producto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }
    //Reactivar producto
    @PutMapping("/activar/{id}")
    public ResponseEntity<?> activarProducto(@PathVariable Long id) {
        Optional<Producto> op = service.buscarPorId(id);
        if (op.isPresent()) {
            Producto producto = op.get();
            producto.setEstatus(1); // activo
            return ResponseEntity.ok(service.guardar(producto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
    }

    


    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setIdProducto(id);
        return service.guardar(producto);
    }


    @GetMapping("/{id}")
    public Optional<Producto> obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
