package com.sistema.barbline.repositories;

import com.sistema.barbline.entities.Barbeiro;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeiroRepository extends MongoRepository<Barbeiro, String> {

    public Barbeiro findByCpf(String cpf);
}
