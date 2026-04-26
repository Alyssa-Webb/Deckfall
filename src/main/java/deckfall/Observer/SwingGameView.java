package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.Entity;
import deckfall.Entity.IntentType;
import deckfall.Entity.Slayer;
import deckfall.Game.MoveTypes;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI aligned with {@link ConsoleLogger}: same turn menu (1–6), sub-flows for reading cards/enemies
 * and playing a card, and parallel observer log messages. Sprites: {@code <entity getName()>.png}
 * with a special case for "Demon King" → {@code demonlord.png}. Images are loaded from
 * {@code /resources/} on the classpath, or {@code src/main/java/resources/} as a dev fallback.
 */
public class SwingGameView extends JFrame implements GameEventObserver {

    private static final int SPRITE_PX = 88;

    private static final Color BG_ROOT = new Color(0x0d0d14);
    private static final Color BG_PANEL = new Color(0x13131f);
    private static final Color BORDER_MUTED = new Color(0x2a2a4a);
    private static final Color ACCENT_GOLD = new Color(0xc8a84b);
    private static final Color ACCENT_RED = new Color(0xc0392b);
    private static final Color ACCENT_GREEN = new Color(0x27ae60);
    private static final Color ACCENT_BLUE = new Color(0x2980b9);
    private static final Color TEXT_PRIMARY = new Color(0xe8e0d0);
    private static final Color TEXT_SECONDARY = new Color(0x8a8070);
    private static final Color BG_CARD = new Color(0x1a1a2e);
    private static final Color BG_CARD_HOVER = new Color(0x252540);
    private static final Color BG_CARD_SEL = new Color(0x1f1f35);
    private static final Color LOG_BG = new Color(0x0a0a10);
    private static final Color LOG_TEXT = new Color(0x9a9080);
    private static final Color TEXT_DIM = new Color(0x3a3a5a);
    private static final Color TEXT_CARD_POOR = new Color(0x3a3050);
    private static final Color HP_BG = new Color(0x0d0d14);
    private static final Color TARGET_HIGHLIGHT = new Color(0x2a2a1a);
    private static final Color INTENT_ATTACK_BG = new Color(0x2a0a0a);
    private static final Color INTENT_DEFEND_BG = new Color(0x0a0a2a);
    private static final Color INTENT_OTHER_BG = new Color(0x1a1a1a);
    private static final Color ENEMY_CELL_BG = new Color(0x16161f);

    private static final Map<String, String> SPRITE_NAME_TO_FILE = new HashMap<>();
    static {
        SPRITE_NAME_TO_FILE.put("Slayer", "slayer.png");
        SPRITE_NAME_TO_FILE.put("Skeleton", "skeleton.png");
        SPRITE_NAME_TO_FILE.put("Goblin", "goblin.png");
        SPRITE_NAME_TO_FILE.put("Troll", "troll.png");
        SPRITE_NAME_TO_FILE.put("Demon King", "demonlord.png");
    }

    private static Font fontTitleGold() {
        return safeSerif("Palatino Linotype", Font.BOLD, 13);
    }

    private static Font fontHeader() {
        return safeSerif("Palatino Linotype", Font.BOLD, 14);
    }

    private static Font fontBody() {
        return safeSerif("Palatino Linotype", Font.PLAIN, 12);
    }

    private static Font fontBodyItalic() {
        return safeSerif("Palatino Linotype", Font.ITALIC, 12);
    }

    private static Font fontLog() {
        return new Font("Courier New", Font.PLAIN, 11);
    }

    private static Font safeSerif(String preferred, int style, int size) {
        Font t = new Font(preferred, style, size);
        if (t.getFamily().equals("Dialog") && (preferred.toLowerCase().contains("palatino")
                || preferred.toLowerCase().contains("crimson")
                || preferred.toLowerCase().contains("cinzel"))) {
            return new Font("Serif", style, size);
        }
        return t;
    }

    private static TitledBorder goldTitledBorder(String title) {
        LineBorder line = new LineBorder(BORDER_MUTED, 1, true);
        TitledBorder tb = new TitledBorder(line, " " + title + " ", TitledBorder.LEFT, TitledBorder.TOP, fontTitleGold(), ACCENT_GOLD);
        return tb;
    }

