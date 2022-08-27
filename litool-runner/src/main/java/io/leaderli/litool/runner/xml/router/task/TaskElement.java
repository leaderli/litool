package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;

public abstract class TaskElement<R extends SaxBean & ElementExecutor<R, T>, T extends BaseElementExecutor<R>> extends BaseElement<R, T> {

protected TaskList taskList = new TaskList();

protected TaskElement(String tag) {
    super(tag);
}

public TaskList getTaskList() {
    return taskList;
}

public void setTaskList(TaskList taskList) {
    this.taskList = taskList;
}


}
