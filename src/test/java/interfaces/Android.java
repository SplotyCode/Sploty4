package interfaces;

public class Android implements Interface {

    @Override
    public void sendNotification(String title, String message) {
        System.out.println(title);
    }

    @Override
    public long getMemoryUsage() {
        return 0;
    }
}