    private static JProgressBar hpBar(int current, int max, Color fill) {
        int m = Math.max(1, max);
        int c = Math.min(Math.max(0, current), m);
        JProgressBar bar = new JProgressBar(0, m);
        bar.setValue(c);
        bar.setForeground(fill);
        bar.setBackground(HP_BG);
        bar.setStringPainted(false);
        bar.setBorderPainted(true);
        bar.setBorder(BorderFactory.createLineBorder(BORDER_MUTED, 1));
        bar.setPreferredSize(new Dimension(100, 12));
        return bar;
    }

    private static JButton styledButton(String label) {
        JButton b = new JButton(label);
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setFont(safeSerif("Palatino Linotype", Font.BOLD, 13));
        b.setForeground(ACCENT_GOLD);
        b.setBackground(BG_CARD);
        b.setBorder(new CompoundBorder(
                new LineBorder(BORDER_MUTED, 1, true),
                new EmptyBorder(8, 8, 8, 8)
        ));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(BG_CARD_HOVER);
                    b.setBorder(new CompoundBorder(
                            new LineBorder(ACCENT_GOLD, 1, true),
                            new EmptyBorder(8, 8, 8, 8)
                    ));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(BG_CARD);
                b.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_MUTED, 1, true),
                        new EmptyBorder(8, 8, 8, 8)
                ));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(BG_ROOT);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (b.isEnabled() && b.getMousePosition() != null) {
                    b.setBackground(BG_CARD_HOVER);
                } else {
                    b.setBackground(BG_CARD);
                }
            }
        });
        return b;
    }

    private void applyOptionPaneDarkTheme() {
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", BG_CARD);
        UIManager.put("Button.foreground", ACCENT_GOLD);
        UIManager.put("TextArea.background", LOG_BG);
        UIManager.put("TextArea.foreground", TEXT_PRIMARY);
    }

    private void styleScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setBackground(BG_ROOT);
        sp.getVerticalScrollBar().setForeground(ACCENT_GOLD);
        sp.getHorizontalScrollBar().setBackground(BG_ROOT);
    }

    private final EntityAction emptyEntityAction = new EntityAction();

    private Listener userInputListener;
    private Listener displayFinishedListener;

    private RelevantGameData currentData;

    /** For intent pills on enemy cells (set from {@link #onDecideIntent}). */
    private final Map<String, IntentType> lastIntentByEnemy = new HashMap<>();

    private enum MenuState {
        /** Console main menu: choose 1–6 */
        MAIN,
        PICK_CARD_FOR_DESC,
        PICK_ENEMY_FOR_DESC,
        PICK_CARD_FOR_PLAY,
        PICK_TARGET_FOR_PLAY
    }

    private MenuState menuState = MenuState.MAIN;
    private Card playFlowCard;
    private Card selectedCard = null;

    private final JTextArea logArea = new JTextArea(8, 40);
    private final JScrollPane logScroll = new JScrollPane(logArea);

    private final JPanel mainTurnPanel = new JPanel(new BorderLayout(8, 4));
    private final JPanel slayerRow = new JPanel();
    private final JLabel slayerSpriteLabel = new JLabel();
    private final JLabel slayerNameLabel = new JLabel();
    private JProgressBar slayerHpBar;
    private final JPanel slayerEnergyDots = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
    private final JLabel slayerBlockLabel = new JLabel();

    private final JPanel enemyPanel = new JPanel();
    private JScrollPane enemyScroll;
    private final JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
    private final JLabel menuHint = new JLabel(" ");
    private final JPanel actionPanel = new JPanel();
    private final JButton[] menuButtons = new JButton[6];
    private static final String[] MENU_LABELS = {
            "1 · Card description",
            "2 · Enemy description",
            "3 · Play card",
            "4 · Pass",
            "5 · Lil jig",
            "6 · Exit"
    };

    public SwingGameView() {
        getContentPane().setBackground(BG_ROOT);
        setTitle("DeckFall");
        setSize(1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(4, 4));

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(fontLog());
        logArea.setBackground(LOG_BG);
        logArea.setForeground(LOG_TEXT);
        logArea.setCaretColor(ACCENT_GOLD);
        logArea.setSelectedTextColor(TEXT_PRIMARY);
        logArea.setSelectionColor(BORDER_MUTED);
        logScroll.setOpaque(true);
        logScroll.getViewport().setBackground(LOG_BG);
        logScroll.setBorder(BorderFactory.createEmptyBorder());
        logScroll.setPreferredSize(new Dimension(280, 0));
        JPanel logWrap = new JPanel(new BorderLayout());
        logWrap.setBackground(BG_ROOT);
        logWrap.setBorder(goldTitledBorder("Chronicle"));
        logWrap.add(logScroll, BorderLayout.CENTER);
        add(logWrap, BorderLayout.EAST);
        styleScrollBar(logScroll);

        mainTurnPanel.setOpaque(true);
        mainTurnPanel.setBackground(BG_PANEL);
        mainTurnPanel.setBorder(goldTitledBorder("Battlefield"));

        slayerRow.setLayout(new BoxLayout(slayerRow, BoxLayout.X_AXIS));
        slayerRow.setBackground(BG_PANEL);
        slayerRow.setOpaque(true);
        slayerRow.setBorder(new EmptyBorder(4, 8, 4, 8));
        slayerNameLabel.setFont(fontHeader());
        slayerNameLabel.setForeground(TEXT_PRIMARY);
        slayerBlockLabel.setFont(fontBody());
        slayerBlockLabel.setForeground(ACCENT_BLUE);
        slayerEnergyDots.setOpaque(false);
        mainTurnPanel.add(slayerRow, BorderLayout.NORTH);

        enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.X_AXIS));
        enemyPanel.setBackground(BG_PANEL);
        enemyPanel.setOpaque(true);
        enemyScroll = new JScrollPane(enemyPanel);
        enemyScroll.setBorder(BorderFactory.createEmptyBorder());
        enemyScroll.getViewport().setBackground(BG_PANEL);
        enemyScroll.setOpaque(true);
        styleScrollBar(enemyScroll);
        JPanel enemyOuter = new JPanel(new BorderLayout());
        enemyOuter.setBackground(BG_PANEL);
        enemyOuter.setBorder(goldTitledBorder("Enemies"));
        enemyOuter.add(enemyScroll, BorderLayout.CENTER);
        mainTurnPanel.add(enemyOuter, BorderLayout.CENTER);

        JPanel southBattle = new JPanel(new BorderLayout(4, 4));
        southBattle.setBackground(BG_PANEL);
        menuHint.setBackground(BG_PANEL);
        menuHint.setForeground(TEXT_SECONDARY);
        menuHint.setFont(fontBodyItalic());
        menuHint.setOpaque(true);
        menuHint.setBorder(new CompoundBorder(
                new javax.swing.border.MatteBorder(0, 0, 1, 0, BORDER_MUTED),
                new EmptyBorder(2, 8, 6, 4)
        ));
        southBattle.add(menuHint, BorderLayout.NORTH);

        cardPanel.setBackground(BG_PANEL);
        cardPanel.setOpaque(true);
        JPanel handWrap = new JPanel(new BorderLayout());
        handWrap.setBackground(BG_PANEL);
        handWrap.setBorder(goldTitledBorder("Hand"));
        handWrap.add(cardPanel, BorderLayout.CENTER);
        southBattle.add(handWrap, BorderLayout.CENTER);
        mainTurnPanel.add(southBattle, BorderLayout.SOUTH);

        add(mainTurnPanel, BorderLayout.CENTER);

        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(BG_PANEL);
        actionPanel.setOpaque(true);
        actionPanel.setBorder(goldTitledBorder("Your Move"));
        for (int i = 0; i < 6; i++) {
            int choice = i + 1;
            menuButtons[i] = styledButton(MENU_LABELS[i]);
            menuButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            menuButtons[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            final int c = choice;
            menuButtons[i].addActionListener(e -> onMenuAction(c));
            actionPanel.add(menuButtons[i]);
            actionPanel.add(Box.createVerticalStrut(6));
        }
        add(actionPanel, BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Sprite & resource loading (entity name string → file in resources) ---

    private static String spriteFileForEntityName(String name) {
        if (name == null) {
            return "slayer.png";
        }
        if (SPRITE_NAME_TO_FILE.containsKey(name)) {
            return SPRITE_NAME_TO_FILE.get(name);
        }
        String base = name.trim().isEmpty() ? "unknown" : name.trim();
        char c0 = Character.toLowerCase(base.charAt(0));
        if (base.length() == 1) {
            return c0 + ".png";
        }
        return c0 + base.substring(1) + ".png";
    }

    private Image loadSpriteImage(String entityName) {
        String file = spriteFileForEntityName(entityName);
        String[] classPaths = {"/resources/" + file, "resources/" + file};

        for (String cp : classPaths) {
            try (InputStream in = getClass().getResourceAsStream(cp)) {
                if (in != null) {
                    return ImageIO.read(in);
                }
            } catch (IOException ignored) { /* try next */ }
        }

        ClassLoader cl = getClass().getClassLoader();
        for (String cp : classPaths) {
            try (InputStream in = cl.getResourceAsStream(cp.startsWith("/") ? cp.substring(1) : cp)) {
                if (in != null) {
                    return ImageIO.read(in);
                }
            } catch (IOException ignored) { /* try next */ }
        }

        String[] relPaths = {
                "src/main/java/resources/" + file,
                "../src/main/java/resources/" + file
        };
        for (String rel : relPaths) {
            try {
                Path p = Path.of(rel).toAbsolutePath().normalize();
                if (Files.isRegularFile(p)) {
                    return ImageIO.read(p.toFile());
                }
            } catch (IOException | RuntimeException ignored) { /* */ }
        }

        return null;
    }

    private static JLabel newSpriteLabel(String entityName) {
        JLabel l = new JLabel();
        l.setVerticalAlignment(SwingConstants.BOTTOM);
        l.setVerticalTextPosition(SwingConstants.BOTTOM);
        l.setHorizontalTextPosition(SwingConstants.CENTER);
        l.setText(" ");
        l.setForeground(TEXT_PRIMARY);
        l.setFont(fontHeader());
        return l;
    }

    private void applySpriteToLabel(JLabel label, String entityName) {
        Image img = loadSpriteImage(entityName);
        if (img != null) {
            Image scaled = img.getScaledInstance(SPRITE_PX, SPRITE_PX, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
            label.setText(null);
        } else {
            label.setIcon(null);
            label.setText("[" + (entityName != null && !entityName.isEmpty() ? entityName.charAt(0) : '?') + "]");
        }
    }

    // --- Core UI updates ---

    private void refreshUI() {
        if (currentData == null) {
            return;
        }

        updateSlayerStrip();
        updateEnemies();
        updateCards();
        syncMenuEnabledState();
        revalidate();
        repaint();
    }

    private void updateSlayerStrip() {
        for (java.awt.event.MouseListener ml : slayerRow.getMouseListeners()) {
            slayerRow.removeMouseListener(ml);
        }
        slayerRow.setCursor(Cursor.getDefaultCursor());
        slayerRow.setBorder(new EmptyBorder(4, 8, 4, 8));
        slayerRow.removeAll();
        slayerEnergyDots.removeAll();

        Slayer s = currentData.getSlayer();
        applySpriteToLabel(slayerSpriteLabel, s.getName());
        slayerNameLabel.setText(s.getName());
        slayerNameLabel.setForeground(TEXT_PRIMARY);
        slayerNameLabel.setFont(fontHeader());

        slayerHpBar = hpBar(s.getHP(), s.getMaxHP(), ACCENT_GREEN);
        slayerHpBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < s.getMaxEnergy(); i++) {
            JLabel d = new JLabel(i < s.getEnergy() ? "●" : "○");
            d.setFont(fontHeader());
            d.setForeground(ACCENT_BLUE);
            slayerEnergyDots.add(d);
        }
        slayerBlockLabel.setText("🛡 " + s.getBlock());
        slayerBlockLabel.setForeground(ACCENT_BLUE);

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(BG_PANEL);
        col.setOpaque(false);
        col.add(slayerNameLabel);
        col.add(Box.createVerticalStrut(2));
        JPanel hpRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hpRow.setOpaque(false);
        slayerHpBar.setPreferredSize(new Dimension(160, 12));
        hpRow.add(slayerHpBar);
        col.add(hpRow);
        col.add(Box.createVerticalStrut(2));
        JPanel enRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        enRow.setOpaque(false);
        JLabel enL = new JLabel("Energy: ");
        enL.setFont(fontBody());
        enL.setForeground(TEXT_SECONDARY);
        enRow.add(enL);
        enRow.add(slayerEnergyDots);
        enRow.add(Box.createHorizontalStrut(8));
        enRow.add(slayerBlockLabel);
        col.add(enRow);

        slayerRow.add(slayerSpriteLabel);
        slayerRow.add(Box.createHorizontalStrut(12));
        slayerRow.add(col);

        boolean selfTarget = menuState == MenuState.PICK_ENEMY_FOR_DESC
                || menuState == MenuState.PICK_TARGET_FOR_PLAY;
        if (selfTarget) {
            slayerRow.setOpaque(true);
            slayerRow.setBackground(TARGET_HIGHLIGHT);
            slayerRow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            slayerRow.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_GOLD, 2, true),
                    new EmptyBorder(2, 6, 2, 6)
            ));
            slayerRow.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (menuState == MenuState.PICK_ENEMY_FOR_DESC) {
                        appendLogLine(s.getDescription());
                        toMainMenu("Read: Slayer (you).");
                    } else if (menuState == MenuState.PICK_TARGET_FOR_PLAY && playFlowCard != null) {
                        sendUseCard(playFlowCard, s);
                    }
                }
            });
        } else {
            slayerRow.setOpaque(true);
            slayerRow.setBackground(BG_PANEL);
        }
    }

    private static JLabel makeIntentPill(String enemyName, IntentType intent) {
        JLabel l = new JLabel();
        l.setFont(fontBody());
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBorder(new CompoundBorder(
                new LineBorder(BORDER_MUTED, 1, true),
                new EmptyBorder(2, 6, 2, 6)
        ));
        if (intent == null) {
            l.setText("· · ·");
            l.setBackground(INTENT_OTHER_BG);
            l.setForeground(TEXT_SECONDARY);
            return l;
        }
        switch (intent) {
            case ATTACK: {
                l.setText("⚔ Attacks");
                l.setBackground(INTENT_ATTACK_BG);
                l.setForeground(ACCENT_RED);
                break;
            }
            case DEFEND: {
                l.setText("🛡 Defends");
                l.setBackground(INTENT_DEFEND_BG);
                l.setForeground(ACCENT_BLUE);
                break;
            }
            default: {
                l.setText(String.valueOf(intent));
                l.setBackground(INTENT_OTHER_BG);
                l.setForeground(TEXT_SECONDARY);
            }
        }
        return l;
    }

    private void updateEnemies() {
        enemyPanel.removeAll();
        for (Entity enemy : currentData.getEnemies()) {
            JPanel cell = new JPanel();
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_RED, 1, true),
                    new EmptyBorder(4, 4, 4, 4)
            ));
            cell.setBackground(ENEMY_CELL_BG);
            cell.setOpaque(true);

            JLabel sp = newSpriteLabel(enemy.getName());
            applySpriteToLabel(sp, enemy.getName());
            sp.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nameL = new JLabel(enemy.getName());
            nameL.setFont(fontHeader());
            nameL.setForeground(TEXT_PRIMARY);
            nameL.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameL.setHorizontalAlignment(SwingConstants.CENTER);

            JProgressBar ehp = hpBar(enemy.getHP(), enemy.getMaxHP(), ACCENT_RED);
            ehp.setAlignmentX(Component.CENTER_ALIGNMENT);
            ehp.setMaximumSize(new Dimension(140, 12));

            JLabel blockL = new JLabel("🛡 " + enemy.getBlock());
            blockL.setFont(fontBody());
            blockL.setForeground(ACCENT_BLUE);
            blockL.setAlignmentX(Component.CENTER_ALIGNMENT);

            IntentType it = lastIntentByEnemy.get(enemy.getName());
            JLabel intentP = makeIntentPill(enemy.getName(), it);
            intentP.setAlignmentX(Component.CENTER_ALIGNMENT);

            cell.add(sp);
            cell.add(Box.createVerticalStrut(2));
            cell.add(nameL);
            cell.add(Box.createVerticalStrut(4));
            cell.add(ehp);
            cell.add(blockL);
            cell.add(Box.createVerticalStrut(2));
            cell.add(intentP);

            boolean targetEnabled = menuState == MenuState.PICK_ENEMY_FOR_DESC
                    || menuState == MenuState.PICK_TARGET_FOR_PLAY;
            if (targetEnabled) {
                cell.setBackground(TARGET_HIGHLIGHT);
                cell.setBorder(new CompoundBorder(
                        new LineBorder(ACCENT_GOLD, 2, true),
                        new LineBorder(ACCENT_RED, 1, true)
                ));
                cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (menuState == MenuState.PICK_ENEMY_FOR_DESC) {
                            appendLogLine(enemy.getDescription());
                            toMainMenu("Read description: " + enemy.getName() + ".");
                        } else if (menuState == MenuState.PICK_TARGET_FOR_PLAY && playFlowCard != null) {
                            sendUseCard(playFlowCard, enemy);
                        }
                    }
                });
            }
            enemyPanel.add(cell);
            enemyPanel.add(Box.createHorizontalStrut(8));
        }
    }

    private void applyHandCardBorderAndBg(JButton b, boolean selected) {
        if (selected) {
            b.setBackground(BG_CARD_SEL);
            b.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_GOLD, 2, true),
                    new EmptyBorder(6, 8, 6, 8)
            ));
        } else {
            b.setBackground(BG_CARD);
            b.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_MUTED, 1, true),
                    new EmptyBorder(6, 8, 6, 8)
            ));
        }
    }

    private void updateCards() {
        cardPanel.removeAll();
        List<Card> cards = currentData.getCards();
        int energy = currentData.getSlayer().getEnergy();
        for (Card card : cards) {
            boolean affordable = card.getEnergyCost() <= energy;
            String cost = String.valueOf(card.getEnergyCost());
            String body = card.getName();
            // Bright yellow cost + white (or dim warm) name — avoid default L&F black on HTML
            String nameColor = (affordable || !isCardPickRelevant()) ? "#ffffff" : "#a89870";
            String html = "<html><div style='text-align:center;color:#ffffff'>"
                    + "<div style='color:#ffe066;font-size:10px;font-weight:bold;text-align:left'>" + cost + " ⚡</div>"
                    + "<div style='color:" + nameColor
                    + ";font-size:12px'>" + body + "</div></div></html>";
            JButton b = new JButton(html);
            b.setFocusPainted(false);
            b.setContentAreaFilled(true);
            b.setOpaque(true);
            b.setFont(fontBody());
            b.setVerticalTextPosition(SwingConstants.CENTER);
            b.setForeground(new Color(0xffffff));
            b.setToolTipText("<html><div style='background:#13131f;color:#e8e0d0;padding:8px;border:1px solid #2a2a4a'>"
                    + card.getDescription() + "</div></html>");

            boolean cardPick = menuState == MenuState.PICK_CARD_FOR_DESC
                    || menuState == MenuState.PICK_CARD_FOR_PLAY;
            b.setEnabled(cardPick);
            boolean sel = (card == selectedCard);
            applyHandCardBorderAndBg(b, sel);

            if (cardPick && !affordable) {
                b.setForeground(new Color(0xa89870));
            } else {
                b.setForeground(new Color(0xffffff));
            }

            b.setPreferredSize(new Dimension(110, 70));
            b.setMinimumSize(new Dimension(110, 70));
            if (!b.isEnabled()) {
                b.setForeground(new Color(0x5a5a6a));
            }
            b.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!b.isEnabled()) {
                        return;
                    }
                    if (card == selectedCard) {
                        applyHandCardBorderAndBg(b, true);
                        return;
                    }
                    b.setBackground(BG_CARD_HOVER);
                    b.setBorder(new CompoundBorder(
                            new LineBorder(ACCENT_GOLD, 1, true),
                            new EmptyBorder(6, 8, 6, 8)
                    ));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!b.isEnabled()) {
                        return;
                    }
                    boolean stillSel = (card == selectedCard);
                    applyHandCardBorderAndBg(b, stillSel);
                }
            });
            b.addActionListener(e -> onHandCardButton(card, cardPick));
            cardPanel.add(b);
        }
    }

    private boolean isCardPickRelevant() {
        return menuState == MenuState.PICK_CARD_FOR_PLAY;
    }

    private void onHandCardButton(Card card, boolean cardPick) {
        if (!cardPick) {
            return;
        }
        if (menuState == MenuState.PICK_CARD_FOR_DESC) {
            appendLogLine(card.toString());
            toMainMenu("Card description shown.");
        } else if (menuState == MenuState.PICK_CARD_FOR_PLAY) {
            playFlowCard = card;
            selectedCard = card;
            menuState = MenuState.PICK_TARGET_FOR_PLAY;
            appendLogLine("* Now choose a target (enemy, or the Slayer portrait).");
            menuHint.setText("Choose a target (enemy or yourself).");
            refreshUI();
        }
    }

    private void syncMenuEnabledState() {
        boolean inTurn = (currentData != null);
        boolean inMain = inTurn && menuState == MenuState.MAIN;
        for (JButton b : menuButtons) {
            b.setEnabled(inMain);
        }
    }

    private void toMainMenu(String logLine) {
        if (logLine != null) {
            appendLogLine(logLine);
        }
        menuState = MenuState.MAIN;
        playFlowCard = null;
        selectedCard = null;
        menuHint.setText("* What's your move, Slayer? (1-6)");
        refreshUI();
    }

    // --- Console-style menu (1–6) ---

    private void onMenuAction(int n) {
        if (currentData == null || userInputListener == null) {
            return;
        }
        if (menuState != MenuState.MAIN) {
            return;
        }

        switch (n) {
            case 1:
                menuHint.setText("Select a card in your hand to read its full text.");
                menuState = MenuState.PICK_CARD_FOR_DESC;
                refreshUI();
                break;
            case 2:
                menuHint.setText("Select an enemy (or the Slayer portrait) to read a description.");
                menuState = MenuState.PICK_ENEMY_FOR_DESC;
                refreshUI();
                break;
            case 3:
                appendLogLine("* Remember, you need enough energy to play a card!");
                if (currentData.getCards().isEmpty()) {
                    appendLogLine("* You have no cards in hand to play.");
                    return;
                }
                menuHint.setText("1) Click a card  2) Then choose a target.");
                appendLogLine("* Play: pick a card, then a target.");
                menuState = MenuState.PICK_CARD_FOR_PLAY;
                refreshUI();
                break;
            case 4:
                appendLogLine("* You passed your turn.");
                userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.PASS));
                toMainMenu(null);
                break;
            case 5:
                appendLogLine("* You did a lil jig! ...Nothing happened... ");
                applyOptionPaneDarkTheme();
                JOptionPane.showMessageDialog(this, "You did a lil jig!\nNothing happened.");
                break;
            case 6: {
                applyOptionPaneDarkTheme();
                int r = JOptionPane.showConfirmDialog(this, "Exit the game?", "Exit", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    appendLogLine("* Exiting game...");
                    System.exit(0);
                }
                break;
            }
            default:
        }
    }

    private void sendUseCard(Card card, Entity target) {
        if (userInputListener == null) {
            return;
        }
        userInputListener.ActionPerformed(
                new EntityAction()
                        .setAction_enum(MoveTypes.USE_CARD)
                        .setSelectedCard(card)
                        .setTarget(target)
        );
        toMainMenu(null);
    }

    // --- Log (mirrors console println / notifications) ---

    private void appendLogLine(String line) {
        logArea.append(line + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void logNotificationsFromData(RelevantGameData gameData) {
        for (String s : gameData.getNotifications()) {
            appendLogLine(s);
        }
    }

    // --- User input entry ---

    public void requestUserInput(RelevantGameData gameData) {
        this.currentData = gameData;
        logNotificationsFromData(gameData);
        appendLogLine("* Requesting user input.");
        toMainMenu(null);
    }

    @Override
    public void addDisplayFinishedListener(Listener listener) {
        this.displayFinishedListener = listener;
    }

    @Override
    public void addUserInputListener(Listener listener) {
        this.userInputListener = listener;
    }

    // --- Observers: align text with ConsoleLogger where possible ---

    @Override
    public void onInvalidMoveSelected(String message) {
        appendLogLine("(!) Invalid move: " + message);
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, message, "Invalid move", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onSlayerDefeat() {
        appendLogLine("(!) The Slayer has fallen...");
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, "The Slayer has fallen…", "Defeat", JOptionPane.ERROR_MESSAGE);
        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(emptyEntityAction);
        }
    }

    @Override
    public void onEnemyDefeat(String enemyName) {
        appendLogLine(enemyName + " has been defeated!");
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, enemyName + " has been defeated!", "Enemy down", JOptionPane.INFORMATION_MESSAGE);
        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(emptyEntityAction);
        }
    }

    @Override
    public void onVictory() {
        appendLogLine("* Well. You didn't die.\n" +
                "Against all reasonable odds, the Tower is cleared, the Demon King is defeated, and you -somehow- are still breathing. The villagers owe you a drink. Several, actually. Still, they shall sing your name for generations to come.");
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, "* You win!", "Victory", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onDefeat() {
        String t = String.join("\n",
                "Tragically, you meet your end.",
                "The Tower held up to its reputation — you wonder, briefly, whether there was anything you could have done to prevent this.",
                "…Your last thought as you slip into the great unknown, is the fate of the poor villagers, whose fates will never be known."
        );
        appendLogLine(t);
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, t, "Defeat", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void startGame() {
        String intro = String.join("\n",
                "You are the Slayer; a powerful and accomplished being heralded across the lands.",
                "",
                "Today, you're tackling your biggest challenge yet: the Tower.",
                "What's inside is completely unknown, but none who step inside are ever seen again.",
                "And yet, reports of missing persons in the surrounding areas have skyrocketed since its sudden appearance a few months ago.",
                "At their wits end, the desperate people came to you, seeking aid.",
                "You push open the large doors, and step inside…"
        );
        JTextArea a = new JTextArea(intro, 12, 48);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setEditable(false);
        a.setBackground(LOG_BG);
        a.setForeground(TEXT_PRIMARY);
        a.setFont(fontBody());
        JScrollPane sc = new JScrollPane(a);
        sc.getViewport().setBackground(LOG_BG);
        sc.setBorder(BorderFactory.createEmptyBorder());
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, sc, "DeckFall", JOptionPane.INFORMATION_MESSAGE);
        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(new EntityAction());
        }
    }

    @Override
    public void update(RelevantGameData relevantGameData) {
        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(new EntityAction());
        }
    }
    
    @Override
    public void onFloorEntry(int floor) {
        appendLogLine("* You enter floor " + floor + " of the Tower...");
    }

    @Override
    public void onFloorClear(int floor) {
        appendLogLine("* Floor " + floor + " cleared!");
    }

    @Override
    public void onBattleEntry() {
        appendLogLine("* A new battle begins!");
    }

    @Override
    public void onBattleChange(List<Entity> a, List<Entity> b) {
        appendLogLine(b.size() + " enemy(s) remain.");
        if (currentData != null) {
            refreshUI();
        }
    }

    @Override
    public void onBattleWin() {
        appendLogLine("* You won the battle!");
    }

    @Override
    public void onTurnStart(String entityName) {
        appendLogLine("=-=- " + entityName + "'s turn -=-=");
    }

    @Override
    public void onTurnEnd(String entityName) {
        appendLogLine(entityName + "'s turn ends.");
    }

    @Override
    public void onTurnPass(String entityName) {
        appendLogLine(entityName + " passes their turn.");
    }

    @Override
    public void onEntityAttack(String attacker, String target, int damage) {
        appendLogLine(attacker + " attacks " + target + " for " + damage + " damage!");
    }

    @Override
    public void onEntityDefense(String e, int block) {
        appendLogLine(e + " gained " + block + " block!");
    }

    @Override
    public void onEntityHeal(String e, int h) {
        appendLogLine(e + " restored " + h + " HP!");
    }

    @Override
    public void onEntityDamaged(String e, int d) {
        appendLogLine(e + " took " + d + " damage!");
    }

    @Override
    public void onNotEnoughEnergy(String entityName, Card card) {
        String line = entityName + " does not have enough energy to play " + card.getName() + "!";
        appendLogLine(line);
        applyOptionPaneDarkTheme();
        JOptionPane.showMessageDialog(this, line, "Not enough energy", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onCardDrawn(Card c) {
        appendLogLine("Drew: " + c.getSimpleString());
    }

    @Override
    public void onCardPlayed(Card c) {
        appendLogLine("Played: " + c.getSimpleString());
    }

    @Override
    public void onDeckShuffled() {
        appendLogLine("* Deck reshuffled from discard pile.");
    }

    @Override
    public void onDecideIntent(String enemyName, IntentType intent) {
        lastIntentByEnemy.put(enemyName, intent);
        appendLogLine(enemyName + " prepares to " + intent + "!");
        if (currentData != null) {
            SwingUtilities.invokeLater(this::updateEnemiesOnly);
        }
    }

    private void updateEnemiesOnly() {
        if (currentData == null) {
            return;
        }
        updateEnemies();
        enemyPanel.revalidate();
        enemyPanel.repaint();
    }

    @Override
    public void onDemonKingFloor() {
        appendLogLine("* A dark presence fills the room... The Demon King awaits.");
    }
}
