import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Casa {

    private final String endereco;
    private final List<Comodo> comodos;

    public Casa(String endereco) {
        this.endereco = endereco;
        this.comodos = new ArrayList<>();
    }

    public void adicionarComodo(Comodo comodo) {
        comodos.add(comodo);
    }

    public String getEndereco() {
        return endereco;
    }

    // Devolve a lista "somente leitura": para incluir cômodos é preciso usar adicionarComodo()
    public List<Comodo> getComodos() {
        return Collections.unmodifiableList(comodos);
    }

    public int getQuantidadeComodos() {
        return comodos.size();
    }

    public double calcularAreaTotal() {
        double total = 0;
        for (Comodo comodo : comodos) {
            total += comodo.getTamanho();
        }
        return total;
    }
}
