# Lab 4 WebSocket -- Project Report

## Description of Changes
In this section, all the changes done are described.

### onChat test implementation
The onChat test was implemented in the `ElizaServerTest` to validate bidirectional WebSocket communication.
In the code, `size = list.size` was necessary because the size of the list may change, so in that way all the tests would use the same value for it.
The assertion `assert(size in 4..5)` was used instead of `assertEquals` because the number of responses from the Eliza chatbot may vary.
In addition, it is verified that, initially, the message through which the doctor answers the client ("Can you think of a specific example?") occupies the fourth position in the list, just after the two first messages of the doctor and the "---" message.
The test logic was completed so that after the third message, the client automatically sends another message ("I am always feeling sad") to continue the interaction. 

### Bonus: STOMP!
STOMP protocol support was added to the server by creating a configuration class named `StompConfig`, which enables a message broker (`/topic`) and sets the application destination prefix (`/app`). The `/stompClient` endpoint was registered with SockJS support to ensure compatibility with all browsers.

The `ElizaStompController` class was implemented to manage incoming STOMP messages. It handles messages arriving at `/app/chat` and sends responses to `/topic/replies`, they are annotated with `@MessageMapping("/chat")` and `@SendTo("/topic/replies")`.

To test and demonstrate the WebSocket + STOMP functionality, a lightweight HTML client using SockJS and STOMP was developed. This client connects to `/stompClient`, subscribes to `/topic/replies`, and sends messages to `/app/chat`.

This functionality was verified using a browser in which the STOMP client was opened in several windows to see the broadcasting.

### Bonus: Sock from WebSocket
SockJS was enabled in the STOMP endpoint configuration (`withSockJS()`), so the system can use polling transport when WebSocket is unavailable.

### Bonus: Add Real-time Analytics Dashboard
Finally, it was also implemented a WebSocket-based dashboard, which provides real-time analytics of the server’s performance and activity.  
A new `/dashboard` endpoint streams live JSON metrics to connected clients every second. These metrics are the followings:
- Active connections  
- Total messages exchanged  
- Message rate (messages per second)  

Metrics are collected through a shared `Metrics` singleton that tracks WebSocket events (`onOpen`, `onClose`, `onMessage`) and broadcasts updates to all dashboard sessions.

A lightweight HTML dashboard was develop to subscribe to these updates and display them dynamically.

This functionality was verified using a browser in which the dashboard was opened to see the updates in the metrics, using postman to start/end a connection, as well as to send messages.

## Technical Decisions
- Interval-based assertion (`assert(size in 4..5)`) was chosen to account for possible asynchronous differences in message reception timing.
- STOMP & SockJS integration was done using Spring Boot’s WebSocketMessageBrokerConfigurer interface, taking suppport on Spring Boot's documentation.
- Chose to implement the dashboard as a simple HTML/JavaScript client without frameworks, to focus on the WebSocket logic itself.  
- Metrics are sent as JSON-formatted strings over WebSocket for easy parsing by front-end scripts.  
- Implemented a shared `Metrics` object using `CopyOnWriteArraySet` to ensure thread-safe access to session data.  
- Used a `fixedRateTimer` to broadcast live metrics every second to all connected dashboard clients.  

## Learning Outcomes
- Improved skills in designing tests for concurrent and asynchronous systems.
- Gained practical understanding of WebSocket asynchronous communication.
- Learned how to implement STOMP protocol support.
- Learned how to collect, update, and stream server metrics in real time using WebSocket communication.  

## AI Disclosure
### AI Tools Used
The only AI tool which was used was ChatGPT.

### AI-Assisted Work
ChatGPT was used for three purposes:
- Refine the documentation wording and ensure clarity for external readers.
- Generate the initial version of the HTML STOMP client, which was later modified to add interaction logic with STOMP.
- Create the initial structure of the real-time analytics dashboard (HTML layout and basic script skeleton), to which the full WebSocket connection logic and dynamic metric updates were added.

The percentage of AI-assisted vs. original work is approximately 20% AI-assisted and 80% original.

The AI-generated code was modified in this following ways:
- To adapt the AI-generated client code to connect correctly with the Spring Boot backend and add message handling for real-time chat.
- To adapt the AI-generated dashboard code to connect the logic and dynamic metric updates.

### Original Work
Implemented the `onChat` test, all server-side STOMP configuration (`StompConfig`, `ElizaStompController`), the dashboard endpoint (`/dashboard`) and the `Metrics` singleton for real-time analytics, manually.
