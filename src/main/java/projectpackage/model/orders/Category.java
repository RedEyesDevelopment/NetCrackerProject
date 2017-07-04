package projectpackage.model.orders;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import projectpackage.dto.JacksonMappingMarker;
import projectpackage.model.maintenances.Complimentary;
import projectpackage.repository.reacteav.annotations.ReactEntity;
import projectpackage.repository.reacteav.annotations.ReactAttrField;
import projectpackage.repository.reacteav.annotations.ReactNativeField;
import projectpackage.repository.reacteav.annotations.ReactReference;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;

import java.util.Set;

/**
 * Created by Arizel on 19.05.2017.
 */
@Data
@ReactEntity(entityTypeId = 13)
@ReactReference(referenceName = "CategoryToOrder", outerEntityClass = Order.class, outerFieldName = "category")
public class Category implements ReactEntityWithId {
    @JsonView(JacksonMappingMarker.List.class)
    @ReactNativeField(valueObjectClass = Integer.class, databaseObjectCodeValue = "%OBJECT_ID")
    private int objectId;
    @JsonView(JacksonMappingMarker.List.class)
    @ReactAttrField(valueObjectClass = String.class, databaseAttrtypeIdValue = 45)
    private String categoryTitle;
    @JsonView(JacksonMappingMarker.List.class)
    @ReactAttrField(valueObjectClass = Long.class, databaseAttrtypeIdValue = 46)
    private Long categoryPrice;

    @JsonView(JacksonMappingMarker.Data.class)
    private Set<Complimentary> complimentaries;

}
