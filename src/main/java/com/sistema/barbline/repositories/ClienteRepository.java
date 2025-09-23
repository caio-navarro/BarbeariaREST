package com.sistema.barbline.repositories;

import com.sistema.barbline.entities.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {

    public Cliente findByTelefone(String telefone);
    public Cliente findByCpf(String cpf);
}
