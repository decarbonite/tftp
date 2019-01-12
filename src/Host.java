import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Ahmed Romih
 */

public class Host {

    private DatagramSocket receiveSocket, sendReceiveSocket;
    private DatagramPacket receivePacket, sendPacket;
    byte[] data;

    public Host() {
        try {
            receiveSocket = new DatagramSocket(23);
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException e) {   // Can't create the socket.
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                System.out.println("Host: Waiting for a packet...\n");
                data = new byte[1024];
                receivePacket = new DatagramPacket(data, data.length);
                receiveSocket.receive(receivePacket);

                int clientPort = receivePacket.getPort();
                //Printing content of the packet
                System.out.println("Host: Packet received from Client:");
                System.out.println("Client at port: " + clientPort);
                System.out.println("Length: " + receivePacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(data, 0, receivePacket.getLength()));
                System.out.print("In bytes: ");

                for (int i = 0; i < receivePacket.getLength(); i++) {
                    System.out.print(receivePacket.getData()[i]+ " ");
                }

                sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), 69);
                sendReceiveSocket.send(sendPacket);

                System.out.println("\n\n\nHost: Sending packet to Server:-");
                System.out.println("Destination server port: " + sendPacket.getPort());
                System.out.println("Length: " + sendPacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                System.out.print("In bytes: ");
                for (int j = 0; j < sendPacket.getLength(); j++) {
                    System.out.print(sendPacket.getData()[j]+ " ");
                }
                System.out.println("\n");

                byte[] response = new byte[1024];
                receivePacket = new DatagramPacket(response, response.length);
                sendReceiveSocket.receive(receivePacket);
                System.out.println("\n\nHost: Packet received from Server:");
                System.out.println("Address: " + receivePacket.getAddress());
                System.out.println("Host port: " + receivePacket.getPort());
                System.out.println("Length: " + receivePacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                System.out.print("In bytes: ");

                for (int j = 0; j < receivePacket.getLength(); j++) {
                    System.out.print(receivePacket.getData()[j]+ " ");
                }
                System.out.println("\n");

                sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), receivePacket.getAddress(), clientPort);
                sendReceiveSocket.send(sendPacket);

                System.out.println("\nHost: Sending packet to Client:-");
                System.out.println("Destination Client port: " + sendPacket.getPort());
                System.out.println("Length: " + sendPacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                System.out.print("In bytes: ");
                for (int j = 0; j < sendPacket.getLength(); j++) {
                    System.out.print(sendPacket.getData()[j]+ " ");
                }
                System.out.println("\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Host();
    }
}
