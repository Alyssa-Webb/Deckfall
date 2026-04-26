
# Deckfall

Deckfall is a course project being developed by two students at the University of Colorado Boulder. It is a simplified version of the game *Slay the Spire*, with a focus on following Object Oriented Design and Analysis (i.e. SOLID) principles, implementing design patterns, and demonstrating core Java concepts through test-driven-development. 

---

## Gameplay
The player controls a Slayer who is navigating a tower, with the goal of defeating
the Demon King on the tenth floor. Each floor of the tower, the Slayer encounters enemies, and the Slayer
will draw a hand of cards, where they will need to spend their energy to play these cards against enemies. Enemies act 
automatically each turn. After the enemies on the floor are defeated, the Slayer advances
to the next floor, until they reach the Demon King. Upon defeating the Demon King, the game ends, Victory! Otherwise,
the Slayer is defeated and must climb the tower once again...

---
## How to Run Deckfall
The game is run from the terminal using Gradle: 


| Argument | Options |
|---|---|
| `<interface>` | `console`, `gui` |
| `<difficulty>` | `easy`, `medium`, `hard` |
| `<tower>` | `standard`, `small`, `tiny` |

### Console UI
    Usage: ./gradlew -q run --args='console <difficulty> <tower>'

    e.g. ./gradlew -q run --args='console easy tiny'
  
### Swing GUI
    Usage: ./gradlew -q run --args='gui <difficulty> <tower>'

    e.g. ./gradlew -q run --args='gui easy tiny'
  
---

## Team Members
- Adara Noble
- Alyssa Webb

## Tech Stack
- **Language:** Java 25
- **Build Tool:** Gradle
- **GUI Tool:** Java Swing
- **Testing:** JUnit 5

---

## Mid-Project Review

### Design Patterns
- The first design pattern we are implementing is the Factory Pattern, which can be found in the “Factory” package for the classes “SlayerFactory”, “EnemyFactory”, and “CardFactory”. We found it necessary to create the CardFactory as multiple types of cards are being created and inheriting from the same abstract parent Card class. Additioanlly, we are currently planning to add more cards (like poison and healing cards); using this pattern will help us follow the Open-Closed Principle and ensure encapsulation as we expand the scope of our project. This principle also applies to our EnemyFactory and SlayerFactory, as the Factory Pattern will make it easier to extend each class for more types, and create additional methods that could randomly generate Enemies, and generate starting decks for the Slayer. 

- The second pattern we are implementing is the Observer Pattern, which currently exists under the “Observer” package, for the “GameEventManager”, and “GameEventObserver”. We found the Observer pattern necessary for printing events to the console and GUI we will use. These events include, but are certainly not limited to, telling the Player what level or floor they are on, printing Cards when a fight begins, printing a “Defeat” or “Victory” screen when the Player is killed in a battle, or when the Player wins against the Demon King. Using the Observer Pattern removes the complexity of print statements from the main game code, and helps us keep track of what game events are listening for input. We also intend to use the Observer pattern as a bridge between our Model and Controller as we move up in complexity.
  
- The third pattern we have implemented is the Adapter Pattern, which can be found in the "Observer" package, called "GameEventAdapter", which works hand in hand with the Observer Pattern. The purpose of this class is to act as a sort of filter so that the listeners/observers we create do not need to implement all 20+ Event Methods that are in our current GameEventObserver Interface. For example, if we want to add sound effects (e.g. SoundEffectManager) down the line, we may not want to implement all of the card sound effects, and just want trigger sounds on events like Victory and Defeat. So, by extending GameEventAdapter, we only need to implement the necessary onVictory() and onDefeat() methods. 

- The fourth pattern we are implementing is the Model-View-Controller Pattern, which can be found in the "Game" and "Controller" package, where "Game" is currently representing our Model. This design pattern is currently a work-in-progress as we are trying to integrate Controller to work with both the ConsoleUI and the SwingUI as we work to implement Player input.

- The fifth pattern we are planning to implement is the State pattern, which is located in the "Game" package as the enum class "GameState", and the class "GameStateManager". This will be necessary in controlling game flow, and determining what the Player can do depending on the current state of the game. For example, if the current state is "PLAYER_TURN", the player will be able to choose what Card to use, otherwise, if the current state is "ENEMY_TURN", the Player should not be able to take any action or give any input. 

