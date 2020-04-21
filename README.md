# SnowyAssistant
SnowyAssistant is a Discord bot written in Kotlin to provide support for the Discord server, through allowing tickets, conversations, and automated replies based on certain keywords

# Building
The bot uses Maven as its build system. To allow portability, the bot supports being ran on different platforms (currently 2 only).

1. Clone or download the repository
2. Replace the main platform adapter in the main class to be your desired adapter
3. If your platform provides the required third-party libraries, set the scope of any already-provided dependency in **pom.xml** to `provided`
4. Run `mvn clean package` to build the bot's jar, or run the adapter if it has no specific platform.

# Contribution
Feel free to contribute through opening [issues](https://github.com/ReflxctionDev/SnowyAssistant/issues) or [pull requests](https://github.com/ReflxctionDev/SnowyAssistant/pulls).