import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Client {
    public static void main(String args[]) throws Exception {

        try {
            while (true) {
                BufferedReader inFromUser =
                        new BufferedReader(new InputStreamReader(System.in));
                DatagramSocket clientSocket = new DatagramSocket();
                clientSocket.setSoTimeout(5000);
                InetAddress IPAddress = InetAddress.getByName("localhost");
                byte[] sendData = new byte[8192];
                byte[] receiveData = new byte[8192];
                String sentence = inFromUser.readLine();
                sendData = serialize(sentence);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println(modifiedSentence);
                clientSocket.close();
                if (sentence.equals("exit")) {
                    break;
                }
            }
        } catch (SocketTimeoutException e){
            System.out.println("Время ожидания превысило допустимое...");
            System.out.println("Проверьте правильность подключения или повторите попытку позже.");
        }
    }

    private static byte[] serialize(String str) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeUTF(str);
        oos.flush();
        oos.close();
        return bos.toByteArray();
    }
}
