package com.sistema.barbline.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "barbeiros")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Barbeiro {

    @Id
    private String idBarbeiro;

    private String nome;

    private String telefone;

    private String cpf;

    private String senha;

    @Builder.Default
    private String role = "barbeiro";
}
