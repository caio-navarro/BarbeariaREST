package com.sistema.barbline.repositories;

import com.sistema.barbline.entities.Agendamento;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends MongoRepository<Agendamento, String> {

    public List<Agendamento> findByIdCliente(String idCliente);
    public List<Agendamento> findByIdBarbeiro(String idBarbeiro);
    public List<Agendamento> findByStatusAndIdBarbeiro(String status, String idBarbeiro);
    public List<Agendamento> findByIdBarbeiroAndData(String idBarbeiro, String data);
}
