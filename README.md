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

## Bonus Features
- Built-In session history system
- Session history panel in the UI
- Clean JSON-Based Storage of User Sessions


## Design Patterns
Strategy: Different writing styles
Factory: Centralized creation of writing requests
Singleton: APIClient ensures a single HTTP client + API key loader
Observer: Response Listener recieves onRequestStarted(), onRequestComplete(), onRequestError(String). UI reacts automatically.

## Demo:
Youtube link: https://youtu.be/-viQrVCmisA
Updated Link (After Bonus Features): https://youtu.be/8waqQxWk0ts