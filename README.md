# WordRush: Real-Time Multiplayer Crossword Game

Temporary MVP Demo Link: https://wr.bijeshmanstha.com.np/ (Might have some CSS issues)

**WordRush** is a full-stack multiplayer crossword game designed for 2–4 players. It features real-time gameplay, a Java-based backend with clean OOP design, and a responsive JavaScript frontend. Players can compete, chat, and solve crosswords together in real time.

## Features

- Real-time multiplayer crossword gameplay (2–4 players)
- Java backend using OOP principles: Encapsulation, Inheritance, Polymorphism, Abstraction
- Real-time chat and game state sync using WebSockets
- Interactive JavaScript frontend for game board and player interactions

## Tech Stack

- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Java (Spring Boot)
- **Communication**: WebSockets (Java Sockets)

#Future Improvements
Add leaderboard and persistent player stats

Word validation via dictionary API

Improve mobile responsiveness

## How to Run Locally

```bash
# Set Java environment
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

# Build and run
mvn clean
mvn compile
mvn package
mvn exec:java -Dexec.mainClass=uta.cse3310.App






