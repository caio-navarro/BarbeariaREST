package com.sistema.barbline.controller;

import com.sistema.barbline.application.AgendamentoApplication;
import com.sistema.barbline.entities.Agendamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamento")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoApplication agendamentoApplication;

    @GetMapping("/listar")
    public List listar() {
        return agendamentoApplication.listar();
    }

    @PostMapping("/cadastrar")
    public Agendamento cadastrar(@RequestBody Agendamento agendamento) {
        return agendamentoApplication.cadastrar(agendamento);
    }

    @PutMapping("/atualizar")
    public void atualizar(@RequestBody Agendamento agendamento) {
        agendamentoApplication.atualizar(agendamento);
    }

    @GetMapping("/buscarPorId/{idAgendamento}")
    public Optional<Agendamento> buscarPorId(@PathVariable String idAgendamento) {
        return agendamentoApplication.buscarPorId(idAgendamento);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable String id) {
        agendamentoApplication.excluir(id);
    }

    @GetMapping("/barbeiro/{idBarbeiro}/horarios-disponiveis")
    public List<String> getHorariosDisponiveis(@PathVariable String idBarbeiro, @RequestParam String data) {
        return agendamentoApplication.getHorariosDisponiveis(idBarbeiro, data);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Agendamento>> listarAgendamentosDoCliente(@PathVariable String idCliente) {
        return agendamentoApplication.listarAgendamentosDoCliente(idCliente);
    }

    @GetMapping("/{idBarbeiro}")
    public List agendamentosBarbeiro(@PathVariable String idBarbeiro) {
        return agendamentoApplication.agendamentosBarbeiro(idBarbeiro);
    }

    @GetMapping("/{status}/{idBarbeiro}")
    public List agendamentosFinalizadosBarbeiro(@PathVariable String status, @PathVariable String idBarbeiro) {
        return agendamentoApplication.agendamentosFinalizadosBarbeiro(status, idBarbeiro);
    }

    @PutMapping("/cancelar/id/{idAgendamento}")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable String idAgendamento){
        return agendamentoApplication.cancelarAgendamento(idAgendamento);
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<Agendamento>> listarAgendamentosFuturos() {
        return agendamentoApplication.listarAgendamentosFuturos();
    }

}
