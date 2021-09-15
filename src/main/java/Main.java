import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if ("App".equals(System.getProperty("clazz"))) {
            App.main(args);
            return;
        }
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            Runtime.getRuntime().exec("cmd /c start java -Dclazz=App -jar " + path.substring(1));
        } else {
            App.main(args);
        }
    }
}