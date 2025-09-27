package com.sistema.barbline.controller;

import com.sistema.barbline.application.AgendamentoApplication;
import com.sistema.barbline.entities.Agendamento;
import com.sistema.barbline.facade.AgendamentoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agendamento")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoFacade agendamentoFacade;

    @GetMapping("/listar")
    @PreAuthorize("hasAuthority('BARBEIRO')")
    public List listar() {
        return agendamentoFacade.listar();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Agendamento agendamento) {
        return agendamentoFacade.cadastrar(agendamento);
    }

    @PutMapping("/atualizar")
    public void atualizar(@RequestBody Agendamento agendamento) {
        agendamentoFacade.atualizar(agendamento);
    }

    @GetMapping("/buscarPorId/{idAgendamento}")
    public Optional<Agendamento> buscarPorId(@PathVariable String idAgendamento) {
        return agendamentoFacade.buscarPorId(idAgendamento);
    }

    @DeleteMapping("/excluir/{id}")
    public void excluir(@PathVariable String id) {
        agendamentoFacade.excluir(id);
    }

    @GetMapping("/barbeiro/{idBarbeiro}/horarios-disponiveis")
    public List<String> getHorariosDisponiveis(@PathVariable String idBarbeiro, @RequestParam String data) {
        return agendamentoFacade.getHorariosDisponiveis(idBarbeiro, data);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Agendamento> listarAgendamentosDoCliente(@PathVariable String idCliente) {
        return agendamentoFacade.listarAgendamentosDoCliente(idCliente);
    }

    @GetMapping("/cliente/meus-agendamentos")
    public List<Agendamento> listarMeusAgendamentos(Authentication authentication) {
        String idCliente = authentication.getName();
        return agendamentoFacade.listarAgendamentosDoCliente(idCliente);
    }


    @GetMapping("/{idBarbeiro}")
    public List agendamentosBarbeiro(@PathVariable String idBarbeiro) {
        return agendamentoFacade.agendamentosBarbeiro(idBarbeiro);
    }

    @GetMapping("/{status}/{idBarbeiro}")
    @PreAuthorize("hasAuthority('BARBEIRO')")
    public List agendamentosFinalizadosBarbeiro(@PathVariable String status, @PathVariable String idBarbeiro) {
        return agendamentoFacade.agendamentosFinalizadosBarbeiro(status, idBarbeiro);
    }

    @PutMapping("/cancelar/id/{idAgendamento}")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable String idAgendamento){
        return agendamentoFacade.cancelarAgendamento(idAgendamento);
    }

    @GetMapping("/futuros")
    public ResponseEntity<List<Agendamento>> listarAgendamentosFuturos() {
        return agendamentoFacade.listarAgendamentosFuturos();
    }

}
