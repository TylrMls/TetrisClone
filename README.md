# Android Tetris Clone

A modern Tetris-style falling-block puzzle game for Android, built with **Kotlin** and **Jetpack Compose**.

The project separates the game rules from Android-specific code so that the core game engine remains predictable, testable, and easy to extend.

> This is an educational Tetris clone and is not affiliated with or endorsed by The Tetris Company.

## Features

* Standard seven-piece tetromino set
* Piece movement and rotation
* Automatic gravity
* Soft drop and hard drop
* Line clearing
* Score and level tracking
* Increasing fall speed
* Next-piece preview
* Pause, restart, and game-over states
* Touch-based controls
* Game logic that can be tested without an Android device

## Technology

* **Kotlin**
* **Jetpack Compose**
* **Android ViewModel**
* **Kotlin Coroutines**
* **StateFlow**
* **JUnit**
* **Gradle Kotlin DSL**

## Architecture

The application uses a simplified **MVVM architecture** with a separate, platform-independent game engine.

```text
┌─────────────────────────────────────────────┐
│                 Compose UI                  │
│ GameScreen, Board, Controls, ScoreDisplay   │
└──────────────────────┬──────────────────────┘
                       │ User actions
                       ▼
┌─────────────────────────────────────────────┐
│                 GameViewModel               │
│ Owns UI state and manages the game loop     │
└──────────────────────┬──────────────────────┘
                       │ Game commands
                       ▼
┌─────────────────────────────────────────────┐
│                  GameEngine                 │
│ Movement, rotation, collisions, scoring     │
└──────────────────────┬──────────────────────┘
                       │ Uses
                       ▼
┌─────────────────────────────────────────────┐
│                  Game Model                 │
│ Board, Tetromino, Position, GameState       │
└─────────────────────────────────────────────┘
```

### Why this architecture?

The game engine contains no Compose or Android dependencies. This provides several advantages:

* Game rules can be tested with ordinary unit tests.
* UI changes do not require changes to the engine.
* The game loop is controlled in one place.
* State changes flow in one direction.
* New features such as saved games, alternate controls, or multiplayer can be added more easily.

This project does not require a full multi-layer Clean Architecture implementation. Tetris has limited persistence and networking requirements, so additional repository and use-case layers would add unnecessary complexity. These layers can still be introduced later if the project grows.

## Software Layout

```text
app/
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   └── java/com/example/tetris/
│   │       ├── MainActivity.kt
│   │       │
│   │       ├── engine/
│   │       │   ├── GameEngine.kt
│   │       │   ├── CollisionDetector.kt
│   │       │   ├── LineClearer.kt
│   │       │   ├── RotationSystem.kt
│   │       │   └── TetrominoGenerator.kt
│   │       │
│   │       ├── model/
│   │       │   ├── Board.kt
│   │       │   ├── Cell.kt
│   │       │   ├── GameAction.kt
│   │       │   ├── GameStatus.kt
│   │       │   ├── GameState.kt
│   │       │   ├── Position.kt
│   │       │   ├── Tetromino.kt
│   │       │   └── TetrominoType.kt
│   │       │
│   │       ├── ui/
│   │       │   ├── GameScreen.kt
│   │       │   ├── components/
│   │       │   │   ├── GameBoard.kt
│   │       │   │   ├── GameControls.kt
│   │       │   │   ├── GameOverlay.kt
│   │       │   │   ├── NextPiecePreview.kt
│   │       │   │   └── ScorePanel.kt
│   │       │   └── theme/
│   │       │       ├── Color.kt
│   │       │       ├── Theme.kt
│   │       │       └── Type.kt
│   │       │
│   │       └── viewmodel/
│   │           └── GameViewModel.kt
│   │
│   ├── test/
│   │   └── java/com/example/tetris/
│   │       ├── GameEngineTest.kt
│   │       ├── CollisionDetectorTest.kt
│   │       ├── LineClearerTest.kt
│   │       └── RotationSystemTest.kt
│   │
│   └── androidTest/
│       └── java/com/example/tetris/
│           └── GameScreenTest.kt
│
├── build.gradle.kts
└── proguard-rules.pro
```

