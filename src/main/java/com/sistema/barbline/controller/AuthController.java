package com.sistema.barbline.controller;

import com.sistema.barbline.entities.Barbeiro;
import com.sistema.barbline.entities.Cliente;
import com.sistema.barbline.facade.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthFacade authFacade;

    @PostMapping("/cliente")
    private ResponseEntity<?> loginCliente(@RequestBody Cliente clienteEntity){
        return authFacade.loginCliente(clienteEntity);
    }

    @PostMapping("/barbeiro")
    private ResponseEntity<?> loginBarbeiro(@RequestBody Barbeiro barbeiroEntity){
        return authFacade.loginBarbeiro(barbeiroEntity);
    }
}
