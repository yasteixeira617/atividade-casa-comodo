import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

// Tela principal: mostra a casa, a lista de cômodos e o formulário para adicionar novos
public class JanelaCasa extends JFrame {

    private final Casa casa;

    private final JPanel painelLista = new JPanel();
    private final JLabel rotuloQuantidade = new JLabel();
    private final JLabel rotuloAreaTotal = new JLabel();
    private final Componentes.CampoTexto campoTipo = new Componentes.CampoTexto("Ex.: Escritório");
    private final Componentes.CampoTexto campoTamanho = new Componentes.CampoTexto("Ex.: 8,5");
    private final JLabel rotuloMensagem = new JLabel(" ");
    private JScrollPane rolagemLista;

    public JanelaCasa(Casa casa) {
        super("Minha Casa — Gerenciamento de cômodos");
        this.casa = casa;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(criarImagemIcone());

        JPanel raiz = new JPanel(new BorderLayout(0, 22));
        raiz.setBackground(Tema.FUNDO);
        raiz.setBorder(new EmptyBorder(30, 36, 30, 36));
        raiz.add(criarCabecalho(), BorderLayout.NORTH);
        raiz.add(criarConteudo(), BorderLayout.CENTER);
        raiz.add(criarFormulario(), BorderLayout.SOUTH);
        setContentPane(raiz);

        atualizarTela();

        // Ajusta o tamanho da janela à tela disponível (útil em notebooks)
        Rectangle telaDisponivel = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setMinimumSize(new Dimension(680, 600));
        setSize(Math.min(900, telaDisponivel.width), Math.min(860, telaDisponivel.height));
        setLocationRelativeTo(null);
    }

    // ---------- Montagem da tela ----------

    private JComponent criarCabecalho() {
        JLabel titulo = new JLabel("Minha Casa");
        titulo.setFont(Tema.titulo(34));
        titulo.setForeground(Tema.TEXTO);

        JLabel subtitulo = new JLabel("Gerenciamento de cômodos");
        subtitulo.setFont(Tema.texto(Font.PLAIN, 15));
        subtitulo.setForeground(Tema.TEXTO_SUAVE);

        JPanel textos = painelTransparente();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(subtitulo);

        JPanel linhaTitulo = painelTransparente();
        linhaTitulo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaTitulo.add(new JLabel(new Componentes.IconeCasa(56)));
        linhaTitulo.add(Box.createHorizontalStrut(18));
        linhaTitulo.add(textos);

        JPanel cabecalho = new JPanel(new BorderLayout(0, 20));
        cabecalho.setOpaque(false);
        cabecalho.add(linhaTitulo, BorderLayout.CENTER);
        cabecalho.add(new Componentes.LinhaDegrade(), BorderLayout.SOUTH);
        return cabecalho;
    }

