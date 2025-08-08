package br.com.alura.tabelafipe.models;

public record Dados(
        String codigo,
        String nome
) {

	@Override
	public String toString() {
	    return String.format("%-5s | %s", codigo, nome); 
	}
	
}
