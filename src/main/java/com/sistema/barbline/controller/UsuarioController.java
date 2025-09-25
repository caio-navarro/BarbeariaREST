package com.sistema.barbline.controller;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.facade.UsuarioFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioFacade usuarioFacade;

    @GetMapping("/listar")
    public List<Usuario> listar() {
        return usuarioFacade.listar();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        return usuarioFacade.cadastrar(usuario);
    }

    @PutMapping("/atualizar")
    public void atualizar(@RequestBody Usuario usuario) {
        usuarioFacade.atualizar(usuario);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable String id) {
        usuarioFacade.excluir(id);
    }

    @GetMapping("/listar/barbeiros")
    public List<Usuario> listarBarbeiros() {
        return usuarioFacade.listarBarbeiros();
    }

    @GetMapping("/listar/clientes")
    public List<Usuario> listarClientes() {
        return usuarioFacade.listarClientes();
    }
}
