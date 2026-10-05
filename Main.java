import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Criando a casa
        Casa casa = new Casa("Rua das Flores, 100");

        // Criando os cômodos
        Comodo sala = new Comodo("Sala", 10);
        Comodo cozinha = new Comodo("Cozinha", 6);
        Comodo quarto = new Comodo("Quarto", 12);
        Comodo banheiro = new Comodo("Banheiro", 4);

        // Adicionando os cômodos à casa
        casa.adicionarComodo(sala);
        casa.adicionarComodo(cozinha);
        casa.adicionarComodo(quarto);
        casa.adicionarComodo(banheiro);

        imprimirNoConsole(casa);

        // Abre a interface visual (se o computador tiver tela disponível)
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(() -> new JanelaCasa(casa).setVisible(true));
        }
    }

    private static void imprimirNoConsole(Casa casa) {
        System.out.println("================================");
        System.out.println("          MINHA CASA");
        System.out.println("================================");
        System.out.println("Endereço: " + casa.getEndereco());
        System.out.println();

        System.out.println("CÔMODOS:");
        for (Comodo comodo : casa.getComodos()) {
            System.out.println("- " + comodo);
        }

        System.out.println();
        System.out.println("Área total: " + Comodo.formatarArea(casa.calcularAreaTotal()));
    }
}
