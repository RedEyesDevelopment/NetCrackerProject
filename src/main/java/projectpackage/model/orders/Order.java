package projectpackage.model.orders;

import lombok.Data;
import projectpackage.model.auth.User;
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
@ReactReference(outerEntityClass = Notification.class, outerFieldName = "order", outerFieldKey = "objectId", innerFieldKey = "objectId")
public class Order implements ReactEntityWithId {
    @ReactField(valueObjectClass = Integer.class, databaseAttrtypeCodeValue = "%OBJECT_ID")
    private int objectId;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Registration_date")
    private Date registrationDate;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Is_paid_for")
    private String isPaidFor;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Is_confirmed")
    private String isConfirmed;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Living_start_date")
    private Date livingStartDate;
    @ReactField(valueObjectClass = Date.class, databaseAttrtypeCodeValue = "Living_finish_date")
    private Date livingFinishDate;
    @ReactField(valueObjectClass = Long.class, databaseAttrtypeCodeValue = "Sum")
    private Long sum;
    @ReactField(valueObjectClass = String.class, databaseAttrtypeCodeValue = "Comment")
    private String comment;

    private Room room;
    private User client;
    private List<ModificationHistory> historys;

    public boolean isPaidFor(){
        return Boolean.parseBoolean(isPaidFor);
    }

    public boolean isConfirmed(){
        return Boolean.parseBoolean(isConfirmed);
    }

    public void setIsPaidFor(boolean isPaid){
        isPaidFor = String.valueOf(isPaid);
    }

    public void setIsConfirmed(boolean isConfirmed){
        this.isConfirmed = String.valueOf(isConfirmed);
    }
}