Package names may differ depending on the application namespace selected when the project is created.

## Core Classes

### `MainActivity`

The Android entry point.

Responsibilities:

* Applies the application theme.
* Creates the root Compose content.
* Displays `GameScreen`.
* Obtains the `GameViewModel`.

`MainActivity` should contain very little game-specific logic.

### `GameViewModel`

Connects the Compose UI to the game engine.

Responsibilities:

* Owns the observable `GameState`.
* Receives commands from the UI.
* Starts and stops the game loop.
* Advances the game at the current gravity interval.
* Pauses updates when the game is paused or over.
* Cancels coroutine work when the ViewModel is cleared.

Example commands include:

```kotlin
fun moveLeft()
fun moveRight()
fun rotateClockwise()
fun softDrop()
fun hardDrop()
fun pauseGame()
fun resumeGame()
fun restartGame()
```

The UI observes a read-only `StateFlow<GameState>` and redraws whenever the state changes.

### `GameEngine`

Contains the main game rules.

Responsibilities:

* Creates new games.
* Processes player actions.
* Advances the game by one tick.
* Locks pieces into the board.
* Clears completed lines.
* Updates the score and level.
* Spawns the next piece.
* Detects game-over conditions.

A useful interface is:

```kotlin
class GameEngine {
    fun createGame(): GameState

    fun update(
        state: GameState,
        action: GameAction
    ): GameState
}
```

Returning a new state rather than modifying UI state directly makes the engine easier to test.

### `GameState`

An immutable snapshot of the complete game.

```kotlin
data class GameState(
    val board: Board,
    val activePiece: Tetromino,
    val nextPiece: TetrominoType,
    val score: Int,
    val clearedLines: Int,
    val level: Int,
    val status: GameStatus
)
```

Additional fields may include the held piece, elapsed time, drop interval, or current randomizer state.

### `Board`

Represents the fixed playfield.

The standard visible board is 10 cells wide and 20 cells high. Hidden spawn rows may be added above the visible area.

Responsibilities:

* Stores locked cells.
* Determines whether coordinates are occupied.
* Adds a locked tetromino.
* Detects complete rows.
* Removes rows and shifts cells downward.

The board should not contain rendering code.

### `Cell`

Represents one board location.

```kotlin
data class Cell(
    val isOccupied: Boolean,
    val type: TetrominoType? = null
)
```

An alternative representation can use nullable `TetrominoType` values directly.

### `Tetromino`

Represents the currently falling piece.

```kotlin
data class Tetromino(
    val type: TetrominoType,
    val position: Position,
    val rotation: Int
)
```

The piece's occupied cells can be calculated from its type, position, and rotation.

### `TetrominoType`

Defines the seven standard tetromino types:

```kotlin
enum class TetrominoType {
    I, O, T, S, Z, J, L
}
```

Each type provides its shape for every supported orientation.

### `Position`

Stores a location in board coordinates.

```kotlin
data class Position(
    val row: Int,
    val column: Int
)
```

Board coordinates should remain independent of screen pixels.

### `GameAction`

Represents actions understood by the engine.

```kotlin
sealed interface GameAction {
    data object Tick : GameAction
    data object MoveLeft : GameAction
    data object MoveRight : GameAction
    data object SoftDrop : GameAction
    data object HardDrop : GameAction
    data object RotateClockwise : GameAction
    data object RotateCounterclockwise : GameAction
}
```

This keeps input handling separate from the implementation of game rules.

### `GameStatus`

Represents the current lifecycle state.

```kotlin
enum class GameStatus {
    READY,
    RUNNING,
    PAUSED,
    GAME_OVER
}
```

### `CollisionDetector`

Determines whether a tetromino can occupy a requested position.

It checks for:

* The left and right board boundaries
* The bottom boundary
* Occupied board cells
* Invalid positions after rotation

The detector should accept board coordinates rather than display coordinates.

### `RotationSystem`

Calculates rotated piece positions.

A basic implementation can rotate pieces around a fixed origin. A more accurate implementation can use the **Super Rotation System**, including wall-kick tests when a rotation would otherwise collide with the board.

