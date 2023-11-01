package cc.robotdreams.app.site;

import cc.robotdreams.base.BaseSelenidePage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

public class LoginPage extends BaseSelenidePage {
    final public SelenideElement username   = Selenide.$x("//*[@id='form-username']");
    final public SelenideElement password   = Selenide.$x("//*[@id='form-password']");
    final public SelenideElement signInBtn  = Selenide.$x("//*[@Class='btn btn-blue']");

    @Override
    protected SelenideElement readyElement() {
        return this.signInBtn;
    }

    public Dashboard login(String username, String password) {
        this.username.val(username);
        this.password.val(password);
        this.signInBtn.click();

        return new Dashboard();
    }
}
