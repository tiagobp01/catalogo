package br.com.unicsul.catalogo.service.impl;

import br.com.unicsul.catalogo.domain.Produto;
import br.com.unicsul.catalogo.service.ProdutoService;
import br.com.unicsul.catalogo.service.RelatorioService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import br.com.unicsul.catalogo.domain.Contato;
import br.com.unicsul.catalogo.domain.Usuario;
import br.com.unicsul.catalogo.domain.UsuarioPerfil;
import br.com.unicsul.catalogo.repository.ContatoRepository;
import br.com.unicsul.catalogo.repository.UsuarioRepository;

@Service
public class RelatorioServiceImpl implements RelatorioService {
    
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    
    private final ProdutoService produtoService;
    private final UsuarioRepository usuarioRepository;
    private final ContatoRepository contatoRepository;
    
    @Autowired
    public RelatorioServiceImpl(ProdutoService produtoService, 
                              UsuarioRepository usuarioRepository,
                              ContatoRepository contatoRepository) {
        this.produtoService = produtoService;
        this.usuarioRepository = usuarioRepository;
        this.contatoRepository = contatoRepository;
    }
    
    @Override
    public byte[] gerarRelatorio(String tipo, String dataInicio, String dataFim) {
        Document document = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Cabeçalho
            addHeader(document);
            
            // Título do relatório
            addTitle(document, "Relatório de " + getTituloRelatorio(tipo));
            

            // Conteúdo específico do relatório
            addConteudoRelatorio(document, tipo);
            
            // Rodapé
            addFooter(document);
            
            document.close();
            return baos.toByteArray();
            
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Erro ao gerar o documento do relatório: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao gerar relatório: " + e.getMessage(), e);
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }
    
    private void addHeader(Document document) throws DocumentException {
        Paragraph header = new Paragraph();
        header.setAlignment(Element.ALIGN_RIGHT);
        header.add(new Chunk("Catálogo de Produtos\n", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC)));
        header.add(new Chunk("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        document.add(header);
        
        // Adiciona uma linha separadora
        document.add(new Paragraph("\n\n"));
    }
    
    private void addTitle(Document document, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, TITLE_FONT);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(20);
        document.add(paragraph);
    }
    
    private void addSubtitle(Document document, String subtitle) throws DocumentException {
        Paragraph paragraph = new Paragraph(subtitle, SUBTITLE_FONT);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(15);
        document.add(paragraph);
    }
    
