package com.tienda.repository;

import com.tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    //Ejemplo de método utilizando consultas derivadas
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(double precioInf, double precioSup);

    //Ejemplo de método utilizando consultas JPQL
    @Query(value = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(@Param("precioInf") double precioInf, @Param("precioSup") double precioSup);

    //Ejemplo de método utilizando consultas SQL nativas
    @Query(nativeQuery = true,
            value = "SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(@Param("precioInf") double precioInf, @Param("precioSup") double precioSup);

     // CONSULTA DERIVADA
    public List<Producto> findByActivoTrueAndPrecioBetweenAndExistenciasGreaterThanAndCategoria_ActivoTrueAndCategoria_DescripcionContainingIgnoreCaseOrderByPrecioAsc(
            double precioMin,
            double precioMax,
            int existenciasMin,
            String descripcionCategoria
    );

    // CONSULTA JPQL
    @Query("""
           SELECT p
           FROM Producto p
           JOIN p.categoria c
           WHERE p.activo = true
             AND p.precio BETWEEN :precioMin AND :precioMax
             AND p.existencias > :existenciasMin
             AND c.activo = true
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcionCategoria, '%'))
           ORDER BY p.precio ASC
           """)
    public List<Producto> consultaProductoJPQL(
            @Param("precioMin") double precioMin,
            @Param("precioMax") double precioMax,
            @Param("existenciasMin") int existenciasMin,
            @Param("descripcionCategoria") String descripcionCategoria
    );

    // CONSULTA SQL NATIVA
    @Query(value = """
           SELECT p.*
           FROM producto p
           INNER JOIN categoria c ON p.id_categoria = c.id_categoria
           WHERE p.activo = 1
             AND p.precio BETWEEN :precioMin AND :precioMax
             AND p.existencias > :existenciasMin
             AND c.activo = 1
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcionCategoria, '%'))
           ORDER BY p.precio ASC
           """, nativeQuery = true)
    public List<Producto> consultaProductoSQL(
            @Param("precioMin") double precioMin,
            @Param("precioMax") double precioMax,
            @Param("existenciasMin") int existenciasMin,
            @Param("descripcionCategoria") String descripcionCategoria
    );
}
