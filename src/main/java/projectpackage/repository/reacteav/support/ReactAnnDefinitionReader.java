package projectpackage.repository.reacteav.support;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import projectpackage.repository.reacteav.annotations.*;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class ReactAnnDefinitionReader {
    private String packageName;
    private Set<Class> classList;
    private static final Class ENTITYANNOTATION = ReactEntity.class;
    private static final Class REFERENCEANNOTATION = ReactReference.class;
    private static final Class REFERENCEBUCKETANNOTATION = References.class;
    private static final Class CHILDANNOTATION = ReactChild.class;
    private static final Class FIELDANNOTATION = ReactField.class;

    public ReactAnnDefinitionReader(String packageName) {
        this.packageName = packageName;
        this.classList = new HashSet<>();

        Reflections reflections = new Reflections(packageName);
        Set<Class> allClasses = reflections.getTypesAnnotatedWith(ENTITYANNOTATION);
        classList = allClasses;
    }

    public Map<Class, String> makeClassesMap() {
        Map<Class, String> makeClassesMap = new HashMap<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(ENTITYANNOTATION)) {
                ReactEntity annotation = (ReactEntity) clazz.getAnnotation(ENTITYANNOTATION);
                String entityTypeName = annotation.entityTypeName();
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
                    if (field.isAnnotationPresent(FIELDANNOTATION)) {
                        Annotation[] annotations = field.getAnnotations();
                        for (Annotation annotation : annotations) {
                            ReactField reactField = (ReactField) annotation;
                            String fieldName = field.getName();
                            String fieldDatabaseName = reactField.databaseAttrtypeCodeValue();
                            Class objectType = reactField.valueObjectClass();
                            EntityVariablesData data = new EntityVariablesData(objectType, fieldDatabaseName);
                            thisClassData.put(fieldName, data);
                        }
                    }
                }
            entitysVariables.put(clazz, thisClassData);
        }
        return entitysVariables;
    }

    public Map<Class, HashMap<Class, EntityReferenceRelationshipsData>> makeObjectsReferenceRelationsMap(){
        Map<Class, HashMap<Class, EntityReferenceRelationshipsData>> objectReferenceRelations = new HashMap<>();
        for (Class clazz:classList){
            if (clazz.isAnnotationPresent(REFERENCEANNOTATION)) {
                HashMap<Class, EntityReferenceRelationshipsData> thisClassMap = new HashMap<>();
                for (Annotation annot : clazz.getAnnotationsByType(REFERENCEANNOTATION)) {
                    ReactReference reactReference = (ReactReference) annot;
                    String outerFieldName = reactReference.outerFieldName();
                    String outerFieldKey = reactReference.outerFieldKey();
                    String innerFieldKey = reactReference.innerFieldKey();
                    Class outerClass = reactReference.outerEntityClass();
                    EntityReferenceRelationshipsData data = new EntityReferenceRelationshipsData(outerFieldName, innerFieldKey, outerFieldKey);
                    thisClassMap.put(outerClass, data);
                }
                objectReferenceRelations.put(clazz,thisClassMap);
            } else if (clazz.isAnnotationPresent(REFERENCEBUCKETANNOTATION)) {
                HashMap<Class, EntityReferenceRelationshipsData> thisClassMap = new HashMap<>();
                References references = (References) clazz.getAnnotation(REFERENCEBUCKETANNOTATION);
                for (ReactReference reactReference : references.value()) {
                        String outerFieldName = reactReference.outerFieldName();
                        String outerFieldKey = reactReference.outerFieldKey();
                        String innerFieldKey = reactReference.innerFieldKey();
                        Class outerClass = reactReference.outerEntityClass();
                        EntityReferenceRelationshipsData data = new EntityReferenceRelationshipsData(outerFieldName, innerFieldKey, outerFieldKey);
                        thisClassMap.put(outerClass, data);
                    }
                objectReferenceRelations.put(clazz,thisClassMap);
            }
        }
        return objectReferenceRelations;
    }

    public void printPackageName() {
        System.out.println(packageName);
    }

    public void printClassesList() {
        System.out.println("ClassList size is " + classList.size());
        for (Class clazz : classList) {
            System.out.println("Class=" + clazz.getName());
        }
    }

}
