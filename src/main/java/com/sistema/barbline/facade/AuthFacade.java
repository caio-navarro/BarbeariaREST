package com.sistema.barbline.facade;

import com.sistema.barbline.application.AuthApplication;
import com.sistema.barbline.entities.Barbeiro;
import com.sistema.barbline.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {

    @Autowired
    private AuthApplication authApplication;

    public ResponseEntity<?> loginCliente(Cliente clienteEntity) {
        try {
            Cliente cliente = authApplication.loginCliente(clienteEntity);
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public ResponseEntity<?> loginBarbeiro(Barbeiro barbeiroEntity) {
        try {
            Barbeiro barbeiro = authApplication.loginBarbeiro(barbeiroEntity);
            return ResponseEntity.ok(barbeiro);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }
}
