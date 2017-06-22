package projectpackage.repository.reacteav;

import lombok.extern.log4j.Log4j;
import projectpackage.repository.reacteav.conditions.ConditionExecutor;
import projectpackage.repository.reacteav.querying.ReactQueryTaskHolder;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceRelationshipsData;
import projectpackage.repository.reacteav.relationsdata.EntityReferenceTaskData;
import projectpackage.repository.reacteav.relationsdata.EntityVariablesData;
import projectpackage.repository.reacteav.support.ReactConnectionsDataBucket;
import projectpackage.repository.reacteav.support.ReactConstantConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.Callable;

@Log4j
class ReacQueryTasksPreparator {
    private ReactConstantConfiguration config;
    private ReactConnectionsDataBucket dataBucket;
    private Set<ConditionExecutor> executors;
    private ReactQueryBuilder builder;

    public ReacQueryTasksPreparator(ReactConstantConfiguration config, ReactConnectionsDataBucket dataBucket) {
        this.config = config;
        this.dataBucket = dataBucket;
    }

    List<ReactQueryTaskHolder> prepareTasks(ReacTask rootNode, Set<ConditionExecutor> executors,ReactQueryBuilder builder) {
        this.executors = executors;
        this.builder = builder;
        List<ReactQueryTaskHolder> taskList = new ArrayList<>();
        creatingReacTaskTree(taskList, rootNode);
        return taskList;
    }

    private void creatingReacTaskTree(List<ReactQueryTaskHolder> taskList, ReacTask task) {
        addReferenceLinks(task);
        if (!task.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : task.getInnerObjects()) {
                creatingReacTaskTree(taskList, reacTask);
            }
        }
        Exe
        taskList.add(prepareReacTask(task));
    }

    //   /Добавляем в задание ссылки и маркера на reference-объекты
    private void addReferenceLinks(ReacTask currentTask) {
        int per = 0;
        for (ReacTask task : currentTask.getInnerObjects()) {
            for (Map.Entry<String, EntityReferenceRelationshipsData> outerData : task.getCurrentEntityReferenceRelations().entrySet()) {
                if (outerData.getValue().getOuterClass().equals(currentTask.getObjectClass()) && null != task.getReferenceId() && task.getReferenceId().equals(outerData.getKey())) {
                    EntityReferenceTaskData newReferenceTask = new EntityReferenceTaskData(task.getObjectClass(), task.getObjectClass().getSimpleName(), outerData.getValue().getOuterFieldName(), outerData.getValue().getReferenceAttrId());
                    currentTask.addCurrentEntityReferenceTasks(per++, newReferenceTask);
                }
            }
        }
    }

    private class TaskPreparator implements Callable<ReactQueryTaskHolder>{
        private ReacTask currentNode;

        public TaskPreparator(ReacTask currentNode) {
            this.currentNode = currentNode;
        }

        @Override
        public ReactQueryTaskHolder call() throws Exception {
            return prepareReacTask(currentNode);
        }

        private ReactQueryTaskHolder prepareReacTask(ReacTask currentNode) {
            StringBuilder query = null;
            Map<String, Object> sqlParameterSource = new HashMap<>();
            if (currentNode.isForSingleObject()) {
                try {
                    query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, true, null, false);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    log.error(e);
                }
                sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                sqlParameterSource.put(config.getEntityIdConstant(), currentNode.getTargetId());
                if (currentNode.hasReferencedObjects()) {
                    for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                        sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                    }
                }
            } else {
                if (null != currentNode.getOrderingParameter()) {
                    try {
                        query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, currentNode.getOrderingParameter(), currentNode.isAscend());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        log.error(e);
                    }
                    sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                    if (currentNode.hasReferencedObjects()) {
                        for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                            sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                        }
                    }
                } else {
                    try {
                        query = getQueryForEntity(currentNode.getCurrentEntityParameters(), currentNode, false, null, false);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        log.error(e);
                    }
                    sqlParameterSource.put(config.getEntityTypeIdConstant(), currentNode.getThisClassObjectTypeName());
                    if (currentNode.hasReferencedObjects()) {
                        for (EntityReferenceTaskData data : currentNode.getCurrentEntityReferenceTasks().values()) {
                            sqlParameterSource.put(data.getInnerClassObjectTypeName(), dataBucket.getClassesMap().get(data.getInnerClass()));
                        }
                    }
                }
            }
            return new ReactQueryTaskHolder(currentNode, query, sqlParameterSource);
        }

        //Метод создания ссылки в билдере
        private StringBuilder getQueryForEntity(LinkedHashMap<String, EntityVariablesData> currentNodeVariables, ReacTask currentNode, boolean isSearchById, String orderingParameter, boolean ascend) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
            WhereAppendingConditionExecutor afterWhereExecutor = null;
            for (ConditionExecutor executor: executors){
                if (executor.getExecutorClass().equals(WhereAppendingConditionExecutor.class)){
                    afterWhereExecutor = (WhereAppendingConditionExecutor) executor;
                }
            }
            return builder.getQueryForEntity(currentNodeVariables, currentNode, isSearchById, orderingParameter, ascend, afterWhereExecutor);
        }
    }
}
