# Catálogo Virtual - Unicsul

Sistema de catálogo virtual desenvolvido para a Unicsul utilizando Spring Boot.

## 📋 Pré-requisitos

- Java 21 ou superior
- Gradle 
- IDE de sua preferência (recomendado IntelliJ IDEA ou VS Code)

## 🚀 Como executar o projeto

1. **Clone o repositório**
   ```bash
   git clone https://github.com/tiagobp01/catalogo
   cd catalogo
   ```

2. **Execute a aplicação**
   ```bash
   # Usando o Gradle Wrapper (Linux/macOS)
   ./gradlew bootRun
   
   # Windows
   gradlew.bat bootRun
   ```

3. **Acesse a aplicação**
   - Aplicação: http://localhost:8080
   - Console H2 (banco de dados): http://localhost:8080/h2-console
     - JDBC URL: jdbc:h2:mem:testdb
     - User Name: sa
     - Password: (deixe em branco)

## 🔧 Configuração

### Variáveis de ambiente
Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```
# Configurações do banco de dados (H2 em memória por padrão)
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=

# Configurações de segurança
JWT_SECRET=seuSegredoMuitoSecretoAqui
```

## 🛠️ Desenvolvimento

### Estrutura do projeto
```
src/
├── main/
│   ├── java/br/com/unicsul/catalogo/
│   │   ├── config/         # Configurações do Spring
│   │   ├────── security/       # Configurações de segurança   
│   │   ├── controller/     # Controladores REST
│   │   ├── model/          # Entidades JPA
│   │   ├── repository/     # Repositórios Spring Data
│   │   ├── service/        # Lógica de negócios
│   │   └── CatalogoApplication.java
│   └── resources/
│       ├── static/         # Arquivos estáticos (CSS, JS, imagens)
│       ├── templates/      # Templates Thymeleaf
│       └── application.yml # Configurações da aplicação
└── test/                   # Testes automatizados
```

### Comandos úteis

```bash
# Executar testes
./gradlew test

# Construir o projeto
./gradlew build

# Executar verificações de qualidade de código
./gradlew check
```

## 🔒 Segurança

O sistema utiliza Spring Security com autenticação baseada em JWT. Os endpoints protegidos exigem um token de autenticação no cabeçalho das requisições.

## 📄 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🤝 Contribuição

1. Faça um Fork do projeto
2. Crie uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Adicione suas mudanças (`git add .`)
4. Comite suas mudanças (`git commit -m 'Add some AmazingFeature'`)
5. Faça o Push da Branch (`git push origin feature/AmazingFeature`)
6. Abra um Pull Request
