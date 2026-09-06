# Sudoku CLI

A command-line Sudoku game implemented in Kotlin and Java.

The game generates a standard 9x9 Sudoku puzzle with exactly **30 pre-filled cells** and a **unique solution**. Players can insert and clear values, request hints, check for Sudoku rule violations, undo moves, and quit the game.

## Requirements

- Java 21
- Git

The project includes the Gradle Wrapper, so a separate Gradle installation is not required. Java 21 is selected automatically through the configured Gradle toolchain resolver. The first run may download Gradle and the required Java toolchain.

## Run the Game

Clone the repository and run the application from the project root.

### macOS / Linux

```bash
./gradlew run
```

### Windows PowerShell

```powershell
.\gradlew.bat run
```

## Game Commands

Cells are identified using a row letter (`A-I`) followed by a column number (`1-9`). For example, `A3 5` inserts `5` into cell A3.

| Command | Description |
| --- | --- |
| `A3 5` | Insert `5` into cell A3 |
| `A3 clear` | Clear the value in cell A3 |
| `hint` | Reveal the correct value for one empty cell without placing it |
| `check` | Check the current board for Sudoku rule violations |
| `undo` | Undo the previous move |
| `last` | Show the previous move |
| `violations` | Check the board for Sudoku rule violations |
| `exit` | End the game |

Commands and coordinates are case-insensitive. Pre-filled cells cannot be modified or cleared, and entered values must be between 1 and 9.

A move may create a Sudoku rule violation. The move is still accepted, and the player can use `check` to identify the violation afterwards.

The game ends automatically when every cell is filled and the board contains no Sudoku rule violations.

## Rule Validation

The `check` command detects duplicate non-zero values in each row, column, and 3x3 subgrid.

Example violations:

```text
Number 5 already exists in Row A.
Number 5 already exists in Column 1.
Number 5 already exists in the same 3x3 subgrid #1.
```

### Subgrid Numbering

The nine 3x3 subgrids are numbered left-to-right, then top-to-bottom:

```text
+-------+-------+-------+
|   1   |   2   |   3   |
+-------+-------+-------+
|   4   |   5   |   6   |
+-------+-------+-------+
|   7   |   8   |   9   |
+-------+-------+-------+
```

For example, `subgrid #4` refers to the middle-left 3x3 region.

## Puzzle Generation

A complete valid Sudoku board is first generated using randomized backtracking. Cells are then removed in random order, with the number of possible solutions counted after each removal. A removal is kept only if exactly one solution remains.

The standard game continues until exactly **30 clues** remain. If a generation attempt cannot reach the requested clue count while preserving uniqueness, a new puzzle is generated.

A standard 9x9 Sudoku with a unique solution cannot contain fewer than 17 clues ([proof](https://arxiv.org/pdf/1201.0749)). This implementation uses a higher configurable minimum to keep generation time practical with the current backtracking approach, while the normal game uses 30 clues.

## Design and Architecture

The application separates CLI concerns, game orchestration, board state, move history, puzzle generation, and Sudoku validation so that the core game logic is not coupled to console I/O.

```text
               CLI
        (Input / Renderer)
                |
                v
           GameService
          /     |      \
         v      v       v
      Board  MoveService  ViolationTracker
        ^
        |
 PuzzleGenerator
```

### Core Responsibilities

- **`GameService`** coordinates gameplay and determines game completion.
- **`Board`** owns the 81 cells and board-level operations. Rows, columns, and subgrids are derived from the flat cell collection when required.
- **`PuzzleGenerator`** creates uniquely solvable puzzles using randomized backtracking.
- **`MoveService`** manages move history and undo.
- **`ViolationTracker`** is a stateless Java validator for row, column, and subgrid duplicates.
- **`CommandParser` / `Renderer`** translate between CLI input/output and the application layer.
- **`GameIO`** abstracts console I/O, allowing complete game flows to be tested with `FakeGameIO`.

### Move Validation vs Sudoku Validation

Player move validation and Sudoku rule validation are intentionally separate.

A player may insert a value that creates a duplicate. The move is accepted, and `check` reports the violation afterwards:

```text
A3 3
Move accepted.

check
Number 3 already exists in Row A.
```

Puzzle generation has a different requirement: candidate values must already satisfy Sudoku rules. `Board.canPlaceValue()` is therefore used by the backtracking generator, while `ViolationTracker` validates the player's current board.

Game completion is independent of the `check` command: the board must be fully filled and contain no violations.

## Testing

Run all tests with:

### macOS / Linux

```bash
./gradlew test
```

### Windows PowerShell

```powershell
.\gradlew.bat test
```

The test suite covers puzzle generation and uniqueness, board and cell behaviour, command parsing, insert/clear operations, hints, move history and undo, row/column/subgrid violations, game completion, and CLI game flows.

`FakePuzzleGenerator` provides deterministic boards for higher-level tests, while `FakeGameIO` allows command sequences to be tested without real console input.

## Build a Runnable JAR

Build a self-contained JAR containing the Kotlin runtime.

### macOS / Linux

```bash
./gradlew clean fatJar
```

### Windows PowerShell

```powershell
.\gradlew.bat clean fatJar
```

The JAR is created at:

```text
build/libs/sudoku_kotlin-1.0-SNAPSHOT-all.jar
```

Run it with Java 21 or newer:

```bash
java -jar build/libs/sudoku_kotlin-1.0-SNAPSHOT-all.jar
```

The same command can be used on Windows.
