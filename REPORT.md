# Project Report  
## AI Writing Assistant  
**Course:** CS 3650  
**Student:** Andrew Palacios Jr

---

# 1. Overview

The AI Writing Assistant is a Java Swing program that uses the OpenAI API to generate writing in multiple styles including Creative, Professional, and Academic. The project demonstrates API integration, UI handling, MVC architecture, 3+ design patterns, and a full JUnit test suite.

---

# 2. Challenges I Faced

## **Challenge 1 – Setting up Maven & Environment**
**Problem:**  
Initially, Maven was not installed correctly, and `mvn` was not recognized in PowerShell.  
Also, environment variables did not load until I restarted VS Code.

**Solution:**  
Installed Maven manually and added the `bin` directory to the system PATH. 
Restarted the terminal and confirmed installation with `mvn -v`.

**What I Learned:**  
Java projects really depend on proper environment setup, especially with JUnit.

---

## **Challenge 2 – Packaging and Import Errors**
**Problem:**  
Running the project produced errors like:

This happened because the package structure didn't match `package app;` and Maven couldn't locate classes.

**Solution:**  
Reorganized all source files under:
src/main/java/app/ 
and mirrored the structure under `/test/java/app`.

**What I Learned:**  
Maven absolutely requires correct directory structure for both main and test sources, which took a large amount of time and testing.

---

## **Challenge 3 – JSON Parsing for OpenAI Output**
**Problem:**  
OpenAI responses can vary, especially with nested JSON structures in `choices[].message.content`.  
Early attempts caused null pointer issues. Being specific in prompting helped solve the problems with recieving json responses.

**Solution:**  
Used Gson to robustly parse:

# Design Pattern Justifications:

The Strategy Pattern was used to support different writing behaviors (Creative, Professional, Academic), with each mode implemented in its own class so the controller never needs conditional logic and new modes can be added easily. The Factory Pattern was implemented in WritingRequestFactory to centralize the creation of request objects and ensure the controller does not need to know which strategy or prompt format to build. The Observer Pattern appears in the way the UI reacts to asynchronous API responses: the controller triggers a background SwingWorker, and UI components such as the StatusBar and EditorPanel update themselves when the background task completes or fails, effectively “listening” for events. The Singleton Pattern is used in APIClient, which ensures only one HTTP client instance exists for the entire application, providing consistent configuration and quick API communication.

# AI Usage:

Used ChatGPT to figure out Maven errors
Asked: "How do I install Maven so that I can use the command 'mvn test'"
Modified: Installed the required package and added to path to allow for testing
Verified: Ran the tests afterwards using several tests.

## Time Spent: ~10 hours