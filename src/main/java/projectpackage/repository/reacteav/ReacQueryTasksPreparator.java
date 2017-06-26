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
import java.util.concurrent.*;

@Log4j
class ReacQueryTasksPreparator {
    private static final int THREADSCOUNT = Runtime.getRuntime().availableProcessors()-1;
    private ReactConstantConfiguration config;
    private ReactConnectionsDataBucket dataBucket;
    private Set<ConditionExecutor> executors;
    private ReactQueryBuilder builder;


    ReacQueryTasksPreparator(ReactConstantConfiguration config, ReactConnectionsDataBucket dataBucket) {
        this.config = config;
        this.dataBucket = dataBucket;
    }

    List<ReactQueryTaskHolder> prepareTasks(ReacTask rootNode, Set<ConditionExecutor> executors, ReactQueryBuilder builder) {
        this.executors = executors;
        this.builder = builder;

        ExecutorService threadPool = Executors.newFixedThreadPool(THREADSCOUNT);
        List<FutureTask<ReactQueryTaskHolder>> futureTasks = new ArrayList<>(6);
        recursiveTaskCreation(rootNode, futureTasks);
        List<ReactQueryTaskHolder> results = new ArrayList<>(futureTasks.size());

        for (FutureTask futureTask : futureTasks) {
            threadPool.execute(futureTask);
        }
        int endedFutures;

        //КАК ЭТО, МАТЬ ЕГО, РАБОТАЕТ??????????
        while (true) {
            endedFutures=0;
            for (FutureTask<ReactQueryTaskHolder> currentTask : futureTasks) {
                if (currentTask.isDone()) {
                    endedFutures++;
                }
            }
            if (endedFutures >= futureTasks.size()) {
                break;
            }
            //ВОТ ТУТ ИМЕННО - ПРИ СЛЕДУЮЩЕМ ПРОХОДЕ, ДАЖЕ ЕСЛИ КАРРЕНТТАСК
        }

        for (FutureTask<ReactQueryTaskHolder> futureTask : futureTasks) {
            try {
                results.add(futureTask.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
            }
        }
        return results;
    }

    private void recursiveTaskCreation(ReacTask task, List<FutureTask<ReactQueryTaskHolder>> futures) {
        addReferenceLinks(task);
        if (!task.getInnerObjects().isEmpty()) {
            for (ReacTask reacTask : task.getInnerObjects()) {
                recursiveTaskCreation(reacTask, futures);
            }
        }
        futures.add(new FutureTask<>(new TaskPreparator(task)));
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

    //ВНУТРЕННИЙ КЛАСС ПО СОЗДАНИЮ REACT QUERY TASK HOLDER
    private class TaskPreparator implements Callable<ReactQueryTaskHolder> {
        private ReacTask currentNode;

        TaskPreparator(ReacTask currentNode) {
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
            for (ConditionExecutor executor : executors) {
                if (executor.getExecutorClass().equals(WhereAppendingConditionExecutor.class)) {
                    afterWhereExecutor = (WhereAppendingConditionExecutor) executor;
                }
            }
            return builder.getQueryForEntity(currentNodeVariables, currentNode, isSearchById, orderingParameter, ascend, afterWhereExecutor);
        }
    }
}
