package com.tienda.controller;

import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ConsultaController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        var categoriasResultado = categoriaService.getCategorias(false);

        model.addAttribute("productos", productos);
        model.addAttribute("categoriasResultado", categoriasResultado);

        return "/consultas/listado";
    }

    @PostMapping("/producto/derivada")
    public String consultaProductoDerivada(@RequestParam() double precioMin,
            @RequestParam() double precioMax,
            @RequestParam() int existenciasMin,
            @RequestParam() String descripcionCategoria,
            Model model) {
        var lista = productoService.consultaDerivada(precioMin, precioMax, existenciasMin, descripcionCategoria);
        model.addAttribute("productos", lista);
        model.addAttribute("categoriasResultado", categoriaService.getCategorias(false));

        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/producto/jpql")
    public String consultaProductoJPQL(@RequestParam() double precioMin,
            @RequestParam() double precioMax,
            @RequestParam() int existenciasMin,
            @RequestParam() String descripcionCategoria,
            Model model) {
        var productos = productoService.consultaJPQL(precioMin, precioMax, existenciasMin, descripcionCategoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriasResultado", categoriaService.getCategorias(false));

        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/producto/sql")
    public String consultaProductoSQL(@RequestParam() double precioMin,
            @RequestParam() double precioMax,
            @RequestParam() int existenciasMin,
            @RequestParam() String descripcionCategoria,
            Model model) {
        var lista = productoService.consultaSQL(precioMin, precioMax, existenciasMin, descripcionCategoria);
        model.addAttribute("productos", lista);
        model.addAttribute("categoriasResultado", categoriaService.getCategorias(false));

        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/derivada")
    public String consultaCategoriaDerivada(@RequestParam() long cantidadMinProductos,
            @RequestParam() String textoDescripcion,
            Model model) {
        var categoriasResultado = categoriaService.consultaDerivada(cantidadMinProductos, textoDescripcion);

        model.addAttribute("productos", productoService.getProductos(false));
        model.addAttribute("categoriasResultado", categoriasResultado);

        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/jpql")
    public String consultaCategoriaJPQL(@RequestParam() long cantidadMinProductos,
            @RequestParam() String textoDescripcion,
            Model model) {
        var categoriasResultado = categoriaService.consultaJPQL(cantidadMinProductos, textoDescripcion);

        model.addAttribute("productos", productoService.getProductos(false));
        model.addAttribute("categoriasResultado", categoriasResultado);

        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/sql")
    public String consultaCategoriaSQL(@RequestParam() long cantidadMinProductos,
            @RequestParam() String textoDescripcion,
            Model model) {
        var categoriasResultado = categoriaService.consultaSQL(cantidadMinProductos, textoDescripcion);

        model.addAttribute("productos", productoService.getProductos(false));
        model.addAttribute("categoriasResultado", categoriasResultado);

        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }
}