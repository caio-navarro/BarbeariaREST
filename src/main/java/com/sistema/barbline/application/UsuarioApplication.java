package com.sistema.barbline.application;

import com.sistema.barbline.controller.dto.LoginRequest;
import com.sistema.barbline.controller.dto.LoginResponse;
import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.models.UsuarioModels;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.List;

@Component
public class UsuarioApplication {

    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtEncoder jwtEnconder;

    public UsuarioApplication(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEnconder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEnconder = jwtEnconder;
    }

    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Usuario cadastrar(Usuario usuarioEntity) {
        UsuarioModels usuarioModels = UsuarioModels.toUsuario(usuarioEntity);
        usuarioModels.validarCompleto();

        usuarioEntity.setSenha(passwordEncoder.encode(usuarioModels.getSenha()));

        if (usuarioEntity.getRole() == null) {
            throw new IllegalArgumentException("Tipo de usuário deve ser informado (CLIENTE ou BARBEIRO).");
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

    public boolean LoginCorreto(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        var usuario = usuarioRepository.findByCpf(loginRequest.cpf());
        return passwordEncoder.matches(loginRequest.senha(), usuario.get().getSenha());
    }

    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = usuarioRepository.findByCpf(loginRequest.cpf());

        if(user.isEmpty() || !this.LoginCorreto(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha inválidos");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var userRole = user.get().getRole();

        var claims = JwtClaimsSet.builder()
                .issuer("barbline")
                .subject(user.get().getIdUsuario())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("role", userRole)
                .build();

        var jwtValue = jwtEnconder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}
