# 📊 Consórcio de Veículos – Tabela FIPE  

Este projeto é uma aplicação Java que simula um **sistema de consórcio de veículos**, utilizando a **API pública da Tabela FIPE** para buscar e exibir dados de veículos para análise de preço e tomada de decisão.  

O sistema é executado via **linha de comando** e permite ao usuário pesquisar por **carros, motos ou caminhões**, filtrar modelos e visualizar avaliações por ano, servindo como uma ferramenta de apoio para escolha do melhor veículo.  

Resultado:

![tela-inicial](https://github.com/user-attachments/assets/9191ea71-a0ae-4a64-b1de-46be0e9f39b1)


---

## 🚀 Funcionalidades
- Consulta de **marcas, modelos e preços** de veículos diretamente da API FIPE.  
- Filtro por **trecho do nome do modelo** para buscas mais rápidas.  
- Exibição de **histórico de preço por ano e tipo de combustível**.  
- Interface simples no **console** para facilitar a navegação.  

---

## 🛠️ Tecnologias Utilizadas
- **Java 21**  
- **Jackson** → Serialização e desserialização de JSON  
- **API pública Tabela FIPE** → [https://deividfortuna.github.io/fipe/](https://deividfortuna.github.io/fipe/)  

---

## 📂 Estrutura do Projeto
```
tabelafipe/
├── models/          # DTOs da aplicação
│   ├── Dados.java
│   ├── Modelos.java
│   └── Veiculo.java
│
├── principal/       # Controller + View (fluxo do menu no console)
│   └── Principal.java
│
├── services/        # Serviços e utilitários
│   ├── ConsumoApi.java
│   ├── ConverteDados.java
│   └── IConverteDados.java
│
└── TabelafipeApplication.java  # Classe principal
```

---

## 📦 Pré-requisitos
Antes de começar, você precisa ter instalado:
- **JDK 21** ou superior  
- **Maven** ou **Gradle** para gerenciamento de dependências  
- **Acesso à internet** (para consumir a API FIPE)  

---

## ▶️ Como Executar
1. **Clonar o repositório**  
   ```bash
   git clone https://github.com/seu-usuario/tabelafipe.git
   cd tabelafipe
   ```

2. **Compilar o projeto** (com Maven)  
   ```bash
   mvn clean install
   ```

3. **Executar a aplicação**  
   ```bash
   java -jar target/tabelafipe-1.0.jar
   ```

4. **Seguir as instruções no console** para buscar e filtrar veículos.  

---

## 📌 Observações
- Este projeto tem foco em **aprendizado** e **demonstração de consumo de APIs** em Java.  
- Não há persistência em banco de dados, todos os dados vêm **diretamente da API FIPE** no momento da consulta.  
