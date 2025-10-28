---
title: "Web Engineering 2025-2026"
subtitle: "Lab 4: WebSockets ELIZA"
format:
  html:
    toc: true
    toc-depth: 3
    number-sections: true
    code-fold: true
    code-tools: true
    code-overflow: wrap
    theme: cosmo
  pdf:
    documentclass: article
    classoption: [11pt, a4paper]
    toc: true
    toc-depth: 3
    number-sections: true
    geometry: [margin=2.5cm, headheight=15pt]
    fontsize: 11pt
    linestretch: 1.15
    colorlinks: true
    breakurl: true
    urlcolor: blue
    linkcolor: blue
    citecolor: blue
    hyperrefoptions:
      - linktoc=all
      - bookmarksnumbered=true
      - bookmarksopen=true
    header-includes:
      - |
        \usepackage{helvet}
        \renewcommand{\familydefault}{\sfdefault}
        \usepackage{hyperref}
        \usepackage{fancyhdr}
        \pagestyle{fancy}
        \fancyhf{}
        \fancyhead[L]{Web Engineering 2025-2026}
        \fancyhead[R]{Lab 4: WebSockets ELIZA}
        \fancyfoot[C]{\thepage}
        \renewcommand{\headrulewidth}{0.4pt}
        \usepackage{microtype}
        \usepackage{booktabs}
        \usepackage{array}
        \usepackage{longtable}
        \usepackage{xcolor}
        \definecolor{sectioncolor}{RGB}{44,62,80}
        \usepackage{sectsty}
        \allsectionsfont{\color{sectioncolor}}
        \usepackage{graphicx}
        \DeclareGraphicsExtensions{.pdf,.png,.jpg,.jpeg}
        % Wrap long lines in code blocks for PDF output
        \usepackage{fvextra}
        \fvset{breaklines=true, breakanywhere=true}
        \DefineVerbatimEnvironment{Highlighting}{Verbatim}{breaklines,breakanywhere,commandchars=\\\{\}}
lang: en
---

Welcome to the fourth lab assignment of the 2025–2026 course! This guide will help you complete the assignment efficiently. Although this guide is command-line oriented, you are welcome to use IDEs like **VS Code**, **IntelliJ IDEA**, or **Eclipse**—all of which fully support the tools we'll be using.

Ensure you have at least **Java 21** installed on your system before getting started.

**Estimated time**: 2 hours.

## System requirements

For this assignment, we'll be using the following technologies:

1. **Java Version**: The project targets **Java 21** (toolchain).
2. **Programming Language**: Kotlin 2.2.10.
3. **Framework**: Spring Boot 3.5.3.
4. **Build System**: Gradle 9.0.0.
5. **Code Quality**: Ktlint Gradle plugin for code formatting.

## Getting started

### Clone the repository

1. Clone your Lab 4 repository and change into the directory:

   ```bash
   git clone https://github.com/UNIZAR-30246-WebEngineering/lab-4-websocket-<your-github-user>.git
   cd lab-4-websocket-<your-github-user>
   ```

2. Run tests:

   ```bash
   ./gradlew test
   ```

### Run the application

If you wish to test the server locally, use:

```bash
./gradlew bootRun
```

WebSocket endpoint: `ws://localhost:8080/eliza`.

## Historical background

