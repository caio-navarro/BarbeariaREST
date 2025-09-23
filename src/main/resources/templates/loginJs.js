localStorage.clear();

// Máscara para CPF
document.getElementById("login-cpf").addEventListener("input", function (e) {
    let value = e.target.value.replace(/\D/g, '');
    value = value.slice(0, 11);

    // Se o usuário está apagando, não aplicar máscara
    if (e.inputType === 'deleteContentBackward') {
        e.target.value = value;
        return;
    }

    if (value.length >= 9) {
        e.target.value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, '$1.$2.$3-$4');
    } else if (value.length >= 6) {
        e.target.value = value.replace(/(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
    } else if (value.length >= 3) {
        e.target.value = value.replace(/(\d{3})(\d{0,3})/, '$1.$2');
    } else {
        e.target.value = value;
    }
});

// Alternar abas
document.querySelectorAll(".role-tab").forEach(tab => {
    tab.addEventListener("click", () => {
        document.querySelectorAll(".role-tab").forEach(t => t.classList.remove("active"));
        tab.classList.add("active");
        document.getElementById("login-role").value = tab.getAttribute("data-role");
    });
});

// login
document.getElementById("login-form").addEventListener("submit", function (event) {
    event.preventDefault();

    const role = document.getElementById("login-role").value;

    const cpf = document.getElementById("login-cpf").value.replace(/\D/g, '');
    const senha = document.getElementById("login-senha").value;

    const loginData = {
        cpf,
        senha
    };

    let url = "";
    if (role === "cliente") {
        url = "http://localhost:8080/cliente/login";
    } else {
        url = "http://localhost:8080/barbeiro/login";
    }

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    throw new Error(msg);
                });
            }
            return response.json();
        })
        .then(cliente => {
            const idCliente = cliente.idCliente;

            // Salvar informações no localStorage
            if (role === "cliente") {
                localStorage.setItem('idCliente', cliente.idCliente);
                localStorage.setItem('nomeCliente', cliente.nome);
                localStorage.setItem('telCliente', cliente.telefone);
                localStorage.setItem('cpfCliente', cliente.cpf);
                window.location.href = "dashboard-cliente.html";
            } else {
                localStorage.setItem('idBarbeiro', cliente.idBarbeiro);
                localStorage.setItem('nomeBarbeiro', cliente.nome);
                localStorage.setItem('telBarbeiro', cliente.telefone);
                localStorage.setItem('cpfBarbeiro', cliente.cpf);
                window.location.href = "dashboard-barbeiro.html";
            }
        })
        .catch(error => {
            alert(error.message);
        });
});
