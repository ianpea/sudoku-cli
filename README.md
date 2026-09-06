# Sudoku CLI

A command-line Sudoku game implemented in Kotlin and Java.

The game generates a standard 9x9 Sudoku puzzle with exactly **30 pre-filled cells** and a **unique solution**. Players
can insert and clear values, request hints, check the board for Sudoku rule violations, and quit the game.

## Requirements

* Java 21
* Git

The project includes the Gradle Wrapper, so a separate Gradle installation is not required.

Java 21 is selected automatically by Gradle through the configured toolchain resolver. The first run may download Gradle
and the required Java toolchain.

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

Cells are identified using a row letter (`A-I`) followed by a column number (`1-9`).

For example:

```text
A3 5
```

inserts `5` into cell A3.

Supported commands:

| Command      | Description                                        |
|--------------|----------------------------------------------------|
| `A3 5`       | Insert `5` into cell A3                            |
| `A3 clear`   | Clear the value in cell A3                         |
| `hint`       | Reveal a correct value for an empty cell           |
| `check`      | Check the current board for Sudoku rule violations |
| `undo`       | Undo the previous move                             |
| `last`       | Show the previous move                             |
| `violations` | Check the board for Sudoku rule violations         |
| `exit`       | End the game                                       |

Commands and coordinates are case-insensitive.

Pre-filled cells cannot be modified or cleared. Values entered by the player must be between 1 and 9.

A move may create a Sudoku rule violation. The move is still accepted, and the player can use `check` to identify the
violation.

The game is completed automatically when every cell has been filled and the board contains no Sudoku rule violations.

## Rule Validation

The `check` command checks for duplicate non-zero values in:

* each row;
* each column;
* each 3x3 subgrid.

For example, a violation may be reported as:

```text
Number 5 already exists in Row A.
Number 5 already exists in Column 1.
Number 5 already exists in the same 3x3 subgrid #1.
```

### Subgrid Numbering

The nine 3x3 subgrids are numbered from **1 to 9**, starting at the top-left and proceeding from left to right, then top
to bottom:

```text
+-------+-------+-------+
|   1   |   2   |   3   |
+-------+-------+-------+
|   4   |   5   |   6   |
+-------+-------+-------+
|   7   |   8   |   9   |
+-------+-------+-------+
```

Therefore, a message such as:

```text
Number 5 already exists in the same 3x3 subgrid #4.
```

refers to the 3x3 subgrid on the **middle-left** of the Sudoku board.

## Puzzle Generation

A complete valid Sudoku board is first generated using randomized backtracking.

Cells are then removed in random order. After each removal, the puzzle's solutions are counted using backtracking. A
removal is retained only when the puzzle continues to have exactly one solution.

Puzzle generation continues until exactly **30 clues** remain. If a generation attempt cannot reach the requested clue
count while maintaining uniqueness, a new puzzle is generated.

