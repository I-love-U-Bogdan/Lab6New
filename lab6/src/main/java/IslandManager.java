import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class IslandManager {
    List<Thing> vectorJSON = Collections.synchronizedList(new Vector<Thing>());
    private String message = "";
    Island island = new Island("Мадагаскар");
    Island island_north = new Island("Северная часть острова");
    Island island_south = new Island("Южная часть острова");
    Island island_east = new Island("Восточная часть острова");
    Island island_west = new Island("Западная часть острова");
    Mother_Mumi mother_mumi = new Mother_Mumi("Муми Мама", new Boil("кофе"), new Search("банку с маслом"));
    Sniff sniff = new Sniff("Снифф", new Dig ("песок"));
    Snusmumrik snusmumrik = new Snusmumrik("Снусмурик", new Swim("море"), new See("золотое небо"));
    public void displayGreetings(){
        addMessageWithN("Приветствую");
    }
    public final Date createDate;
    static File file = new File("Input.json");
    static File output = new File("Output.json");
    static File outputBackUp = new File("OutputBackup.json");
    String fileName = "Output";
    String format = ".json";
    String info = new String(" ");
    String outInfo = new String(" ");
    IslandManager(){
        createDate = new Date();
    }
    public void createDirection(){
        island.AddInnerLocation(island_north, island_south, island_east, island_west);
    }
    /**

     * Метод для чтения текста из файла fileName

     * @return String info - строка-содержимое файла

     */
    public String readFile(File file){
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                info += line;
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        addMessageWithN(info);
        return info;
    }
    /**
     * Метод, создающий коллекцию vectorJson по данным из файла info
     */
    public List createVector() {//шаблон фабрик автосохранение бэкап
        JSONParser parser = new JSONParser();
        try {
            JSONArray a = (JSONArray) parser.parse(info);
            for (Object o : a) {
                JSONObject thing = (JSONObject) o;

                String name = (String) thing.get("name");
                String d = (String) thing.get("direction");
                vectorJSON.add(new Thing(name,d));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }return vectorJSON;
    }
    public void removeAll(){
        island_east.InnerThing.removeAllElements();
        island_west.InnerThing.removeAllElements();
        island_south.InnerThing.removeAllElements();
        island_north.InnerThing.removeAllElements();
    }
    /**
    * Метод, удаляющий последний объект в коллекции
     * Во многих методах могут быть методы removeAll и creation.
     * Они нужды для того, чтобы после изменения коллекции заново отсортировать объекты по сторонам света
     */
    public void removeLast(){
        if (vectorJSON.isEmpty()){
            addMessageWithN("Коллекция пуста.");
            return;
        }else{
            addMessageWithN("Элемент " + vectorJSON.get(vectorJSON.size() - 1).name + " удален.");
            vectorJSON.remove(vectorJSON.size() - 1);
            removeAll();
            creation();
            save(true);
        }
    }

    /**
     * Метод, сортирующий коллекцию в обратном порядке
     */
    public void reorder(){
        if (vectorJSON.isEmpty()) {
            addMessageWithN("Коллекция пуста.");
            return;
        }else{
        Collections.reverse(vectorJSON);
        addMessageWithN("Коллекция выстроена в обратном порядке");
        }
    }

    /**
     * Метод для получения помощи по командам
     */
    public void help(){
        addMessageWithN("revomeLast для удаления последнего объект");
        addMessageWithN("info для получения информации");
        addMessageWithN("reorder для обратного порядка");
        addMessageWithN("exit для выхода");
        addMessageWithN("show для показа");
        addMessageWithN("add [] для добавления нового объекта в коллекци.");
        addMessageWithN("save для сохранения коллекции");
        addMessageWithN("SortByLangth для сортировки по размеру");
    }

    /**
     * Метод для сортировки объектов вектора по сторонам света
     */
    public void creation( ) {
        for (Thing thing : vectorJSON) {
            if (thing.direction.equals("n")) {
                island_north.InnerThing.add(thing);

            } else if (thing.direction.equals("s")) {
                island_south.InnerThing.add(thing);

            } else if (thing.direction.equals("w")) {
                island_west.InnerThing.add(thing);

            } else if (thing.direction.equals("e")) {
                island_east.InnerThing.add(thing);

            } else {
                addMessageWithN("Неверная локация объекта " + thing.name);
            }
        }
    }
    /**

     * add {element} Метод для добавления элемента в коллекцию в интерактивном режиме

     * Формат задания элемента {element}- json

     * При вводе {element} другого формата или при вводе некорректного представления объекта - бросается исключение

     */
    public List add(String command)  {
        JSONParser parser = new JSONParser();
        try {
            JSONArray a = (JSONArray) parser.parse(command);
            for (Object ob1 : a) {
                JSONObject ob = (JSONObject) ob1;
                    String name = (String) ob.get("name");
                    String d = (String) ob.get("direction");
                    vectorJSON.add(new Thing(name, d));
                break;
            }
            addMessageWithN("Объект " + vectorJSON.get(vectorJSON.size()-1).name + " добавлен");
            removeAll();
            creation();
            save(true);
        } catch (Exception e) {
            addMessageWithN("Неподобающий формат");
        }return vectorJSON;
    }

    /**
     * Метод для получения из вектора String в Json формате
     * @return String с информацией о векторе в Json формате
     */
    public String toJson(){
        outInfo = "";
        outInfo += "[";
        for (Thing thing: vectorJSON){
           JSONObject jsonObject = new JSONObject();
           //if (outInfo.endsWith("]"))

               jsonObject.put("name",thing.name);
               jsonObject.put("direction",thing.direction);
               String objectInfo = jsonObject.toJSONString();
               outInfo += objectInfo;
               outInfo += ",";

        }if (outInfo != null && outInfo.length() > 0 && outInfo.charAt(outInfo.length() - 1) == ',') {
            outInfo = outInfo.substring(0, outInfo.length() - 1);
        }
        outInfo += "]";
        return outInfo;
    }

    /**
     * Метод для сохранения
     * @param copy true - сохранение в BackUp, false - сохранение в главный файл
     */
    public void save(boolean copy) {
        toJson();
        String copyString = "";
        if (copy){
            try {
                FileWriter fileWriter = new FileWriter(outputBackUp);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(outInfo);
                bufferedWriter.flush();
                bufferedWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            try {
                FileWriter fileWriter = new FileWriter(output);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(outInfo);
                bufferedWriter.flush();
                bufferedWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Удаление объекта из вектора по его имени и расположению
     * @param string На вход идет всё сообщение пользователя
     */
    public void remove(String string) {
        String nameObject = "";
        String location = "";
        String thatObject = "";
        if (vectorJSON.isEmpty()) {
            addMessageWithN("Коллекция пуста.");
            return;
        }else {
            try {
                thatObject = string.substring(7,string.length()-1+1);
                addMessageWithN(thatObject);
                String[] removeList = thatObject.split(",");
                nameObject = removeList[0];
                location = removeList[1];
                for (int i = 0; i < vectorJSON.size(); i++) {
                    if (vectorJSON.get(i).name.equalsIgnoreCase(nameObject)&& vectorJSON.get(i).direction.equalsIgnoreCase(location)) {
                        addMessageWithN("Объект " + nameObject + " был удален");
                        vectorJSON.remove(i);
                    }
                }
                save(true);
                removeAll();
                creation();
            }catch (Exception ex){
                addMessageWithN("Такого объекта нет!");
            }
        }
    }

    /**
     * Добавление в вектор нового объекта НЕ в формате Json
     * @param string Сообщение пользователя
     */
    public void addNormal(String string){
        String nameObject1 = "";
        String location1 = "";
        String addObject1 = "";
        try {
            addObject1 = string.substring(9,string.length()-1+1);
            String[] addList = addObject1.split(",");
            nameObject1 = addList[0];
            location1 = addList[1];
            vectorJSON.add(new Thing(nameObject1,location1));
            removeAll();
            creation();
            save(true);
            addMessageWithN("Объект " + nameObject1 + " был успешно добавлен");
        }catch (Exception ex){
            addMessageWithN("Извините, но даже тут вы умудрились что-то неправильно ввести.");
            addMessageWithN("Мне вас жаль.");
        }
    }
    /**

     * Выводит в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов)

     */
    public void info(){
        addMessageWithN("Тип коллекции: " + vectorJSON.getClass());
        addMessageWithN("Количество элементов в коллекци: " + vectorJSON.size());
        addMessageWithN("Дата создания: " + createDate);
    }
    public void TellStory(){//это и есть show
        //island_east.DescribeTrolls();//повествует о нелегком быте муми троллей
        island.DescribeLocations();// Проходит по ArrayList InnerLocation и рассказывает о каждой локации
        //mother_mumi.take(new Axe("топор","s"));//убери
    }
    public void Event(){
        class Ship extends Thing{ //локальный класс, потому что надо по заданию
            public void getDescribe() { }
            public void action() { }
            public Ship(String name, String direction, ThingActions ... thingActions){
                super(name,direction);
                addThingAction(thingActions);
            }
        }
        Ship ship = new Ship("Корабль", "n");
        island_north.AddInnerThing(ship);
        addMessageWithN(ship.name + " потерпел крушение в " + island_north.getName());
        island_east.GoTo(island_north,mother_mumi, snusmumrik);
        island_south.GoTo(island_north,sniff);
        int luck = (int)(Math.random()*10);
        if (luck>5){
            Treasure treasure = new Treasure("сокровища","n");
            island_north.AddInnerThing(treasure);
            for (Trolls trr : island_north.InnerTrolls){
                addMessage(trr.TrollName + ", ");
            }
            addMessageWithN("нашли " + treasure.name + "!");
            addMessage("Теперь там было ");
            for (Thing thing : island_north.InnerThing){
                addMessage(thing.getName() + ",");
            }
            addMessageWithN("");
            mother_mumi.take(treasure);
            try{
                mother_mumi.openChest(treasure);
            }
            catch (CheckException e){
                addMessageWithN(e.getMessage());
            }
        }
        if (luck<=5){
            addMessageWithN(ship.name + " оказался пуст");
        }

    }

    public List SortByLangth(){
        vectorJSON = vectorJSON.stream().sorted((e1,e2) -> {
            if(e1.getName().length()>e2.getName().length())
                return 1;
            if(e1.getName().length()==e2.getName().length())
                return 0;
            return -1;
        }).collect(Collectors.toList());
        return vectorJSON;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void addMessage(String message){
        this.message += message;
    }

    public void addMessageWithN(String message){
        this.message += message + "\n";
    }
}
