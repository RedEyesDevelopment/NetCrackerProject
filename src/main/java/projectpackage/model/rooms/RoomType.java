package projectpackage.model.rooms;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import projectpackage.dto.JacksonMappingMarker;
import projectpackage.model.rates.Rate;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

@Data
@ReactEntity(entityTypeId = 5)
@ReactReference(referenceName = "RoomTypeToRoom", outerEntityClass = Room.class, outerFieldName = "roomType")
public class RoomType implements ReactEntityWithId, Cloneable {
    @JsonView(JacksonMappingMarker.List.class)
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @JsonView(JacksonMappingMarker.List.class)
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 28)
    private String roomTypeTitle;
    @JsonView(JacksonMappingMarker.Data.class)
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 29)
    private String content;
    @JsonView(JacksonMappingMarker.Reception.class)
    private Set<Rate> rates;

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
