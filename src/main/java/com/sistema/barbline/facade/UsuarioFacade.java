package com.sistema.barbline.facade;

import com.sistema.barbline.application.UsuarioApplication;
import com.sistema.barbline.controller.dto.LoginRequest;
import com.sistema.barbline.controller.dto.LoginResponse;
import com.sistema.barbline.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
public class UsuarioFacade {

    @Autowired
    private UsuarioApplication usuarioApplication;

    public List<Usuario> listar(){
        return usuarioApplication.listar();
    }

    public ResponseEntity<?> cadastrar(Usuario usuarioEntity) {
        try{
            Usuario usuario = usuarioApplication.cadastrar(usuarioEntity);
            return ResponseEntity.ok(usuario);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            System.out.println("Erro no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return usuarioApplication.login(loginRequest);
    }

    public void excluir(String id) {
        usuarioApplication.excluir(id);
    }

    public Usuario atualizar(Usuario usuario) {
        return usuarioApplication.atualizar(usuario);
    }

    public List<Usuario> listarBarbeiros() {
        return usuarioApplication.listarBarbeiros();
    }

    public List<Usuario> listarClientes() {
        return usuarioApplication.listarClientes();
    }
}
