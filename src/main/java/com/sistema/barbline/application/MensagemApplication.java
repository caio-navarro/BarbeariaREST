package com.sistema.barbline.application;

import com.sistema.barbline.entities.Agendamento;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MensagemApplication {

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
}
