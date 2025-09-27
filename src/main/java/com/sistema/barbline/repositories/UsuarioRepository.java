package com.sistema.barbline.repositories;

import com.sistema.barbline.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    public Usuario findByTelefone(String telefone);
    public Optional<Usuario> findByCpf(String cpf);
    public List<Usuario> findByRole(String role);
}
