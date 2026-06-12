package com.example.inventario.service;

import com.example.inventario.model.Producto;

import com.example.inventario.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * Obtiene un producto por ID
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }
    
    /**
     * Lista todos los productos
     */
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Crea un nuevo producto
     */
    public Producto crearProducto(Producto producto) {
        producto.setFechaCreacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }
    
    /**
     * Actualiza un producto existente
     */
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setNombre(productoDetalles.getNombre());
            producto.setDescripcion(productoDetalles.getDescripcion());
            producto.setPrecio(productoDetalles.getPrecio());
            producto.setStock(productoDetalles.getStock());
            producto.setCategoria(productoDetalles.getCategoria());
            // No actualizamos fechaCreacion, mantenemos la original
            return productoRepository.save(producto);
        }
        return null;
    }
    
    /**
     * Elimina un producto por ID
     */
    public boolean eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Busca productos por categoría (usa el nuevo método del repo)
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }
    
    /**
     * Actualiza el stock de un producto
     */
    public Producto actualizarStock(Long id, int nuevoStock) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setStock(nuevoStock);
            return productoRepository.save(producto);
        }
        return null;
    }
    
    
}