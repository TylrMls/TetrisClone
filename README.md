# Android Tetris Clone

A modern Tetris-style falling-block puzzle game for Android, built with **Kotlin** and **Jetpack Compose**.

The project separates the game rules from Android-specific code so that the core game engine remains predictable, testable, and easy to extend.

> This is an educational Tetris clone and is not affiliated with or endorsed by The Tetris Company.

## Features

* Standard seven-piece tetromino set
* Piece movement and rotation
* Automatic gravity
* Soft drop and hard drop
* **Ghost piece landing indicator**
* Line clearing
* Score and level tracking
* Increasing fall speed
* Next-piece preview
* Pause, restart, and game-over states (with final score)
* **Adaptive layout for portrait and landscape**
* **Intelligent orientation management** (portrait-locked on small vertical displays)
* Touch-based controls with semi-transparent overlays
* Game logic that can be tested without an Android device

## Technology

* **Kotlin**
* **Jetpack Compose (Material 3)**
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

## Software Layout

```text
app/
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   └── java/com/tylrmls/tetrisclone/
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
│   │   └── java/com/tylrmls/tetrisclone/
│   │       ├── GameEngineTest.kt
│   │       ├── CollisionDetectorTest.kt
│   │       ├── LineClearerTest.kt
│   │       └── RotationSystemTest.kt
│   │
│   └── androidTest/
│       └── java/com/tylrmls/tetrisclone/
│           └── GameScreenTest.kt
│
├── build.gradle.kts
└── proguard-rules.pro
```

## Core Classes

### `MainActivity`

The Android entry point.

Responsibilities:
* Applies the application theme.
* Sets up edge-to-edge display.
* Implements orientation locking logic for small vertical displays.
* Creates the root Compose content.

### `GameViewModel`

Connects the Compose UI to the game engine.

Responsibilities:
* Owns the observable `GameState`.
* Receives commands from the UI.
* Manages the game loop coroutine.
* Advances the game at an interval that decreases as the level increases.

### `GameEngine`

Contains the main game rules.

Responsibilities:
* Processes player actions and game ticks.
* Handles piece movement and rotation.
* Manages line clearing and scoring.
* **Calculates the ghost piece landing position.**

### `GameState`

An immutable snapshot of the complete game.

```kotlin
data class GameState(
    val board: Board,
    val activePiece: Tetromino,
    val ghostPiece: Tetromino?,
    val nextPiece: TetrominoType,
    val score: Int,
    val lines: Int,
    val level: Int,
    val status: GameStatus
)
```

## User Interface

The UI is implemented with Jetpack Compose using a **Unidirectional Data Flow** pattern.

### `GameScreen`

Detects orientation using `BoxWithConstraints` and switches between:
* **Portrait Layout**: Board occupies the center with semi-transparent controls overlapping the bottom area to maximize board size.
* **Landscape Layout**: Board on the left with a wider side panel for stats and controls.

### `GameBoard`

Renders the 10x20 grid using a Compose `Canvas`. It draws the locked cells, the faint **ghost piece** indicator, and the current active piece.

### `GameControls`

Provides large (64dp) touch targets for gameplay actions. In portrait mode, these use a semi-transparent "glass" effect so the board remains visible underneath.

## State Flow

```text
Touch input
    ↓
GameViewModel method
    ↓
GameAction
    ↓
GameEngine
    ↓
New GameState (with Ghost Piece)
    ↓
StateFlow
    ↓
Compose Recomposition
```

## Game Loop

The loop runs in a `viewModelScope` coroutine and dynamically adjusts the tick delay:
```kotlin
val interval = max(100L, 1000L - ((state.level - 1) * 100L))
```

## Orientation Management

To ensure a playable experience, the app restricts rotation on devices where the vertical resolution in landscape would be too small to display the full 20-row board. This is managed in `MainActivity.kt`.

## Building the Project

1. Open in **Android Studio**.
2. Allow Gradle sync to complete.
3. Select an emulator or device.
4. Click **Run**.

## Testing

Run unit tests to verify the engine:
```bash
./gradlew test
```

## License

MIT License. See `LICENSE` for details.
