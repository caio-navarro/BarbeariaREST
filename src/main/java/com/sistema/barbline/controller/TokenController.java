package com.sistema.barbline.controller;

import com.sistema.barbline.controller.dto.LoginRequest;
import com.sistema.barbline.controller.dto.LoginResponse;
import com.sistema.barbline.facade.UsuarioFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final UsuarioFacade usuarioFacade;

    public TokenController(UsuarioFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return usuarioFacade.login(loginRequest);
    }
}
