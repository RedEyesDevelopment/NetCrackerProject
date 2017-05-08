package projectpackage.repository.reacdao.models;

import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.LinkedHashMap;

/**
 * Created by Gvozd on 06.05.2017.
 */
public abstract class ReacEntity {
    public abstract String getEntityObjectTypeForEav();
    public abstract int getObjectId();
    public abstract LinkedHashMap<String, EntityVariablesNode> getEntityFields();
}
