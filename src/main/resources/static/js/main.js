"use strict";

var usernamePage = document.querySelector("#username-page");
var chatPage = document.querySelector("#chat-page");
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElement = document.querySelector(".connecting");

var stompClient = null;
var username = null;
//mycode
var password = null;
var room = null;

var colors = [
    "#2196F3",
    "#32c787",
    "#00BCD4",
    "#ff5652",
    "#ffc107",
    "#ff85af",
    "#FF9800",
    "#39bbb0",
    "#fcba03",
    "#fc0303",
    "#de5454",
    "#b9de54",
    "#54ded7",
    "#54ded7",
    "#1358d6",
    "#d611c6",
];

function connect(event) {
    username = document.querySelector("#name").value.trim();
    room = document.querySelector("#room").value.trim();
    password = document.querySelector("#password").value;
    // if (username) {
    //     //Enter your password
    //     if (password == "hello") {
    //         usernamePage.classList.add("hidden");
    //         chatPage.classList.remove("hidden");
    //
    //         var socket = new SockJS("/websocket");
    //         stompClient = Stomp.over(socket);
    //         // stompClient.connect({}, onConnected, onError);
    //
    //         stompClient.connect({}, function() {
    //             onConnected();
    //
    //             // Fetch and display chat history
    //             fetch(`api/v1/chat_room/history/${room}`)
    //                 .then(response => response.json())
    //                 .then(messages => {
    //                     messages.forEach(message => onMessageReceived({ body: JSON.stringify(message) }));
    //                 });
    //         }, onError);
    //
    //     } else {
    //         let mes = document.getElementById("mes");
    //         mes.innerText = "Wrong password";
    //     }
    // }
    // event.preventDefault();

    if (username) {
        // Fetch the list of participants
        fetch(`api/chat_room/participants/${room}`)
            .then(response => {
                console.log(response);
                return response.json();
            })
            .then(participants => {
                console.log(participants);
                console.log(username);
                username = Number(username); // Convert username ID to number
                console.log('participants.includes(username) ' + participants.includes(username));

                // Check if the username is in the list of participants
                if (participants.includes(username)) {
                    //Enter your password
                    if (password == "hello") {
                        usernamePage.classList.add("hidden");
                        chatPage.classList.remove("hidden");

                        var socket = new SockJS("/websocket");
                        stompClient = Stomp.over(socket);

                        stompClient.connect({}, function() {
                            onConnected();

                            // Fetch and display chat history
                            fetch(`api/chat_room/history/${room}`)
                                .then(response => response.json())
                                .then(messages => {
                                    messages.forEach(message => onMessageReceived({ body: JSON.stringify(message) }));
                                });
                        }, onError);
                    } else {
                        let mes = document.getElementById("mes");
                        mes.innerText = "Wrong password";
                    }
                } else {
                    let mes = document.getElementById("mes");
                    mes.innerText = "You are not a participant in this room";
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
    event.preventDefault();
}

function onConnected() {
    // Subscribe to the Public Topic
    // stompClient.subscribe("/topic/public", onMessageReceived);
    stompClient.subscribe("/topic/room/" + room, onMessageReceived);


    // Tell your username to the server
    // stompClient.send(
    //     "/app/chat.register",
    //     {},
    //     JSON.stringify({ sender: username, type: "JOIN" })
    // );

    stompClient.send(
        "/app/chat.register/" + room,
        {},
        JSON.stringify({ sender: username, type: "JOIN" })
    );

    connectingElement.classList.add("hidden");
}

function onError(error) {
    connectingElement.textContent =
        "Could not connect to WebSocket! Please refresh the page and try again or contact your administrator.";
    connectingElement.style.color = "red";
}

function send(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: "CHAT",
        };

        // Send the message to the selected room
        stompClient.send("/app/chat.send/" + room, {}, JSON.stringify(chatMessage));
        messageInput.value = "";
    }
    event.preventDefault();
}

/**
 * Handles the received message and updates the chat interface accordingly.
 * param {Object} payload - The payload containing the message data.
 */
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement("li");

    if (message.type === "JOIN") {
        messageElement.classList.add("event-message");
        message.content = message.sender + " joined!";
    } else if (message.type === "LEAVE") {
        messageElement.classList.add("event-message");
        message.content = message.sender + " left!";
    } else {
        messageElement.classList.add("chat-message");

        var avatarElement = document.createElement("i");
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style["background-color"] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement("span");
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        // * update
        usernameElement.style["color"] = getAvatarColor(message.sender);
        //* update end
    }

    var textElement = document.createElement("p");
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    // * update
    if (message.sender === username) {
        // Add a class to float the message to the right
        messageElement.classList.add("own-message");
    } // * update end
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener("submit", connect, true);
messageForm.addEventListener("submit", send, true);
