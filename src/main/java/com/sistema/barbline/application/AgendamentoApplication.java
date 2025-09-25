package com.sistema.barbline.application;

import com.sistema.barbline.entities.Agendamento;
import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.repositories.AgendamentoRepository;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AgendamentoApplication {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MensagemApplication mensagem;

    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    public Agendamento cadastrar(Agendamento agendamento) {
        Usuario barbeiro = usuarioRepository.findById(agendamento.getIdBarbeiro())
                .filter(u -> "barbeiro".equalsIgnoreCase(u.getRole()))
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        agendamento.setNomeBarbeiro(barbeiro.getNome());

        Usuario cliente = usuarioRepository.findById(agendamento.getIdCliente())
                .filter(u -> "cliente".equalsIgnoreCase(u.getRole()))
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setNomeCliente(cliente.getNome());
        agendamento.setNumeroWhatsappCliente(cliente.getTelefone());
        agendamento.setNumeroWhatsappBarbeiro(barbeiro.getTelefone());

        mensagem.enviarMensagemAgendamentoConfirmadoCliente(agendamento);
        mensagem.enviarMensagemAgendamentoConfirmadoBarbeiro(agendamento);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento cancelarAgendamento(String idAgendamento) {
        Agendamento agendamento = agendamentoRepository.findById(idAgendamento)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setStatus("cancelado");
        mensagem.enviarMensagemAgendamentoCanceladoCliente(agendamento);
        mensagem.enviarMensagemAgendamentoCanceladoBarbeiro(agendamento);

        return agendamentoRepository.save(agendamento);
    }

    public Optional<Agendamento> buscarPorId(String id) {
        return agendamentoRepository.findById(id);
    }

    public Agendamento atualizar(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    public void excluir(String id) {
        agendamentoRepository.deleteById(id);
    }

    private final List<String> horariosFixos = Arrays.asList(
            "08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00");

    public List<String> getHorariosDisponiveis(String idBarbeiro, String data) {
        List<Agendamento> agendamentos = agendamentoRepository.findByIdBarbeiroAndData(idBarbeiro, data);

        List<String> horariosOcupados = agendamentos.stream()
                .filter(ag -> !ag.getStatus().equalsIgnoreCase("cancelado") &&
                        !ag.getStatus().equalsIgnoreCase("finalizado")) // ignora cancelados e finalizados
                .map(Agendamento::getHora)
                .collect(Collectors.toList());

        return horariosFixos.stream()
                .filter(horario -> !horariosOcupados.contains(horario))
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<Agendamento>> listarAgendamentosDoCliente(String idCliente) {
        List<Agendamento> agendamentos = agendamentoRepository.findByIdCliente(idCliente);
        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(agendamentos);
    }

    public List agendamentosBarbeiro(String idBarbeiro) {
        return agendamentoRepository.findByIdBarbeiro(idBarbeiro);
    }

    // mostra o faturamento mensal (apenas de agendamentos finalizados)
    public List<Agendamento> agendamentosFinalizadosBarbeiro(String status, String idBarbeiro) {
        return agendamentoRepository.findByStatusAndIdBarbeiro(status, idBarbeiro);
    }

    // lista agendamentos futuros
    public ResponseEntity<List<Agendamento>> listarAgendamentosFuturos() {
        LocalDateTime agora = LocalDateTime.now();

        List<Agendamento> todosAgendamentos = agendamentoRepository.findAll();

        List<Agendamento> agendamentosFuturos = new ArrayList<>();

        for (Agendamento ag : todosAgendamentos) {
            // ignora agendamentos cancelados ou finalizados
            if ("cancelado".equalsIgnoreCase(ag.getStatus()) || "finalizado".equalsIgnoreCase(ag.getStatus())) {
                continue;
            }

            if ("enviado".equalsIgnoreCase(ag.getStatusLembrete())) {
                continue;
            }

            try {
                LocalDate data = LocalDate.parse(ag.getData());
                LocalTime hora = LocalTime.parse(ag.getHora());
                LocalDateTime dataHoraAgendamento = LocalDateTime.of(data, hora);

                if (dataHoraAgendamento.isAfter(agora)) {
                    // marca como enviado
                    ag.setStatusLembrete("enviado");
                    agendamentoRepository.save(ag);

                    agendamentosFuturos.add(ag);
                }
            } catch (Exception e) {
                // ignora agendamentos com data/hora inválidas
            }
        }

        return ResponseEntity.ok(agendamentosFuturos);
    }

}
