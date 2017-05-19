package projectpackage.repository.reacteav.relationsdata;

/**
 * Created by Lenovo on 14.05.2017.
 */
public class EntityReferenceIdRelation {
    private int innerId;
    private int outerId;
    private Class innerClass;
    private String fieldName;

    public EntityReferenceIdRelation(int innerId, int outerId, Class innerClass, String fieldName) {
        this.innerId = innerId;
        this.outerId = outerId;
        this.innerClass = innerClass;
        this.fieldName = fieldName;
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

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "EntityReferenceIdRelation{" +
                "innerId=" + innerId +
                ", outerId=" + outerId +
                ", innerClass=" + innerClass +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}
