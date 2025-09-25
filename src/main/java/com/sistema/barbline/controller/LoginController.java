package com.sistema.barbline.controller;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.facade.LoginFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private LoginFacade loginFacade;

    @PostMapping("/cliente")
    private ResponseEntity<?> loginCliente(@RequestBody Usuario clienteEntity){
        return loginFacade.loginCliente(clienteEntity);
    }

    @PostMapping("/barbeiro")
    private ResponseEntity<?> loginBarbeiro(@RequestBody Usuario barbeiroEntity){
        return loginFacade.loginBarbeiro(barbeiroEntity);
    }
}
