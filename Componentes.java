import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

// Componentes visuais personalizados (cantos arredondados, botão moderno etc.)
public final class Componentes {

    private Componentes() {
    }

    private static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    /** Painel com fundo e borda arredondados. */
    public static class PainelArredondado extends JPanel {
        private final Color corFundo;
        private Color corBorda;
        private final int raio;

        public PainelArredondado(Color corFundo, Color corBorda, int raio) {
            this.corFundo = corFundo;
            this.corBorda = corBorda;
            this.raio = raio;
            setOpaque(false);
        }

        public void setCorBorda(Color corBorda) {
            this.corBorda = corBorda;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            RoundRectangle2D forma = new RoundRectangle2D.Float(
                    0.5f, 0.5f, getWidth() - 1, getHeight() - 1, raio, raio);
            g2.setColor(corFundo);
            g2.fill(forma);
            if (corBorda != null) {
                g2.setColor(corBorda);
                g2.draw(forma);
            }
            g2.dispose();
        }
    }

    /** Botão vinho com cantos arredondados e efeito ao passar o mouse. */
    public static class BotaoModerno extends JButton {

        public BotaoModerno(String texto) {
            super(texto);
            setFont(Tema.texto(Font.BOLD, 14));
            setForeground(Tema.TEXTO);
            setBorder(new EmptyBorder(12, 26, 12, 26));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            Color cor;
            if (getModel().isPressed()) {
                cor = Tema.VINHO_ESCURO;
            } else if (getModel().isRollover()) {
                cor = Tema.VERMELHO_ESCURO;
            } else {
                cor = Tema.VINHO;
            }
            g2.setPaint(new GradientPaint(0, 0, cor.brighter(), 0, getHeight(), cor));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Campo de texto escuro, arredondado e com texto de exemplo (placeholder). */
    public static class CampoTexto extends JTextField {
        private final String textoExemplo;

        public CampoTexto(String textoExemplo) {
            this.textoExemplo = textoExemplo;
            setOpaque(false);
            setFont(Tema.texto(Font.PLAIN, 14));
            setForeground(Tema.TEXTO);
            setCaretColor(Tema.TEXTO);
            setSelectionColor(Tema.VINHO);
            setSelectedTextColor(Tema.TEXTO);
            setBorder(new EmptyBorder(11, 14, 11, 14));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setColor(Tema.CAMPO);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.setColor(hasFocus() ? Tema.VERMELHO_ESCURO : Tema.BORDA);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            if (getText().isEmpty() && !hasFocus()) {
                g2.setColor(Tema.TEXTO_APAGADO);
                g2.setFont(getFont());
                int y = (getHeight() + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
                g2.drawString(textoExemplo, getInsets().left, y);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Barra fina que mostra quanto um cômodo representa da área total. */
    public static class BarraProporcao extends JComponent {
        private final double proporcao;

        public BarraProporcao(double proporcao) {
            this.proporcao = Math.max(0, Math.min(1, proporcao));
            Dimension tamanho = new Dimension(220, 4);
            setPreferredSize(tamanho);
            setMaximumSize(tamanho);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setColor(Tema.BORDA);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
            int preenchido = (int) Math.round(getWidth() * proporcao);
            g2.setPaint(new GradientPaint(0, 0, Tema.VINHO, getWidth(), 0, Tema.VERMELHO_ESCURO));
            g2.fillRoundRect(0, 0, preenchido, getHeight(), 4, 4);
            g2.dispose();
        }
    }

    /** Linha decorativa que vai do vinho até desaparecer no fundo. */
    public static class LinhaDegrade extends JComponent {

        public LinhaDegrade() {
            setPreferredSize(new Dimension(10, 2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setPaint(new GradientPaint(0, 0, Tema.VERMELHO_ESCURO, getWidth() * 0.7f, 0, Tema.FUNDO));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    /** Ícone de casa desenhado com formas geométricas (não depende de imagens). */
    public static class IconeCasa implements Icon {
        private final int tamanho;

        public IconeCasa(int tamanho) {
            this.tamanho = tamanho;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = suavizar(g);
            g2.translate(x, y);
            float t = tamanho;

            g2.setPaint(new GradientPaint(0, 0, Tema.VINHO_CLARO, t, t, Tema.VINHO_ESCURO));
            g2.fill(new RoundRectangle2D.Float(0, 0, t, t, t * 0.3f, t * 0.3f));

            g2.setColor(Tema.TEXTO);
            g2.setStroke(new BasicStroke(t * 0.055f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D casa = new Path2D.Float();
            casa.moveTo(t * 0.24, t * 0.50);
            casa.lineTo(t * 0.50, t * 0.27);
            casa.lineTo(t * 0.76, t * 0.50);
            casa.moveTo(t * 0.32, t * 0.44);
            casa.lineTo(t * 0.32, t * 0.74);
            casa.lineTo(t * 0.68, t * 0.74);
            casa.lineTo(t * 0.68, t * 0.44);
            casa.moveTo(t * 0.45, t * 0.74);
            casa.lineTo(t * 0.45, t * 0.60);
            casa.lineTo(t * 0.55, t * 0.60);
            casa.lineTo(t * 0.55, t * 0.74);
            g2.draw(casa);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    /** Barra de rolagem fina e discreta, combinando com o tema escuro. */
    public static class BarraRolagemUI extends BasicScrollBarUI {

        @Override
        protected JButton createDecreaseButton(int orientacao) {
            return botaoInvisivel();
        }

        @Override
        protected JButton createIncreaseButton(int orientacao) {
            return botaoInvisivel();
        }

        private JButton botaoInvisivel() {
            JButton botao = new JButton();
            Dimension zero = new Dimension(0, 0);
            botao.setPreferredSize(zero);
            botao.setMinimumSize(zero);
            botao.setMaximumSize(zero);
            return botao;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle area) {
            // trilho transparente
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle area) {
            if (area.isEmpty()) {
                return;
            }
            Graphics2D g2 = suavizar(g);
            g2.setColor(isThumbRollover() ? Tema.VINHO_CLARO : new Color(0x3A3A40));
            g2.fillRoundRect(area.x + 2, area.y + 2, area.width - 4, area.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
