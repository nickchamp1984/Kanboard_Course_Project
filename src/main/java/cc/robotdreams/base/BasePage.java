package cc.robotdreams.base;

import cc.robotdreams.Session;
import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver wd() {
        return Session.get().webdriver();
    }

}
