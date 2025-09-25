package com.sistema.barbline.facade;

import com.sistema.barbline.application.LoginApplication;
import com.sistema.barbline.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoginFacade {

    @Autowired
    private LoginApplication loginApplication;

    public ResponseEntity<?> loginCliente(Usuario clienteEntity) {
        try {
            Usuario cliente = loginApplication.loginCliente(clienteEntity);
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public ResponseEntity<?> loginBarbeiro(Usuario barbeiroEntity) {
        try {
            Usuario barbeiro = loginApplication.loginBarbeiro(barbeiroEntity);
            return ResponseEntity.ok(barbeiro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }
}
