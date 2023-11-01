package cc.robotdreams.base;

import cc.robotdreams.utils.Wait;
import com.codeborne.selenide.SelenideElement;

import java.time.Instant;

abstract public class BaseSelenidePage extends BasePage {
    public boolean isPageLoaded() {
        return this.isPageLoaded(5);
    }

    public boolean isPageLoaded(int timeoutSec) {
        Boolean customConfirm = this.customConfirm();
        if (customConfirm != null)
            return customConfirm;

        boolean result = false;

        long timeout = Instant.now().getEpochSecond() + timeoutSec;
        while (timeout > Instant.now().getEpochSecond()) {
            result = this.readyElement().exists();
            if (result)
                break;
            Wait.sleep(500);
        }

        return result;
    }

    public void confirm() {
        // Confirm that page is loaded
        if (!this.isPageLoaded(10))
            throw new RuntimeException("Could not confirm that page is loaded: " + this.getClass().getSimpleName());
    }

    protected Boolean customConfirm() {
        return null;
    }

    abstract protected SelenideElement readyElement();
}
