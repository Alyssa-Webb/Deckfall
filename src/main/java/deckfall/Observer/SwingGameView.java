package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Card.CardType;
import deckfall.Card.TargetType;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.Entity;
import deckfall.Entity.IntentType;
import deckfall.Entity.Slayer;
import deckfall.Game.MoveTypes;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingGameView extends JFrame implements GameEventObserver {

    private Listener userInputListener;
    private Listener displayFinishedListener;

    private RelevantGameData currentData;
    private boolean started = false;
    private String selectedPlayerName = "Slayer";
    private boolean appliedPlayerName = false;
    private int currentFloor = 1;
    private int currentBattleNumber = 0;
    private int totalBattlesOnFloor = 0;

    private enum UIState {
        IDLE,
        CARD_SELECTED
    }

    private UIState uiState = UIState.IDLE;
    private Card selectedCard = null;
    private Entity selectedEnemy = null;
    private final Map<String, IntentType> enemyIntentMap = new HashMap<>();

    // Root Layout
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    // Start Screen
    private final JPanel startPanel = new JPanel();
    private final JTextField nameInput = new JTextField("Slayer", 18);
    private final JButton startButton = new JButton("Start New Game");

    // Game Panels
    private final JPanel gamePanel = new JPanel(new BorderLayout());
    private final JPanel playerPanel = new JPanel();
    private final JPanel enemyPanel = new JPanel();
    private final JPanel cardPanel = new JPanel();
    private final JButton passButton = new JButton("Pass Action");
    private final JButton endTurnButton = new JButton("End Turn");
    private final JButton cardInfoButton = new JButton("View Card Description");
    private final JButton enemyInfoButton = new JButton("View Enemy Description");
    private final JTextArea logArea = new JTextArea();

    // Player labels
    private final JLabel playerLabel = new JLabel();

    public SwingGameView() {
        setTitle("DeckFall");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        setupStartPanel();
        setupGamePanel();

        rootPanel.add(startPanel, "START");
        rootPanel.add(gamePanel, "GAME");
        add(rootPanel, BorderLayout.CENTER);

        rootLayout.show(rootPanel, "START");
        setupPlayerPanel();
        setupEnemyPanel();
        setupCardPanel();
        setupSidePanel();

        setVisible(true);
    }

    private void setupStartPanel() {
        startPanel.setLayout(new GridBagLayout());
        startPanel.setBackground(new Color(18, 18, 18));

        JPanel startCard = new JPanel();
        startCard.setLayout(new BoxLayout(startCard, BoxLayout.Y_AXIS));
        startCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        startCard.setBackground(new Color(45, 45, 45));

        JLabel title = new JLabel("Deckfall");
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(220, 220, 220));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Enter the Slayer's name:");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(210, 210, 210));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameInput.setMaximumSize(new Dimension(260, 30));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> beginGameFromStartScreen());

        startCard.add(title);
        startCard.add(Box.createVerticalStrut(16));
        startCard.add(subtitle);
        startCard.add(Box.createVerticalStrut(8));
        startCard.add(nameInput);
        startCard.add(Box.createVerticalStrut(14));
        startCard.add(startButton);

        startPanel.add(startCard);
    }

    private void setupGamePanel() {
        gamePanel.setBackground(new Color(20, 20, 20));
    }

    private void setupPlayerPanel() {
        playerPanel.setLayout(new FlowLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        playerPanel.setBackground(new Color(50, 50, 50));

        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerLabel.setForeground(new Color(230, 230, 230));
        playerPanel.add(playerLabel);

        // Click player
        playerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlePlayerClicked();
            }
        });

        gamePanel.add(playerPanel, BorderLayout.NORTH);
    }

    private void setupEnemyPanel() {
        enemyPanel.setLayout(new FlowLayout());
        enemyPanel.setBorder(BorderFactory.createTitledBorder("Enemies"));
        enemyPanel.setBackground(new Color(35, 35, 35));
        gamePanel.add(enemyPanel, BorderLayout.CENTER);
    }

    private void setupCardPanel() {
        cardPanel.setLayout(new FlowLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder("Cards"));
        cardPanel.setBackground(new Color(25, 25, 25));
        gamePanel.add(cardPanel, BorderLayout.SOUTH);
    }

    private void setupSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        sidePanel.setBackground(new Color(30, 30, 30));

        passButton.addActionListener(e -> {
            sendPass();
        });
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.setMaximumSize(new Dimension(220, 35));

        endTurnButton.addActionListener(e -> {
            sendEndTurn();
        });
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setMaximumSize(new Dimension(220, 35));

        cardInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardInfoButton.setMaximumSize(new Dimension(220, 35));
        cardInfoButton.addActionListener(e -> showSelectedCardDescription());

        enemyInfoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enemyInfoButton.setMaximumSize(new Dimension(220, 35));
        enemyInfoButton.addActionListener(e -> showSelectedEnemyDescription());

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(new Color(15, 15, 15));
        logArea.setForeground(new Color(215, 215, 215));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(280, 500));
        sidePanel.add(logScrollPane);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(cardInfoButton);
        sidePanel.add(Box.createVerticalStrut(8));
        sidePanel.add(enemyInfoButton);
        sidePanel.add(Box.createVerticalStrut(8));
        sidePanel.add(passButton);
        sidePanel.add(Box.createVerticalStrut(8));
        sidePanel.add(endTurnButton);

        gamePanel.add(sidePanel, BorderLayout.EAST);
    }

    // =============================
    // Core UI Logic
    // =============================

    private void refreshUI() {
        if (currentData == null) return;

        updatePlayer();
        updateEnemies();
        updateCards();
        passButton.setEnabled(started && currentData != null);
        endTurnButton.setEnabled(started && currentData != null);
        cardInfoButton.setEnabled(started && currentData != null);
        enemyInfoButton.setEnabled(started && currentData != null);

        revalidate();
        repaint();
    }

    private void updatePlayer() {
        Entity player = currentData.getSlayer();
        int energy = 0;
        int maxEnergy = 0;
        if (player instanceof Slayer slayer) {
            energy = slayer.getEnergy();
            maxEnergy = slayer.getMaxEnergy();
        }
        playerLabel.setText(player.getName() +
                " | HP: " + player.getHP() + "/" + player.getMaxHP() +
                " | Block: " + player.getBlock() +
                " | Energy: " + energy + "/" + maxEnergy);
    }

    private void updateEnemies() {
        enemyPanel.removeAll();

        for (Entity enemy : currentData.getEnemies()) {
            JButton enemyButton = new JButton(formatEnemyButtonText(enemy));
            enemyButton.setPreferredSize(new Dimension(180, 80));

            if (enemy == selectedEnemy) {
                enemyButton.setBorder(new LineBorder(Color.CYAN, 3));
            }

            enemyButton.addActionListener(e -> {
                handleEnemyClicked(enemy);
            });

            enemyPanel.add(enemyButton);
        }
    }

    private void updateCards() {
        cardPanel.removeAll();

        List<Card> cards = currentData.getCards();

        for (Card card : cards) {
            JButton cardButton = new JButton(formatCardButtonText(card));
            cardButton.setPreferredSize(new Dimension(160, 220));
            cardButton.setVerticalAlignment(SwingConstants.TOP);
            cardButton.setUI(new BasicButtonUI());
            cardButton.setBackground(getCardColor(card));
            cardButton.setForeground(new Color(245, 245, 245));
            cardButton.setOpaque(true);
            cardButton.setContentAreaFilled(true);
            cardButton.setFocusPainted(false);
            cardButton.setBorderPainted(false);
            cardButton.setHorizontalAlignment(SwingConstants.CENTER);
            cardButton.setVerticalTextPosition(SwingConstants.TOP);
            cardButton.setMargin(new Insets(8, 8, 8, 8));

            // Highlight if selected
            if (card == selectedCard) {
                cardButton.setBorder(new LineBorder(Color.GREEN, 3));
            }

            cardButton.addActionListener(e -> {
                handleCardClicked(card);
            });

            cardPanel.add(cardButton);
        }
    }

    // =============================
    // Interaction Handlers
    // =============================

    private void handleCardClicked(Card card) {
        selectedCard = card;
        if (currentData != null && card.getTargetType() == TargetType.SELF_ONLY) {
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Use " + card.getName() + " on yourself?",
                    "Confirm Self-Target Card",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmation != JOptionPane.YES_OPTION) {
                selectedCard = null;
                uiState = UIState.IDLE;
                refreshUI();
                return;
            }
            sendUseCard(currentData.getSlayer());
            return;
        }
        uiState = UIState.CARD_SELECTED;
        refreshUI();
    }

    private void handleEnemyClicked(Entity enemy) {
        selectedEnemy = enemy;
        if (uiState != UIState.CARD_SELECTED) {
            refreshUI();
            return;
        }

        sendUseCard(enemy);
    }

    private void handlePlayerClicked() {
        if (currentData == null) {
            return;
        }
        if (uiState == UIState.CARD_SELECTED) {
            sendUseCard(currentData.getSlayer());
        } else {
            appendLog("The Slayer does a lil jig. Morale increases.");
        }
    }

    private void sendUseCard(Entity target) {
        if (userInputListener == null) {
            return;
        }
        userInputListener.ActionPerformed(
                new EntityAction()
                        .setAction_enum(MoveTypes.USE_CARD)
                        .setSelectedCard(selectedCard)
                        .setTarget(target)
        );

        resetState();
    }

    private void sendPass() {
        if (userInputListener == null) {
            return;
        }
        userInputListener.ActionPerformed(
                new EntityAction().setAction_enum(MoveTypes.PASS)
        );
    }

    private void sendEndTurn() {
        if (userInputListener == null) {
            return;
        }
        userInputListener.ActionPerformed(
                new EntityAction().setAction_enum(MoveTypes.END_TURN)
        );
        resetState();
    }

    private void resetState() {
        selectedCard = null;
        selectedEnemy = null;
        uiState = UIState.IDLE;
        refreshUI();
    }

    // =============================
    // Observer Implementation
    // =============================

    @Override
    public void requestUserInput(RelevantGameData gameData) {
        this.currentData = gameData;
        if (!appliedPlayerName && selectedPlayerName != null && !selectedPlayerName.isBlank()) {
            currentData.getSlayer().setName(selectedPlayerName);
            appliedPlayerName = true;
        }
        resetState();
    }

    @Override
    public void addDisplayFinishedListener(Listener listener) {
        this.displayFinishedListener = listener;
    }

    @Override
    public void addUserInputListener(Listener listener) {
        this.userInputListener = listener;
    }

    @Override
    public void onInvalidMoveSelected(String message) {
        appendLog("Invalid move: " + message);
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void onSlayerDefeat() {
        appendLog("The Slayer has fallen.");
        JOptionPane.showMessageDialog(this, "You died!");
    }

    @Override
    public void onEnemyDefeat(String enemyName) {
        appendLog(enemyName + " was defeated.");
        JOptionPane.showMessageDialog(this, enemyName + " died!");
    }

    @Override
    public void onVictory() {
        appendLog("Victory. The tower has been conquered.");
        JOptionPane.showMessageDialog(this, "You win!");
    }

    @Override
    public void onDefeat() {
        appendLog("Defeat.");
        JOptionPane.showMessageDialog(this, "You lose :(");
    }

    @Override
    public void startGame() {
        rootLayout.show(rootPanel, "START");
    }

    // =============================
    // Unused Observer Methods
    // =============================

    @Override public void defaultNotif(String message) {}

    @Override
    public void onFloorEntry(int floor) {
        currentFloor = floor;
        currentBattleNumber = 0;
        totalBattlesOnFloor = estimateBattlesForFloor(floor);
        appendLog("Entering Level " + floor + ".");
        appendLog("Entering Floor " + floor + " of 10.");
    }

    @Override
    public void onFloorClear(int floor) {
        appendLog("Floor " + floor + " cleared.");
    }

    @Override
    public void onBattleEntry() {
        currentBattleNumber++;
        String totalBattlesDisplay = totalBattlesOnFloor > 0 ? String.valueOf(totalBattlesOnFloor) : "?";
        appendLog("Starting Battle " + currentBattleNumber + " of " + totalBattlesDisplay + " on Floor " + currentFloor + ".");
    }
    @Override public void onBattleChange(List<Entity> a, List<Entity> b) { refreshUI(); }
    @Override public void onBattleWin() {
        appendLog("Battle won.");
    }
    @Override public void onTurnStart(String entityName) {
        appendLog("Turn: " + entityName);
    }
    @Override public void onTurnEnd(String entityName) {}
    @Override public void onTurnPass(String entityName) {
        appendLog(entityName + " passed their action.");
    }
    @Override public void onEntityAttack(String attackerName, String targetName, int damageDealt) {
        if (currentData != null && attackerName.equals(currentData.getSlayer().getName())) {
            appendLog(attackerName + " hit " + targetName + " for " + damageDealt + " damage.");
            return;
        }
        appendLog(attackerName + " attacked " + targetName + " for " + damageDealt + " damage.");
    }
    @Override public void onEntityDefense(String entityName, int damageBlocked) {
        appendLog(entityName + " defended and gained " + damageBlocked + " block.");
    }
    @Override public void onEntityHeal(String e, int h) {}
    @Override public void onCardDrawn(Card c) {}
    @Override public void onCardPlayed(Card c) {
        if (c != null && currentData != null) {
            appendLog(currentData.getSlayer().getName() + " played " + c.getName() + ".");
        }
    }
    @Override public void onDeckShuffled() {}
    @Override public void onDecideIntent(String enemyName, IntentType intent) {
        if (enemyName != null && intent != null) {
            enemyIntentMap.put(enemyName, intent);
            appendLog(enemyName + " intends to " + intent + ".");
            refreshUI();
        }
    }
    @Override public void onEntityPass(String e) {}
    @Override public void onEntityDamaged(String e, int d) {}
    @Override public void onDemonKingFloor() {}

    private void beginGameFromStartScreen() {
        String enteredName = nameInput.getText();
        if (enteredName != null && !enteredName.isBlank()) {
            selectedPlayerName = enteredName.trim();
        } else {
            selectedPlayerName = "Slayer";
        }

        started = true;
        appliedPlayerName = false;
        rootLayout.show(rootPanel, "GAME");
        appendLog("A shadowed tower rises beyond the ruined gates.");
        appendLog("Insert tower story description here.");
        appendLog("Slayer: " + selectedPlayerName);
        appendLog(selectedPlayerName + " enters the Tower.");

        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(new EntityAction());
        }
    }

    private void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private int estimateBattlesForFloor(int floor) {
        if (floor <= 0) {
            return 0;
        }
        if (floor <= 5) {
            return 2;
        }
        if (floor <= 9) {
            return 3;
        }
        return 1;
    }

    private String formatCardButtonText(Card card) {
        return "<html><div style='width:136px;text-align:center;'><b>" + card.getName() + "</b><br/>"
                + "Cost: " + card.getEnergyCost() + " Energy<br/>"
                + card.getType() + "<br/>"
                + card.getDescription() + "</div></html>";
    }

    private String formatEnemyButtonText(Entity enemy) {
        IntentType intent = enemyIntentMap.get(enemy.getName());
        String intentText = intent == null ? "Unknown" : intent.toString();
        return "<html><center><b>" + enemy.getName() + "</b><br/>"
                + "HP: " + enemy.getHP() + " | Block: " + enemy.getBlock() + "<br/>"
                + "<span style='color:#d7d7d7;'>\uD83D\uDCAC " + intentText + "</span></center></html>";
    }

    private Color getCardColor(Card card) {
        if (card.getType() == CardType.HEAL) {
            return new Color(41, 126, 65);
        }
        if (card.getType() == CardType.SHIELD) {
            return new Color(44, 90, 146);
        }
        return new Color(148, 44, 44);
    }

    private void showSelectedCardDescription() {
        if (currentData == null || currentData.getCards().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards available.");
            return;
        }
        Card selected = (Card) JOptionPane.showInputDialog(
                this,
                "Choose a card to inspect:",
                "Card Description",
                JOptionPane.PLAIN_MESSAGE,
                null,
                currentData.getCards().toArray(),
                currentData.getCards().get(0)
        );
        if (selected == null) {
            return;
        }
        JOptionPane.showMessageDialog(this,
                selected.getName() + ":\n" + selected.getDescription(),
                "Card Description",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSelectedEnemyDescription() {
        if (currentData == null || currentData.getEnemies().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No enemies available.");
            return;
        }
        Entity selected = (Entity) JOptionPane.showInputDialog(
                this,
                "Choose an enemy to inspect:",
                "Enemy Description",
                JOptionPane.PLAIN_MESSAGE,
                null,
                currentData.getEnemies().toArray(),
                currentData.getEnemies().get(0)
        );
        if (selected == null) {
            return;
        }
        JOptionPane.showMessageDialog(this,
                selected.getName() + ":\n" + selected.getDescription(),
                "Enemy Description",
                JOptionPane.INFORMATION_MESSAGE);
    }
}