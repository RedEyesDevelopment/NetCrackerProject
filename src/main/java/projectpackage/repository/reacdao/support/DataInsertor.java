//package projectpackage.repository.reacdao.support;
//
//import projectpackage.repository.reacdao.ReacTask;
//import projectpackage.repository.reacdao.exceptions.WrongFieldNameException;
//import projectpackage.repository.reacdao.fetch.EntityOuterRelationshipsData;
//import projectpackage.repository.reacdao.fetch.EntityReferenceIdRelation;
//import projectpackage.repository.reacdao.fetch.EntityReferenceTaskData;
//import projectpackage.repository.reacdao.models.ReacEntity;
//
//import java.lang.reflect.Field;
//import java.util.*;
//
///**
// * Created by Lenovo on 08.05.2017.
// */
//public class DataInsertor {
//    private ReacTask outerEntity;
//    private ReacTask innerEntity;
//
//    DataInsertor(ReacTask outerEntity, ReacTask innerEntity) {
//        this.outerEntity = outerEntity;
//        this.innerEntity = innerEntity;
//    }
//
//    void connectBy() {
//
//        if (outerEntity.hasReferencedObjects()) {
//            for (Map.Entry<Integer, EntityReferenceIdRelation> entry : outerEntity.getReferenceIdRelations().entrySet()) {
//                Class targetClass = entry.getValue().getInnerClass();
//                Field parentToInputField = null;
//                String fieldName = null;
//
//                for (EntityReferenceTaskData taskdata : outerEntity.getCurrentEntityReferenceTasks().values()) {
//                    if (taskdata.getInnerClass().equals(targetClass)) {
//                        fieldName = taskdata.getThisFieldName();
//                    }
//                }
//                try {
//                    parentToInputField = outerEntity.getObjectClass().getDeclaredField(fieldName);
//                } catch (NoSuchFieldException e) {
//                    e.printStackTrace();
//                }
//                parentToInputField.setAccessible(true);
//
//                for (ReacEntity outerObject : outerEntity.getResultList()) {
//                    boolean doNotModifyTheField = false;
//                    for (ReacEntity innerObject : innerEntity.getResultList()) {
//                        if (entry.getKey().equals(outerObject.getObjectId()) && entry.getValue().getInnerId() == innerObject.getObjectId()) {
//                            insertInnerEntity(outerObject, innerObject, parentToInputField, doNotModifyTheField);
//                            doNotModifyTheField = true;
//                            outerEntity.getInnerObjects().remove(innerObject);
//                        }
//                    }
//                }
//            }
//        }
//
//
//        for (EntityOuterRelationshipsData entry : innerEntity.getCurrentEntityOuterLinks().values()) {
//            if (entry.getOuterClass().equals(outerEntity.getObjectClass())) {
//                Field parentToInputField;
//                Field outerKeyField;
//                Field innerKeyField;
//
//                try {
//                    parentToInputField = outerEntity.getObjectClass().getDeclaredField(entry.getOuterFieldName());
//                    outerKeyField = outerEntity.getObjectClass().getDeclaredField(entry.getOuterFieldKey());
//                    innerKeyField = innerEntity.getObjectClass().getDeclaredField(entry.getInnerFieldKey());
//                } catch (NoSuchFieldException e) {
//                    throw new WrongFieldNameException(outerEntity.getObjectClass(), innerEntity.getObjectClass());
//                }
//                outerKeyField.setAccessible(true);
//                innerKeyField.setAccessible(true);
//
//
//                for (ReacEntity outerObject : outerEntity.getResultList()) {
//                    boolean doNotModifyTheField = false;
//                    for (ReacEntity innerObject : innerEntity.getResultList()) {
//                        try {
//                            if (outerKeyField.get(outerObject).equals(innerKeyField.get(innerObject))) {
//                                insertInnerEntity(outerObject, innerObject, parentToInputField, doNotModifyTheField);
//                                doNotModifyTheField = true;
//                            }
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//        }
//
//        outerEntity.getInnerObjects().remove(innerEntity);
//    }
//
//    void insertInnerEntity(ReacEntity outer, ReacEntity inner, Field outerField, boolean doNotModifyTheField) {
//        try {
//            outerField.setAccessible(true);
//            if (outerField.getType().equals(Set.class)) {
//                if (doNotModifyTheField) {
//                    HashSet<ReacEntity> set = (HashSet<ReacEntity>) outerField.get(outer);
//                    set.add(inner);
//                } else {
//                    HashSet<ReacEntity> set = new HashSet<>();
//                    set.add(inner);
//                    outerField.set(outer, set);
//                }
//            } else if (outerField.getType().equals(List.class)) {
//                if (doNotModifyTheField) {
//                    List<ReacEntity> list = (List<ReacEntity>) outerField.get(outer);
//                    list.add(inner);
//                } else {
//                    List<ReacEntity> list = new ArrayList<>();
//                    list.add(inner);
//                    outerField.set(outer, list);
//                }
//            } else {
//                outerField.set(outer, inner);
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}