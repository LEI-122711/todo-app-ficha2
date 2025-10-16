
## Video de demonstração da Aplicação – To-Do List

O vídeo abaixo apresenta uma demonstração prática da aplicação To-Do List, após a implementação das funcionalidades desenvolvidas pelo grupo.

Nesta demo é possível observar:

1. 📅 Autenticação com a conta Google, permitindo a sincronização automática das tarefas com o Google Calendar
2. 🧾 Geração de um ficheiro PDF com a lista de tarefas do utilizador
3. 📧 Envio da lista de tarefas por e-mail, diretamente a partir da aplicação
4. 📲 Envio da lista de tarefas por SMS ou WhatsApp, através da integração com o serviço Twoido
   
O objetivo deste vídeo é ilustrar o resultado final do projeto, mostrando o fluxo completo de utilização e a integração das diferentes componentes desenvolvidas. Ver demonstração no Youtube: **(https://youtu.be/xwyGvZCXlZw)**
(Nota: para melhorar a visualização colocar em 1080p :))

---

## ⚙️ GitHub Actions – Compilação do Projeto Java

Este projeto inclui um workflow automatizado com **GitHub Actions**.  
Sempre que é feito um **push para a branch `main`**, o workflow executa os seguintes passos:

1. 🧾 **Faz o checkout** do código do repositório  
2. ☕ **Configura o Java 21 (Temurin)** como ambiente de execução  
3. 🧱 **Compila e empacota o projeto com o Maven** (`mvn clean package -DskipTests`)  
4. 📦 **Guarda o ficheiro `.jar` gerado** como artefacto de build, disponível para download na aba **Actions**
   
Nota: Não foi possível configurar o workflow para copiar o .jar para o repositório uma vez que o jar é demasiado grande :)

---

## ⚠️ Sincronização com o Google Calendar

A sincronização com o Google Calendar está atualmente **restrita a ambientes de teste**.  
Apenas **contas de teste selecionadas** têm acesso, de forma a limitar a utilização da API.

Neste caso, apenas funciona com as nossas contas de gmail privadas, tal como está presente na demo. Se for preciso acrescentar alguma conta, é avisar-nos e nós acrescentamos.

---
## Contas Pessoais

Por engano alguns commits/push foram feitos com as nossas contas pessoais:
 - LEI-122674 = Biancs13
 - LEI-122711 = Laranjaaa
 - LEI-122677 = LFunico

---
## Twoido
Uma vez que os github secrets apenas estão disponíveis após a geração do  `.jar` e nós não temos um web server nos nossos pcs, para efeitos de teste configurámos no nosso IDE Run Configurations para os tokens.
Como usámos uma trial account, há um limite de mensagens que poderão ser enviadas e apenas a números pré-selecionados.

