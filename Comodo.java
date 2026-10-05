import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Comodo {

    // Formata números no padrão brasileiro: 10 -> "10", 8.5 -> "8,5"
    private static final DecimalFormat FORMATO_AREA =
            new DecimalFormat("#,##0.##", DecimalFormatSymbols.getInstance(Locale.of("pt", "BR")));

    private final String tipo;
    private final double tamanho;

    public Comodo(String tipo, double tamanho) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Informe o tipo do cômodo.");
        }
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho deve ser maior que zero.");
        }
        this.tipo = tipo.trim();
        this.tamanho = tamanho;
    }

    public String getTipo() {
        return tipo;
    }

    public double getTamanho() {
        return tamanho;
    }

    public String getTamanhoFormatado() {
        return formatarArea(tamanho);
    }

    public static String formatarArea(double valor) {
        return FORMATO_AREA.format(valor) + " m²";
    }

    @Override
    public String toString() {
        return tipo + ": " + getTamanhoFormatado();
    }
}