### Sudoku Knowledge!
Do you know that **a standard 9x9 Sudoku with a unique solution cannot
contain fewer than 17 clues** as proven [here](https://arxiv.org/pdf/1201.0749)
Generally, published puzzles aimed at human solvers rarely go below 22 clues, as mentioned [here](https://sudoku.by/sudoku-faq/minimum-clues).

*This application restricts the configured clue count to a minimum bound of **30**.

## Design and Architecture

The application is separated into domain, application, and CLI responsibilities so that the Sudoku rules and game logic
are not coupled to console input/output.

At a high level, the application follows this flow:

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

### Board and Cells

`Board` represents the current state of the Sudoku puzzle and owns a flat collection of 81 `Cell` objects.

Each `Cell` contains its `CellPosition`, current value, cell type, and solution value. `CellPosition` represents the row
and column of a cell.

A flat collection is used instead of exposing a two-dimensional array as the primary model. Rows, columns, and 3x3
subgrids are derived from the cells when required. This keeps the cell and its position together while still allowing
the board to expose Sudoku-specific views for validation.

`Board` is responsible for board-level operations such as inserting, clearing, restoring, and determining whether the
board is completely filled.

### GameService

`GameService` acts as the application layer and coordinates the main gameplay operations.

It delegates responsibilities rather than implementing them directly:

* board mutations are delegated to `Board`;
* move history is delegated to `MoveService`;
* Sudoku rule validation is delegated to `ViolationTracker`;
* puzzle creation is delegated through the `BoardGenerator` abstraction.

Game completion is determined by combining two conditions:

1. the board is completely filled; and
2. the current board contains no Sudoku rule violations.

This also keeps completion separate from the `check` command. The player does not need to manually run `check` to win
the game.

### Move Validation and Sudoku Validation

The application intentionally distinguishes between whether a **move can be performed** and whether the resulting board
is **valid according to Sudoku rules**.

For a player move, the application validates constraints such as:

* the target cell must not be pre-filled;
* the supplied value must be within the supported range.

A move that introduces a duplicate Sudoku value is still accepted. Sudoku rule violations are detected separately when
the player runs `check`.

This allows interactions such as:

```text
A3 3
Move accepted.

check
Number 3 already exists in Row A.
```

This separation follows the required gameplay behaviour and avoids coupling cell mutation to whole-board Sudoku
validation.

Puzzle generation has a different requirement. When constructing or solving a puzzle, a candidate value must obey Sudoku
rules before it can be placed. `Board.canPlaceValue()` provides this check for the backtracking algorithm without
modifying the player-facing insertion behaviour.

### Violation Tracking

Sudoku rule validation is handled by the Java `ViolationTracker`.

The tracker receives the board's rows, columns, and subgrids and checks each group for duplicate non-zero values. It
returns a structured `Violation` containing the violation type, group index, and duplicated value.

Using a structured result keeps detection separate from presentation. `ViolationTracker` determines **what is wrong**,
while the corresponding `GameStatus` determines **how the result is displayed to the player**.

The validator is stateless and derives violations from the current board whenever validation is requested. This means
operations such as clear and undo do not require a separate violation cache to be synchronised with the board.

`ViolationTracker` and the related violation model are implemented in Java, while the rest of the application is
primarily Kotlin. Both compile to JVM bytecode, allowing the Java validation component to be consumed directly from the
Kotlin application layer.

### Subgrid Numbering

For violation messages, the nine 3x3 subgrids are numbered from left to right and top to bottom:

```text
+-------+-------+-------+
|   1   |   2   |   3   |
+-------+-------+-------+
|   4   |   5   |   6   |
+-------+-------+-------+
|   7   |   8   |   9   |
+-------+-------+-------+
```

For example:

```text
Number 5 already exists in the same 3x3 subgrid #4.
```

refers to the middle-left 3x3 subgrid.

### Puzzle Generation

`PuzzleGenerator` implements the `BoardGenerator` abstraction.

Puzzle generation occurs in two stages.

First, a complete valid Sudoku board is created using randomized backtracking. Candidate values are checked using
`Board.canPlaceValue()` before being placed.

Second, cells are removed in random order. After each removal, the number of possible solutions is counted using
backtracking. The removal is retained only if exactly one solution remains.

This continues until the requested number of clues is reached. The default game uses exactly **30 pre-filled cells**.

`BoardGenerator` also provides a boundary around puzzle generation. This allows `GameService` and the CLI game flow to
be tested with a deterministic `FakePuzzleGenerator` rather than depending on randomized puzzle generation.

### Input and Output

Console interaction is abstracted behind `GameIO`.

The production application uses `ConsoleGameIO`, while tests can use `FakeGameIO`. This prevents application logic from
depending directly on `readln()` or `println()` and allows complete command sequences to be exercised deterministically
in tests.

`CommandParser` is responsible for converting raw input into typed commands such as `InsertCommand`, `ClearCommand`,
`CheckCommand`, and `HintCommand`.

The application returns `GameStatus` objects from gameplay operations. `Renderer` is then responsible for presenting the
board and status messages through `GameIO`.

This gives the main flow a clear separation:

```text
raw input
   ↓
Command
   ↓
GameService
   ↓
GameStatus
   ↓
Renderer
   ↓
output
```

### Move History and Undo

`MoveService` owns the player's move history.

Each recorded move contains enough information to restore the previous board state. Undo removes the latest move from
the history and restores its previous value through the board.

Restoration is intentionally separate from normal player insertion so that undoing a move does not create another
move-history entry.

### Testing Approach

The design keeps most components testable without running an interactive console.

Unit tests cover individual responsibilities such as:

* board and cell behaviour;
* command parsing;
* puzzle generation and solution counting;
* insertion and clearing;
* move history and undo;
* row, column, and subgrid violations;
* game completion.

`FakeGameIO` and `FakePuzzleGenerator` are used for higher-level game-flow tests. This allows sequences of user commands
to be executed deterministically while asserting against the resulting output.

For example, game completion, violation reporting, invalid operations, and command flows can be tested without requiring
manual console input.

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

The test suite covers areas including:

* puzzle generation and unique-solution validation;
* board and cell behaviour;
* command parsing;
* insertion and clearing;
* pre-filled cell protection;
* row, column, and subgrid violations;
* hints;
* move history and undo;
* game completion;
* CLI game flows using a fake I/O implementation.

## Build a Runnable JAR

Build a self-contained JAR containing the Kotlin runtime:

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
