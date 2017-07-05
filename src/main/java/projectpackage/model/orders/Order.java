package projectpackage.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import projectpackage.dto.JacksonMappingMarker;
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
@ReactEntity(entityTypeId = 2)
@ReactReference(referenceName = "OrderToNotification", outerEntityClass = Notification.class, outerFieldName =
        "order")
public class Order implements ReactEntityWithId {
    @JsonView(JacksonMappingMarker.List.class)
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 8)
    private Date registrationDate;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = 9)
    private Boolean isPaidFor;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Boolean.class, databaseAttrtypeIdValue = 10)
    private Boolean isConfirmed;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 11)
    private Date livingStartDate;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Date.class, databaseAttrtypeIdValue = 12)
    private Date livingFinishDate;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 13)
    private Long sum;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 14)
    private String comment;

    private Category category;
    private Room room;
    private User client;
    private User lastModificator;
    private List<ModificationHistory> historys;
    private List<JournalRecord> journalRecords;

}
