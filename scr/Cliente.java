package scr;
public class Cliente {

    private final String nome;
    private final String email;
    private final String telefone;
    private final String logradouro;
    private final String bairro;
    
    public Cliente(String nome, String email, String telefone, String logradouro, String bairro) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio!");
        }
        this.nome = nome;

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser vazio!");
        }
        this.email = email;
        
        if (telefone == null ||telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone não pode ser vazio!");
        }
        this.telefone = telefone;

        if (logradouro == null || logradouro.trim().isEmpty()) {
            throw new IllegalArgumentException("O logradouro não pode ser vazio!");
        }
        this.logradouro = logradouro;

        if (bairro == null || bairro.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode ser vazio!");
        }
        this.bairro = bairro;
        
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    
    
} 
