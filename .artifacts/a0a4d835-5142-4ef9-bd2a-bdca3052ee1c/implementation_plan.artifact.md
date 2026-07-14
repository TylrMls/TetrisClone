# Implementation Plan - Fix Board Overflow & Button Transparency

Resolve the issues where the game board extends off the bottom of the screen and the control buttons appear opaque.

## Proposed Changes

### UI Components

#### [MODIFY] [GameBoard.kt](file:///home/tylrmls/AndroidStudioProjects/TetrisClone2/app/src/main/java/com/tylrmls/tetrisclone/ui/components/GameBoard.kt)
- Remove `aspectRatio(0.5f)` from the internal `BoxWithConstraints`.
- Instead, the `GameBoard` should respect the `modifier` passed to it, which will handle the sizing and aspect ratio.

#### [MODIFY] [GameControls.kt](file:///home/tylrmls/AndroidStudioProjects/TetrisClone2/app/src/main/java/com/tylrmls/tetrisclone/ui/components/GameControls.kt)
- Replace `Surface` with a `Box` in `ControlButton`.
- Use `Modifier.background(color.copy(alpha = 0.4f))` and `Modifier.border(...)` directly to ensure transparency is correctly applied and visible.
- Add `Modifier.clickable` to the `Box`.

#### [MODIFY] [GameScreen.kt](file:///home/tylrmls/AndroidStudioProjects/TetrisClone2/app/src/main/java/com/tylrmls/tetrisclone/ui/GameScreen.kt)
- In `PortraitLayout`:
    - Update the `GameBoard` modifier to use `fillMaxHeight().aspectRatio(0.5f)` to ensure it fits vertically within its weighted parent without overflowing.
    - Keep the `Box` alignment as `Alignment.BottomCenter` but ensure the `GameBoard` is centered if there is extra width.
- In `LandscapeLayout`:
    - Ensure `GameBoard` also uses `aspectRatio(0.5f)` to maintain the correct proportions.

## Verification Plan

### Automated Tests
- None required for UI layout changes.

### Manual Verification
- **Overflow Check**: Run the app in portrait mode and verify the entire board (including the bottom border) is visible above the bottom of the screen.
- **Transparency Check**: Verify that the game board's black background and any blocks behind the buttons are clearly visible through the semi-transparent buttons.
- **Interaction Check**: Ensure buttons still respond to touches correctly.
