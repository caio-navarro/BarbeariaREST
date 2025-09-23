package com.sistema.barbline.controller;

import com.sistema.barbline.entities.Cliente;
import com.sistema.barbline.facade.ClienteFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteFacade clienteFacade;

    @GetMapping("/listar")
    public List<Cliente> listar() {
        return clienteFacade.listar();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Cliente cliente) {
        return clienteFacade.cadastrar(cliente);
    }

    @PutMapping("/atualizar")
    public void atualizar(@RequestBody Cliente cliente) {
        clienteFacade.atualizar(cliente);
    }

    @DeleteMapping("/deletar/{id}")
    public void excluir(@PathVariable String id) {
        clienteFacade.excluir(id);
    }

}
