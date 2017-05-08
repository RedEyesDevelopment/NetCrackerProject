package projectpackage.repository.reacdao.models;

import projectpackage.repository.reacdao.fetch.EntityInnerObjectNode;
import projectpackage.repository.reacdao.fetch.EntityVariablesNode;

import java.util.LinkedHashMap;

/**
 * Created by Gvozd on 06.05.2017.
 */
public abstract class ReacEntity {
    public abstract String getEntityObjectTypeForEav();
    public abstract int getObjectId();
    public abstract LinkedHashMap<String, EntityVariablesNode> getEntityFields();
    public abstract LinkedHashMap<String, EntityInnerObjectNode> getEntityInnerObjects();
}
