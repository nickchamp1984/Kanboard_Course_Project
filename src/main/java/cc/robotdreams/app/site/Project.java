package cc.robotdreams.app.site;

import cc.robotdreams.Config;
import cc.robotdreams.base.BaseSelenidePage;
import cc.robotdreams.utils.Wait;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class Project extends BaseSelenidePage {
    final public SelenideElement addTaskToBacklogBtn = Selenide.$x("(//a[contains(@href, '/task/create/swimlane/')])[1]");
    final public SelenideElement taskTitleField = Selenide.$x("//*[@id='form-title']");
    final public SelenideElement signInBtn  = Selenide.$x("//*[@Class='btn btn-blue']");
    final public SelenideElement taskDraggableItem =
            Selenide.$x("//*[contains(@class, 'task-board draggable-item')]");
    final public SelenideElement taskDraggableItemTitle = Selenide.$x("//*[@class='task-board-title']");
    final public SelenideElement taskDraggableItemID =
            Selenide.$x("(//*[contains(@class, 'task-board draggable-item')]//a/strong)[1]");
    final public SelenideElement errorMsg = Selenide.$x("//*[@class='form-errors']");
    final public SelenideElement modalOverlay = Selenide.$x("//*[@id='modal-overlay']");
    final public SelenideElement closeThisTaskItem = Selenide.$x("//*[@id='dropdown']//a[contains(@href,'close')]");
    final public SelenideElement addCommentItem = Selenide.$x("//*[@id='dropdown']//a[contains(@href,'comment')]");
    final public SelenideElement modalConfirmBtn = Selenide.$x("//*[@id='modal-confirm-button']");
    final public SelenideElement modalTextEditor = Selenide.$x("//*[@name='comment']");
    final public SelenideElement modalSaveBtn = Selenide.$x("//*[@type='submit']");


    @Override
    protected SelenideElement readyElement() {
        return addTaskToBacklogBtn;
    }

    public void addTaskToBacklog(String taskTitle) {
        addTaskToBacklogBtn.click();
        Wait.sleep(500);
        if (modalOverlay.exists()) {
            taskTitleField.val(taskTitle);
            signInBtn.click();
        } else {
            throw new RuntimeException("Element modalOverlay cannot be located");
        }
    }

    public void openContextMenu(){
        taskDraggableItemID.click();
    }

    public void closeTask() {
        closeThisTaskItem.click();
    }

    public void confirmDeletion() {
        modalConfirmBtn.click();
    }

    public void addComment(String comment) {
        addCommentItem.click();
        Wait.sleep(500);
        if (modalTextEditor.exists()) {
            modalTextEditor.val(comment);
            modalSaveBtn.click();
        }
        else {
            throw new RuntimeException("Element modalTextEditor cannot be located");
        }
    }

    public TaskPage goToTaskPage(Integer taskID) {
        wd().get(String.format("%s://%s/task/%s",
                Config.HTTP_BASE_PROTO.value,
                Config.HTTP_BASE_HOST.value,
                taskID));

        return new TaskPage();
    }
}
