package deckfall.Observer;

import deckfall.Card.Card;
import deckfall.Controller.Listener;
import deckfall.DataClasses.EntityAction;
import deckfall.DataClasses.RelevantGameData;
import deckfall.Entity.Entity;
import deckfall.Entity.IntentType;
import deckfall.Game.MoveTypes;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class SwingGameView extends JFrame implements GameEventObserver {

    private Listener userInputListener;
    private Listener displayFinishedListener;

    private RelevantGameData currentData;

    private enum UIState {
        IDLE,
        CARD_SELECTED
    }

    private UIState uiState = UIState.IDLE;
    private Card selectedCard = null;

    // Panels
    private JPanel playerPanel = new JPanel();
    private JPanel enemyPanel = new JPanel();
    private JPanel cardPanel = new JPanel();
    private JButton passButton = new JButton("Pass Turn");

    // Player labels
    private JLabel playerLabel = new JLabel();

    public SwingGameView() {
        setTitle("DeckFall");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupPlayerPanel();
        setupEnemyPanel();
        setupCardPanel();
        setupSidePanel();

        setVisible(true);
    }

    private void setupPlayerPanel() {
        playerPanel.setLayout(new FlowLayout());
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));

        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerPanel.add(playerLabel);

        // Click player
        playerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handlePlayerClicked();
            }
        });

        add(playerPanel, BorderLayout.NORTH);
    }

    private void setupEnemyPanel() {
        enemyPanel.setLayout(new FlowLayout());
        enemyPanel.setBorder(BorderFactory.createTitledBorder("Enemies"));
        add(enemyPanel, BorderLayout.CENTER);
    }

    private void setupCardPanel() {
        cardPanel.setLayout(new FlowLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder("Cards"));
        add(cardPanel, BorderLayout.SOUTH);
    }

    private void setupSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        passButton.addActionListener(e -> {
            sendPass();
        });

        sidePanel.add(passButton);

        add(sidePanel, BorderLayout.EAST);
    }

    // =============================
    // Core UI Logic
    // =============================

    private void refreshUI() {
        if (currentData == null) return;

        updatePlayer();
        updateEnemies();
        updateCards();

        revalidate();
        repaint();
    }

    private void updatePlayer() {
        Entity player = currentData.getSlayer();
        playerLabel.setText(player.getName() +
                " | HP: " + player.getHP() +
                " | Block: " + player.getBlock());
    }

    private void updateEnemies() {
        enemyPanel.removeAll();

        for (Entity enemy : currentData.getEnemies()) {
            JButton enemyButton = new JButton(enemy.toString());

            enemyButton.setEnabled(uiState == UIState.CARD_SELECTED);

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
            JButton cardButton = new JButton(card.getSimpleString());

            // Tooltip = description
            cardButton.setToolTipText("<html>" + card.getDescription() + "</html>");

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
        uiState = UIState.CARD_SELECTED;
        refreshUI();
    }

    private void handleEnemyClicked(Entity enemy) {
        if (uiState != UIState.CARD_SELECTED) return;

        sendUseCard(enemy);
    }

    private void handlePlayerClicked() {
        if (uiState == UIState.CARD_SELECTED) {
            sendUseCard(currentData.getSlayer());
        } else {
            JOptionPane.showMessageDialog(this, "You did a lil jig!\nNothing happened.");
        }
    }

    private void sendUseCard(Entity target) {
        userInputListener.ActionPerformed(
                new EntityAction()
                        .setAction_enum(MoveTypes.USE_CARD)
                        .setSelectedCard(selectedCard)
                        .setTarget(target)
        );

        resetState();
    }

    private void sendPass() {
        userInputListener.ActionPerformed(
                new EntityAction().setAction_enum(MoveTypes.PASS)
        );
        resetState();
    }

    private void resetState() {
        selectedCard = null;
        uiState = UIState.IDLE;
        refreshUI();
    }

    // =============================
    // Observer Implementation
    // =============================

    @Override
    public void requestUserInput(RelevantGameData gameData) {
        this.currentData = gameData;
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
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void onSlayerDefeat() {
        JOptionPane.showMessageDialog(this, "You died!");
    }

    @Override
    public void onEnemyDefeat(String enemyName) {
        JOptionPane.showMessageDialog(this, enemyName + " died!");
    }

    @Override
    public void onVictory() {
        JOptionPane.showMessageDialog(this, "You win!");
    }

    @Override
    public void onDefeat() {
        JOptionPane.showMessageDialog(this, "You lose :(");
    }

    @Override
    public void startGame() {
        JOptionPane.showMessageDialog(this, "Game started!");
        if (displayFinishedListener != null) {
            displayFinishedListener.ActionPerformed(new EntityAction());
        }
    }

    @Override
    public void update(RelevantGameData relevantGameData) {

    }

    // =============================
    // Unused Observer Methods
    // =============================

    @Override public void defaultNotif(String message) {}

    @Override
    public void onFloorEntry(int floor) {

    }

    @Override
    public void onFloorClear(int floor) {

    }

    @Override public void onBattleEntry() {}
    @Override public void onBattleChange(List<Entity> a, List<Entity> b) { refreshUI(); }
    @Override public void onBattleWin() {}
    @Override public void onTurnStart(String entityName) {}
    @Override public void onTurnEnd(String entityName) {}
    @Override public void onTurnPass(String entityName) {}
    @Override public void onEntityAttack(String a, String b, int d) {}
    @Override public void onEntityDefense(String e, int b) {}
    @Override public void onEntityHeal(String e, int h) {}
    @Override public void onCardDrawn(Card c) {}
    @Override public void onCardPlayed(Card c) {}
    @Override public void onDeckShuffled() {}
    @Override public void onDecideIntent(String e, IntentType i) {}
    @Override public void onEntityPass(String e) {}
    @Override public void onEntityDamaged(String e, int d) {}

    @Override
    public void onDemonKingFloor() {

    }
}


/*
TODO: add to a dedicated txt file? IntelliJ gives warnings about large, commented out section of code, so it seems to matter somewhat?
List of prompts used:
{
    For my Java class' final project, we're allowed to AI generate our GUI. I made our consoleLogger compatible with MVC, so theoretically a GUI should be able to replace it smoothly with no changes to the Model or Controller.

    Here's our current view:

    (I then pasted it our code for ConsoleLogger. For obvious reasons, I'm not including it here lol)

    The user's cards change every turn. Also, we have no preference between Swing and Java2D -- whatever is easier for us to edit, to make the GUI more to what we want. Don't write any code -- first, plan it out with me
}
{
    The general structure is as follows: you're in a tower, and each floor you fight a couple battles. The enemies on screen will change as enemies die and battles start/end. You should be able to click yourself in order to 'do a little jig' (which either prints something or moves the Slayer sprite up and down a bit). Your and enemy stats should be visible at all times (so yes, enemies should only be clickable after you select a card).

    Card:

    (I then pasted the abstract Card class)

    Entity:

    (I then pasted the abstract Entity class)

    We will be adding a sprite/image section to cards and entities. Please create us a GUI using Swing
}
Context for the next one: instead of creating a class, it just gave a general outline of what we should do with swing, to help us understand swing.
{
    I do understand swing. My teacher is allowing us to have AI completely write the GUI (which is great, because I've written Swing code before, and it is tedious). In his words: "this isn't a GUI class"
}
{
    Clicking a card immediately enters targeting mode. And yes, those assumptions are all correct
}
It then provided me this class. Missing a couple methods, but IntelliJ made fixing that easy.
 */