## ⚙️ GitHub Actions – Compilação do Projeto Java

Este projeto inclui um workflow automatizado com **GitHub Actions**.  
Sempre que é feito um **push para a branch `main`**, o workflow executa os seguintes passos:

1. 🧾 **Faz o checkout** do código do repositório  
2. ☕ **Configura o Java 21 (Temurin)** como ambiente de execução  
3. 🧱 **Compila e empacota o projeto com o Maven** (`mvn clean package -DskipTests`)  
4. 📦 **Guarda o ficheiro `.jar` gerado** como artefacto de build, disponível para download na aba **Actions**

---

## ⚠️ Sincronização com o Google Calendar

A sincronização com o Google Calendar está atualmente **restrita a ambientes de teste**.  
Apenas **contas de teste selecionadas** têm acesso, de forma a limitar a utilização da API.

Se pretender ser adicionado à lista de contas de teste, por favor contacte os **colaboradores do projeto**.
