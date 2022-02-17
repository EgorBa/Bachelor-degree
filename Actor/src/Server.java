import java.util.ArrayList;

public interface Server {
    ArrayList<String> getRequest(String query, ServerType serverType);

    default ServerType getServerTypeFromString(String name) {
        if (name.contains("Google")) return ServerType.GOOGLE;
        if (name.contains("Yandex")) return ServerType.YANDEX;
        if (name.contains("Bing")) return ServerType.BING;
        return ServerType.UNDEFINED;
    }
}
