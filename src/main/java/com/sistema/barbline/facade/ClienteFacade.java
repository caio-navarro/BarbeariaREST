package com.sistema.barbline.facade;

import com.sistema.barbline.application.ClienteApplication;
import com.sistema.barbline.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteFacade {

    @Autowired
    private ClienteApplication clienteApplication;

    public List<Cliente> listar() {
        return clienteApplication.listar();
    }

    public ResponseEntity<?> cadastrar(Cliente clienteEntity) {
        try {
            Cliente cliente = clienteApplication.cadastrar(clienteEntity);
            return ResponseEntity.ok(cliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public void excluir(String id) {
        clienteApplication.excluir(id);
    }

    public Cliente atualizar(Cliente cliente) {
        return clienteApplication.atualizar(cliente);
    }
}