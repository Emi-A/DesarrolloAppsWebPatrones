package com.tienda.controller;

import com.tienda.domain.Categoria;
import com.tienda.service.CategoriaService;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MessageSource messageSource;

    // ================= LISTADO =================
    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());
        return "/categoria/listado";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@Valid Categoria categoria,
                          @RequestParam MultipartFile imagenFile,
                          RedirectAttributes redirectAttributes) {

        categoriaService.save(categoria, imagenFile);
        redirectAttributes.addFlashAttribute(
                "exito",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/categoria/listado";
    }

    // ================= ELIMINAR =================
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idCategoria,
                           RedirectAttributes redirectAttributes) {

        String titulo = "exito";
        String detalle = "mensaje.eliminado";

        try {
            categoriaService.delete(idCategoria);
        }
        catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "categoria.error1";
        }
        catch (IllegalStateException e) {
            titulo = "error";
            detalle = "categoria.error2";
        }
        catch (Exception e) {
            titulo = "error";
            detalle = "categoria.error3";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/categoria/listado";
    }

    // ================= MODIFICAR =================
    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable Integer idCategoria,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(idCategoria);
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("categoria.error1", null, Locale.getDefault())
            );
            return "redirect:/categoria/listado";
        }

        model.addAttribute("categoria", categoriaOpt.get());
        return "/categoria/modifica";
    }
}
