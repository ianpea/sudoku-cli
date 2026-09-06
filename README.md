
## Run the game

Install Git and keep the Gradle wrapper files in the project. Java 21 is selected
automatically by Gradle through the toolchain resolver.

On macOS or Linux:

```bash
./gradlew run
```

On Windows PowerShell:

```powershell
.\gradlew.bat run
```

The first run may download Gradle and the Java 21 toolchain.

To run the tests instead:

```bash
./gradlew test
```

Use `gradlew.bat test` on Windows.

## Build a runnable JAR

Build a self-contained JAR that includes the Kotlin runtime:

On macOS or Linux:

```bash
./gradlew clean fatJar
```

On Windows PowerShell:

```powershell
.\gradlew.bat clean fatJar
```

The JAR is created at `build/libs/sudoku_kotlin-1.0-SNAPSHOT-all.jar`.
Run it with Java 21 or newer:

```bash
java -jar build/libs/sudoku_kotlin-1.0-SNAPSHOT-all.jar
```

The Windows command is the same because Java runs JAR files the same way on
both platforms. The player can type `exit` to quit the game.