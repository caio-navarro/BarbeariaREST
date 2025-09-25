// Função para formatar data ISO para dd-MM-yyyy
function formatarDataPtBr(dataIso) {
    const data = new Date(dataIso);
    const dia = data.getUTCDate();
    const mes = data.getUTCMonth() + 1;
    const ano = data.getUTCFullYear();

    return `${String(dia).padStart(2, '0')}/${String(mes).padStart(2, '0')}/${ano}`;
}

function verificarLogin() {
    if (!localStorage.getItem('idCliente')) {
        alert("Você precisa fazer login para acessar o dashboard.");
        window.location.href = "login.html";
    } else {
        const nomeCliente = localStorage.getItem('nomeCliente');
        const spanNome = document.getElementById("nome-cliente");
        if (spanNome) {
            spanNome.textContent = nomeCliente;
        }
    }
}

function logout() {
    localStorage.clear();
    alert("Você saiu com sucesso!");
    window.location.href = "index.html";
}

window.addEventListener('load', verificarLogin);

window.addEventListener('pageshow', (event) => {
    if (event.persisted) {
        window.location.reload();
    }
});

const btnLogout = document.getElementById('btn-sair');
if (btnLogout) {
    btnLogout.addEventListener('click', logout);
}

async function carregarBarbeiros() {
    try {
        const response = await fetch('http://localhost:8080/usuario/listar/barbeiros');
        if (!response.ok) throw new Error('Erro ao buscar barbeiros');

        const barbeiros = await response.json();

        const selectBarbeiro = document.getElementById('selecao-barbeiro');
        selectBarbeiro.innerHTML = '<option value="">Selecione um Profissional</option>';

        barbeiros.forEach(barbeiro => {
            const option = document.createElement('option');
            option.value = barbeiro.idUsuario;
            option.textContent = barbeiro.nome;
            selectBarbeiro.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        alert('Falha ao carregar barbeiros');
    }
}

async function carregarHorariosDisponiveis() {
    const selectBarbeiro = document.getElementById('selecao-barbeiro');
    const selectData = document.getElementById('selecao-data');
    const selectHora = document.getElementById('selecao-hora');

    const idBarbeiro = selectBarbeiro.value;
    const dataSelecionada = selectData.value;

    if (!idBarbeiro || !dataSelecionada) {
        // Limpa os horários se barbeiro ou data não estiver selecionado
        selectHora.innerHTML = '<option value="">Selecione um horário</option>';
        return;
    }

    try {
        const url = `http://localhost:8080/agendamento/barbeiro/${idBarbeiro}/horarios-disponiveis?data=${dataSelecionada}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error('Erro ao buscar horários disponíveis');

        const horariosDisponiveis = await response.json();

        selectHora.innerHTML = '<option value="">Selecione um horário</option>';

        horariosDisponiveis.forEach(hora => {
            const option = document.createElement('option');
            option.value = hora;
            option.textContent = hora;
            selectHora.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        alert('Falha ao carregar horários disponíveis');
    }
}

document.getElementById('selecao-barbeiro').addEventListener('change', carregarHorariosDisponiveis);
document.getElementById('selecao-data').addEventListener('change', carregarHorariosDisponiveis);

// fazer agendamento
document.getElementById("form-agendamento").addEventListener("submit", function (event) {
    event.preventDefault();

    const servico = document.getElementById("selecao-servico").value;
    const idCliente = localStorage.getItem("idCliente");
    const idBarbeiro = document.getElementById("selecao-barbeiro").value;
    const data = document.getElementById("selecao-data").value;
    const hora = document.getElementById("selecao-hora").value;

    if (!data || !hora) {
        alert("Por favor, selecione data e horário.");
        return;
    }

    const agendamentoDateTime = new Date(`${data}T${hora}:00`);
    const agora = new Date();

    if (agendamentoDateTime <= agora) {
        alert("A data e hora do agendamento devem ser futuras ao momento atual.");
        return;
    }

    let valorTotal;
    if (servico.toLowerCase() === "corte") {
        valorTotal = 35.0;
    } else if (servico.toLowerCase() === "barba") {
        valorTotal = 25.0;
    } else {
        valorTotal = 50.0;
    }

    const agendamentoData = {
        servico,
        idCliente,
        idBarbeiro,
        valorTotal,
        data,
        hora
    };

    // verifica se já existe agendamento
    fetch(`http://localhost:8080/agendamento/cliente/${idCliente}`)
        .then(res => {
            if (!res.ok) throw new Error("Erro ao buscar agendamentos.");

            // Se não houver conteúdo, retorna array vazio
            if (res.status === 204) return [];

            return res.text().then(text => text ? JSON.parse(text) : []);
        })
        .then(agendamentos => {
            const existeFuturo = agendamentos.some(ag => {
                const agDataHora = new Date(`${ag.data}T${ag.hora}`);
                return agDataHora > agora && ag.status !== "finalizado" && ag.status !== "cancelado";
            });

            if (existeFuturo) {
                alert("Você já possui um agendamento marcado. Não é possível marcar outro.");
                return;
            }

            return fetch("http://localhost:8080/agendamento/cadastrar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(agendamentoData)
            });
        })
        .then(response => {
            if (!response) return;
            if (!response.ok) {
                return response.text().then(msg => { throw new Error(msg); });
            }
            return response.text();
        })
        .then(msg => {
            if (!msg) return;
            alert("Agendamento realizado com sucesso!");
            carregarAgendamentosCliente();

            document.getElementById("selecao-servico").value = "";
            document.getElementById("selecao-barbeiro").value = "";
            document.getElementById("selecao-data").value = "";
            document.getElementById("selecao-hora").value = "";
        })
        .catch(error => {
            alert("Erro: " + error.message);
        });

});

//carregar agendamentos
async function carregarAgendamentosCliente() {
    const idCliente = localStorage.getItem("idCliente");
    if (!idCliente) return;

    try {
        const response = await fetch(`http://localhost:8080/agendamento/cliente/${idCliente}`);
        if (!response.ok) {
            if (response.status === 204) {
                document.getElementById("client-bookings").innerHTML = "<p>Você não tem agendamentos.</p>";
                return;
            }
            throw new Error("Erro ao buscar agendamentos");
        }

        const agendamentos = await response.json();
        const container = document.getElementById("client-bookings");
        container.innerHTML = "";

        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        const agendamentosFuturos = agendamentos.filter(agendamento => {
            const dataStr = agendamento.data.split('T')[0];
            const partes = dataStr.split('-');
            const dataAgendamento = new Date(Number(partes[0]), Number(partes[1]) - 1, Number(partes[2]));

            return dataAgendamento >= hoje && agendamento.status !== "finalizado" && agendamento.status !== "cancelado";
        });

        if (agendamentosFuturos.length === 0) {
            container.innerHTML = "<p>Você não tem agendamentos futuros.</p>";
            return;
        }

        agendamentosFuturos.forEach(agendamento => {
            const div = document.createElement("div");
            div.classList.add("booking-item");
            div.innerHTML = `
                <p><strong>📝 Serviço:</strong> ${agendamento.servico}</p>
                <p><strong>📅 Data:</strong> ${formatarDataPtBr(agendamento.data)} às ${agendamento.hora}h</p>
                <p><strong>💇‍♂️ Barbeiro:</strong> ${agendamento.nomeBarbeiro}</p>
                <button class="btn-cancel" onClick="cancelarAgendamento('${agendamento.idAgendamento}')">Cancelar</button>

            `;
            container.appendChild(div);
        });

    } catch (error) {
        console.error(error);
    }
}

//cancelar agendamento
async function cancelarAgendamento(idAgendamento) {
    try {
        // Passo 1: buscar o agendamento atual
        const getResponse = await fetch(`http://localhost:8080/agendamento/buscarPorId/${idAgendamento}`);
        if (!getResponse.ok) {
            throw new Error("Erro ao buscar agendamento");
        }
        const agendamento = await getResponse.json();

        // Passo 2: enviar PUT com o objeto completo
        const putResponse = await fetch(`http://localhost:8080/agendamento/cancelar/id/${idAgendamento}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(agendamento)
        });

        if (!putResponse.ok) {
            const msg = await putResponse.text();
            throw new Error(msg || "Erro ao cancelar agendamento");
        }

        alert("Agendamento cancelado com sucesso!");
        carregarAgendamentosCliente();
    } catch (error) {
        alert("Erro: " + error.message);
    }
}


window.addEventListener('load', () => {
    carregarBarbeiros();
    carregarAgendamentosCliente();

    const selectServico = document.getElementById('selecao-servico');
    const selectBarbeiro = document.getElementById('selecao-barbeiro');
    const selectData = document.getElementById('selecao-data');
    const selectHora = document.getElementById('selecao-hora');

    selectBarbeiro.disabled = true;
    selectHora.disabled = true;
    selectData.disabled = true;

    selectServico.addEventListener('change', () => {
        if (selectServico.value) {
            selectBarbeiro.disabled = false;
        } else {
            selectBarbeiro.disabled = true;
            selectBarbeiro.value = "";
            selectData.disabled = true;
            selectData.value = "";
            selectHora.disabled = true;
            selectHora.value = "";
        }
    });

    selectBarbeiro.addEventListener('change', () => {
        if (selectBarbeiro.value) {
            selectData.disabled = false;
        } else {
            selectData.disabled = true;
            selectData.value = "";
            selectHora.disabled = true;
            selectHora.value = "";
        }
    });

    selectData.addEventListener('change', () => {
        if (selectData.value) {
            selectHora.disabled = false;
        } else {
            selectHora.disabled = true;
            selectHora.value = "";
        }
    });
});
