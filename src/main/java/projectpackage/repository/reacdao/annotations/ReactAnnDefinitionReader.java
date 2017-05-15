package projectpackage.repository.reacdao.annotations;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityReferenceRelationshipsData;
import projectpackage.repository.reacdao.fetch.EntityVariablesData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gvozd on 15.05.2017.
 */
@Component
public class ReactAnnDefinitionReader {
    private String packageName;
    private Set<Class> classList;
    private static final Class ENTITYANNOTATION = ReactEntity.class;
    private static final Class REFERENCEANNOTATION = ReactReference.class;
    private static final Class CHILDANNOTATION = ReactChild.class;
    private static final Class FIELDANNOTATION = ReactField.class;

    public ReactAnnDefinitionReader(String packageName) {
        this.packageName = packageName;
        this.classList = new HashSet<>();
    }

    public void getClasses() {
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

    public Map<Class, Map<Class, EntityOuterRelationshipsData>> makeOuterRelationshipsMap() {
        Map<Class, Map<Class, EntityOuterRelationshipsData>> entityRelationsMap = new HashMap<>();
        for (Class clazz : classList) {
            if (clazz.isAnnotationPresent(CHILDANNOTATION)) {
                Map<Class, EntityOuterRelationshipsData> thisClassOuterRelations = new HashMap<>();
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

    public Map<Class, Map<String, EntityVariablesData>> makeObjectsVariablesMap() {
        Map<Class, Map<String, EntityVariablesData>> entitysVariables = new HashMap<>();
        for (Class clazz : classList) {
            Map<String, EntityVariablesData> thisClassData = new HashMap<>();
            if (clazz.isAnnotationPresent(FIELDANNOTATION)) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(FIELDANNOTATION)) {
                        System.out.println("ANNOTATIONPRESENT");
                        Annotation[] annotations = field.getAnnotations();
                        for (Annotation annotation : annotations) {
                            ReactField reactField = (ReactField) annotation;
                            String fieldName = field.getName();
                            String fieldDatabaseName = reactField.databaseAttrtypeCodeValue();
                            Class objectType = reactField.valueObjectClass();
                            System.out.println("fieldName="+fieldName+" fieldDatabaseName="+fieldDatabaseName+" objectType="+objectType);
                            EntityVariablesData data = new EntityVariablesData(objectType, fieldDatabaseName);
                            thisClassData.put(fieldName, data);
                        }
                    }
                }
            }
            entitysVariables.put(clazz, thisClassData);
        }
        return entitysVariables;
    }

    public Map<Class, Map<Class, EntityReferenceRelationshipsData>> makeObjectsReferenceRelationsMap(){
        Map<Class, Map<Class, EntityReferenceRelationshipsData>> objectReferenceRelations = new HashMap<>();
        for (Class clazz:classList){
            if (clazz.isAnnotationPresent(REFERENCEANNOTATION)) {
                Map<Class, EntityReferenceRelationshipsData> thisClassMap = new HashMap<>();
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
