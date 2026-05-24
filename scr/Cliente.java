package scr;
public class Cliente {
    private final String nome;

    public Cliente(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio!");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}