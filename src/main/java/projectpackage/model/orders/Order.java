package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.notifications.Notification;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.List;

@Data
@ReactEntity(entityTypeName = "Order")
@ReactReference(referenceName = "OrderToNotification", outerEntityClass = Notification.class, outerFieldName =
        "order", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Order implements ReactEntityWithId, Cloneable {
    @ReactAttrField(valueObjectClass = Integer.class, databaseAttrtypeIdValue = "%OBJECT_ID")
    private int objectId;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Registration_date")
    private Date registrationDate;
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = "Is_paid_for")
    private Boolean isPaidFor;
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = "Is_confirmed")
    private Boolean isConfirmed;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Living_start_date")
    private Date livingStartDate;
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = "Living_finish_date")
    private Date livingFinishDate;
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = "Sum")
    private Long sum;
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = "Comment")
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
