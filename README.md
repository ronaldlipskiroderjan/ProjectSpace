# ProjectSpace

API Spring Boot com Java 21 e PostgreSQL 17. O Docker compila a aplicação com Maven; não é necessário instalar Java ou Maven no host.

## Docker no Arch Linux

Instale os pacotes e habilite o serviço:

```bash
sudo pacman -Syu --needed docker docker-compose docker-buildx
sudo systemctl enable --now docker
sudo docker info
docker compose version
```

Os comandos abaixo usam `sudo` para acessar o Docker. Para usar sem `sudo`, opcionalmente execute `sudo usermod -aG docker "$USER"` e encerre e inicie sua sessão. O grupo `docker` concede privilégios equivalentes aos de root.

## Configuração local

Se ainda não existir um `.env`, copie o modelo:

```bash
cp -n .env.example .env
chmod 600 .env
```

Defina uma senha local em `POSTGRES_PASSWORD`. Configure `LOCAL_UID` e `LOCAL_GID` com os valores de `id -u` e `id -g`, respectivamente, para que os testes criem relatórios com seu usuário. O `.env` é ignorado pelo Git.

## Subir a aplicação

Na raiz do projeto:

```bash
sudo docker compose config --quiet
sudo docker compose up -d --build app
sudo docker compose ps
sudo docker compose logs -f app
```

A API fica em `http://localhost:8080`. Rotas protegidas exigem autenticação. O PostgreSQL fica em `localhost:5432`; credenciais e portas são configuradas no `.env`. Se uma porta já estiver ocupada, altere `APP_PORT` ou `POSTGRES_PORT`. A aplicação aguarda o banco ficar disponível antes de iniciar.

O primeiro build baixa as imagens e dependências Maven. Após alterações no código, execute novamente `sudo docker compose up -d --build app`.

## Testes Cucumber

```bash
sudo docker compose run --rm --build cucumber
```

Os testes usam mocks e não precisam iniciar a aplicação ou o banco. Relatórios: `target/cucumber.html` e `target/cucumber.json`. Veja [o guia de testes](src/test/java/com/ProjectSpace/bdd/README.md).

## Parar os serviços

```bash
sudo docker compose down
```

Os dados permanecem no volume `postgres_data`. O comando `sudo docker compose down -v` também apaga o banco. A senha e o usuário do PostgreSQL são aplicados na criação inicial do volume; editar o `.env` não altera as credenciais de um banco já existente.
