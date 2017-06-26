package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
import projectpackage.repository.reacteav.exceptions.WrongFieldNameException;
import projectpackage.repository.reacteav.modelinterface.ReactEntityWithId;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceIdRelation;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;

import java.lang.reflect.Field;
import java.util.*;

@Log4j
class DataInsertor {
    private ReacTask outerEntity;
    private ReacTask innerEntity;

    DataInsertor(ReacTask outerEntity, ReacTask innerEntity) {
        this.outerEntity = outerEntity;
        this.innerEntity = innerEntity;
    }

    void connectBy() {
        if (outerEntity.hasReferencedObjects()) {
            for (Map.Entry<Integer, EntityReferenceIdRelation> entry : outerEntity.getReferenceIdRelations().entrySet()) {
                Field parentToInputField = null;
                String fieldName = null;

                for (Map.Entry<Integer, EntityReferenceTaskData> taskdata : outerEntity.getCurrentEntityReferenceTasks().entrySet()) {
                    if (taskdata.getKey().equals(entry.getValue().getReferenceTaskId())) {
                        fieldName = taskdata.getValue().getThisFieldName();
                    }
                }
                try {
                    parentToInputField = outerEntity.getObjectClass().getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    log.error(e);
                }
                parentToInputField.setAccessible(true);


                for (Object outerObject : outerEntity.getResultList()) {
                    boolean doNotModifyTheField = false;
                    for (Object innerObject : innerEntity.getResultList()) {

                        if (entry.getValue().getOuterId() == getEntityId(innerObject) && entry.getValue().getInnerId() == getEntityId(outerObject)) {
                            insertInnerEntity(outerObject, innerObject, parentToInputField, doNotModifyTheField);
                            doNotModifyTheField = true;
                            outerEntity.getInnerObjects().remove(innerObject);
                        }
                    }
                }
            }
        }


        for (Map.Entry<Class, EntityOuterRelationshipsData> entry : innerEntity.getCurrentEntityOuterLinks().entrySet()) {
            if (entry.getKey().equals(outerEntity.getObjectClass())) {
                Field parentToInputField;
                Field outerKeyField;
                Field innerKeyField;

                try {
                    parentToInputField = outerEntity.getObjectClass().getDeclaredField(entry.getValue().getOuterFieldName());
                    outerKeyField = outerEntity.getObjectClass().getDeclaredField(entry.getValue().getOuterFieldKey());
                    innerKeyField = innerEntity.getObjectClass().getDeclaredField(entry.getValue().getInnerFieldKey());
                } catch (NoSuchFieldException e) {
                    throw new WrongFieldNameException(outerEntity.getObjectClass(), innerEntity.getObjectClass());
                }
                outerKeyField.setAccessible(true);
                innerKeyField.setAccessible(true);


                for (Object outerObject : outerEntity.getResultList()) {
                    boolean doNotModifyTheField = false;
                    for (Object innerObject : innerEntity.getResultList()) {
                        try {
                            if (outerKeyField.get(outerObject).equals(innerKeyField.get(innerObject))) {
                                insertInnerEntity(outerObject, innerObject, parentToInputField, doNotModifyTheField);
                                doNotModifyTheField = true;
                            }
                        } catch (IllegalAccessException e) {
                            log.error(e);
                        }
                    }
                }
            }
        }
    }

    private void insertInnerEntity(Object outer, Object inner, Field outerField, boolean doNotModifyTheField) {
        try {
            outerField.setAccessible(true);
            if (outerField.getType().equals(Set.class)) {
                if (doNotModifyTheField) {
                    HashSet set = (HashSet) outerField.get(outer);
                    set.add(inner);
                } else {
                    HashSet set = new HashSet<>();
                    set.add(inner);
                    outerField.set(outer, set);
                }
            } else if (outerField.getType().equals(List.class)) {
                if (doNotModifyTheField) {
                    List list = (List) outerField.get(outer);
                    list.add(inner);
                } else {
                    List list = new ArrayList<>();
                    list.add(inner);
                    outerField.set(outer, list);
                }
            } else {
                outerField.set(outer, inner);
            }
        } catch (IllegalAccessException e) {
            log.error(e);
        }
    }

    private int getEntityId(Object entity) {
        ReactEntityWithId reactEntityWithId = (ReactEntityWithId) entity;
        return reactEntityWithId.getObjectId();
    }
}