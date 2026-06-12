package com.example.inventario.controller;

import com.example.inventario.model.Producto;
import com.example.inventario.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ✅ GET: Solo listar
    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")  ← COMENTAR TEMPORALMENTE
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    // ✅ GET: Buscar por ID
    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')")  ← COMENTAR TEMPORALMENTE
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST: Crear
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")  ← COMENTAR TEMPORALMENTE
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevo = productoService.crearProducto(producto);
        return ResponseEntity.ok(nuevo);
    }

    // ✅ PUT: Actualizar
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")  ← COMENTAR TEMPORALMENTE
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        Producto actualizado = productoService.actualizarProducto(id, productoDetalles);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ DELETE: Eliminar
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")  ← COMENTAR TEMPORALMENTE
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
 // ✅ GET: Buscar por Categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }
}