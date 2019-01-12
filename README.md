# tftp
TFTP Server, Client and an Intermediate Host

The host manages the packets coming from the Client and Server and communicates back and forth between them
The host appears to be the Server in Client perspective and appears to be Server in the Server perspective.

11 Packets are sent from the client and the server sends back a response depending on the packet received, with the 11th packet being and invalid packet. (program throws error at the 11th packet)
