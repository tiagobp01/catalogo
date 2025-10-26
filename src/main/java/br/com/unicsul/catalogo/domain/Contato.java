package br.com.unicsul.catalogo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contat_con")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(name = "nm_contat_con", nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    @Column(name = "ds_email_con", nullable = false, length = 100)
    private String email;

    @Size(min = 2, max = 2, message = "O DDD deve ter 2 dígitos")
    @Pattern(regexp = "\\d{2}", message = "O DDD deve conter apenas números")
    @Column(name = "ds_telddd_con", length = 2)
    private String ddd;

    @Size(max = 15, message = "O número de telefone deve ter no máximo 15 caracteres")
    @Pattern(regexp = "^\\d{8,9}$", message = "Número de telefone inválido. Use apenas números (8 ou 9 dígitos)")
    @Column(name = "ds_telnum_con", length = 15)
    private String telefone;

    @NotBlank(message = "A mensagem é obrigatória")
    @Column(name = "mm_mensag_con", nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "dt_envio_con", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataEnvio;

    @PrePersist
    protected void onCreate() {
        if (this.dataEnvio == null) {
            this.dataEnvio = LocalDateTime.now();
        }
    }
    
    public String getTelefoneFormatado() {
        if (ddd != null && !ddd.isEmpty() && telefone != null && !telefone.isEmpty()) {
            return String.format("(%s) %s", ddd, telefone);
        }
        return "";
    }
}
