package com.sistema.barbline.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    private String idUsuario;

    private String nome;

    private String telefone;

    private String cpf;

    private String senha;

    private String role;
}