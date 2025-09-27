# BarbeariaREST

Projeto Java Spring Boot para aprendizado!

Utiliza **JWT (JSON Web Token)** para autenticação, alguns testes unitários e integração com whatsapp.

As chaves **privada** e **pública** são geradas automaticamente na primeira execução.

---

## Pré-requisitos

* Java 17 ou superior
* Maven
* Git Bash ou WSL (necessário para rodar o script OpenSSL)
* Node.js

> No Windows, recomenda-se usar Git Bash ou WSL para executar o script de geração de chaves.

---

## Estrutura das chaves

* **private.pem** → usada para assinar tokens JWT
* **public.pem** → usada para validar tokens JWT
* **Não versionar `private.pem` no GitHub**

As chaves serão criadas dentro de `src/main/resources/keys/`.

---

## Gerando as chaves

Na primeira vez que rodar o projeto, execute o script de geração de chaves:

```bash
./generate-keys.sh
```

> Esse comando cria `private.pem` e `public.pem` dentro da pasta `src/main/resources/keys/`.

---

## Rodando o projeto

1. Clone o repositório:

```bash
git clone https://github.com/caio-navarro/BarbeariaREST.git
cd seu-projeto
```

2. Gere as chaves (apenas na primeira vez):

```bash
./generate-keys.sh
```

3. Instale todas as dependências do Node.js:

```bash
npm install
```

4. Execute a api WhatsApp:

```bash
node src/main/resources/static/whatsapp.js
```

* Aparecerá um QR code na tela.
* No WhatsApp do celular: Configurações → Dispositivos conectados → Conectar dispositivo → escanear QR code.
* O serviço iniciará na porta `3001`.

5. Execute a aplicação Spring Boot:

```bash
mvn spring-boot:run
```

* O Spring Boot iniciará na porta padrão `8080`.

---

## Observações de segurança

* **Não versionar `private.pem`**
* `public.pem` pode ser incluído no GitHub
* Para produção, gere chaves seguras e mantenha a chave privada protegida

---

## Suporte

Para dúvidas ou problemas, abra uma issue no GitHub ou entre em contato comigo.

