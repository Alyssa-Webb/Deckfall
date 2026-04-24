package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Card.CardType;
import deckfall.Card.TargetType;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.DemonKing;
import deckfall.Entity.Enemy;
import deckfall.Entity.Entity;
import deckfall.Entity.IntentType;
import deckfall.Entity.Slayer;
import deckfall.Game.MoveTypes;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingGameView extends JFrame implements GameEventObserver {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color COL_STONE       = new Color(0x1A, 0x18, 0x14);
    private static final Color COL_STONE2      = new Color(0x24, 0x20, 0x18);
    private static final Color COL_STONE3      = new Color(0x2E, 0x2A, 0x22);
    private static final Color COL_PANEL       = new Color(0x0D, 0x0C, 0x0A);
    private static final Color COL_PARCHMENT   = new Color(0xF0, 0xE8, 0xD5);
    private static final Color COL_PARCH_DIM   = new Color(0xB0, 0xA8, 0x95);
    private static final Color COL_PARCH_MUTED = new Color(0x70, 0x68, 0x58);
    private static final Color COL_GOLD        = new Color(0xB8, 0x92, 0x2A);
    private static final Color COL_GOLD2       = new Color(0xD4, 0xA8, 0x3A);
    private static final Color COL_EMBER       = new Color(0xC0, 0x42, 0x1A);
    private static final Color COL_EMBER2      = new Color(0xE8, 0x62, 0x1A);
    private static final Color COL_BLOOD       = new Color(0x8B, 0x1A, 0x1A);
    private static final Color COL_SAPPHIRE    = new Color(0x1A, 0x3A, 0x6E);
    private static final Color COL_FOREST      = new Color(0x3A, 0x6E, 0x2A);
    private static final Color COL_DIVIDER     = new Color(0x40, 0x38, 0x20);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    // Cinzel is not bundled in the JDK; fall back gracefully to Palatino/Serif.
    private static final Font FONT_DISPLAY_MD  = new Font("Palatino Linotype", Font.BOLD,  11);
    private static final Font FONT_DISPLAY_SM  = new Font("Palatino Linotype", Font.BOLD,   9);
    private static final Font FONT_BODY_SM     = new Font("Georgia",           Font.PLAIN, 11);

    // ── State ─────────────────────────────────────────────────────────────────
    private Listener userInputListener;
    private Listener displayFinishedListener;
    private RelevantGameData currentData;
    private boolean started = false;

    private final Map<String, IntentType> enemyIntentMap = new HashMap<>();
    private final Map<String, ImageIcon> spriteCache = new HashMap<>();

    private Card cardAwaitingEnemyTarget = null;

    // ── Root ──────────────────────────────────────────────────────────────────
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    // ── Start screen ──────────────────────────────────────────────────────────
    private final JPanel startPanel = new JPanel();
    private final JTextField nameInput = new JTextField("Slayer", 18);
    private final JButton startButton = new JButton("Begin Your Descent");

    // ── Game panels ───────────────────────────────────────────────────────────
    private final JPanel gamePanel    = new JPanel(new BorderLayout(0, 0));
    private final JPanel centerPanel  = new JPanel(new BorderLayout(0, 8));
    private final JPanel slayerCombatPanel = new JPanel(new BorderLayout(0, 6));
    private final JPanel enemyCombatPanel  = new JPanel(new BorderLayout(0, 6));
    private final JPanel enemyPanel   = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
    private final JPanel cardRow      = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
    private final JPanel rightPanel   = new JPanel();

    // ── Battlefield (equal slayer / enemy columns) ────────────────────────────
    private final JLabel slayerPortraitLabel = new JLabel("⚔", SwingConstants.CENTER);
    private final JProgressBar slayerHpBar     = makeBar(COL_BLOOD);
    private final JLabel       slayerHpText  = makeStatLabel("");
    private final JProgressBar slayerBlockBar = makeBar(COL_SAPPHIRE);
    private final JLabel       slayerBlockText = makeStatLabel("");
    private final JPanel       slayerEnergyDots = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
    private final JLabel       slayerEnergyNumbers = makeStatLabel("");

    // ── Right panel: combat vitals + chronicle + actions ───────────────────────
    private final JLabel rightStatHp     = makeStatLabel("HP —");
    private final JLabel rightStatBlock  = makeStatLabel("Block —");
    private final JLabel rightStatEnergy = makeStatLabel("Energy —");
    private final JTextArea  logArea        = new JTextArea();
    private final JButton    passButton     = makeActionButton("Pass Action",    false);
    private final JButton    endTurnButton  = makeActionButton("End Turn",       true);
    private final JButton    cardInfoButton = makeActionButton("Inspect Card",   false);
    private final JButton    enemyInfoButton= makeActionButton("Inspect Enemy",  false);

    // ── Header ────────────────────────────────────────────────────────────────
    private final JLabel floorLabel  = new JLabel("Floor 1 · Battle 1", SwingConstants.CENTER);

    // =========================================================================
    public SwingGameView() {
        setTitle("DeckFall");
        setSize(1160, 740);
        setMinimumSize(new Dimension(900, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COL_STONE);

        buildStartPanel();
        buildGamePanel();

        rootPanel.add(startPanel, "START");
        rootPanel.add(gamePanel,  "GAME");
        add(rootPanel, BorderLayout.CENTER);
        rootLayout.show(rootPanel, "START");

        setVisible(true);
    }

    // =========================================================================
    // Start Panel
    // =========================================================================
    private void buildStartPanel() {
        startPanel.setBackground(COL_STONE);
        startPanel.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COL_STONE2);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(COL_DIVIDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        JLabel title = new JLabel("DECKFALL", SwingConstants.CENTER);
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 34));
        title.setForeground(COL_GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel divider = new JLabel("— The Tower Awaits —", SwingConstants.CENTER);
        divider.setFont(new Font("Georgia", Font.ITALIC, 13));
        divider.setForeground(COL_PARCH_MUTED);
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel namePrompt = new JLabel("Name thy Slayer:", SwingConstants.CENTER);
        namePrompt.setFont(FONT_DISPLAY_MD);
        namePrompt.setForeground(COL_PARCH_DIM);
        namePrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleTextField(nameInput);
        nameInput.setMaximumSize(new Dimension(260, 34));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        styleStartButton(startButton);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> beginGame());

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(divider);
        card.add(Box.createVerticalStrut(22));
        card.add(namePrompt);
        card.add(Box.createVerticalStrut(8));
        card.add(nameInput);
        card.add(Box.createVerticalStrut(16));
        card.add(startButton);

        startPanel.add(card);
    }

    // =========================================================================
    // Game Panel
    // =========================================================================
    private void buildGamePanel() {
        gamePanel.setBackground(COL_STONE);

        // Header bar
        JPanel header = buildHeader();
        gamePanel.add(header, BorderLayout.NORTH);

        // Center (battlefield)
        buildCenterPanel();
        gamePanel.add(centerPanel, BorderLayout.CENTER);

        // Right panel
        buildRightPanel();
        gamePanel.add(rightPanel, BorderLayout.EAST);

        // Card row
        buildCardRow();
        JScrollPane cardScroll = new JScrollPane(cardRow);
        cardScroll.setBorder(new MatteBorder(1, 0, 0, 0, COL_DIVIDER));
        cardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        cardScroll.setPreferredSize(new Dimension(0, 190));
        cardScroll.getHorizontalScrollBar().setUnitIncrement(20);
        gamePanel.add(cardScroll, BorderLayout.SOUTH);
    }

    // ── Header ────────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COL_STONE);
        header.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, COL_DIVIDER),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));

        JLabel title = new JLabel("DECKFALL");
        title.setFont(new Font("Palatino Linotype", Font.BOLD, 15));
        title.setForeground(COL_GOLD);

        floorLabel.setFont(FONT_DISPLAY_MD);
        floorLabel.setForeground(COL_PARCH_MUTED);

        header.add(title, BorderLayout.WEST);
        header.add(floorLabel, BorderLayout.CENTER);
        return header;
    }

    // ── Center Panel ─────────────────────────────────────────────────────────
    private void buildCenterPanel() {
        centerPanel.setBackground(COL_STONE2);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));

        JPanel battleSplit = new JPanel(new GridLayout(1, 2, 16, 0));
        battleSplit.setOpaque(false);

        slayerCombatPanel.setOpaque(false);
        slayerCombatPanel.setBackground(COL_STONE2);
        JLabel slayerTitle = sectionLabel("The Slayer");
        slayerTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        slayerCombatPanel.add(slayerTitle, BorderLayout.NORTH);

        JPanel slayerPortraitWrap = new JPanel(new BorderLayout());
        slayerPortraitWrap.setOpaque(false);
        slayerPortraitLabel.setFont(new Font("Dialog", Font.PLAIN, 64));
        slayerPortraitLabel.setOpaque(true);
        slayerPortraitLabel.setBackground(COL_STONE3);
        slayerPortraitLabel.setBorder(new LineBorder(COL_DIVIDER, 1));
        slayerPortraitWrap.add(slayerPortraitLabel, BorderLayout.CENTER);

        JPanel slayerStats = new JPanel();
        slayerStats.setOpaque(false);
        slayerStats.setLayout(new BoxLayout(slayerStats, BoxLayout.Y_AXIS));
        slayerStats.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        slayerHpText.setHorizontalAlignment(SwingConstants.LEFT);
        slayerHpText.setAlignmentX(Component.LEFT_ALIGNMENT);
        slayerStats.add(slayerHpText);
        slayerStats.add(Box.createVerticalStrut(2));
        slayerHpBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        slayerStats.add(slayerHpBar);
        slayerStats.add(Box.createVerticalStrut(8));

        slayerBlockText.setHorizontalAlignment(SwingConstants.LEFT);
        slayerBlockText.setAlignmentX(Component.LEFT_ALIGNMENT);
        slayerStats.add(slayerBlockText);
        slayerStats.add(Box.createVerticalStrut(2));
        slayerBlockBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        slayerStats.add(slayerBlockBar);
        slayerStats.add(Box.createVerticalStrut(8));

        JPanel energyRow = new JPanel(new BorderLayout(8, 0));
        energyRow.setOpaque(false);
        energyRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        energyRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        slayerEnergyDots.setOpaque(false);
        energyRow.add(slayerEnergyDots, BorderLayout.WEST);
        slayerEnergyNumbers.setHorizontalAlignment(SwingConstants.RIGHT);
        energyRow.add(slayerEnergyNumbers, BorderLayout.CENTER);
        slayerStats.add(energyRow);

        slayerPortraitWrap.add(slayerStats, BorderLayout.SOUTH);
        slayerCombatPanel.add(slayerPortraitWrap, BorderLayout.CENTER);

        enemyCombatPanel.setOpaque(false);
        enemyCombatPanel.setBackground(COL_STONE2);
        JLabel enemiesTitle = sectionLabel("Enemies");
        enemiesTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        enemyCombatPanel.add(enemiesTitle, BorderLayout.NORTH);
        enemyPanel.setOpaque(false);
        JScrollPane enemyScroll = new JScrollPane(enemyPanel);
        enemyScroll.setBorder(BorderFactory.createEmptyBorder());
        enemyScroll.setOpaque(false);
        enemyScroll.getViewport().setOpaque(false);
        enemyScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        enemyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        enemyCombatPanel.add(enemyScroll, BorderLayout.CENTER);

        battleSplit.add(slayerCombatPanel);
        battleSplit.add(enemyCombatPanel);

        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.setOpaque(false);
        JLabel standbyLbl = new JLabel("— The Slayer stands ready —", SwingConstants.CENTER);
        standbyLbl.setFont(new Font("Georgia", Font.ITALIC, 10));
        standbyLbl.setForeground(COL_PARCH_MUTED);
        bottomArea.add(standbyLbl, BorderLayout.CENTER);

        centerPanel.add(battleSplit, BorderLayout.CENTER);
        centerPanel.add(bottomArea, BorderLayout.SOUTH);
    }

    // ── Right Panel ───────────────────────────────────────────────────────────
    private void buildRightPanel() {
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(COL_PANEL);
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 1, 0, 0, COL_DIVIDER),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));

        rightPanel.add(sectionLabel("Chronicle"));
        rightPanel.add(Box.createVerticalStrut(6));

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(new Color(0x0A, 0x09, 0x07));
        logArea.setForeground(COL_PARCH_DIM);
        logArea.setFont(new Font("Georgia", Font.ITALIC, 11));
        logArea.setMargin(new Insets(6, 6, 6, 6));
        logArea.setBorder(BorderFactory.createEmptyBorder());
        logArea.setCaretColor(COL_GOLD);

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(new LineBorder(COL_DIVIDER, 1));
        logScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        logScroll.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        logScroll.getVerticalScrollBar().setUnitIncrement(8);

        rightPanel.add(logScroll);
        rightPanel.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setForeground(COL_DIVIDER);
        sep.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
        rightPanel.add(sep);
        rightPanel.add(Box.createVerticalStrut(8));

        for (JButton btn : new JButton[]{ cardInfoButton, enemyInfoButton, passButton, endTurnButton }) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
            rightPanel.add(btn);
            rightPanel.add(Box.createVerticalStrut(6));
        }

        passButton.addActionListener(e -> sendPass());
        endTurnButton.addActionListener(e -> sendEndTurn());
        cardInfoButton.addActionListener(e -> showCardDescription());
        enemyInfoButton.addActionListener(e -> showEnemyDescription());
    }

    // ── Card Row ──────────────────────────────────────────────────────────────
    private void buildCardRow() {
        cardRow.setBackground(COL_STONE);
        cardRow.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    // =========================================================================
    // UI Refresh
    // =========================================================================
    private void refreshUI() {
        if (currentData == null) return;
        SwingUtilities.invokeLater(() -> {
            refreshHeader();
            refreshSlayerStatBars();
            refreshSlayerPortrait();
            refreshEnemies();
            refreshCards();
            boolean enabled = started && currentData != null;
            passButton.setEnabled(enabled);
            endTurnButton.setEnabled(enabled);
            cardInfoButton.setEnabled(enabled);
            enemyInfoButton.setEnabled(enabled);
            gamePanel.revalidate();
            gamePanel.repaint();
        });
    }

    private void refreshHeader() {
        int floor = currentData.getFloorNumber();
        int battleIndex = currentData.getCurrentBattleIndex();
        int totalBattlesOnFloor = currentData.getTotalBattlesOnCurrentFloor();
        String total = totalBattlesOnFloor > 0 ? String.valueOf(totalBattlesOnFloor) : "?";
        floorLabel.setText("Floor " + floor + "  ·  Battle " + battleIndex + " of " + total);
    }

    private void refreshSlayerStatBars() {
        Entity p = currentData.getSlayer();
        slayerHpText.setText("Health  " + p.getHP() + " / " + p.getMaxHP());
        slayerHpBar.setMaximum(Math.max(1, p.getMaxHP()));
        slayerHpBar.setValue(p.getHP());

        slayerBlockText.setText("Shield  " + p.getBlock());
        int blk = p.getBlock();
        slayerBlockBar.setMaximum(Math.max(20, blk));
        slayerBlockBar.setValue(blk);

        slayerEnergyDots.removeAll();
        if (p instanceof Slayer s) {
            int cur = s.getEnergy();
            int max = s.getMaxEnergy();
            slayerEnergyNumbers.setText("Energy  " + cur + " / " + max);
            for (int i = 0; i < max; i++) {
                slayerEnergyDots.add(makePip(i < cur));
            }
        } else {
            slayerEnergyNumbers.setText("Energy  —");
        }
        slayerEnergyDots.revalidate();
    }

    private void refreshSlayerPortrait() {
        Entity p = currentData.getSlayer();
        int w = 240;
        int h = 260;
        ImageIcon icon = loadPngSprite(p, w, h);
        if (icon != null) {
            slayerPortraitLabel.setIcon(icon);
            slayerPortraitLabel.setText(null);
        } else {
            slayerPortraitLabel.setIcon(null);
            slayerPortraitLabel.setText("⚔");
            slayerPortraitLabel.setFont(new Font("Dialog", Font.PLAIN, 72));
        }
    }

    private void refreshEnemies() {
        if (currentData == null) {
            return;
        }
        enemyPanel.removeAll();
        for (Entity enemy : currentData.getEnemies()) {
            enemyPanel.add(buildEnemyCard(enemy));
        }
        enemyPanel.revalidate();
        enemyPanel.repaint();
    }

    private void refreshCards() {
        if (currentData == null) {
            return;
        }
        cardRow.removeAll();
        for (Card card : currentData.getCards()) {
            cardRow.add(buildCardButton(card));
        }
        cardRow.revalidate();
        cardRow.repaint();
    }

    // =========================================================================
    // Enemy Card Widget
    // =========================================================================
    private JPanel buildEnemyCard(Entity enemy) {
        boolean targetPickMode = cardAwaitingEnemyTarget != null;
        boolean isSelected = targetPickMode && enemy.isAlive();
        IntentType intent = enemyIntentMap.get(enemy.getName());
        if (intent == null && enemy instanceof Enemy en) {
            intent = en.getCurrentIntent();
        }

        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COL_STONE3);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Left accent line
                g2.setColor(isSelected ? COL_EMBER : COL_DIVIDER);
                g2.fillRect(0, 0, 2, getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(168, 228));
        panel.setBorder(new CompoundBorder(
                new LineBorder(isSelected ? COL_EMBER : COL_DIVIDER, 1),
                BorderFactory.createEmptyBorder(6, 7, 7, 7)
        ));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel art = new JLabel("?", SwingConstants.CENTER);
        art.setFont(new Font("Dialog", Font.PLAIN, 36));
        art.setOpaque(true);
        art.setBackground(new Color(0, 0, 0, 60));
        art.setPreferredSize(new Dimension(140, 120));
        art.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
        art.setAlignmentX(Component.LEFT_ALIGNMENT);

        ImageIcon sprite = loadPngSprite(enemy, 132, 112);
        if (sprite != null) {
            art.setIcon(sprite);
            art.setText(null);
        }

        // Intent badge
        if (intent != null) {
            art.setText(null);
            String intentText = intentShortLabel(intent);
            Color intentColor = intentBadgeColor(intent);
            JLabel badge = new JLabel(intentText, SwingConstants.CENTER);
            badge.setFont(FONT_DISPLAY_SM);
            badge.setForeground(COL_PARCHMENT);
            badge.setBackground(intentColor);
            badge.setOpaque(true);
            badge.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            badge.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(art);
            panel.add(Box.createVerticalStrut(2));
            panel.add(badge);
        } else {
            panel.add(art);
        }

        panel.add(Box.createVerticalStrut(5));

        // Name
        JLabel nameLabel = new JLabel(enemy.getName(), SwingConstants.LEFT);
        nameLabel.setFont(FONT_DISPLAY_MD);
        nameLabel.setForeground(COL_PARCHMENT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(4));

        // HP bar
        int hpPct = enemy.getMaxHP() > 0 ? (enemy.getHP() * 100 / enemy.getMaxHP()) : 0;
        JPanel hpRow = new JPanel(new BorderLayout(4, 0));
        hpRow.setOpaque(false);
        hpRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        hpRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 14));
        JLabel hpTxt = new JLabel(enemy.getHP() + "/" + enemy.getMaxHP());
        hpTxt.setFont(FONT_BODY_SM);
        hpTxt.setForeground(COL_PARCH_MUTED);
        JPanel miniBar = buildMiniBar(hpPct, COL_BLOOD);
        hpRow.add(hpTxt, BorderLayout.WEST);
        hpRow.add(miniBar, BorderLayout.CENTER);
        panel.add(hpRow);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { handleEnemyClicked(enemy); }
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new CompoundBorder(
                        new LineBorder(COL_GOLD, 1),
                        BorderFactory.createEmptyBorder(6, 7, 7, 7)));
            }
            public void mouseExited(MouseEvent e) {
                Color edge = isSelected ? COL_EMBER : COL_DIVIDER;
                panel.setBorder(new CompoundBorder(
                        new LineBorder(edge, 1),
                        BorderFactory.createEmptyBorder(6, 7, 7, 7)));
            }
        });

        return panel;
    }

    // =========================================================================
    // Card Button Widget
    // =========================================================================
    private JPanel buildCardButton(Card card) {
        boolean isSelected = card == cardAwaitingEnemyTarget;
        Color accentColor = cardAccentColor(card);

        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COL_STONE3);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Top accent stripe
                g2.setColor(accentColor);
                g2.fillRect(0, 0, getWidth(), 2);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(118, 165));
        panel.setBorder(new CompoundBorder(
                new LineBorder(COL_DIVIDER, 1),
                BorderFactory.createEmptyBorder(7, 7, 8, 7)
        ));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Cost badge (top-right corner)
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 22));
        JLabel costBadge = new JLabel(String.valueOf(card.getEnergyCost()), SwingConstants.CENTER);
        costBadge.setFont(FONT_DISPLAY_MD);
        costBadge.setForeground(COL_EMBER2);
        costBadge.setBackground(COL_STONE);
        costBadge.setOpaque(true);
        costBadge.setPreferredSize(new Dimension(20, 20));
        costBadge.setBorder(new LineBorder(COL_EMBER, 1));
        topRow.add(costBadge, BorderLayout.EAST);
        panel.add(topRow);

        // Art area
        JLabel artLabel = new JLabel(getEmojiForCard(card), SwingConstants.CENTER);
        artLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        artLabel.setOpaque(true);
        artLabel.setBackground(new Color(0, 0, 0, 40));
        artLabel.setPreferredSize(new Dimension(100, 52));
        artLabel.setMaximumSize(new Dimension(Short.MAX_VALUE, 52));
        artLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(artLabel);
        panel.add(Box.createVerticalStrut(5));

        // Card name
        JLabel nameLabel = new JLabel(card.getName().toUpperCase());
        nameLabel.setFont(FONT_DISPLAY_SM);
        nameLabel.setForeground(COL_PARCHMENT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(2));

        // Type
        JLabel typeLabel = new JLabel(card.getType().toString());
        typeLabel.setFont(new Font("Georgia", Font.ITALIC, 9));
        typeLabel.setForeground(COL_GOLD);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(typeLabel);
        panel.add(Box.createVerticalStrut(4));

        // Description
        JTextArea desc = new JTextArea(card.getDescription());
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setOpaque(false);
        desc.setFont(new Font("Georgia", Font.ITALIC, 10));
        desc.setForeground(COL_PARCH_MUTED);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        desc.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));
        panel.add(desc);

        // Hover & click
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { handleCardClicked(card); }
            public void mouseEntered(MouseEvent e) {
                panel.setBorder(new CompoundBorder(
                        new LineBorder(COL_GOLD2, 1),
                        BorderFactory.createEmptyBorder(7, 7, 8, 7)));
            }
            public void mouseExited(MouseEvent e) {
                panel.setBorder(new CompoundBorder(
                        new LineBorder(COL_DIVIDER, 1),
                        BorderFactory.createEmptyBorder(7, 7, 8, 7)));
            }
        });

        return panel;
    }

    // =========================================================================
    // Interaction Handlers
    // =========================================================================
    private void handleCardClicked(Card card) {
        if (currentData == null || userInputListener == null) {
            return;
        }
        if (card.getTargetType() == TargetType.SELF_ONLY) {
            cardAwaitingEnemyTarget = null;
            sendUseCard(card, currentData.getSlayer());
            return;
        }
        List<Entity> foes = currentData.getEnemies();
        if (foes.isEmpty()) {
            chronicleLine("No foe stands before you — that card cannot be played.");
            return;
        }
        if (cardAwaitingEnemyTarget == card) {
            cardAwaitingEnemyTarget = null;
            chronicleLine("Cancelled — choose another card or click a foe again to target.");
            refreshUI();
            return;
        }
        cardAwaitingEnemyTarget = card;
        chronicleLine("«" + card.getName() + "» selected — click an enemy to play it on them.");
        refreshUI();
    }

    private void handleEnemyClicked(Entity enemy) {
        if (!enemy.isAlive()) {
            return;
        }
        if (cardAwaitingEnemyTarget != null && userInputListener != null) {
            Card toPlay = cardAwaitingEnemyTarget;
            cardAwaitingEnemyTarget = null;
            sendUseCard(toPlay, enemy);
            return;
        }
        chronicleLine("Choose a card first, then click an enemy to target.");
        refreshEnemies();
    }

    private void sendUseCard(Card card, Entity target) {
        if (userInputListener == null) {
            return;
        }
        userInputListener.ActionPerformed(new EntityAction()
                .setAction_enum(MoveTypes.USE_CARD)
                .setSelectedCard(card)
                .setTarget(target));
        resetState();
    }

    private void sendPass() {
        if (userInputListener == null) return;
        userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.PASS));
    }

    private void sendEndTurn() {
        if (userInputListener == null) return;
        userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.END_TURN));
        resetState();
    }

    private void resetState() {
        refreshUI();
    }

    // =========================================================================
    // Observer Implementation
    // =========================================================================
    @Override
    public void requestUserInput(RelevantGameData gameData) {
        SwingUtilities.invokeLater(() -> {
            this.currentData = gameData;
            resetState();
        });
    }

    @Override public void addDisplayFinishedListener(Listener l) { this.displayFinishedListener = l; }
    @Override public void addUserInputListener(Listener l)        { this.userInputListener = l; }

    public void chronicleLine(String message) {
        appendLog(message);
    }

    public void promptBattleVictory(String message, Runnable onContinue) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(SwingGameView.this, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(onContinue);
        });
    }

    @Override
    public void promptContinue(String message, Runnable onAcknowledge) {
        Runnable run = () -> {
            JOptionPane.showMessageDialog(SwingGameView.this, message, "Continue", JOptionPane.INFORMATION_MESSAGE);
            if (onAcknowledge != null) {
                onAcknowledge.run();
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            run.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(run);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(run);
            } catch (java.lang.reflect.InvocationTargetException e) {
                SwingUtilities.invokeLater(run);
            }
        }
    }

    @Override public void onInvalidMoveSelected(String message) {
        chronicleLine("Invalid: " + message);
        showDialog("Invalid Move", message, JOptionPane.WARNING_MESSAGE);
    }

    @Override public void onSlayerDefeat() {
        chronicleLine("The Slayer has fallen...");
    }

    @Override public void onEnemyDefeat(String name) {
        chronicleLine(name + " has been defeated.");
    }

    @Override public void onVictory() {
        chronicleLine("The Tower has been conquered, the realm is saved.");
    }

    @Override public void onDefeat() {
        chronicleLine("The darkness wins.");
        SwingUtilities.invokeLater(() -> {
            Object[] options = {"Try Again", "Close"};
            int choice = JOptionPane.showOptionDialog(
                    SwingGameView.this,
                    "You were overcome. Try again?",
                    "Defeat",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == JOptionPane.YES_OPTION && userInputListener != null) {
                cardAwaitingEnemyTarget = null;
                currentData = null;
                enemyIntentMap.clear();
                userInputListener.ActionPerformed(new EntityAction().setAction_enum(MoveTypes.RESTART_GAME));
            }
        });
    }

    @Override public void startGame() { rootLayout.show(rootPanel, "START"); }

    @Override public void defaultNotif(String message) {
        if (message != null && !message.isBlank()) {
            chronicleLine(message);
        }
    }

    @Override public void onFloorEntry(int floor) {
        cardAwaitingEnemyTarget = null;
        chronicleLine("── The Slayer enters floor " + floor + "...   ──");
    }

    @Override public void onFloorClear(int floor)  { chronicleLine("Floor " + floor + " lies behind you."); }
    @Override public void onBattleEntry() {
        enemyIntentMap.clear();
        cardAwaitingEnemyTarget = null;
        chronicleLine("The battle begins.");
    }

    @Override public void onBattleChange(List<Entity> a, List<Entity> b) { refreshUI(); }
    @Override public void onBattleWin()  { chronicleLine("Victory — the battle is won, for now."); }

    @Override public void onTurnStart(String name) { chronicleLine("Turn: " + name + "."); refreshUI(); }
    @Override public void onTurnEnd(String name) {}
    @Override public void onTurnPass(String name) { chronicleLine(name + " passes."); }

    @Override public void onEntityAttack(String attacker, String target, int dmg) {
        chronicleLine(attacker + " deals " + dmg + " damage to " + target + ".");
    }

    @Override public void onEntityDefense(String name, int blocked) {
        chronicleLine(name + " raises " + blocked + " block.");
    }

    @Override public void onEntityHeal(String name, int hp) { chronicleLine(name + " heals for " + hp + " HP."); }

    @Override public void onCardDrawn(Card c) {}
    @Override public void onCardPlayed(Card c) { }

    @Override public void onDeckShuffled() { chronicleLine("The deck is shuffled."); }

    @Override public void onDecideIntent(String name, IntentType intent) {
        if (name != null && intent != null) {
            enemyIntentMap.put(name, intent);
            refreshEnemies();
        }
    }

    @Override public void onEntityPass(String name) {}
    @Override public void onEntityDamaged(String name, int dmg) {
        if (dmg > 0) {
            chronicleLine(name + " loses " + dmg + " HP.");
        }
    }
    @Override public void onDemonKingFloor() {}

    // =========================================================================
    // Helpers & Factories
    // =========================================================================
    private void beginGame() {
        String name = nameInput.getText();
        String playerName = (name != null && !name.isBlank()) ? name.trim() : "Slayer";
        started = true;
        rootLayout.show(rootPanel, "GAME");
        chronicleLine("A shadowed tower rises beyond the ruined gates.");
        chronicleLine(playerName + " enters the Tower.");
        if (displayFinishedListener != null)
            displayFinishedListener.ActionPerformed(new EntityAction().setPlayerName(playerName));
    }

    private void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void showDialog(String title, String msg, int type) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, msg, title, type));
    }

    private void showCardDescription() {
        if (currentData == null || currentData.getCards().isEmpty()) {
            showDialog("No Cards", "No cards in hand.", JOptionPane.PLAIN_MESSAGE); return;
        }
        Card c = (Card) JOptionPane.showInputDialog(this, "Inspect card:",
                "Card Description", JOptionPane.PLAIN_MESSAGE, null,
                currentData.getCards().toArray(), currentData.getCards().get(0));
        if (c != null && userInputListener != null) {
            userInputListener.ActionPerformed(new EntityAction()
                    .setAction_enum(MoveTypes.GET_CARD_DESCRIPTION)
                    .setSelectedCard(c));
        }
    }

    private void showEnemyDescription() {
        if (currentData == null || currentData.getEnemies().isEmpty()) {
            showDialog("No Enemies", "No enemies present.", JOptionPane.PLAIN_MESSAGE); return;
        }
        Entity e = (Entity) JOptionPane.showInputDialog(this, "Inspect enemy:",
                "Enemy Description", JOptionPane.PLAIN_MESSAGE, null,
                currentData.getEnemies().toArray(), currentData.getEnemies().get(0));
        if (e != null && userInputListener != null) {
            userInputListener.ActionPerformed(new EntityAction()
                    .setAction_enum(MoveTypes.GET_ENTITY_DESCRIPTION)
                    .setTarget(e));
        }
    }

    private static JProgressBar makeBar(Color fill) {
        JProgressBar bar = new JProgressBar(0, 100) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0xFF, 0xFF, 0xFF, 12));
                g2.fillRect(0, 0, getWidth(), getHeight());
                int max = Math.max(1, getMaximum());
                int w = (int) (getWidth() * (double) getValue() / max);
                g2.setColor(getForeground());
                g2.fillRect(0, 0, w, getHeight());
                g2.dispose();
            }
        };
        bar.setForeground(fill);
        bar.setBackground(COL_STONE3);
        bar.setBorderPainted(false);
        bar.setStringPainted(false);
        bar.setPreferredSize(new Dimension(160, 6));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 6));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        return bar;
    }

    private JPanel makePip(boolean active) {
        JPanel pip = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(active ? COL_EMBER2 : COL_STONE3);
                g2.fillOval(0, 0, getWidth(), getHeight());
                if (!active) {
                    g2.setColor(COL_DIVIDER);
                    g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                }
                g2.dispose();
            }
        };
        pip.setOpaque(false);
        pip.setPreferredSize(new Dimension(10, 10));
        return pip;
    }

    // ── Mini bar ─────────────────────────────────────────────────────────────
    private JPanel buildMiniBar(int pct, Color fill) {
        JPanel track = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(COL_STONE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(fill);
                g.fillRect(0, 0, getWidth() * pct / 100, getHeight());
            }
        };
        track.setPreferredSize(new Dimension(60, 3));
        track.setMaximumSize(new Dimension(Short.MAX_VALUE, 3));
        return track;
    }

    // ── Section label ────────────────────────────────────────────────────────
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(COL_DIVIDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        lbl.setFont(FONT_DISPLAY_SM);
        lbl.setForeground(COL_GOLD);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Short.MAX_VALUE, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    // ── Stat row ─────────────────────────────────────────────────────────────
    private JPanel statRowPanel(String name, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 16));
        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(FONT_BODY_SM);
        nameLbl.setForeground(COL_PARCH_MUTED);
        row.add(nameLbl, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private static JLabel makeStatLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(new Font("Palatino Linotype", Font.BOLD, 11));
        lbl.setForeground(COL_PARCHMENT);
        return lbl;
    }

    // ── Action button ────────────────────────────────────────────────────────
    private static JButton makeActionButton(String text, boolean primary) {
        JButton btn = new JButton("▶  " + text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getModel().isRollover()
                        ? (primary ? new Color(0xC0, 0x42, 0x1A, 40) : new Color(0xFF,0xFF,0xFF,10))
                        : new Color(0,0,0,0));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Palatino Linotype", Font.BOLD, 10));
        btn.setForeground(primary ? COL_EMBER2 : COL_PARCH_DIM);
        btn.setBackground(new Color(0,0,0,0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(primary ? COL_EMBER : COL_DIVIDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    // ── Text field styling ────────────────────────────────────────────────────
    private static void styleTextField(JTextField field) {
        field.setBackground(COL_STONE3);
        field.setForeground(COL_PARCHMENT);
        field.setCaretColor(COL_GOLD);
        field.setFont(new Font("Georgia", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
                new LineBorder(COL_DIVIDER, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private static void styleStartButton(JButton btn) {
        btn.setFont(new Font("Palatino Linotype", Font.BOLD, 12));
        btn.setForeground(COL_PARCHMENT);
        btn.setBackground(COL_EMBER);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(COL_EMBER2, 1),
                BorderFactory.createEmptyBorder(8, 22, 8, 22)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static String spritePngBaseName(Entity e) {
        if (e instanceof DemonKing) return "demonlord";
        return e.getClass().getSimpleName().toLowerCase();
    }

    private ImageIcon loadPngSprite(Entity entity, int w, int h) {
        String path = "/sprites/" + spritePngBaseName(entity) + ".png";
        String key = path + ":" + w + "x" + h;
        if (spriteCache.containsKey(key)) {
            return spriteCache.get(key);
        }
        URL res = getClass().getResource(path);
        if (res == null) {
            return null;
        }
        ImageIcon raw = new ImageIcon(res);
        if (raw.getIconWidth() <= 0) {
            return null;
        }
        Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);
        spriteCache.put(key, icon);
        return icon;
    }

    private String getEmojiForCard(Card card) {
        if (card.getType() == CardType.HEAL)   return "🌿";
        if (card.getType() == CardType.SHIELD) return "🛡";
        return "⚔";
    }

    // ── Intent helpers ────────────────────────────────────────────────────────
    private String intentShortLabel(IntentType i) {
        return switch (i) {
            case ATTACK  -> "⚔ Attack";
            case DEFEND  -> "🛡 Defend";
        };
    }

    private Color intentBadgeColor(IntentType i) {
        return switch (i) {
            case ATTACK -> COL_BLOOD;
            case DEFEND -> COL_SAPPHIRE;
        };
    }

    // ── Card accent color ─────────────────────────────────────────────────────
    private Color cardAccentColor(Card card) {
        if (card.getType() == CardType.HEAL)   return COL_FOREST;
        if (card.getType() == CardType.SHIELD) return COL_SAPPHIRE;
        return COL_BLOOD;
    }
}