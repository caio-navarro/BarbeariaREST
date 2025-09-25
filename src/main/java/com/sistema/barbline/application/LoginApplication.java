package com.sistema.barbline.application;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginApplication {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario loginCliente(Usuario clienteEntity) {
        Usuario cliente = usuarioRepository.findByCpf(clienteEntity.getCpf());

        if (cliente == null) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        if (!cliente.getSenha().equals(clienteEntity.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha incorretos.");
        }

        return cliente;
    }

    public Usuario loginBarbeiro(Usuario barbeiroEntity) {
        Usuario barbeiro = usuarioRepository.findByCpf(barbeiroEntity.getCpf());

        if (barbeiro == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (!barbeiro.getSenha().equals(barbeiroEntity.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha incorretos.");
        }

        return barbeiro;
    }
}
