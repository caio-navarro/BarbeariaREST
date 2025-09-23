package com.sistema.barbline.dto;

import java.util.List;

import com.sistema.barbline.entities.Agendamento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteComAgendamentosDTO {
    private String id;
    private String nome;
    private String telefone;
    private List<Agendamento> agendamentos;
}
