package com.sistema.barbline.facade;

import com.sistema.barbline.application.AgendamentoApplication;
import com.sistema.barbline.entities.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class AgendamentoFacade {

    @Autowired
    private AgendamentoApplication agendamentoApplication;

    public List<Agendamento> listar() {
        return agendamentoApplication.listar();
    }

    public ResponseEntity<?> cadastrar(Agendamento agendamentoEntity){
        try{
            Agendamento agendamento = agendamentoApplication.cadastrar(agendamentoEntity);
            return ResponseEntity.ok(agendamento);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            System.out.println("Erro no agendamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public ResponseEntity<?> cancelarAgendamento(String idAgendamento){
        try{
            Agendamento agendamento = agendamentoApplication.cancelarAgendamento(idAgendamento);
            return ResponseEntity.ok(agendamento);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e ){
            System.out.println("Erro ao cancelar agendamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    public Optional<Agendamento> buscarPorId(String id) {
        return agendamentoApplication.buscarPorId(id);
    }

    public Agendamento atualizar(Agendamento agendamento) {
        return agendamentoApplication.atualizar(agendamento);
    }

    public void excluir(String id) {
        agendamentoApplication.excluir(id);
    }

    public List<String> getHorariosDisponiveis(String idBarbeiro, String data) {
        return agendamentoApplication.getHorariosDisponiveis(idBarbeiro, data);
    }

    public ResponseEntity<List<Agendamento>> listarAgendamentosDoCliente(String idCliente) {
        return agendamentoApplication.listarAgendamentosDoCliente(idCliente);
    }

    public List agendamentosBarbeiro(String idBarbeiro) {
        return agendamentoApplication.agendamentosBarbeiro(idBarbeiro);
    }

    public List<Agendamento> agendamentosFinalizadosBarbeiro(String status, String idBarbeiro) {
        return agendamentoApplication.agendamentosFinalizadosBarbeiro(status, idBarbeiro);
    }

    public ResponseEntity<List<Agendamento>> listarAgendamentosFuturos() {
        return agendamentoApplication.listarAgendamentosFuturos();
    }
}
