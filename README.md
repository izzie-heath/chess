# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Server Design

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-gQIBf+KTDK-LmG0gsQNwjBECDMxMNiAYNCWSsktrT2n1ArVW71sDmL+EsGfJymxNZIASGAXtfYIADoAFIQHFHGww-gBkgDVI8yezzZkmyaMyGSPQWlkLVYpUY2AEDAF7VAdJTkoBrBTdIGtGFXYZs1Z7PKULmEwsUfu8Yh7j2UDPdAL4oylRlrbZW6tf9kWrrRTAAAVtOtAWKxFrCneKXNTKvLEt2p+k9P7YAhSvRFAU4oqAmiQDSs1RbSzpQ4IS7KNrWX2rrVPMqzrcy8shvyxqIYhWMpFd6sVgjj6jiTkyURb7ZXRvlWNfOe9C5TTEWhlRb6dV6q0VGxcfqDFbVpR2QBkpLVD3Mblb6bKipgKddy5jrrWPut0+471yS1MBuVeGpANABQDSDToyVWKr2iaJuJ2J+S1pTSvXJ2dRgIBqEUzAK0aIVO6NRQ5E1abjYwAo1Rllhm6PspM4xsz1UWNQwFRx4VbVbOJpSQ5ALgblEwCo29GAR1z2VjNPKeI9dlUYdoJp5LgZmthiQulsemXjO2IzExvLFmCs0KrOaGAtZ6w8dI+K-cMNsBaExWGlpModR6jOp8mdxXcTrga9AHzd95UZE-d6Sryqm6wGC5p6hMAf4jlA1+c1YEb1zOGwDJZkDO27NVlAY9g7h1eGB4gYMsBgDYEPYQehy7zDgckmbC2VsbbGCBemr86LuB4E4yh17tl4v6JALjqAET3P8Zx5DinyT76P2foYE08S1vAGGCFnkegDBRf5bFpNy1W4DbtR-Mj9RBc0Yy7M0BJUACsXKHHmYhpNmAGJmGqHHMQPKxJRWkbvdjp7Phf7VLpR96xWWSq-agdsoAA)