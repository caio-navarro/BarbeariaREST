package com.sistema.barbline.application;

import com.sistema.barbline.entities.Barbeiro;
import com.sistema.barbline.entities.Cliente;
import com.sistema.barbline.repositories.BarbeiroRepository;
import com.sistema.barbline.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthApplication {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    public Cliente loginCliente(Cliente clienteEntity) {
        Cliente cliente = clienteRepository.findByCpf(clienteEntity.getCpf());

        if (cliente == null) {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }

        if (!cliente.getSenha().equals(clienteEntity.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha incorretos.");
        }

        return cliente;
    }

    public Barbeiro loginBarbeiro(Barbeiro barbeiroEntity) {
        Barbeiro barbeiro = barbeiroRepository.findByCpf(barbeiroEntity.getCpf());

        if (barbeiro == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (!barbeiro.getSenha().equals(barbeiroEntity.getSenha())) {
            throw new IllegalArgumentException("Usuário ou senha incorretos.");
        }

        return barbeiro;
    }
}
