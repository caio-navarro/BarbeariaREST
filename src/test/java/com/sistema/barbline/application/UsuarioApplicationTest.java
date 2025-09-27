package com.sistema.barbline.application;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioApplicationTest {

    @InjectMocks
    private UsuarioApplication usuarioApplication;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void deveRetornarUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = new Usuario("123", "12312", "12312", "57477449090", "7123717", "CLIENTE");
        Usuario usuario2 = new Usuario("123", "12312", "12312", "66609454078", "7123717", "CLIENTE");

        usuarios.add(usuario);
        usuarios.add(usuario2);

        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> usuariosContagem = usuarioApplication.listar();

        Assertions.assertEquals(2, usuariosContagem.size());
    }

    @Test
    public void cadastrarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Caio");
        usuario.setSenha("123");
        usuario.setCpf("66609454078");
        usuario.setRole("CLIENTE");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setNome("Caio");
        usuarioSalvo.setSenha("senhaHash");
        usuarioSalvo.setCpf("66609454078");
        usuarioSalvo.setRole("CLIENTE");

        Mockito.when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senhaHash");

        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuarioSalvo);

        Usuario resultado = usuarioApplication.cadastrar(usuario);

        Assertions.assertEquals("Caio", resultado.getNome());
        Assertions.assertEquals("senhaHash", resultado.getSenha());
    }

    @Test
    void atualizarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Caio");
        usuario.setSenha("123");
        usuario.setCpf("86562701503");
        usuario.setRole("CLIENTE");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome("Caio atualizado");
        usuarioAtualizado.setSenha("123");
        usuarioAtualizado.setCpf("03472457570");
        usuarioAtualizado.setRole("CLIENTE");

        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioApplication.atualizar(usuario);

        Assertions.assertEquals("Caio atualizado", resultado.getNome());
        Assertions.assertEquals("03472457570", resultado.getCpf());
    }

    @Test
    void excluirUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("1");
        usuario.setNome("Caio");
        usuario.setSenha("123");
        usuario.setCpf("86562701503");
        usuario.setRole("CLIENTE");

        usuarioApplication.excluir(usuario.getIdUsuario());

        Mockito.verify(usuarioRepository).deleteById(usuario.getIdUsuario());
    }
}