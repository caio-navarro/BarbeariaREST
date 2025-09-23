package com.sistema.barbline.application;

import com.sistema.barbline.entities.Agendamento;
import com.sistema.barbline.entities.Barbeiro;
import com.sistema.barbline.entities.Cliente;
import com.sistema.barbline.repositories.AgendamentoRepository;
import com.sistema.barbline.repositories.BarbeiroRepository;
import com.sistema.barbline.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List listar() {
        return agendamentoRepository.findAll();
    }

    public void enviarMensagemAgendamentoConfirmadoCliente(Agendamento agendamento) {
        String url = "http://localhost:3001/agendamento/confirmacao";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Agendamento> request = new HttpEntity<>(agendamento, headers);

        String resposta = restTemplate.postForObject(url, request, String.class);
        System.out.println("Resposta do Node.js: " + resposta);
    }

    public void enviarMensagemAgendamentoConfirmadoBarbeiro(Agendamento agendamento) {
        String url = "http://localhost:3001/agendamento/confirmacao/barbeiro";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Agendamento> request = new HttpEntity<>(agendamento, headers);

        String resposta = restTemplate.postForObject(url, request, String.class);
        System.out.println("Resposta do Node.js: " + resposta);
    }

    public void enviarMensagemAgendamentoCanceladoCliente(Agendamento agendamento) {
        String url = "http://localhost:3001/agendamento/cancelamento/cliente";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Agendamento> request = new HttpEntity<>(agendamento, headers);

        String resposta = restTemplate.postForObject(url, request, String.class);
        System.out.println("Resposta do Node.js: " + resposta);
    }

    public void enviarMensagemAgendamentoCanceladoBarbeiro(Agendamento agendamento) {
        String url = "http://localhost:3001/agendamento/cancelamento/barbeiro";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Agendamento> request = new HttpEntity<>(agendamento, headers);

        String resposta = restTemplate.postForObject(url, request, String.class);
        System.out.println("Resposta do Node.js: " + resposta);
    }

    public Agendamento cadastrar(Agendamento agendamento) {
        Barbeiro barbeiro = barbeiroRepository.findById(agendamento.getIdBarbeiro())
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado"));

        agendamento.setNomeBarbeiro(barbeiro.getNome());

        Cliente cliente = clienteRepository.findById(agendamento.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        agendamento.setNomeCliente(cliente.getNome());
        agendamento.setNumeroWhatsappCliente(cliente.getTelefone());
        agendamento.setNumeroWhatsappBarbeiro(barbeiro.getTelefone());

        enviarMensagemAgendamentoConfirmadoCliente(agendamento);
        enviarMensagemAgendamentoConfirmadoBarbeiro(agendamento);
        return agendamentoRepository.save(agendamento);
    }

    public ResponseEntity<?> cancelarAgendamento(String idAgendamento) {
        try {
            Agendamento agendamento = agendamentoRepository.findById(idAgendamento)
                    .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

            agendamento.setStatus("cancelado");
            enviarMensagemAgendamentoCanceladoCliente(agendamento);
            enviarMensagemAgendamentoCanceladoBarbeiro(agendamento);

            agendamentoRepository.save(agendamento);
            return ResponseEntity.ok("Agendamento cancelado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar agendamento");
        }
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
