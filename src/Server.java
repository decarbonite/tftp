import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Ahmed Romih
 */

public class Server {

    private DatagramPacket sendPacket;
    private DatagramSocket receiveSocket, sendSocket;

    public Server() {
        try {
            receiveSocket = new DatagramSocket(69);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void receiveAndEcho() {

        System.out.println("Server: Waiting for Packet...\n");

        while (true) {
            try {
                byte data[] = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                receiveSocket.receive(receivePacket);

                //Printing content of the packet
                System.out.println("\nServer: received packet from Host:-");
                System.out.println("Host port: " + receivePacket.getPort());
                System.out.println("Length: " + receivePacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                System.out.print("In bytes: ");
                for (int j = 0; j < receivePacket.getLength(); j++) {
                    System.out.print(receivePacket.getData()[j] + " ");
                }
                System.out.println("\n");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!isValid(receivePacket)) {
                    throw new IOException("Invalid Request!");
                } else {
                    sendSocket = new DatagramSocket();
                    if (receivePacket.getData()[1] == 1) {
                        byte[] sendData = {0, 3, 0, 1};
                        sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                        sendSocket.send(sendPacket);
                    } else if (receivePacket.getData()[1] == 2) {
                        byte[] sendData = {0, 4, 0, 0};
                        sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                        sendSocket.send(sendPacket);
                    }
                }

                System.out.println("Server: Sending packet to Host:-");
                System.out.println("Destination host port: " + sendPacket.getPort());
                System.out.println("Length: " + sendPacket.getLength());
                System.out.print("Containing: ");
                System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                System.out.print("In bytes: ");
                for (int j = 0; j < sendPacket.getLength(); j++) {
                    System.out.print(sendPacket.getData()[j] + " ");
                }

                sendSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public boolean isValid(DatagramPacket packet) {
        if (packet == null) return false;
        //byte[] data = packet.getData();
        if (!(packet.getData()[0] == 0 && (packet.getData()[1] == 1 || packet.getData()[1] == 2))) return false;

        int i;
        //check for 0 after filename text
        for (i = 2; i < packet.getLength() - 1; i++) {
            if (packet.getData()[i] == 0) break;

            //reached the end without finding a 0
            if (i == packet.getLength() - 2) return false;
        }

        //build up mode and convert it to string
        StringBuilder mode = new StringBuilder();
        for (int j = i+1; j < packet.getLength()-1; j++) {
            mode.append((char) packet.getData()[j]);
        }
        //check if mode is correct
        if (!(mode.toString().equalsIgnoreCase("netascii") || mode.toString().equalsIgnoreCase("octet"))) {
            return false;
        }

        //return true if last index is 0
        return packet.getData()[packet.getLength()- 1] == 0;
    }

    public static void main(String args[]) {
        new Server().receiveAndEcho();
    }
}
