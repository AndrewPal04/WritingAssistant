# AI Writing Assistant

## Setup
1. Create an OpenAI account at https://platform.openai.com
2. Generate an API key from the dashboard.
3. Set the API key in your environment (PowerShell):
   ```powershell
   setx OPENAI_API_KEY "your-real-api-key-here"
4. Restart VS Code so that variable loads
5. Run the app:
    mvn compile
    mvn exec:java -Dexec.mainClass="app.Main"

## Features
- Creative writing mode
- Professional writing mode
- Academic writing mode
- OpenAI API integration
- Asynchronous operations
- Error handling
- 14+ JUnit tests

## Design Patterns
Strategy: Different writing styles
Factory: Centralized creation of writing requests
Singleton: One shared API client instance
Observer: UI updates after synchronous API responses

## Demo:
Youtube link: https://youtu.be/-viQrVCmisA