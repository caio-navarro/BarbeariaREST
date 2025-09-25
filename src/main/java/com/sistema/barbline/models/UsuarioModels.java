package com.sistema.barbline.models;

import com.sistema.barbline.entities.Usuario;
import org.springframework.data.annotation.Id;

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
public class UsuarioModels {

    @Id
    private String idUsuario;

    private String nome;

    private String telefone;

    private String cpf;

    private String senha;

    private String role;

    private String refId;

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

    public static UsuarioModels toUsuario(Usuario usuarioEntity) {
        UsuarioModels usuario = new UsuarioModels();

        usuario.setIdUsuario(usuario.getIdUsuario());
        usuario.setNome(usuarioEntity.getNome());
        usuario.setTelefone(usuarioEntity.getTelefone());
        usuario.setCpf(usuarioEntity.getCpf());
        usuario.setSenha(usuarioEntity.getSenha());
        usuario.setRole(usuarioEntity.getRole());

        return usuario;
    }
}
