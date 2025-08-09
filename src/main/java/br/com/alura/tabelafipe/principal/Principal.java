package br.com.alura.tabelafipe.principal;

import br.com.alura.tabelafipe.models.Dados;
import br.com.alura.tabelafipe.models.Modelos;
import br.com.alura.tabelafipe.models.Veiculo;
import br.com.alura.tabelafipe.services.ConsumoApi;
import br.com.alura.tabelafipe.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();
    

    public void exibeMenu() {
    	
    	System.out.println("""
    		    ===============================================
    		    | Consórcio Fipe Veículos para análise e compra |
    		    | Acesse nossa tabela e veja qual o melhor      |
    		    | veículo para o seu trabalho                   |
    		    ===============================================
    		    """);

    	
        String tipo = solicitarEntrada("Tipo (carros, motos, caminhoes): ").toLowerCase();
        if (!tipo.startsWith("car") && !tipo.startsWith("mot") && !tipo.startsWith("cam")) {
            System.out.println("Tipo inválido.");
            return;
        }
        if (tipo.startsWith("car")) tipo = "carros";
        if (tipo.startsWith("mot")) tipo = "motos";
        if (tipo.startsWith("cam")) tipo = "caminhoes";

        // Marca
        String codigoMarca = solicitarEntrada("\nCódigo da marca (ou ENTER para acessar a lista): ");
        if (codigoMarca.isBlank()) {
            List<Dados> marcas = buscarMarcas(tipo);
            marcas.forEach(m -> System.out.printf("%-6s | %s%n", m.codigo(), m.nome()));
            codigoMarca = solicitarEntrada("Código da marca: ");
        }

        // Modelos
        String trechoModelo = solicitarEntrada("\nCódigo do modelo (ou ENTER para acessar a lista): ");
        List<Dados> modelos = buscarModelos(tipo, codigoMarca);
        if (!trechoModelo.isBlank()) {
            modelos = filtrarModelos(modelos, trechoModelo);
        }

        if (modelos.isEmpty()) {
            System.out.println("Nenhum modelo encontrado.");
            return;
        }

        if (modelos.size() == 1) {
            System.out.println("Modelo encontrado: " + modelos.get(0).nome());
            exibirVeiculos(tipo, codigoMarca, modelos.get(0).codigo());
            return;
        }

        modelos.forEach(m -> System.out.printf("%-6s | %s%n", m.codigo(), m.nome()));
        String codigoModelo = solicitarEntrada("Código do modelo (ou ENTER para acessar a lista): ");
        if (codigoModelo.isBlank()) {
            System.out.println("Nenhum modelo selecionado.");
            return;
        }

        exibirVeiculos(tipo, codigoMarca, codigoModelo);
    }

    private void exibirVeiculos(String tipo, String codigoMarca, String codigoModelo) {
        List<Veiculo> veiculos = buscarVeiculosPorAno(tipo, codigoMarca, codigoModelo);

        System.out.println("\nAvaliações por ano:");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-10s | %-15s | %-25s | %-4s | %s%n", 
                          "Valor", "Marca", "Modelo", "Ano", "Combustível");
        System.out.println("--------------------------------------------------------------------------");

        for (Veiculo v : veiculos) {
            System.out.printf("%-10s | %-15s | %-25s | %-4d | %s%n",
                    v.valor(), v.marca(), v.modelo(), v.ano(), v.tipoCombustivel());
        }
    }


    private List<Dados> buscarMarcas(String tipo) {
        String url = URL_BASE + tipo + "/marcas";
        String json = consumoApi.obterDados(url);
        return conversor.obterLista(json, Dados.class)
                .stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .toList();
    }

    private List<Dados> buscarModelos(String tipo, String codigoMarca) {
        String url = URL_BASE + tipo + "/marcas/" + codigoMarca + "/modelos";
        String json = consumoApi.obterDados(url);
        Modelos modelos = conversor.obterDados(json, Modelos.class);
        return modelos.modelos()
                .stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .toList();
    }

    private List<Dados> filtrarModelos(List<Dados> modelos, String filtro) {
        return modelos.stream()
                .filter(m -> m.nome().toLowerCase().contains(filtro.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Veiculo> buscarVeiculosPorAno(String tipo, String codigoMarca, String codigoModelo) {
        String url = URL_BASE + tipo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos";
        String json = consumoApi.obterDados(url);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();
        for (Dados ano : anos) {
            String anoUrl = URL_BASE + tipo + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigo();
            String jsonAno = consumoApi.obterDados(anoUrl);
            Veiculo veiculo = conversor.obterDados(jsonAno, Veiculo.class);
            veiculos.add(veiculo);
        }
        return veiculos;
    }

    private String solicitarEntrada(String mensagem) {
        System.out.print(mensagem);
        return leitura.nextLine();
    }
}
