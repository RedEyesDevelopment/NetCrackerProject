package projectpackage.repository.reacdao.models;

import java.util.Map;

/**
 * Created by Gvozd on 06.05.2017.
 */
public abstract class ReacEntityWithInnerObjects extends ReacEntity {
    public abstract Map<String,String> getEntityInnerObjects();
}
