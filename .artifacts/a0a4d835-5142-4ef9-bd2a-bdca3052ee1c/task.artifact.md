# Tetris Clone Task List - UI Fixes (Overflow & Transparency)

- [x] Fix Board Sizing
    - [x] Update `GameBoard.kt` to remove internal aspect ratio restriction
    - [x] Update `GameScreen.kt` to correctly size board in Portrait and Landscape
- [x] Fix Button Transparency
    - [x] Update `GameControls.kt` to use a transparent `Box` for buttons
- [x] Verification
    - [x] Verify board fits on screen in both orientations
    - [x] Verify buttons are transparent and board is visible behind them