    private JComponent criarConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(0, 18));
        conteudo.setOpaque(false);
        conteudo.add(criarPainelInformacoes(), BorderLayout.NORTH);
        conteudo.add(criarPainelLista(), BorderLayout.CENTER);
        return conteudo;
    }

    /** Cartões com endereço, quantidade de cômodos e área total. */
    private JComponent criarPainelInformacoes() {
        JLabel rotuloEndereco = new JLabel(casa.getEndereco());
        rotuloEndereco.setFont(Tema.texto(Font.BOLD, 18));
        rotuloEndereco.setForeground(Tema.TEXTO);

        estilizarNumero(rotuloQuantidade);
        estilizarNumero(rotuloAreaTotal);

        JPanel painel = painelTransparente();
        painel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;

        c.gridx = 0;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 14);
        painel.add(criarCartaoInformacao("ENDEREÇO", rotuloEndereco), c);

        c.gridx = 1;
        c.weightx = 0;
        painel.add(criarCartaoInformacao("CÔMODOS", rotuloQuantidade), c);

        c.gridx = 2;
        c.insets = new Insets(0, 0, 0, 0);
        painel.add(criarCartaoInformacao("ÁREA TOTAL", rotuloAreaTotal), c);
        return painel;
    }

    private JComponent criarCartaoInformacao(String titulo, JLabel valor) {
        Componentes.PainelArredondado cartao =
                new Componentes.PainelArredondado(Tema.SUPERFICIE, Tema.BORDA, 16);
        cartao.setLayout(new BorderLayout(0, 6));
        cartao.setBorder(new EmptyBorder(16, 20, 16, 24));
        cartao.add(criarRotuloSecao(titulo), BorderLayout.NORTH);
        cartao.add(valor, BorderLayout.CENTER);
        // Largura mínima fixa para o cartão não "pular" quando o número muda
        cartao.setPreferredSize(new Dimension(150, cartao.getPreferredSize().height));
        return cartao;
    }

    private JComponent criarPainelLista() {
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
        painelLista.setOpaque(false);

        // Mantém os cartões no topo, mesmo quando há poucos cômodos
        JPanel alinhadorTopo = new JPanel(new BorderLayout());
        alinhadorTopo.setOpaque(false);
        alinhadorTopo.add(painelLista, BorderLayout.NORTH);

        rolagemLista = new JScrollPane(alinhadorTopo);
        rolagemLista.setBorder(BorderFactory.createEmptyBorder());
        rolagemLista.setOpaque(false);
        rolagemLista.getViewport().setOpaque(false);
        rolagemLista.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollBar barra = rolagemLista.getVerticalScrollBar();
        barra.setUI(new Componentes.BarraRolagemUI());
        barra.setPreferredSize(new Dimension(10, 0));
        barra.setOpaque(false);
        barra.setUnitIncrement(16);

        Componentes.PainelArredondado cartao =
                new Componentes.PainelArredondado(Tema.SUPERFICIE, Tema.BORDA, 16);
        cartao.setLayout(new BorderLayout(0, 14));
        cartao.setBorder(new EmptyBorder(18, 20, 18, 14));
        cartao.add(criarRotuloSecao("LISTA DE CÔMODOS"), BorderLayout.NORTH);
        cartao.add(rolagemLista, BorderLayout.CENTER);
        return cartao;
    }

    /** Cartão de um cômodo: número, tipo, proporção da área e tamanho em m². */
    private JComponent criarLinhaComodo(int numero, Comodo comodo, double areaTotal) {
        double proporcao = areaTotal > 0 ? comodo.getTamanho() / areaTotal : 0;

        JLabel rotuloNumero = new JLabel(String.format("%02d", numero));
        rotuloNumero.setFont(Tema.texto(Font.BOLD, 13));
        rotuloNumero.setForeground(Tema.VERMELHO_ESCURO);
        rotuloNumero.setPreferredSize(new Dimension(28, 20));

        JLabel rotuloTipo = new JLabel(comodo.getTipo());
        rotuloTipo.setFont(Tema.texto(Font.BOLD, 16));
        rotuloTipo.setForeground(Tema.TEXTO);
        rotuloTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rotuloPercentual = new JLabel(Math.round(proporcao * 100) + "% da área total");
        rotuloPercentual.setFont(Tema.texto(Font.PLAIN, 12));
        rotuloPercentual.setForeground(Tema.TEXTO_SUAVE);
        rotuloPercentual.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textos = painelTransparente();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(rotuloTipo);
        textos.add(Box.createVerticalStrut(3));
        textos.add(rotuloPercentual);
        textos.add(Box.createVerticalStrut(7));
        textos.add(new Componentes.BarraProporcao(proporcao));

        JLabel rotuloTamanho = new JLabel(comodo.getTamanhoFormatado(), SwingConstants.CENTER);
        rotuloTamanho.setFont(Tema.texto(Font.BOLD, 15));
        rotuloTamanho.setForeground(Tema.TEXTO);
        Componentes.PainelArredondado etiquetaTamanho =
                new Componentes.PainelArredondado(Tema.VINHO_ESCURO, Tema.VINHO, 14);
        etiquetaTamanho.setLayout(new BorderLayout());
        etiquetaTamanho.setBorder(new EmptyBorder(8, 18, 8, 18));
        etiquetaTamanho.add(rotuloTamanho);
        etiquetaTamanho.setPreferredSize(new Dimension(96, etiquetaTamanho.getPreferredSize().height));

        JPanel direita = painelTransparente();
        direita.setLayout(new GridBagLayout()); // centraliza a etiqueta verticalmente
        direita.add(etiquetaTamanho);

        Componentes.PainelArredondado linha =
                new Componentes.PainelArredondado(Tema.SUPERFICIE_ELEVADA, Tema.BORDA, 14);
        linha.setLayout(new BorderLayout(12, 0));
        linha.setBorder(new EmptyBorder(12, 18, 12, 16));
        linha.add(rotuloNumero, BorderLayout.WEST);
        linha.add(textos, BorderLayout.CENTER);
        linha.add(direita, BorderLayout.EAST);

        // Destaca a borda em vinho quando o mouse passa por cima
        linha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                linha.setCorBorda(Tema.VINHO_CLARO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                linha.setCorBorda(Tema.BORDA);
            }
        });

        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, linha.getPreferredSize().height));
        return linha;
    }

    private JComponent criarFormulario() {
        Componentes.BotaoModerno botaoAdicionar = new Componentes.BotaoModerno("+  Adicionar cômodo");
        botaoAdicionar.addActionListener(e -> adicionarComodo());

        // Pressionar Enter em qualquer campo também adiciona
        campoTipo.addActionListener(e -> adicionarComodo());
        campoTamanho.addActionListener(e -> adicionarComodo());

        rotuloMensagem.setFont(Tema.texto(Font.PLAIN, 13));

        Componentes.PainelArredondado cartao =
                new Componentes.PainelArredondado(Tema.SUPERFICIE, Tema.BORDA, 16);
        cartao.setLayout(new GridBagLayout());
        cartao.setBorder(new EmptyBorder(18, 20, 16, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0, 0, 14, 0);
        cartao.add(criarRotuloSecao("NOVO CÔMODO"), c);

        c.gridwidth = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 6, 12);
        cartao.add(criarRotuloCampo("Tipo"), c);
        c.gridx = 1;
        cartao.add(criarRotuloCampo("Tamanho (m²)"), c);

        c.gridy = 2;
        c.gridx = 0;
        c.weightx = 0.6;
        c.insets = new Insets(0, 0, 0, 12);
        cartao.add(campoTipo, c);
        c.gridx = 1;
        c.weightx = 0.4;
        cartao.add(campoTamanho, c);
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.insets = new Insets(0, 0, 0, 0);
        cartao.add(botaoAdicionar, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 2, 0, 0);
        cartao.add(rotuloMensagem, c);
        return cartao;
    }

    // ---------- Ações ----------

    private void adicionarComodo() {
        String tipo = campoTipo.getText();
        String textoTamanho = campoTamanho.getText().trim().replace(',', '.');

        double tamanho;
        try {
            tamanho = Double.parseDouble(textoTamanho);
        } catch (NumberFormatException erro) {
            mostrarMensagem("Informe um tamanho válido em m² (ex.: 8 ou 8,5).", true);
            campoTamanho.requestFocusInWindow();
            return;
        }

        try {
            Comodo novoComodo = new Comodo(tipo, tamanho);
            casa.adicionarComodo(novoComodo);
            mostrarMensagem(novoComodo + " adicionado à casa.", false);
        } catch (IllegalArgumentException erro) {
            // A própria classe Comodo valida os dados e explica o problema
            mostrarMensagem(erro.getMessage(), true);
            return;
        }

        campoTipo.setText("");
        campoTamanho.setText("");
        campoTipo.requestFocusInWindow();
        atualizarTela();

        // Rola a lista até o cômodo recém-adicionado
        SwingUtilities.invokeLater(() -> {
            JScrollBar barra = rolagemLista.getVerticalScrollBar();
            barra.setValue(barra.getMaximum());
        });
    }

    /** Redesenha a lista e os totais a partir dos dados da Casa. */
    private void atualizarTela() {
        double areaTotal = casa.calcularAreaTotal();

        painelLista.removeAll();
        int numero = 1;
        for (Comodo comodo : casa.getComodos()) {
            if (numero > 1) {
                painelLista.add(Box.createVerticalStrut(10));
            }
            painelLista.add(criarLinhaComodo(numero, comodo, areaTotal));
            numero++;
        }

        if (casa.getQuantidadeComodos() == 0) {
            JLabel vazio = new JLabel("Nenhum cômodo cadastrado ainda.");
            vazio.setFont(Tema.texto(Font.ITALIC, 14));
            vazio.setForeground(Tema.TEXTO_APAGADO);
            painelLista.add(vazio);
        }

        rotuloQuantidade.setText(String.valueOf(casa.getQuantidadeComodos()));
        rotuloAreaTotal.setText(Comodo.formatarArea(areaTotal));

        painelLista.revalidate();
        painelLista.repaint();
    }

    private void mostrarMensagem(String texto, boolean erro) {
        rotuloMensagem.setForeground(erro ? Tema.ERRO : Tema.TEXTO_SUAVE);
        rotuloMensagem.setText(texto);
    }

    // ---------- Pequenos auxiliares de estilo ----------

    private static JPanel painelTransparente() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        return painel;
    }

    private static JLabel criarRotuloSecao(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.texto(Font.BOLD, 11));
        rotulo.setForeground(Tema.VERMELHO_ESCURO);
        return rotulo;
    }

    private static JLabel criarRotuloCampo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.texto(Font.PLAIN, 13));
        rotulo.setForeground(Tema.TEXTO_SUAVE);
        return rotulo;
    }

    private static void estilizarNumero(JLabel rotulo) {
        rotulo.setFont(Tema.titulo(22));
        rotulo.setForeground(Tema.TEXTO);
    }

    private static BufferedImage criarImagemIcone() {
        Componentes.IconeCasa icone = new Componentes.IconeCasa(64);
        BufferedImage imagem = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        var g = imagem.createGraphics();
        icone.paintIcon(null, g, 0, 0);
        g.dispose();
        return imagem;
    }
}
