package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.runner.executor.UnitElementExecutor;
import io.leaderli.litool.runner.xml.router.task.AssignElement;
import io.leaderli.litool.runner.xml.router.task.IfElement;
import io.leaderli.litool.runner.xml.router.task.TaskElement;

public class UnitElement extends TaskElement<UnitElement, UnitElementExecutor> {

    private String label;

    public void addIf(IfElement ifElement) {
        taskList.add(ifElement);
    }

    public void addAssign(AssignElement assignElement) {
        taskList.add(assignElement);
    }

    @Override
    public String name() {
        return "unit";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
