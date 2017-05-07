package projectpackage.repository.reacdao.models;

import projectpackage.repository.reacdao.support.EntityVariablesNode;

import java.util.LinkedHashMap;

/**
 * Created by Gvozd on 06.05.2017.
 */
public abstract class ReacEntityWithInnerObjects extends ReacEntity {
    public abstract LinkedHashMap<String, EntityVariablesNode> getEntityInnerObjects();
}
