package com.sistema.barbline.application;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.models.UsuarioModels;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioApplication {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Usuario cadastrar(Usuario usuarioEntity) {
        UsuarioModels usuarioModels = UsuarioModels.toUsuario(usuarioEntity);
        usuarioModels.validarCompleto();

        if (usuarioEntity.getRole() == null) {
            throw new IllegalArgumentException("Tipo de usuário deve ser informado (CLIENTE ou BARBEIRO).");
        }

        if (usuarioRepository.findByCpf(usuarioEntity.getCpf()) != null) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        if(usuarioRepository.findByTelefone(usuarioEntity.getTelefone()) != null){
            throw new IllegalArgumentException("Telefone já cadastrado!");
        }

        return usuarioRepository.save(usuarioEntity);
    }

    public void excluir(String id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarBarbeiros() {
        return usuarioRepository.findByRole("barbeiro");
    }

    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRole("cliente");
    }

}
