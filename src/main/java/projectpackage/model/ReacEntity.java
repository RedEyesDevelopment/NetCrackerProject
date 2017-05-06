package projectpackage.model;

import java.util.Map;

/**
 * Created by Gvozd on 06.05.2017.
 */
public abstract class ReacEntity {
    public abstract int getEntityObjectTypeForEav();
    public abstract int getObjectId();
    public abstract Map<String, String> getEntityFields();
}