    private void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph("\n\n"));
        Paragraph footer = new Paragraph("Sistema de Catálogo - Todos os direitos reservados", 
                                       new Font(Font.FontFamily.HELVETICA, 8));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
    
    private String formatarData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "Período não especificado";
        }
        try {
            LocalDate date = LocalDate.parse(data);
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            return data; // Retorna a string original se não for possível fazer o parse
        }
    }
    
    private String getTituloRelatorio(String tipo) {
        if (tipo == null) {
            return "Geral";
        }
        switch (tipo.toLowerCase()) {
            case "produtos":
                return "Produtos Cadastrados";
            case "usuarios":
                return "Usuários do Sistema";
            case "contatos":
                return "Mensagens de Contato";
            default:
                return "Geral";
        }
    }
    
    private void addConteudoRelatorio(Document document, String tipo )
            throws DocumentException, IOException {
        
        if ("produtos".equalsIgnoreCase(tipo)) {
            gerarRelatorioProdutos(document);
        } else if ("usuarios".equalsIgnoreCase(tipo)) {
            gerarRelatorioUsuarios(document);
        } else if ("contatos".equalsIgnoreCase(tipo)) {
            gerarRelatorioContatos(document);
        } else {
            // Relatório padrão para outros tipos
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            
            addTableHeader(table, "Item", "Descrição", "Quantidade");
            addTableRow(table, "1", "Produtos Ativos", "150");
            addTableRow(table, "2", "Usuários Cadastrados", "42");
            addTableRow(table, "3", "Mensagens Recebidas", "87");
            
            document.add(table);
        }
    }
    
    private void gerarRelatorioProdutos(Document document) 
            throws DocumentException, IOException {
        
        // Buscar todos os produtos ativos
        List<Produto> produtos = produtoService.listarTodos(Pageable.unpaged()).getContent();
        
        // Adicionar título principal
        Paragraph titulo = new Paragraph("Relatório de Produtos", TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        // Adicionar data de geração
        Paragraph dataGeracao = new Paragraph("Gerado em: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT);
        dataGeracao.setAlignment(Element.ALIGN_RIGHT);
        dataGeracao.setSpacingAfter(30);
        document.add(dataGeracao);
        
        // Adicionar cada produto em uma seção separada
        for (Produto produto : produtos) {
            // Adicionar linha divisória entre produtos (exceto antes do primeiro)
            if (produtos.indexOf(produto) > 0) {
                document.add(new Paragraph("\n"));
                document.add(new Chunk(new LineSeparator()));
                document.add(new Paragraph("\n"));
            }
            
            // Título do produto
            Paragraph nomeProduto = new Paragraph(produto.getNome(), new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            nomeProduto.setSpacingAfter(15);
            document.add(nomeProduto);
            
            // Tabela com informações do produto
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3});
            
            // Adicionar informações básicas
            addTableRowWithLabel(table, "Categoria:", 
                produto.getCategoria() != null ? produto.getCategoria().getNome() : "Não informada");
                
            if (produto.getAutor() != null) {
                addTableRowWithLabel(table, "Autor:", produto.getAutor().getNome());
            }
            
            if (produto.getDiretor() != null) {
                addTableRowWithLabel(table, "Diretor:", produto.getDiretor().getNome());
            }
            
            addTableRowWithLabel(table, "Data de Lançamento:", 
                produto.getDataLancamento() != null ? 
                    produto.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : 
                    "Não informada");
                    
            // Preço formatado
            addTableRowWithLabel(table, "Preço:", "R$ " + String.format("%.2f", produto.getPreco()));
            
            // Descrição
            if (produto.getDescricao() != null && !produto.getDescricao().trim().isEmpty()) {
                addTableRowWithLabel(table, "Descrição:", produto.getDescricao());
            }
            
            // Datas de cadastro e atualização
            addTableRowWithLabel(table, "Data de Cadastro:", 
                produto.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                
            addTableRowWithLabel(table, "Última Atualização:", 
                produto.getDataAtualizacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            document.add(table);
        }
        
        // Adicionar rodapé com total de produtos
        Paragraph totalProdutos = new Paragraph(
            String.format("Total de produtos: %d", produtos.size()), 
            new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
        totalProdutos.setAlignment(Element.ALIGN_RIGHT);
        totalProdutos.setSpacingBefore(20);
        document.add(totalProdutos);
    }
    
    private void addTableRowWithLabel(PdfPTable table, String label, String value) {
        // Célula do rótulo
        PdfPCell labelCell = new PdfPCell(new Phrase(label, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        labelCell.setBackgroundColor(new BaseColor(248, 249, 250)); // Cor de fundo suave
        table.addCell(labelCell);
        
        // Célula do valor
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "-", NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }
    
    private Image base64ToImage(String base64) throws IOException, BadElementException {
        try {
            // Remover o cabeçalho da string base64 se existir
            String base64Image = base64.split(",")[1];
            byte[] imageBytes = Base64.decodeBase64(base64Image);
            return Image.getInstance(imageBytes);
        } catch (Exception e) {
            // Se falhar, tentar decodificar diretamente
            try {
                byte[] imageBytes = Base64.decodeBase64(base64);
                return Image.getInstance(imageBytes);
            } catch (Exception ex) {
                throw new IOException("Erro ao converter imagem base64", ex);
            }
        }
    }
    
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(new BaseColor(51, 51, 51)); // Cor mais escura
            cell.setBorderWidth(1);
            cell.setPadding(8);
            cell.setPhrase(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    
    private void addTableRow(PdfPTable table, String... cells) {
        for (String cell : cells) {
            PdfPCell cellElement = new PdfPCell(new Phrase(cell, NORMAL_FONT));
            cellElement.setBorderWidth(0.5f);
            cellElement.setPadding(5);
            cellElement.setBorderColor(BaseColor.LIGHT_GRAY);
            table.addCell(cellElement);
        }
    }
    
    private void gerarRelatorioUsuarios(Document document) 
            throws DocumentException, IOException {
        
        // Buscar todos os usuários com seus perfis
        List<Usuario> usuarios = usuarioRepository.findAllWithPerfis();
        
        // Adicionar título da seção
        Paragraph titulo = new Paragraph("Relatório de Usuários", TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        // Adicionar data de geração
        Paragraph dataGeracao = new Paragraph("Gerado em: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT);
        dataGeracao.setAlignment(Element.ALIGN_RIGHT);
        dataGeracao.setSpacingAfter(20);
        document.add(dataGeracao);
        
        // Criar tabela com 4 colunas: Nome, E-mail, Status, Perfis
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 4, 2, 4});
        
        // Cabeçalho da tabela
        addTableHeader(table, "Nome", "E-mail", "Status", "Perfis");
        
        // Adicionar usuários à tabela
        for (Usuario usuario : usuarios) {
            addUsuarioToTable(table, usuario);
        }
        
        document.add(table);
        
        // Adicionar resumo
        addResumoUsuarios(document, usuarios);
    }
    
    private void addUsuarioToTable(PdfPTable table, Usuario usuario) {
        // Coluna do nome
        PdfPCell cellNome = new PdfPCell(new Phrase(usuario.getNome(), NORMAL_FONT));
        cellNome.setBorder(Rectangle.NO_BORDER);
        cellNome.setPadding(5);
        table.addCell(cellNome);
        
        // Coluna do e-mail
        PdfPCell cellEmail = new PdfPCell(new Phrase(usuario.getEmail(), NORMAL_FONT));
        cellEmail.setBorder(Rectangle.NO_BORDER);
        cellEmail.setPadding(5);
        table.addCell(cellEmail);
        
        // Coluna do status
        String status = usuario.isAtivo() ? "Ativo" : "Inativo";
        Font statusFont = new Font(NORMAL_FONT);
        statusFont.setColor(usuario.isAtivo() ? BaseColor.GREEN.darker() : BaseColor.RED);
        
        PdfPCell cellStatus = new PdfPCell(new Phrase(status, statusFont));
        cellStatus.setBorder(Rectangle.NO_BORDER);
        cellStatus.setPadding(5);
        cellStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellStatus);
        
        // Coluna dos perfis
        String perfis = usuario.getPerfis().stream()
                .map(up -> up.getPerfil().getNome())
                .collect(java.util.stream.Collectors.joining(", "));
                
        if (perfis.isEmpty()) {
            perfis = "Sem perfil atribuído";
        }
        
        PdfPCell cellPerfis = new PdfPCell(new Phrase(perfis, NORMAL_FONT));
        cellPerfis.setBorder(Rectangle.NO_BORDER);
        cellPerfis.setPadding(5);
        table.addCell(cellPerfis);
    }
    
    private void gerarRelatorioContatos(Document document) 
            throws DocumentException, IOException {
        
        // Buscar todos os contatos
        List<Contato> contatos = contatoRepository.findAll();
        
        // Adicionar título da seção
        Paragraph titulo = new Paragraph("Relatório de Contatos", TITLE_FONT);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);
        
        // Adicionar data de geração
        Paragraph dataGeracao = new Paragraph("Gerado em: " + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), NORMAL_FONT);
        dataGeracao.setAlignment(Element.ALIGN_RIGHT);
        dataGeracao.setSpacingAfter(20);
        document.add(dataGeracao);
        
        // Criar tabela com 5 colunas: Nome, E-mail, Telefone, Data, Mensagem
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 2, 2, 4});
        
        // Cabeçalho da tabela
        addTableHeader(table, "Nome", "E-mail", "Telefone", "Data", "Mensagem");
        
        // Adicionar contatos à tabela
        for (Contato contato : contatos) {
            addContatoToTable(table, contato);
        }
        
        document.add(table);
        
        // Adicionar resumo
        addResumoContatos(document, contatos);
    }
    
    private void addContatoToTable(PdfPTable table, Contato contato) {
        // Coluna do nome
        PdfPCell cellNome = new PdfPCell(new Phrase(contato.getNome(), NORMAL_FONT));
        cellNome.setBorder(Rectangle.NO_BORDER);
        cellNome.setPadding(5);
        table.addCell(cellNome);
        
        // Coluna do e-mail
        PdfPCell cellEmail = new PdfPCell(new Phrase(contato.getEmail(), NORMAL_FONT));
        cellEmail.setBorder(Rectangle.NO_BORDER);
        cellEmail.setPadding(5);
        table.addCell(cellEmail);
        
        // Coluna do telefone
        String telefone = (contato.getDdd() != null ? "(" + contato.getDdd() + ") " : "") + 
                         (contato.getTelefone() != null ? contato.getTelefone() : "");
        PdfPCell cellTelefone = new PdfPCell(new Phrase(telefone, NORMAL_FONT));
        cellTelefone.setBorder(Rectangle.NO_BORDER);
        cellTelefone.setPadding(5);
        table.addCell(cellTelefone);
        
        // Coluna da data de envio
        String dataEnvio = "";
        try {
            dataEnvio = contato.getDataEnvio() != null ? 
                contato.getDataEnvio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
        } catch (Exception e) {
            dataEnvio = "N/A";
        }
        
        PdfPCell cellData = new PdfPCell(new Phrase(dataEnvio, NORMAL_FONT));
        cellData.setBorder(Rectangle.NO_BORDER);
        cellData.setPadding(5);
        table.addCell(cellData);
        
        // Coluna da mensagem (limitada a 50 caracteres)
        String mensagem = contato.getMensagem();
        if (mensagem.length() > 50) {
            mensagem = mensagem.substring(0, 47) + "...";
        }
        PdfPCell cellMensagem = new PdfPCell(new Phrase(mensagem, NORMAL_FONT));
        cellMensagem.setBorder(Rectangle.NO_BORDER);
        cellMensagem.setPadding(5);
        table.addCell(cellMensagem);
    }
    
    private void addResumoContatos(Document document, List<Contato> contatos) 
            throws DocumentException {
        
        // Adicionar espaço antes do resumo
        document.add(new Paragraph("\n"));
        
        // Adicionar título do resumo
        Paragraph tituloResumo = new Paragraph("Resumo", SUBTITLE_FONT);
        tituloResumo.setSpacingBefore(20);
        tituloResumo.setSpacingAfter(10);
        document.add(tituloResumo);
        
        // Adicionar total de contatos
        document.add(new Paragraph(String.format("Total de Contatos: %d", contatos.size()), NORMAL_FONT));
    }
    
    private void addResumoUsuarios(Document document, List<Usuario> usuarios) 
            throws DocumentException {
        
        // Contar usuários ativos e inativos
        long totalAtivos = usuarios.stream().filter(Usuario::isAtivo).count();
        long totalInativos = usuarios.size() - totalAtivos;
        
        // Contar perfis
        Map<String, Long> contagemPerfis = new HashMap<>();
        for (Usuario usuario : usuarios) {
            for (UsuarioPerfil up : usuario.getPerfis()) {
                String nomePerfil = up.getPerfil().getNome();
                contagemPerfis.put(nomePerfil, contagemPerfis.getOrDefault(nomePerfil, 0L) + 1);
            }
        }
        
        // Adicionar espaço antes do resumo
        document.add(new Paragraph("\n"));
        
        // Adicionar título do resumo
        Paragraph tituloResumo = new Paragraph("Resumo", SUBTITLE_FONT);
        tituloResumo.setSpacingBefore(20);
        tituloResumo.setSpacingAfter(10);
        document.add(tituloResumo);
        
        // Adicionar totais
        document.add(new Paragraph(String.format("Total de Usuários: %d", usuarios.size()), NORMAL_FONT));
        document.add(new Paragraph(String.format("Usuários Ativos: %d", totalAtivos), NORMAL_FONT));
        document.add(new Paragraph(String.format("Usuários Inativos: %d", totalInativos), NORMAL_FONT));
        
        // Adicionar contagem por perfil
        if (!contagemPerfis.isEmpty()) {
            document.add(new Paragraph("\nTotal por Perfil:", NORMAL_FONT));
            for (Map.Entry<String, Long> entry : contagemPerfis.entrySet()) {
                document.add(new Paragraph(String.format("- %s: %d", entry.getKey(), entry.getValue()), NORMAL_FONT));
            }
        }
    }
}