Rotation should be kept outside the Compose UI so it can be unit tested.

### `LineClearer`

Detects completed rows and produces a board with those rows removed.

Responsibilities:

* Find rows in which every cell is occupied.
* Remove all completed rows simultaneously.
* Insert empty rows at the top.
* Return the number of cleared rows.

### `TetrominoGenerator`

Selects upcoming pieces.

A recommended approach is the seven-bag randomizer:

1. Place one of each tetromino type into a collection.
2. Shuffle the collection.
3. Return the pieces one at a time.
4. Generate another shuffled bag when the current bag is empty.

This avoids excessively long periods without a particular piece.

## User Interface

The UI is implemented with Jetpack Compose.

### `GameScreen`

The top-level game screen.

It observes the ViewModel state and arranges:

* The playfield
* Score and level information
* Next-piece preview
* Game controls
* Pause and game-over overlays

Example state collection:

```kotlin
@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Render the current game state.
}
```

### `GameBoard`

Draws the board and active tetromino.

The board can be rendered using either:

* A Compose `Canvas`
* A grid of composables

A `Canvas` is generally preferable because it reduces the number of composables created during frequent game updates.

The renderer should:

1. Calculate cell dimensions from the available space.
2. Draw locked board cells.
3. Draw the active tetromino.
4. Optionally draw grid lines and a ghost piece.

The renderer must not modify the game state.

### `GameControls`

Displays touch controls for:

* Move left
* Move right
* Rotate
* Soft drop
* Hard drop
* Pause

Controls invoke ViewModel methods rather than calling the engine directly.

### `ScorePanel`

Displays information such as:

* Current score
* Level
* Total cleared lines
* High score

### `NextPiecePreview`

Renders the next tetromino in a small preview area using the same shape definitions used by the game board.

### `GameOverlay`

Displays modal content for states such as:

* Game paused
* Game over
* Ready to start

## State Flow

The project uses unidirectional data flow:

```text
Touch input
    ↓
GameViewModel method
    ↓
GameAction
    ↓
GameEngine
    ↓
New GameState
    ↓
StateFlow
    ↓
Compose recomposition
```

The UI never edits the board directly. Every gameplay change is processed by the engine and represented by a new `GameState`.

## Game Loop

The ViewModel runs the game loop with a coroutine.

A simplified implementation may look like this:

```kotlin
private var gameLoopJob: Job? = null

private fun startGameLoop() {
    gameLoopJob?.cancel()

    gameLoopJob = viewModelScope.launch {
        while (isActive) {
            delay(currentDropInterval())
            dispatch(GameAction.Tick)
        }
    }
}
```

The loop should account for changes in level so that the delay can decrease as the game progresses.

The loop must stop or suspend when:

* The game is paused.
* The game reaches the game-over state.
* The ViewModel is destroyed.

User actions and automatic ticks should be processed sequentially to avoid simultaneous state updates.

## Scoring

A simple scoring system can award points according to the number of lines cleared at once:

| Lines cleared | Base points |
| ------------: | ----------: |
|             1 |         100 |
|             2 |         300 |
|             3 |         500 |
|             4 |         800 |

The awarded points can be multiplied by the current level:

```text
awarded points = base points × level
```

Additional points may be awarded for soft drops, hard drops, combos, or back-to-back clears.

## Building the Project

### Requirements

Install the following software:

* Android Studio
* Android SDK
* Android SDK Platform Tools
* JDK version supported by the project's Android Gradle Plugin

The project includes the Gradle wrapper, so a separate system-wide Gradle installation is not required.

### Clone the repository

```bash
git clone https://github.com/your-username/android-tetris-clone.git
cd android-tetris-clone
```

Replace the repository URL with the actual project URL.

### Build with Android Studio

1. Open Android Studio.
2. Select **Open**.
3. Choose the cloned project directory.
4. Allow Gradle synchronization to complete.
5. Select an emulator or connected Android device.
6. Click **Run**.

### Build from the command line

On Linux or macOS:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK will be generated under:

```text
app/build/outputs/apk/debug/
```

### Install the debug build

