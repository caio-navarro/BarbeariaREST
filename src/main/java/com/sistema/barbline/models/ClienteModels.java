package com.sistema.barbline.models;

import org.springframework.data.annotation.Id;

import com.sistema.barbline.entities.Cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClienteModels {

    @Id
    private String idCliente;

    private String nome;

    private String telefone;

    private String cpf;

    private String senha;

    public boolean isCpfValido() {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }
        int[] peso1 = { 10, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] peso2 = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

        try {
            int soma1 = 0, soma2 = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Character.getNumericValue(cpf.charAt(i));
                soma1 += digito * peso1[i];
                soma2 += digito * peso2[i];
            }

            int digito1 = 11 - (soma1 % 11);
            digito1 = (digito1 > 9) ? 0 : digito1;

            soma2 += digito1 * peso2[9];
            int digito2 = 11 - (soma2 % 11);
            digito2 = (digito2 > 9) ? 0 : digito2;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }



    public void validarCompleto() {
        if (!isCpfValido())
            throw new IllegalArgumentException("CPF inválido.");


    }

    public static ClienteModels toCliente(Cliente clienteEntity) {
        ClienteModels cliente = new ClienteModels();

        cliente.setIdCliente(clienteEntity.getIdCliente());
        cliente.setNome(clienteEntity.getNome());
        cliente.setTelefone(clienteEntity.getTelefone());
        cliente.setCpf(clienteEntity.getCpf());
        cliente.setSenha(clienteEntity.getSenha());
        //cliente.setRole(clienteEntity.getRole());

        return cliente;
    }
}
