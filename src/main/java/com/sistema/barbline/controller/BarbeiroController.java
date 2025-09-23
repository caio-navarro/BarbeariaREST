package com.sistema.barbline.controller;

import com.sistema.barbline.application.BarbeiroApplication;
import com.sistema.barbline.entities.Barbeiro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/barbeiro")
@CrossOrigin(origins = "*")
public class BarbeiroController {

    @Autowired
    private BarbeiroApplication barbeiroApplication;

    @GetMapping("/listar")
    public List listar(){
        return barbeiroApplication.listar();
    }

    @PostMapping("/cadastrar")
    public Barbeiro cadastrar(@RequestBody Barbeiro barbeiro){
        return barbeiroApplication.cadastrar(barbeiro);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable String id){
        barbeiroApplication.excluir(id);;
    }

    @PutMapping("/atualizar")
    public void atualizar(@RequestBody Barbeiro barbeiro){
        barbeiroApplication.atualizar(barbeiro);
    }

}
