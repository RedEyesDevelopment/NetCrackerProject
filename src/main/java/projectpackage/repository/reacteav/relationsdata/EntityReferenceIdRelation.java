package projectpackage.repository.reacteav.relationsdata;

public class EntityReferenceIdRelation {
    private int referenceTaskId;
    private int innerId;
    private int outerId;
    private Class innerClass;

    public EntityReferenceIdRelation(int referenceTaskId, int innerId, int outerId, Class innerClass) {

        this.referenceTaskId = referenceTaskId;
        this.innerId = innerId;
        this.outerId = outerId;
        this.innerClass = innerClass;
    }

    public int getReferenceTaskId() {
        return referenceTaskId;
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
                "referenceTaskId=" + referenceTaskId +
                ", innerId=" + innerId +
                ", outerId=" + outerId +
                ", innerClass=" + innerClass +
                '}';
    }
}
