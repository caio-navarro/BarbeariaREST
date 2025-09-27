package com.sistema.barbline.config;

import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BarbeiroUserConfig {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public BarbeiroUserConfig(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initUser() {
        return args -> {
            if (usuarioRepository.findByCpf("86522281082").isEmpty()) {
                Usuario barbeiro = new Usuario();
                barbeiro.setCpf("86522281082");
                barbeiro.setSenha(passwordEncoder.encode("123"));
                barbeiro.setRole("ROLE_BARBEIRO");
                usuarioRepository.save(barbeiro);
            }
        };
    }
}

