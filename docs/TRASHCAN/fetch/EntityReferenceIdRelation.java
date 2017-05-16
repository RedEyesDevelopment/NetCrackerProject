package projectpackage.repository.reacdao.fetch;

import projectpackage.repository.reacdao.models.ReacEntity;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceIdRelation {
    private int innerId;
    private Class<? extends ReacEntity> innerClass;

    public EntityReferenceIdRelation(int innerId, Class<? extends ReacEntity> innerClass) {
        this.innerId = innerId;
        this.innerClass = innerClass;
    }

    public int getInnerId() {
        return innerId;
    }

    public Class<? extends ReacEntity> getInnerClass() {
        return innerClass;
    }
}
