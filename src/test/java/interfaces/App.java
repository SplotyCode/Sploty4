package interfaces;

public class App {

    private static Interface anInterface;

    public static void main(String[] args){
        anInterface = new Android();

        anInterface.sendNotification("Test", "Test");

    }
}
