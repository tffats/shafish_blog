import java.net.URL;

/**
 * 查看bootstrap加载器所有已经加载的类库
 */
public class ListBootstrapUrl {
    public static void main(String[] args) {
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for(URL url:urls) {
            System.out.println(url.toExternalForm());
        }
    }
}
