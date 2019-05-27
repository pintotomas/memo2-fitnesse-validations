# guarabot

Para ejecutar usando maven:

base_url=http://requestbin.fullcontact.com/1me7bke1/ api_token=zaraza_token mvn clean verify -P wiki


Para ejecutar usando docker:

docker run --rm -p8080:8080 -ebase_url=https://postb.in/LKwEUuod/ -eapi_token=zaraza_token registry.gitlab.com/fiuba-memo2/guarabot:latest

http://localhost:8080/GuaraBot

# Contribuciones

1. Forkear el repositorio
2. Trabajar sobre el branch develop
3. Hacer la contribucion deseada asegurando que al finalizar el build corra correctamente
4. Generar un pull request hacia el branch develop del repo original