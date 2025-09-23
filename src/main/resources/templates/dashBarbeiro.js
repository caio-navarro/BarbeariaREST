function logout() {
    localStorage.clear();
    alert("Você saiu com sucesso!");
    window.location.href = "index.html";
}

function verificarLogin() {
    if (!localStorage.getItem('idBarbeiro')) {
        alert("Você precisa fazer login para acessar o dashboard.");
        window.location.href = "login.html";
        return false;
    }
    return true;
}

// conta o numero de agendamentos para hoje
function contarAgendamentos() {
    const agendamentosHoje = document.getElementById("agendamentos-hoje");
    const idBarbeiro = localStorage.getItem("idBarbeiro");

    // Data local no formato YYYY-MM-DD
    const hoje = new Date();
    const dataFormatada = hoje.getFullYear() + "-" +
        String(hoje.getMonth() + 1).padStart(2, "0") + "-" +
        String(hoje.getDate()).padStart(2, "0");

    fetch(`http://localhost:8080/agendamento/${idBarbeiro}`)
        .then(res => {
            if (!res.ok) throw new Error("Erro ao buscar agendamentos.");
            return res.json();
        })
        .then(agendamentos => {
            const agendamentosDoDia = agendamentos.filter(ag => ag.data === dataFormatada);
            agendamentosHoje.textContent = agendamentosDoDia.length;
        })
        .catch(err => {
            console.error("Erro:", err);
            agendamentosHoje.textContent = "?";
        });
}

// conta o numero de clientes
function contarClientes() {
    const numeroClientes = document.getElementById("clientes-ativos")

    fetch("http://localhost:8080/cliente/listar")
        .then(res => {
            if (!res.ok) throw new Error("Erro ao buscar clientes.")
            return res.json();
        })
        .then(clientes => {
            const totalClientes = clientes.length;

            numeroClientes.textContent = totalClientes;
        })
        .catch(err => {
            numeroClientes.textContent = "?";
        });
}

// calcula o faturamento mensal de acordo com o barbeiro
async function faturamentoMensal(idBarbeiro) {
    const faturamentoMensal = document.getElementById("faturamento-mensal")

    try {
        const response = await fetch(`http://localhost:8080/agendamento/finalizado/${idBarbeiro}`);
        if (!response.ok) throw new Error("Erro ao buscar agendamentos do barbeiro.");
        const agendamentos = await response.json();

        const hoje = new Date();
        const anoAtual = hoje.getFullYear();
        const mesAtual = hoje.getMonth() + 1; // Janeiro = 0, então soma 1

        const faturamento = agendamentos.reduce((total, ag) => {
            const [dataAno, dataMes] = ag.data.split('-');
            if (parseInt(dataAno) === anoAtual && parseInt(dataMes) === mesAtual) {
                return total + (ag.valorTotal || 0);
            }
            return total;
        }, 0);

        faturamentoMensal.textContent = "R$: " + faturamento.toFixed(2);

    } catch (error) {
        console.error("Erro ao calcular faturamento:", error);
        return 0;
    }
}

async function concluirAgendamento(idAgendamento) {
    try {
        // Passo 1: buscar o agendamento atual
        const getResponse = await fetch(`http://localhost:8080/agendamento/buscarPorId/${idAgendamento}`);
        if (!getResponse.ok) {
            throw new Error("Erro ao buscar agendamento");
        }
        const agendamento = await getResponse.json();

        agendamento.status = "finalizado";

        // Passo 3: enviar PUT com o objeto completo
        const putResponse = await fetch("http://localhost:8080/agendamento/atualizar", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agendamento)
        });

        if (!putResponse.ok) {
            const msg = await putResponse.text();
            throw new Error(msg || "Erro ao finalizar agendamento");
        }

        alert("Agendamento finalizado com sucesso!");
        carregarAgendamentos();

    } catch (error) {
        alert("Erro: " + error.message);
    }
    faturamentoMensal(idBarbeiro);
}

async function cancelarAgendamento(idAgendamento){
try {
        // Passo 1: buscar o agendamento atual
        const getResponse = await fetch(`http://localhost:8080/agendamento/buscarPorId/${idAgendamento}`);
        if (!getResponse.ok) {
            throw new Error("Erro ao buscar agendamento");
        }
        const agendamento = await getResponse.json();

        agendamento.status = "cancelado";

        // Passo 3: enviar PUT com o objeto completo
        const putResponse = await fetch("http://localhost:8080/agendamento/atualizar", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agendamento)
        });

        if (!putResponse.ok) {
            const msg = await putResponse.text();
            throw new Error(msg || "Erro ao cancelar agendamento");
        }

        alert("Agendamento cancelado com sucesso!");
        carregarAgendamentos();

    } catch (error) {
        alert("Erro: " + error.message);
    }
    faturamentoMensal(idBarbeiro);
}


async function carregarAgendamentos() {
    const idBarbeiro = localStorage.getItem("idBarbeiro");
    const dataSelecionada = document.getElementById("admin-date-filter").value;

    try {
        const response = await fetch(`http://localhost:8080/agendamento/${idBarbeiro}`);
        const agendamentos = await response.json();

        const container = document.getElementById("admin-bookings");
        container.innerHTML = "";

        const agendamentosFiltrados = agendamentos.filter(a => a.data === dataSelecionada);

        if (agendamentosFiltrados.length === 0) {
            container.innerHTML = `<div class="table-row"><div colspan="6">Nenhum agendamento encontrado para o dia selecionado.</div></div>`;
            return;
        }

        agendamentosFiltrados.forEach(a => {
            const row = document.createElement("div");
            row.classList.add("table-row");

            row.innerHTML = `
                <div>${a.hora}</div> <!-- Horário -->
                <div>${a.nomeCliente}</div>
                <div>${a.servico}</div>
                <div><span class="status ${a.status.toLowerCase()}">${a.status}</span></div>
                <div>R$ ${Number(a.valorTotal).toFixed(2)}</div> <!-- Valor Total -->
                <div>
                    <button class="btn btn-small btn-success" onclick="concluirAgendamento('${a.idAgendamento}')">Finalizar</button>
                    <button class="btn btn-small btn-danger" onclick="cancelarAgendamento('${a.idAgendamento}')">Cancelar</button>
                </div>
            `;

            container.appendChild(row);
        });

    } catch (error) {
        console.error("Erro ao carregar agendamentos:", error);
        alert("Erro ao buscar agendamentos. Tente novamente mais tarde.");
    }

    faturamentoMensal(idBarbeiro);
}


// ao iniciar
document.addEventListener("DOMContentLoaded", () => {
    if (verificarLogin()) {
        const nomeBarbeiro = localStorage.getItem("nomeBarbeiro");
        const idBarbeiro = localStorage.getItem("idBarbeiro");
        document.getElementById("nome-barbeiro").textContent = `Bem-vindo, ${nomeBarbeiro}`;

        carregarAgendamentos();
        contarAgendamentos()
        contarClientes();
        faturamentoMensal(idBarbeiro);

        document.querySelector(".btn.btn-secondary").addEventListener("click", carregarAgendamentos);
    }
});
