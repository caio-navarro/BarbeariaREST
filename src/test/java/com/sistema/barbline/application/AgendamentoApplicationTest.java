package com.sistema.barbline.application;

import com.sistema.barbline.entities.Agendamento;
import com.sistema.barbline.entities.Usuario;
import com.sistema.barbline.repositories.AgendamentoRepository;
import com.sistema.barbline.repositories.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AgendamentoApplicationTest {

    @InjectMocks
    private AgendamentoApplication agendamentoApplication;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MensagemApplication mensagem;

    @Test
    void cadastrarAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento("123");
        agendamento.setIdCliente("321");
        agendamento.setIdBarbeiro("123");

        Usuario cliente = new Usuario();
        cliente.setIdUsuario("321");
        cliente.setNome("Cliente");
        cliente.setRole("CLIENTE");
        cliente.setTelefone("22222222");

        Usuario barbeiro = new Usuario();
        barbeiro.setIdUsuario("123");
        barbeiro.setNome("Barbeiro");
        barbeiro.setRole("BARBEIRO");
        barbeiro.setTelefone("11111111");

        Mockito.when(usuarioRepository.findById(barbeiro.getIdUsuario())).thenReturn(Optional.of(barbeiro));

        Mockito.when(usuarioRepository.findById(cliente.getIdUsuario())).thenReturn(Optional.of(cliente));

        Mockito.when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);

        Agendamento resultado = agendamentoApplication.cadastrar(agendamento);

        Assertions.assertEquals("Barbeiro", resultado.getNomeBarbeiro());
        Assertions.assertEquals("Cliente", resultado.getNomeCliente());

        Mockito.verify(agendamentoRepository).save(agendamento);
    }

    @Test
    void listarAgendamentosDoCliente() {
        Usuario cliente = new Usuario();
        cliente.setIdUsuario("1");
        cliente.setNome("Cliente");
        cliente.setRole("CLIENTE");
        cliente.setTelefone("22222222");

        List<Agendamento> agendamentos = new ArrayList<>();

        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento("2");
        agendamento.setIdCliente("1");

        agendamentos.add(agendamento);

        Mockito.when(agendamentoRepository.findByIdCliente(cliente.getIdUsuario())).thenReturn(agendamentos);

        List<Agendamento> resultado = agendamentoApplication.listarAgendamentosDoCliente(cliente.getIdUsuario());

        Assertions.assertEquals(1, resultado.size());
    }

    @Test
    void atualizarAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento("123");
        agendamento.setNomeCliente("Cliente");
        agendamento.setNomeBarbeiro("Barbeiro");

        Agendamento agendamentoAtualizado = new Agendamento();
        agendamentoAtualizado.setIdAgendamento("123");
        agendamentoAtualizado.setNomeCliente("Cliente atualizado");
        agendamentoAtualizado.setNomeBarbeiro("Barbeiro");

        Mockito.when(agendamentoRepository.save(agendamento)).thenReturn(agendamentoAtualizado);

        Agendamento resultado = agendamentoApplication.atualizar(agendamento);

        Assertions.assertEquals("Cliente atualizado", resultado.getNomeCliente());
    }

    @Test
    void listarMeusAgendamentos() {
        Usuario cliente = new Usuario();
        cliente.setIdUsuario("1");
        cliente.setNome("Cliente");
        cliente.setRole("CLIENTE");
        cliente.setTelefone("22222222");

        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento("123");
        agendamento.setNomeCliente("Cliente");
        agendamento.setNomeBarbeiro("Barbeiro");
        agendamento.setIdCliente("1");

        Mockito.when(agendamentoRepository.findByIdCliente(cliente.getIdUsuario())).thenReturn(List.of(agendamento));

        List<Agendamento> resultado = agendamentoApplication.listarAgendamentosDoCliente(cliente.getIdUsuario());

        Assertions.assertEquals(1, resultado.size());
    }

    @Test
    void cancelarAgendamento() {
        Agendamento agendamento = new Agendamento();
        agendamento.setIdAgendamento("123");
        agendamento.setNomeCliente("Cliente");
        agendamento.setNomeBarbeiro("Barbeiro");
        agendamento.setStatus("ativo");

        Mockito.when(agendamentoRepository.findById(agendamento.getIdAgendamento()))
                .thenReturn(Optional.of(agendamento));

        Agendamento agendamentoAtualizado = new Agendamento();
        agendamentoAtualizado.setIdAgendamento("123");
        agendamentoAtualizado.setNomeCliente("Cliente");
        agendamentoAtualizado.setNomeBarbeiro("Barbeiro");
        agendamentoAtualizado.setStatus("cancelado");

        Mockito.when(agendamentoRepository.save(agendamento)).thenReturn(agendamentoAtualizado);

        Agendamento resultado = agendamentoApplication.cancelarAgendamento(agendamento.getIdAgendamento());

        Assertions.assertEquals("cancelado", resultado.getStatus());
    }

}