The app implements the DOCTOR script of [ELIZA](https://en.wikipedia.org/wiki/ELIZA), an early NLP program by [Joseph Weizenbaum](https://en.wikipedia.org/wiki/Joseph_Weizenbaum). The DOCTOR script simulates a [Rogerian psychotherapist](https://en.wikipedia.org/wiki/Person-centered_therapy).

ELIZA is an early example of primitive natural language processing developed between 1964 and 1966. The most famous script, DOCTOR, simulates a Rogerian psychotherapist, sometimes providing a startlingly human-like interaction.

You can interact using Postman at `ws://localhost:8080/eliza`:

::: {.content-visible when-format=html}
![ELIZA WebSocket interaction in Postman](img/postman.gif)
:::

::: {.content-visible when-format=pdf}
![ELIZA WebSocket interaction in Postman](img/postman-000.png)
:::

Image created by Jorge Laguna (2021)

## Primary task

Complete and verify the tests in `ElizaServerTest.kt` that exercise the WebSocket endpoint. Your goal is to finish the missing setup/verify logic to demonstrate that:

- The server greets on connection (`onOpen`).
- The server responds with a DOCTOR-style message to chat input (`onChat`).

### Steps required

To complete the assignment, follow these steps:

1. Locate the `ElizaServerTest.kt` file in the `src/test/kotlin/websockets` directory.

2. Remove the `@Ignore` annotation from the `onChat` test method to enable the test.

3. Implement the `onChat` test by adding the required code and comments. The test should:
   - Send a message (e.g., `I am feeling sad`) from the client to the server.
   - Verify that the server responds with a DOCTOR-style question or statement about your mental health (e.g., `Why do you say you are feeling sad?`).

   You are expected to add approximately **six lines of code** and include the requested comments to explain your implementation.

   Refer to the `onOpen` test in the same file for guidance on structuring your test.

4. Run the tests locally to ensure they pass:

   ```bash
   ./gradlew test
   ```

5. Commit your changes and push them to your main branch:

   ```bash
   git add src/test/kotlin/websockets/ElizaServerTest.kt
   git commit -m "Implement onChat test in ElizaServerTest"
   git push origin main
   ```

### Manual verification

1. Connecting to `ws://localhost:8080/eliza` sends a welcome message.
2. Sending a message receives a DOCTOR-style response (mirrors pronouns, often asks a follow-up question).
3. No server exceptions are logged during typical interactions.

::: {.callout-warning}
## Important Caveat

WebSocket tests can be influenced by external factors such as network latency, system load, and timing issues. If tests fail intermittently, consider:

- Running tests multiple times to verify consistency
- Checking for proper WebSocket connection lifecycle management
- Ensuring adequate wait times for asynchronous operations
- Verifying that the server is running and accessible before testing
- Updating test code to catch all the scenarios if required (e.g., adding retry logic, better assertions, or more robust error handling)
:::

## How to Pass

Your work passes if:

- Your `main` branch contains evidence that you have fulfilled the primary tasks.
- Your Continuous Integration (CI) workflow passes successfully (green status).

**Important:** Ensure all your code changes are committed and pushed to the `main` branch. Verify your CI pipeline to catch and resolve any build or test failures.

## Insights

### Code Quality Tools: Ktlint

[Ktlint](https://ktlint.github.io/) plugin is enabled. It automatically formats your code according to the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html#source-code-organization).

- Ktlint enforces Kotlin formatting according to the official coding conventions.
- If Ktlint modifies your source code during formatting, the build will fail.
- Run `./gradlew ktlintFormat` to format your code before building.

### Tips for Success

- **Understand WebSockets:** Spend time understanding the WebSocket lifecycle (connection, message exchange, closure) to ensure your implementation is accurate.
- **Test Thoroughly:** Use Postman or similar tools to manually test your WebSocket endpoint and understand the expected behavior.
- **Regular Commits:** Commit your work frequently with clear messages to track your progress effectively.
- **Documentation:** Document any significant changes or decisions to help reviewers understand your work.
- **Seek Assistance:** If you encounter challenges, don't hesitate to reach out to your instructor or classmates for help.

## Submission and Evaluation

### Submission Deadline

Follow the deadline announced for this assignment.

### Submission Requirements

You must submit your work in two ways:

1. **Moodle Submission**: Upload a zip file of your complete project to Moodle.
2. **GitHub Repository**: Push your changes to your GitHub repository for potential evaluation during the Lab 4 session.

#### Moodle Zip File Submission

Include:

- Source code files
- Documentation files (`README.md`, `REPORT.md`)
- Test files
- Configuration files
- Any additional files you've created

#### REPORT.md File Requirements

Create a `REPORT.md` file in your project root that includes:

1. **Description of Changes**: Explanation of modifications and enhancements you made
2. **AI Disclosure**: Complete disclosure of any AI tools or assistance used during development, including:
   - Specific AI tools used (ChatGPT, GitHub Copilot, etc.)
   - What code or documentation was generated with AI assistance
   - How much of your work was AI-assisted vs. original
   - Any AI-generated code that was modified or adapted
3. **Learning Outcomes**: What you learned from completing this assignment
4. **Technical Decisions**: Explanation of technical choices and their rationale

Sample `REPORT.md` structure:

```markdown
# Lab 4 WebSockets ELIZA — Project Report

## Description of Changes
[Detailed description of all changes made]

## Technical Decisions
[Explanation of technical choices made]

## Learning Outcomes
[What you learned from this assignment]

## AI Disclosure
### AI Tools Used
- [List specific AI tools used]

### AI-Assisted Work
- [Describe what was generated with AI assistance]
- [Percentage of AI-assisted vs. original work]
- [Any modifications made to AI-generated code]

### Original Work
- [Describe work done without AI assistance]
- [Your understanding and learning process]
```

---

By carefully following this guide and utilizing tools like **Ktlint**, you will not only complete the assignment but also improve your skills in writing clean, maintainable Kotlin code and working with WebSocket connections. Feel free to reach out if you have any questions or need further assistance.

Good luck with your assignment!
