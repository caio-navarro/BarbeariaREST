package com.sistema.barbline.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "agendamentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Agendamento {

    @Id
    private String idAgendamento;

    private String idCliente;

    private String idBarbeiro;

    private String nomeBarbeiro;

    private String nomeCliente;

    private String numeroWhatsappCliente;

    private String numeroWhatsappBarbeiro;

    private String data;

    private String hora;

    @Builder.Default 
    private String status = "confirmado";

    @Builder.Default
    private String statusLembrete = "nao-enviado";

    private String servico;

    private float valorTotal;

}
