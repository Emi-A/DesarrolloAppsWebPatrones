package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {

    // El repositorio es final para asegurar la inmutabilidad
    private final CategoriaRepository categoriaRepository;
    private final FirebaseStorageService firebaseStorageService;

    public CategoriaService(CategoriaRepository categoriaRepository, FirebaseStorageService firebaseStorageService) {
        this.categoriaRepository = categoriaRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { //Sólo activos...            
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) { //Si no está vacío... pasaron una imagen...            
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "categoria",
                        categoria.getIdCategoria());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {

            }
        }
    }

    @Transactional
    public void delete(Integer idCategoria) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoria. Tiene datos asociados.", e);
        }
    }
    @Transactional(readOnly = true)
    public List<Categoria> consultaDerivada(long cantidadMinProductos, String textoDescripcion) {
        var lista = categoriaRepository.findByActivoTrueAndDescripcionContainingIgnoreCase(textoDescripcion);

        return lista.stream()
                .filter(c -> c.getProductos() != null)
                .filter(c -> c.getProductos().stream().filter(p -> p.isActivo()).count() >= cantidadMinProductos)
                .sorted(Comparator.comparingLong(
                        (Categoria c) -> c.getProductos().stream().filter(p -> p.isActivo()).count()
                ).reversed())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Categoria> consultaJPQL(long cantidadMinProductos, String textoDescripcion) {
        return categoriaRepository.consultaJPQL(cantidadMinProductos, textoDescripcion);
    }

    @Transactional(readOnly = true)
    public List<Categoria> consultaSQL(long cantidadMinProductos, String textoDescripcion) {
        return categoriaRepository.consultaSQL(cantidadMinProductos, textoDescripcion);
    }
}