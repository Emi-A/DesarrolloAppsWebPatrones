package com.tienda.repository;

import com.tienda.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
    public List<Categoria> findByActivoTrue();
    public List<Categoria> findByActivoTrueAndDescripcionContainingIgnoreCase(String textoDescripcion);

    @Query("""
           SELECT c
           FROM Categoria c
           JOIN c.productos p
           WHERE c.activo = true
             AND p.activo = true
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :textoDescripcion, '%'))
           GROUP BY c
           HAVING COUNT(p) >= :cantidadMinProductos
           ORDER BY COUNT(p) DESC
           """)
    public List<Categoria> consultaJPQL(@Param("cantidadMinProductos") long cantidadMinProductos,
            @Param("textoDescripcion") String textoDescripcion);

    @Query(value = """
           SELECT c.*
           FROM categoria c
           INNER JOIN producto p ON c.id_categoria = p.id_categoria
           WHERE c.activo = 1
             AND p.activo = 1
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :textoDescripcion, '%'))
           GROUP BY c.id_categoria
           HAVING COUNT(p.id_producto) >= :cantidadMinProductos
           ORDER BY COUNT(p.id_producto) DESC
           """, nativeQuery = true)
    public List<Categoria> consultaSQL(@Param("cantidadMinProductos") long cantidadMinProductos,
            @Param("textoDescripcion") String textoDescripcion);
}