package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceIdRelation {
    private int innerId;
    private Class innerClass;

    public EntityReferenceIdRelation(int innerId, Class innerClass) {
        this.innerId = innerId;
        this.innerClass = innerClass;
    }

    public int getInnerId() {
        return innerId;
    }

    public Class getInnerClass() {
        return innerClass;
    }
}
