package cc.robotdreams;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Config {
    static final private List<Param> _paramsList = new LinkedList<>();

    static final public Param HTTP_BASE_PROTO   = new Param("http.base.protocol",   "http",      true);
    static final public Param HTTP_BASE_HOST    = new Param("http.base.host",       "localhost", true);
    static final public Param HTTP_BASE_PORT    = new Param("http.base.port",       "8001",      true);

    static final public Param API_AUTH_USER_PASSWORD    = new Param("api.auth.user.password");
    static final public Param API_AUTH_USER_USERNAME    = new Param("api.auth.user.username");
    static final public Param API_AUTH_APP_PASSWORD     = new Param("api.auth.app.password");
    static final public Param API_AUTH_APP_USERNAME     = new Param("api.auth.app.username");
    static final public Param SITE_USER_EMAIL    = new Param("site.user.email");
    static final public Param SITE_USER_PASSWORD = new Param("site.user.password");

    static final public Param MYSQL_HOST        = new Param("mysql.host",           null,        true);
    static final public Param MYSQL_PORT        = new Param("mysql.port",           "3306",      true);
    static final public Param MYSQL_DATABASE    = new Param("mysql.database",       null,        true);
    static final public Param MYSQL_USERNAME    = new Param("mysql.username",       null,        true);
    static final public Param MYSQL_PASSWORD    = new Param("mysql.password",       null,        true);

    static final public Param TESTBED           = new Param("testbed",              "local",     true);

    static final public Param WEB_BROWSER           = new Param("web.browser",   "chrome",       true);
    static final public Param WEB_BROWSER_NO_GUI    = new Param("web.browser.no.gui",   "false", true);
    static final public Param BROWSER_WIN_WIDTH     = new Param("web.browser.win.width",   "1024", true);
    static final public Param BROWSER_WIN_HEIGHT    = new Param("web.browser.win.height",  "786",  true);

    static final public Param API_BASE_PROTO    = new Param("api.base.protocol",    "http",     true);
    static final public Param API_BASE_HOST     = new Param("api.base.host",        "localhost",true);
    static final public Param API_BASE_PORT     = new Param("api.base.port",        "80",       true);
    static final public Param API_USER_NAME     = new Param("api.user.name",        null,       true);
    static final public Param API_USER_PASSWORD = new Param("api.user.password",    null,       true);


    static final public Param SELENIUM_GRID_CHROME_HOST = new Param("selenium.grid.chrome.host",    null, true);
    static final public Param SELENIUM_GRID_CHROME_PORT = new Param("selenium.grid.chrome.port",    null, true);

    static final public Param SELENIUM_GRID_FIREFOX_HOST = new Param("selenium.grid.firefox.host",    null, true);
    static final public Param SELENIUM_GRID_FIREFOX_PORT = new Param("selenium.grid.firefox.port",    null, true);


    static {
        InitErrors.showErrors();
    }

    final static public class Param
    {
        final public String name;
        final public String value;

        public Param(String name) {
            this(name, null, false);
        }

        public Param(String name, String defaultValue) {
            this(name, defaultValue, false);
        }

        public Param(String name, String defaultValue, boolean isSys) {
            this.name = name;
            String tmpVal = null;
            if (isSys) {
                tmpVal = System.getProperty(name);
                if (tmpVal == null)
                    tmpVal = System.getenv(name);
            }
            if (tmpVal == null)
                tmpVal = getEnvProperties().getProperty(name);
            if (tmpVal == null)
                tmpVal = defaultValue;
            if (tmpVal == null)
                InitErrors.addError("Parameter value is not found. Parameter " + name);
            value = tmpVal;
            Config._paramsList.add(this);
        }

        public boolean isTrue() {
            return "true".equalsIgnoreCase(value);
        }

        public Integer asInt() {
            return Integer.parseInt(value);
        }
    }

    static final private class InitErrors
    {
        static final private List<String> _errorsList = new ArrayList<>();

        static private void addError(String errorMessage) {
            _errorsList.add(errorMessage);
        }

        static private void showErrors() {
            if (_errorsList.size() > 0) {
                throw new RuntimeException("\n" + String.join("\n", _errorsList));
            }
        }
    }

    static private Properties envProperties;

    static private String environmentName;

    static private Properties getEnvProperties() {
        if (envProperties == null) {
            envProperties = new Properties();
            environmentName = System.getProperty("env", "dev");
            envProperties.putAll(getResourceProperties("common.properties"));
            envProperties.putAll(getResourceProperties("env/" + environmentName + ".properties"));
            //envProperties.putAll(getResourceProperties("allure.properties"));
        }
        return envProperties;
    }

    static public String getEnvironmentName() {
        return environmentName;
    }

    static private Properties getResourceProperties(String resourceFilePath) {
        Properties  props   = new Properties();
        InputStream iStream = null;
        try {
            iStream = Config.class.getClassLoader().getResourceAsStream(resourceFilePath);
            if (iStream == null)
                throw new RuntimeException("Resource file not found " + resourceFilePath);
            props.load(iStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not read resource properties file " + resourceFilePath, e);
        } finally {
            try {
                if (iStream != null)
                    iStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Could not close input stream", e);
            }
        }
        return props;
    }

    static public URI getSiteBaseURL() {
        String url = String.format(
                "%s://%s:%s",
                HTTP_BASE_PROTO.value,
                HTTP_BASE_HOST.value,
                HTTP_BASE_PORT.value
        );
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL has wrong format: " + url, e);
        }
    }

    static public URI getApiBaseURL() {
        String url = String.format(
                "%s://%s:%s",
                API_BASE_PROTO.value,
                API_BASE_HOST.value,
                API_BASE_PORT.value
        );
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URL has wrong format: " + url, e);
        }
    }
}
