const { Client, LocalAuth } = require('whatsapp-web.js');
const qrcode = require('qrcode-terminal');
const express = require('express');
const app = express();

app.use(express.json());

// formata datas
function formatarData(dataISO) {
    const partes = dataISO.split('-'); // 
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

// Configurar cliente WhatsApp
const client = new Client({
    authStrategy: new LocalAuth()
});

let clientePronto = false;

// QR Code para login
client.on('qr', (qr) => {
    console.log('QR Code recebido, escaneie com seu WhatsApp:');
    qrcode.generate(qr, { small: true });
});

client.on('ready', () => {
    console.log('Cliente WhatsApp pronto!');
    clientePronto = true;
});

client.initialize();

// pega o id correto do whatsapp -> evita mensagens fantasmas
async function pegarIdWhatsapp(numeroFormatado) {
    const IdWhatsapp = await client.getNumberId(numeroFormatado);
    if (!IdWhatsapp) {
        console.log('Número não encontrado no WhatsApp:', numeroFormatado);
        return null;
    }
    return IdWhatsapp;
}

// para formatar o numero no formato certo
function formatarNumero(numeroWhatsapp) {
    let numeroFormatado = numeroWhatsapp.replace(/\D/g, '');

    if (numeroFormatado.length === 11) numeroFormatado = '55' + numeroFormatado;
    if (numeroFormatado.length !== 13) return null;

    return numeroFormatado;
}

// enviar mensagem de confimação do agendamento (para o cliente)
app.post('/agendamento/confirmacao', async (req, res) => {
    const { nomeCliente, numeroWhatsappCliente, servico, valorTotal, nomeBarbeiro, data, hora } = req.body;

    console.log('Recebendo agendamento:', req.body);

    if (!numeroWhatsappCliente) {
        return res.status(400).send('Número WhatsApp é obrigatório');
    }

    if (!clientePronto) {
        return res.status(500).send('WhatsApp ainda não está pronto, tente novamente em alguns segundos');
    }

    try {
        const numeroFormatado = formatarNumero(numeroWhatsappCliente);

        console.log('Número final:', numeroFormatado);

        const IdWhatsapp = await pegarIdWhatsapp(numeroFormatado);
        if (!IdWhatsapp) return res.status(400).send('Número não encontrado no WhatsApp');

        const dataFormatada = formatarData(data);
        const mensagem =
            `Olá ${nomeCliente}! Seu agendamento foi confirmado ✅\n` +
            `📝 Serviço: ${servico}\n` +
            `💇‍♂️ Barbeiro: ${nomeBarbeiro}\n` +
            `🗓️ Data: ${dataFormatada} às ${hora}\n` +
            `💰 Valor total: R$ ${valorTotal},00\n ` +
            `Te esperamos aqui! 😊\n\n` +
            `*Caso precise cancelar acesse nosso site.*`;


        await client.sendMessage(IdWhatsapp._serialized, mensagem);

        console.log('Mensagem enviada com sucesso para', numeroFormatado);
        res.send('Mensagem enviada com sucesso');
    } catch (error) {
        console.error('Erro ao enviar mensagem:', error);
        res.status(500).send('Erro ao enviar mensagem');
    }
});

// enviar mensagem de confimação do agendamento (para o barbeiro)
app.post('/agendamento/confirmacao/barbeiro', async (req, res) => {
    const { nomeCliente, nomeBarbeiro, numeroWhatsappCliente, numeroWhatsappBarbeiro, servico, data, hora } = req.body;

    console.log('Recebendo agendamento:', req.body);

    if (!numeroWhatsappBarbeiro) {
        return res.status(400).send('Número WhatsApp é obrigatório');
    }

    if (!clientePronto) {
        return res.status(500).send('WhatsApp ainda não está pronto, tente novamente em alguns segundos');
    }

    try {
        const numeroFormatado = formatarNumero(numeroWhatsappBarbeiro);

        const IdWhatsapp = await pegarIdWhatsapp(numeroWhatsappBarbeiro);
        if (!IdWhatsapp) return res.status(400).send('Número do barbeiro não encontrado no WhatsApp');

        const dataFormatada = formatarData(data);
        const mensagem =
            `Boas notícias, ${nomeBarbeiro}! Você tem um novo agendamento marcado ✅\n\n` +
            `📝 Serviço: ${servico}\n` +
            `👨 Cliente: ${nomeCliente}\n` +
            `📞 WhatsApp Cliente: ${numeroWhatsappCliente}\n` +
            `🗓️ Data: ${dataFormatada} às ${hora}\n\n` +
            `*Bom trabalho! 😊*` 

        await client.sendMessage(IdWhatsapp._serialized, mensagem);

        console.log('Mensagem enviada com sucesso para', numeroFormatado);
        res.send('Mensagem enviada com sucesso');
    } catch (error) {
        console.error('Erro ao enviar mensagem:', error);
        res.status(500).send('Erro ao enviar mensagem');
    }
});

// enviar mensagem de cancelamento do agendamento (para o cliente)
app.post('/agendamento/cancelamento/cliente', async (req, res) => {
    const { nomeCliente, numeroWhatsappCliente } = req.body;

    console.log('Recebendo cancelamento:', req.body);

    if (!numeroWhatsappCliente) {
        return res.status(400).send('Número WhatsApp é obrigatório');
    }

    if (!clientePronto) {
        return res.status(500).send('WhatsApp ainda não está pronto, tente novamente em alguns segundos');
    }

    try {
        const numeroFormatado = formatarNumero(numeroWhatsappCliente);

        const IdWhatsapp = await pegarIdWhatsapp(numeroFormatado);
        if (!IdWhatsapp) return res.status(400).send('Número não encontrado no WhatsApp');

        console.log('Número final:', numeroFormatado);

        const mensagem =
            `Olá ${nomeCliente}! 📩\n\n` +
        "Recebemos o seu pedido de cancelamento 😔\n\n"+

        "Ficamos tristes em não vê-lo desta vez, mas esperamos por você em breve! 😊\n\n" +

        "📅 Quando quiser, você pode agendar novamente pelo nosso site. Será um prazer recebê-lo! ❤️"

        await client.sendMessage(IdWhatsapp._serialized, mensagem);

        console.log('Mensagem enviada com sucesso para', numeroFormatado);
        res.send('Mensagem enviada com sucesso');
    } catch (error) {
        console.error('Erro ao enviar mensagem:', error);
        res.status(500).send('Erro ao enviar mensagem');
    }
});

// enviar mensagem de cancelamento do agendamento (para o barbeiro)
app.post('/agendamento/cancelamento/barbeiro', async (req, res) => {
    const { nomeBarbeiro, nomeCliente, numeroWhatsappBarbeiro, data, hora} = req.body;

    console.log('Recebendo cancelamento:', req.body);

    if (!numeroWhatsappBarbeiro) {
        return res.status(400).send('Número WhatsApp é obrigatório');
    }

    if (!clientePronto) {
        return res.status(500).send('WhatsApp ainda não está pronto, tente novamente em alguns segundos');
    }

    try {
        const dataFormatada = formatarData(data);
        const numeroFormatado = formatarNumero(numeroWhatsappBarbeiro);

        const IdWhatsapp = await pegarIdWhatsapp(numeroFormatado);
        if (!IdWhatsapp) return res.status(400).send('Número não encontrado no WhatsApp');

        console.log('Número final:', numeroFormatado);

        const mensagem =
            `Olá ${nomeBarbeiro}! 📩\n\n` +
        "Você acabou de ter um agendamento cancelado! ❌\n\n"+

        `🧔‍♂️ Nome do cliente: ${nomeCliente}\n` +
        `🗓️ Data: ${dataFormatada} às ${hora}\n\n` +
        `Esse horário agora está disponível para um novo agendamento ✅`

        await client.sendMessage(IdWhatsapp._serialized, mensagem);

        console.log('Mensagem enviada com sucesso para', numeroFormatado);
        res.send('Mensagem enviada com sucesso');
    } catch (error) {
        console.error('Erro ao enviar mensagem:', error);
        res.status(500).send('Erro ao enviar mensagem');
    }
});

const CHECK_INTERVAL = 60 * 1000; // 1 minuto

async function enviarLembretes() {
    try {
        // Buscar agendamentos futuros
        const response = await fetch('http://localhost:8080/agendamento/futuros');
        if (!response.ok) throw new Error('Erro ao buscar agendamentos futuros');

        const agendamentos = await response.json();
        const agora = new Date();

        agendamentos.forEach(async (ag) => {
            const agendamentoDate = new Date(`${ag.data}T${ag.hora}:00`);

            // calcula a diferença em milissegundos
            const diff = agendamentoDate - agora;

            // se faltar entre 1 minuto e 2h0min para o agendamento, envia lembrete
            if (diff > 0 && diff <= 2 * 60 * 60 * 1000) {
                const numeroFormatado = formatarNumero(ag.numeroWhatsapp);
                const IdWhatsapp = await pegarIdWhatsapp(numeroFormatado);
                if (!IdWhatsapp) return;

                const dataFormatada = formatarData(ag.data);

                const mensagem =
                    `Olá ${ag.nomeCliente}! ⏰\n\n` +
                    `Lembrete: seu agendamento de ${ag.servico} com ${ag.nomeBarbeiro} está marcado para hoje, ` +
                    `às ${ag.hora}h (Data: ${dataFormatada}).\n\n` +
                    `Estamos te esperando! 😊`;

                await client.sendMessage(IdWhatsapp._serialized, mensagem);
                console.log(`Lembrete enviado para ${ag.nomeCliente} (${numeroFormatado})`);
            }
        });
    } catch (error) {
        console.error('Erro ao enviar lembretes:', error);
    }
}

// Executa a cada 1 minuto
setInterval(enviarLembretes, CHECK_INTERVAL); 

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
    console.log(`Servidor rodando na porta ${PORT}`);
});
