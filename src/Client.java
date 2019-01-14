import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Ahmed Romih
 */

public class Client {
    private DatagramSocket sendReceiveSocket;

    public enum RequestType {READ, WRITE, INVALID}

    public Client() {
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendAndReceive() {
        for (int i = 0; i < 11; i++) {
            //5 times write request
            if ((i % 2) == 0 && i != 10) {
                try {
                    DatagramPacket sendPacket = request("name.txt", "octet", RequestType.WRITE);
                    sendReceiveSocket.send(sendPacket);

                    //Printing content of the packet
                    System.out.println("Client: Sending write request to Host:-");
                    System.out.println("Destination host port: " + sendPacket.getPort());
                    System.out.println("Length: " + sendPacket.getLength());
                    System.out.print("Containing: ");
                    System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                    System.out.print("In bytes: ");

                    for (int j = 0; j < sendPacket.getLength(); j++) {
                        System.out.print(sendPacket.getData()[j] + " ");
                    }
                    System.out.println("\n");

                    byte[] data = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                    sendReceiveSocket.receive(receivePacket);

                    System.out.println("Client: Packet received from Host:");
                    System.out.println("Client at port: " + receivePacket.getPort());
                    System.out.println("Length: " + receivePacket.getLength());
                    System.out.print("Containing: ");
                    System.out.println(new String(data, 0, receivePacket.getLength()));
                    System.out.print("In bytes: ");

                    for (int j = 0; j < receivePacket.getLength(); j++) {
                        System.out.print(receivePacket.getData()[j] + " ");
                    }
                    System.out.println("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //5 times read request
            } else if ((i % 2) == 1) {
                try {
                    DatagramPacket sendPacket = request("test.txt", "netascii", RequestType.READ);
                    sendReceiveSocket.send(sendPacket);

                    //Printing content of the packet
                    System.out.println("Client: Sending read request to Host:-");
                    System.out.println("Destination host port: " + sendPacket.getPort());
                    System.out.println("Length: " + sendPacket.getLength());
                    System.out.print("Containing: ");
                    System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                    System.out.print("In bytes: ");

                    for (int j = 0; j < sendPacket.getLength(); j++) {
                        System.out.print(sendPacket.getData()[j] + " ");
                    }
                    System.out.println("\n");

                    byte[] data = new byte[20];
                    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
                    sendReceiveSocket.receive(receivePacket);

                    System.out.println("Client: Packet received from Host:");
                    System.out.println("Client at port: " + receivePacket.getPort());
                    System.out.println("Length: " + receivePacket.getLength());
                    System.out.print("Containing: ");
                    System.out.println(new String(data, 0, receivePacket.getLength()));
                    System.out.print("In bytes: ");

                    for (int j = 0; j < receivePacket.getLength(); j++) {
                        System.out.print(receivePacket.getData()[j] + " ");
                    }
                    System.out.println("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                //11th time invalid request
            } else {
                try {
                    DatagramPacket sendPacket = request("filename.txt", "octet", RequestType.INVALID);
                    sendReceiveSocket.send(sendPacket);

                    System.out.println("Client: Sending invalid request to Host:-");
                    System.out.println("Destination host port: " + sendPacket.getPort());
                    System.out.println("Length: " + sendPacket.getLength());
                    System.out.print("Containing: ");
                    System.out.println(new String(sendPacket.getData(), 0, sendPacket.getLength()));
                    System.out.print("In bytes: ");
                    for (int j = 0; j < sendPacket.getLength(); j++) {
                        System.out.print(sendPacket.getData()[j] + " ");
                    }
                    System.out.println("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        sendReceiveSocket.close();
    }

    public DatagramPacket request(String filename, String mode, RequestType type) throws IOException {
        int fileSize = filename.getBytes().length;
        int modeSize = mode.getBytes().length;
        final int MSG_SIZE = fileSize + modeSize + 4;

        byte[] data = new byte[MSG_SIZE];

        if (!(mode.equalsIgnoreCase("netascii") || mode.equalsIgnoreCase("octet"))) {
            data[1] = 3;    //invalid request if incorrect mode
            return new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
        }

        int dataSize = 0;
        data[dataSize++] = 0;
        switch (type) {
            case READ:
                data[dataSize++] = 1;
                break;
            case WRITE:
                data[dataSize++] = 2;
                break;
            case INVALID:
                data[dataSize++] = 3;
                break;
            default:
                data[dataSize++] = 3;
                break;
        }

        for (int i = 0; i < fileSize; i++) {
            data[dataSize++] = filename.getBytes()[i];
        }
        data[dataSize++] = 0;

        for (int i = 0; i < modeSize; i++) {
            data[dataSize++] = mode.getBytes()[i];
        }
        data[dataSize] = 0;

        return new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
    }

    public static void main(String args[]) {
        new Client().sendAndReceive();
    }
}
