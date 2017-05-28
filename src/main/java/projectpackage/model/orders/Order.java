package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
import projectpackage.model.maintenances.JournalRecord;
import projectpackage.model.notifications.Notification;
import projectpackage.model.rooms.Room;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Date;
import java.util.List;

@Data
@ReactEntity(entityTypeName = "Order")
@ReactReference(referenceName = "OrderToNotification", outerEntityClass = Notification.class, outerFieldName =
        "order", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Order implements ReactEntityWithId, Cloneable {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Registration_date")
    private Date registrationDate;
    @ReactField(valueObjectClass = Boolean.class, databaseAttrtypeCodeValue = "Is_paid_for")
    private Boolean isPaidFor;
    @ReactField(valueObjectClass = Boolean.class, databaseAttrtypeCodeValue = "Is_confirmed")
    private Boolean isConfirmed;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Living_start_date")
    private Date livingStartDate;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Living_finish_date")
    private Date livingFinishDate;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Sum")
    private Long sum;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Comment")
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
