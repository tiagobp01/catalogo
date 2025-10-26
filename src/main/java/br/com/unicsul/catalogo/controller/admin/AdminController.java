package br.com.unicsul.catalogo.controller.admin;

import br.com.unicsul.catalogo.domain.Contato;
import br.com.unicsul.catalogo.service.ContatoService;
import br.com.unicsul.catalogo.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ContatoService contatoService;

    private final RelatorioService relatorioService;

    @Autowired
    public AdminController(ContatoService contatoService, RelatorioService relatorioService) {
        this.contatoService = contatoService;
        this.relatorioService = relatorioService;
    }

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Painel Administrativo");
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }
    
    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        model.addAttribute("pageTitle", "Relatórios");
        model.addAttribute("activePage", "relatorios");
        return "admin/relatorios/lista";
    }
    
    @GetMapping("/relatorios/gerar")
    public ResponseEntity<InputStreamResource> gerarRelatorio(
            @RequestParam("tipo") String tipo) {
        
        byte[] relatorio = relatorioService.gerarRelatorio(tipo, null, null);
        
        String nomeArquivo = "relatorio_" + tipo.toLowerCase() + "_" + LocalDate.now() + ".pdf";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + nomeArquivo)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(relatorio.length)
                .body(new InputStreamResource(new ByteArrayInputStream(relatorio)));
    }

    @GetMapping("/contatos")
    public String listarContatos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataEnvio,desc") String sort,
            @RequestParam(required = false) String search,
            Model model) {
        
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        
        Page<Contato> contatosPage;
        if (search != null && !search.trim().isEmpty()) {
            contatosPage = contatoService.buscarPorTermo(search, pageable);
        } else {
            contatosPage = contatoService.listarTodos(pageable);
        }
        
        model.addAttribute("pageTitle", "Gerenciar Contatos");
        model.addAttribute("activePage", "contatos");
        model.addAttribute("contatos", contatosPage);
        model.addAttribute("searchTerm", search != null ? search : "");
        model.addAttribute("sortField", sortParams[0]);
        model.addAttribute("sortDirection", sortParams[1]);
        
        return "admin/contatos/lista";
    }
    
    @GetMapping("/contatos/{id}")
    public String visualizarContato(@PathVariable Long id, Model model) {
        Optional<Contato> contatoOpt = contatoService.buscarPorId(id);
        if (contatoOpt.isPresent()) {
            model.addAttribute("pageTitle", "Detalhes do Contato");
            model.addAttribute("activePage", "contatos");
            model.addAttribute("contato", contatoOpt.get());
            return "admin/contatos/visualizar";
        }
        return "redirect:/admin/contatos";
    }
}
