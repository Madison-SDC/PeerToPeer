# PeerToPeer

Contributors: Vaughn Kottler

A peer-to-peer model object transfer framework.

## Package: action

Handling user input, routing action objects to their   
destinations and executing incoming objects.

### ActionExecutor

Polling on peek() of SynchonousQueue for ActionItems. Threaded. 

### ActionItem

The capsule for all objects sent over the network.

### ActionRouter

The object that controls all outgoing ActionItems.

### InputHandler

Polls for input on System.in. Threaded.

## Package: main

The application implementation of the supporting framework.

### Application

Main class with main method.

## Package: network

For handling the opening and graceful closing of socket communication.

### ConnectionManager

Keeps track of all connections and allows you to open new ones.

### SocketListener

Opens a ServerSocket if one doesn't exist for the port, then polls for incoming traffic   
on the resulting Socket. Threaded.

### SocketSender 

Attempts to establish a connection to an ip & port pair (can timeout)   
then polls for incoming traffic. Threaded.

