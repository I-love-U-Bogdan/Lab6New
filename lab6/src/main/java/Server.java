
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLOutput;

public class Server {

    private static IslandManager islandManager;

    public static void main(String args[]) throws SocketException {
        {
            islandManager = new IslandManager();
            try {
                DatagramChannel channel = DatagramChannel.open();

                channel.bind(new InetSocketAddress(9876));
                while (true) {

                    Thread t = new Thread(() -> {
                        SocketAddress address;
                        ByteBuffer buf = ByteBuffer.allocate(8196);
                        buf.clear();
                        try {
                            address = channel.receive(buf);
                        } catch (IOException ex) {
                            address = null;
                            ex.printStackTrace();
                        }
                        System.out.println("lol");
                        String command = null;
                        try {
                            command = (String) deserialize(buf.array());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(command);
                        islandManager.setMessage("");

                        if (command.equals("removeLast")) {
                            islandManager.removeLast();
                        } else if (command.equals("reorder")) {
                            islandManager.reorder();
                        } else if (command.equals("info")) {
                            islandManager.info();
                        } else if (command.equals("show")) {
                            islandManager.TellStory();
                        } else if (command.length() > 8 && command.substring(0, 9).equalsIgnoreCase("addNormal")) {
                            islandManager.addNormal(command);
                        } else if (command.equals("help")) {
                            islandManager.help();
                        } else if (command.equals("save")) {
                            islandManager.save(false);
                            islandManager.addMessageWithN(("Успешно сохранено"));
                        } else if (command.length() > 6 && command.substring(0, 6).equals("remove")) {
                            islandManager.remove(command);
                        } else if (command.length() > 3 && command.substring(0, 3).equals("add")) {
                            String kek = (command.substring(3, command.length() - 1 + 1));
                            islandManager.addMessageWithN((kek));
                            islandManager.add(kek);
                        } else {
                            if (command.equals("exit")) {
                                islandManager.addMessageWithN(("Отключение от сервера..."));
                            } else {
                                islandManager.addMessageWithN(("Команда не найдена"));
                                islandManager.addMessageWithN(("Введите help для получения информации о командах"));
                            }
                        }


                        ByteBuffer buffer = ByteBuffer.wrap(islandManager.getMessage().getBytes());
                        //ByteBuffer buffer = ByteBuffer.wrap("lol".getBytes());
                        try {
                            channel.send(buffer, address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                    t.start();
                }
            }catch (IOException e){}
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
    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bos = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bos);
        Object obj = ois.readUTF();
        ois.close();
        return obj;
    }
}