With an emulator or Android device connected:

```bash
./gradlew installDebug
```

Ensure USB debugging is enabled when installing on physical hardware.

### Create a release build

```bash
./gradlew assembleRelease
```

A distributable release build should be signed using a private release key. Do not commit keystores, passwords, or signing credentials to the repository.

## Running Tests

Run local unit tests:

```bash
./gradlew test
```

Run Android instrumentation tests on a connected device or emulator:

```bash
./gradlew connectedAndroidTest
```

Run all verification tasks:

```bash
./gradlew check
```

## Testing Strategy

Most game behavior should be covered by local unit tests because the engine does not depend on the Android framework.

Important test cases include:

### Movement

* A piece moves left when space is available.
* A piece moves right when space is available.
* A piece cannot move beyond either board boundary.
* A piece cannot move into occupied cells.

### Rotation

* Each tetromino rotates into the expected shape.
* Rotation is rejected when no valid position exists.
* Wall kicks are attempted in the correct order.
* The square tetromino remains unchanged.

### Gravity and locking

* A tick moves the active piece down by one row.
* A blocked piece locks into the board.
* A new piece spawns after the previous piece locks.
* The game ends when a new piece cannot spawn.

### Line clearing

* A complete row is removed.
* Multiple rows are removed simultaneously.
* Rows above cleared lines move downward.
* Empty or incomplete rows are not removed.

### Scoring

* Single, double, triple, and four-line clears award the expected score.
* Level multipliers are applied correctly.
* Drop bonuses are calculated correctly, when enabled.

### UI

Instrumentation tests should focus on Android-specific behavior, including:

* Controls dispatch the correct actions.
* Score and level values appear correctly.
* Pause and game-over overlays are displayed.
* Restart returns the UI to its initial state.

## Controls

A default touch-control layout may use:

| Control       | Action           |
| ------------- | ---------------- |
| Left arrow    | Move left        |
| Right arrow   | Move right       |
| Down arrow    | Soft drop        |
| Rotate button | Rotate clockwise |
| Drop button   | Hard drop        |
| Pause button  | Pause or resume  |

Gesture controls may also be added:

* Swipe left or right to move.
* Swipe down to soft drop.
* Tap to rotate.
* Flick down to hard drop.

## Extending the Project

Possible future improvements include:

* Hold-piece support
* Ghost-piece preview
* Super Rotation System wall kicks
* Back-to-back scoring
* Combo scoring
* High-score persistence with DataStore
* Sound effects and music
* Haptic feedback
* Keyboard and game-controller support
* Accessibility options
* Alternate themes
* Adjustable difficulty
* Landscape and tablet layouts
* Replay recording
* Online leaderboards
* Multiplayer modes

Features that add persistence or networking should be placed behind interfaces rather than implemented directly in the ViewModel. For example:

```text
data/
├── HighScoreRepository.kt
├── LocalHighScoreRepository.kt
└── PreferencesDataSource.kt
```

This allows the storage implementation to change without affecting the game engine or UI.

## Design Principles

The project follows these principles:

* **Single source of truth:** `GameState` contains the authoritative game state.
* **Immutable state:** updates create new state values.
* **Separation of concerns:** game rules, Android lifecycle code, and rendering remain separate.
* **Deterministic logic:** engine behavior can be tested with controlled input.
* **Dependency direction:** UI code depends on the engine, but the engine does not depend on the UI.
* **Small classes:** specialized behavior is extracted when it improves clarity or testing.
* **No gameplay logic in composables:** composables display state and report user actions.

## Contributing

1. Fork the repository.
2. Create a feature branch:

```bash
git checkout -b feature/feature-name
```

3. Make the changes.
4. Add or update tests.
5. Run the verification tasks:

```bash
./gradlew check
```

6. Commit the changes:

```bash
git commit -m "Add feature description"
```

7. Push the branch:

```bash
git push origin feature/feature-name
```

8. Open a pull request.

Keep game-engine changes independent of Android UI code whenever possible.

## License

This project is available under the MIT License. See `LICENSE` for details.

The Tetris name and related trademarks belong to their respective owners.
