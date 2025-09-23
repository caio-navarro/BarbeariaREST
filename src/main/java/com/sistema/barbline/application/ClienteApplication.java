package com.sistema.barbline.application;

import com.sistema.barbline.dto.ClienteComAgendamentosDTO;
import com.sistema.barbline.entities.Agendamento;
import com.sistema.barbline.entities.Cliente;
import com.sistema.barbline.models.ClienteModels;
import com.sistema.barbline.repositories.AgendamentoRepository;
import com.sistema.barbline.repositories.ClienteRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ClienteApplication {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente cadastrar(Cliente clienteEntity) {
       ClienteModels clienteModels = ClienteModels.toCliente(clienteEntity);
       clienteModels.validarCompleto();

       if (clienteRepository.findByCpf(clienteEntity.getCpf()) != null) {
           throw new IllegalArgumentException("CPF já cadastrado!");
       }

       if(clienteRepository.findByTelefone(clienteEntity.getTelefone()) != null){
           throw new IllegalArgumentException("Telefone já cadastrado!");
       }

       return clienteRepository.save(clienteEntity);
    }

    public void excluir(String id) {
        clienteRepository.deleteById(id);
    }

    public Cliente atualizar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public ResponseEntity<ClienteComAgendamentosDTO> getClienteComAgendamentos(String id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = clienteOpt.get();

        List<Agendamento> agendamentos = agendamentoRepository.findByIdCliente(cliente.getIdCliente());

        ClienteComAgendamentosDTO dto = new ClienteComAgendamentosDTO();
        dto.setId(cliente.getIdCliente());
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setAgendamentos(agendamentos);

        return ResponseEntity.ok(dto);
    }

}
