localStorage.clear();

// Máscara para telefone
document.getElementById("reg-tel").addEventListener("input", function (e) {
    let value = e.target.value.replace(/\D/g, '');
    value = value.slice(0, 11);
    if (value.length > 10) {
        e.target.value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (value.length > 6) {
        e.target.value = value.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
    } else if (value.length > 2) {
        e.target.value = value.replace(/(\d{2})(\d{0,5})/, '($1) $2');
    } else {
        e.target.value = value;
    }
});

// Máscara para CPF
document.getElementById("reg-cpf").addEventListener("input", function (e) {
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

function limparCampos(){
    const nome = document.getElementById("reg-nome").value = "";
    const cpf = document.getElementById("reg-cpf").value = "";
    const telefone = document.getElementById("reg-tel").value = "";
    const senha = document.getElementById("reg-senha").value = "";
    const confirmarSenha = document.getElementById("confirmar-senha").value = "";
}

// cadastro
document.getElementById("registro-form").addEventListener("submit", function (event) {
    event.preventDefault();

    const nome = document.getElementById("reg-nome").value.trim();
    const cpf = document.getElementById("reg-cpf").value.replace(/\D/g, '');
    const telefone = document.getElementById("reg-tel").value.replace(/\D/g, '');
    const senha = document.getElementById("reg-senha").value;
    const confirmarSenha = document.getElementById("confirmar-senha").value;

    if(telefone.length < 11){
        alert("O telefone precisa ter 11 dígitos!");
        return;
    }

    if (senha !== confirmarSenha) {
        alert("As senhas não coincidem.");
        return;
    }

    const cadastroData = {
        nome,
        cpf,
        telefone,
        senha
    };

    fetch("http://localhost:8080/cliente/cadastrar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cadastroData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(msg => {
                    throw new Error(msg);
                });
            }
            return response.text();
        })
        .then(msg => {
            console.log("Cliente cadastrado:", msg);
            alert("Cadastro realizado com sucesso!");
            window.location.href = "login.html"
        })
        .catch(error => {
            console.error("Erro ao cadastrar cliente:", error.message);
            alert("Erro: " + error.message);
            limparCampos();
        });
});