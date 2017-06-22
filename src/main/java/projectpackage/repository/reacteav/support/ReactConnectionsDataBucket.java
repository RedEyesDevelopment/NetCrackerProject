package projectpackage.repository.reacteav.support;

import com.google.common.collect.ImmutableMap;
import projectpackage.repository.reacteav.relationsdata.EntityOuterRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReactConnectionsDataBucket {
    private Map<Class, Integer> classesMap;
    private Map<Class, LinkedHashMap<String, EntityVariablesData>> entityVariablesMap;
    private Map<Class, HashMap<Class, EntityOuterRelationshipsData>> outerRelationsMap;
    private Map<Class, HashMap<String, EntityReferenceRelationshipsData>> entityReferenceRelationsMap;

    public ReactConnectionsDataBucket(Map<Class, Integer> classesMap, Map<Class, LinkedHashMap<String, EntityVariablesData>> entityVariablesMap, Map<Class, HashMap<Class, EntityOuterRelationshipsData>> outerRelationsMap, Map<Class, HashMap<String, EntityReferenceRelationshipsData>> entityReferenceRelationsMap) {
        this.classesMap = classesMap;
        this.entityVariablesMap = entityVariablesMap;
        this.outerRelationsMap = outerRelationsMap;
        this.entityReferenceRelationsMap = entityReferenceRelationsMap;
    }

    public Map<Class, Integer> getClassesMap() {
        return ImmutableMap.copyOf(classesMap);
    }

    public Map<Class, LinkedHashMap<String, EntityVariablesData>> getEntityVariablesMap() {
        return ImmutableMap.copyOf(entityVariablesMap);
    }

    public Map<Class, HashMap<Class, EntityOuterRelationshipsData>> getOuterRelationsMap() {
        return ImmutableMap.copyOf(outerRelationsMap);
    }

    public Map<Class, HashMap<String, EntityReferenceRelationshipsData>> getEntityReferenceRelationsMap() {
        return ImmutableMap.copyOf(entityReferenceRelationsMap);
    }
}
