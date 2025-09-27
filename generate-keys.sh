#!/bin/bash

KEYS_DIR="src/main/resources/keys"

# cria a pasta se não existir
mkdir -p $KEYS_DIR

PRIVATE_KEY="$KEYS_DIR/private.pem"
PUBLIC_KEY="$KEYS_DIR/public.pem"

# verifica se as chaves já existem
if [ -f "$PRIVATE_KEY" ] && [ -f "$PUBLIC_KEY" ]; then
    echo "Chaves já existem. Nada a fazer."
else
    echo "Gerando chaves RSA..."

    # gera chave privada
    openssl genrsa -out $PRIVATE_KEY 2048

    # gera chave pública
    openssl rsa -in $PRIVATE_KEY -pubout -out $PUBLIC_KEY

    echo "Chaves geradas com sucesso em $KEYS_DIR"
fi
