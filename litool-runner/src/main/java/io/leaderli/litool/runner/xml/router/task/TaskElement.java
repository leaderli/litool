package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.xml.SaxBeanWithID;

public abstract class TaskElement<R extends SaxBean & ElementExecutor<R, T>, T extends BaseElementExecutor<R>> extends SaxBeanWithID implements ElementExecutor<R, T> {

    protected TaskList taskList = new TaskList();

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T executor() {
        Class<?> executorClass = ReflectUtil.getGenericSuperclassType(getClass(), TaskElement.class, 1).get();
        return (T) ReflectUtil.newInstance(executorClass, this).get();
    }
}
