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
        var menu = """
                Carro
                Moto
                Caminhão
                
                Digte uma das opções para consultar:
                
                """;

        System.out.println(menu);
        var opcao = leitura.nextLine();

        String endereco = "";

        if(opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        }else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        }
        else if (opcao.toLowerCase().contains("cam")) {
            endereco = URL_BASE + "caminhoes/marcas";
        }else {
            System.out.println("Veiculo não encontrado");
        }

        var json = consumoApi.obterDados(endereco);
        System.out.println(json);

        //Retorna uma lista das marcas conm dados(codigo e nome)
        var marcas = conversor.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //Retorna as marcas pelo input do user do codigo da marca

        System.out.println("\nInforme o código da marca para consulta.");
        var codigoMarca = leitura.nextLine();
        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumoApi.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\n modelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //Retorna  uma lista dos veiculos pelo input do nome
        System.out.printf("\nDigite um trecho do %s a ser buscado.", opcao);
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados");
        modelosFiltrados.forEach(System.out::println);

        //Retorna o veiculo pelo input do codigo
        System.out.println("\nDigite por favor o codigo do modelo para buscar os valores de avaliação");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumoApi.obterDados(endereco);

        List<Dados> anos = conversor.obterLista(json, Dados.class);
        
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);




    }


}
