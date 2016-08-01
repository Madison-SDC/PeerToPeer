# PeerToPeer

Contributors: Vaughn Kottler

*A peer-to-peer model, event-driven object transfer framework. A drop in for multiplayer games!*   
*supports the use of internet addresses, but may require port forwarding. This issue is being looked into.*

## Package: action

Routes action objects to their destinations and executes on incoming objects.

### ActionExecutor

Non-expensive polling using peek() on LinkedBlockingQueue for ActionItems. Threaded. 

### ActionItem

The capsule for all objects sent over the network.

### ActionRouter

The object that controls all outgoing ActionItems. Threaded and polls on an outgoing LinkedBlockingQueue   
(not expensive).

## Package: main

*The application implementation of the supporting framework.*

### Application

Main class with main method. Polls on stdIn (MOST EXPENSIVE BY FAR).

## Package: network

*For handling the opening and graceful closing of socket communication.*

### ConnectionManager

Keeps track of all connections and allows you to open new ones.

### Connection

Threaded. Polling on the ObjectInputStream, has the ability to write output over ObjectOuputStream.

