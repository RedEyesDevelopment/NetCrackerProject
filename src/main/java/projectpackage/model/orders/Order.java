package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.notifications.Notification;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.List;

@Data
@ReactEntity(entityTypeName = "Order")
@ReactReference(referenceName = "OrderToNotification", outerEntityClass = Notification.class, outerFieldName =
        "order", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Order implements ReactEntityWithId, Cloneable {
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 8)
    private Date registrationDate;
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = 9)
    private Boolean isPaidFor;
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = 10)
    private Boolean isConfirmed;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 11)
    private Date livingStartDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 12)
    private Date livingFinishDate;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 13)
    private Long sum;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 14)
    private String comment;

    private Category category;
    private Room room;
    private User client;
    private User lastModificator;
    private List<ModificationHistory> historys;
    private List<JournalRecord> journalRecords;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
