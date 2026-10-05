import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

// Identidade visual do projeto: cores e fontes usadas em toda a interface
public final class Tema {

    public static final Color FUNDO = new Color(0x0B0B0D);
    public static final Color SUPERFICIE = new Color(0x141416);
    public static final Color SUPERFICIE_ELEVADA = new Color(0x1B1B1E);
    public static final Color CAMPO = new Color(0x101012);
    public static final Color BORDA = new Color(0x2A2A2F);

    public static final Color VINHO = new Color(0x6B1426);
    public static final Color VINHO_CLARO = new Color(0x84192F);
    public static final Color VINHO_ESCURO = new Color(0x3A0A15);
    public static final Color VERMELHO_ESCURO = new Color(0x9E1B24);

    public static final Color TEXTO = new Color(0xF4EFEA);
    public static final Color TEXTO_SUAVE = new Color(0xA39E99);
    public static final Color TEXTO_APAGADO = new Color(0x6A6662);
    public static final Color ERRO = new Color(0xD0505B);

    private static final List<String> FONTES_INSTALADAS = Arrays.asList(
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

    private static final String FONTE_TEXTO = escolherFonte("Segoe UI", "Helvetica Neue", "Arial");
    private static final String FONTE_TITULO = escolherFonte("Georgia", "Times New Roman", "Serif");

    private Tema() {
    }

    public static Font texto(int estilo, int tamanho) {
        return new Font(FONTE_TEXTO, estilo, tamanho);
    }

    public static Font titulo(int tamanho) {
        return new Font(FONTE_TITULO, Font.BOLD, tamanho);
    }

    private static String escolherFonte(String... opcoes) {
        for (String fonte : opcoes) {
            if (FONTES_INSTALADAS.contains(fonte)) {
                return fonte;
            }
        }
        return Font.SANS_SERIF;
    }
}
