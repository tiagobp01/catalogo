package br.com.unicsul.catalogo.service;

public interface RelatorioService {
    byte[] gerarRelatorio(String tipo, String dataInicio, String dataFim);
}
