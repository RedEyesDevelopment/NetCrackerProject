package projectpackage.repository.reacteav.support;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import projectpackage.repository.reacteav.annotations.*;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Log4j
public class ReactAnnDefinitionReader {

    private static final Logger LOGGER = Logger.getLogger(ReactAnnDefinitionReader.class);

    private String packageName;
    private Set<Class> classList;
    private static final Class ENTITYANNOTATION = ReactEntity.class;
    private static final Class REFERENCEANNOTATION = ReactReference.class;
    private static final Class REFERENCEBUCKETANNOTATION = References.class;
    private static final Class CHILDANNOTATION = ReactChild.class;
    private static final Class ATTRFIELDANNOTATION = ReactAttrField.class;
    private static final Class NATIVEFIELDANNOTATION = ReactNativeField.class;

    public ReactAnnDefinitionReader(String packageName) {
        this.packageName = packageName;
        this.classList = new HashSet<>();
        Reflections reflections = new Reflections(packageName);
        classList = (Set<Class>) reflections.getTypesAnnotatedWith(ENTITYANNOTATION);
    }

    public Map<Class, Integer> makeClassesMap() {
        Map<Class, Integer> makeClassesMap = new HashMap<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(ENTITYANNOTATION)) {
                ReactEntity annotation = (ReactEntity) clazz.getAnnotation(ENTITYANNOTATION);
                int entityTypeName = annotation.entityTypeId();
                makeClassesMap.put(clazz, entityTypeName);
            }
        }
        return makeClassesMap;
    }

    public Map<Class, HashMap<Class, EntityOuterRelationshipsData>> makeOuterRelationshipsMap() {
        Map<Class, HashMap<Class, EntityOuterRelationshipsData>> entityRelationsMap = new HashMap<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(CHILDANNOTATION)) {
                HashMap<Class, EntityOuterRelationshipsData> thisClassOuterRelations = new HashMap<>();
                for (Annotation annot : clazz.getAnnotationsByType(CHILDANNOTATION)) {
                    ReactChild annotation = (ReactChild) annot;
                    String outerFieldName = annotation.outerFieldName();
                    String outerFieldKey = annotation.outerFieldKey();
                    String innerFieldKey = annotation.innerFieldKey();
                    Class outerEntityClass = annotation.outerEntityClass();
                    EntityOuterRelationshipsData data = new EntityOuterRelationshipsData(outerFieldName, innerFieldKey, outerFieldKey);
                    thisClassOuterRelations.put(outerEntityClass, data);
                }
                entityRelationsMap.put(clazz, thisClassOuterRelations);
            }
        }
        return entityRelationsMap;
    }

    public Map<Class, LinkedHashMap<String, EntityVariablesData>> makeObjectsVariablesMap() {
        Map<Class, LinkedHashMap<String, EntityVariablesData>> entitysVariables = new HashMap<>();
        for (Class clazz : classList) {
            LinkedHashMap<String, EntityVariablesData> thisClassData = new LinkedHashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ATTRFIELDANNOTATION)) {
                    Annotation[] annotations = field.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().equals(ATTRFIELDANNOTATION)) {
                            ReactAttrField reactAttrField = (ReactAttrField) annotation;
                            String fieldName = field.getName();
                            int fieldDatabaseName = reactAttrField.databaseAttrtypeIdValue();
                            Class objectType = reactAttrField.valueObjectClass();
                            EntityVariablesData data = new EntityVariablesData(objectType, null, fieldDatabaseName);
                            thisClassData.put(fieldName, data);
                        }
                    }
                }
                if (field.isAnnotationPresent(NATIVEFIELDANNOTATION)) {
                    Annotation[] annotations = field.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().equals(NATIVEFIELDANNOTATION)){
                            ReactNativeField reactAttrField = (ReactNativeField) annotation;
                            String fieldName = field.getName();
                            String fieldDatabaseName = reactAttrField.databaseObjectCodeValue();
                            Class objectType = reactAttrField.valueObjectClass();
                            EntityVariablesData data = new EntityVariablesData(objectType, fieldDatabaseName, null);
                            thisClassData.put(fieldName, data);
                        }

                    }
                }
            }
            entitysVariables.put(clazz, thisClassData);
        }
        return entitysVariables;
    }

    public Map<Class, HashMap<String, EntityReferenceRelationshipsData>> makeObjectsReferenceRelationsMap() {
        Map<Class, HashMap<String, EntityReferenceRelationshipsData>> objectReferenceRelations = new HashMap<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(REFERENCEANNOTATION)) {
                HashMap<String, EntityReferenceRelationshipsData> thisClassMap = new HashMap<>();
                for (Annotation annot : clazz.getAnnotationsByType(REFERENCEANNOTATION)) {
                    ReactReference reactReference = (ReactReference) annot;
                    String outerFieldName = reactReference.outerFieldName();
                    String outerFieldKey = reactReference.outerFieldKey();
                    String innerFieldKey = reactReference.innerFieldKey();
                    Class outerClass = reactReference.outerEntityClass();
                    String referenceName = reactReference.referenceName();
                    Integer referenceAttrId = null;
                    if (!reactReference.attrIdField().equals("")) {
                        try {
                            referenceAttrId = Integer.parseInt(reactReference.attrIdField());
                        } catch (Throwable e) {
                            log.error(e);
                        }
                    }
                    EntityReferenceRelationshipsData data = new EntityReferenceRelationshipsData(outerClass, outerFieldName, innerFieldKey, outerFieldKey, referenceAttrId);
                    thisClassMap.put(referenceName, data);
                }
                objectReferenceRelations.put(clazz, thisClassMap);
            } else if (clazz.isAnnotationPresent(REFERENCEBUCKETANNOTATION)) {
                HashMap<String, EntityReferenceRelationshipsData> thisClassMap = new HashMap<>();
                References references = (References) clazz.getAnnotation(REFERENCEBUCKETANNOTATION);
                for (ReactReference reactReference : references.value()) {
                    String outerFieldName = reactReference.outerFieldName();
                    String outerFieldKey = reactReference.outerFieldKey();
                    String innerFieldKey = reactReference.innerFieldKey();
                    Class outerClass = reactReference.outerEntityClass();
                    String referenceName = reactReference.referenceName();
                    Integer referenceAttrId = null;
                    if (!reactReference.attrIdField().equals("")) {
                        try {
                            referenceAttrId = Integer.parseInt(reactReference.attrIdField());
                        } catch (Throwable e) {
                            log.error(e);
                        }
                    }
                    EntityReferenceRelationshipsData data = new EntityReferenceRelationshipsData(outerClass, outerFieldName, innerFieldKey, outerFieldKey, referenceAttrId);
                    thisClassMap.put(referenceName, data);
                }
                objectReferenceRelations.put(clazz, thisClassMap);
            }
        }
        return objectReferenceRelations;
    }

    public void printClassesList() {
        LOGGER.info("ClassList size is " + classList.size());
        for (Class clazz : classList) {
            LOGGER.info("Class=" + clazz.getName());
        }
    }

}
