package projectpackage.dto;

/**
 * Created by Lenovo on 22.05.2017.
 */
public class JacksonMappingMarker {
    public static class List{};

    public static class Data extends List {};

    public static class Reception extends Data{};

    public static class Admin extends Reception{};

    public static class Full extends Admin{};
}
