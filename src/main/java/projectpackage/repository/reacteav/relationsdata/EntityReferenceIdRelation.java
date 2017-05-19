package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceIdRelation {
    private int innerId;
    private int outerId;
    private Class innerClass;

    public EntityReferenceIdRelation(int innerId, int outerId, Class innerClass) {
        this.innerId = innerId;
        this.outerId = outerId;
        this.innerClass = innerClass;
    }

    public int getInnerId() {
        return innerId;
    }

    public Class getInnerClass() {
        return innerClass;
    }

    public int getOuterId() {
        return outerId;
    }

    @Override
    public String toString() {
        return "EntityReferenceIdRelation{" +
                "innerId=" + innerId +
                ", outerId=" + outerId +
                ", innerClass=" + innerClass +
                '}';
    }
}
