package com.angkorteam.mbaas.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLPropertiesConfiguration;

import java.io.File;

/**
 * Created by Khauv Socheat on 2/4/2016.
 */
public class Constants {

    public static final String KEY = "mbaas.properties.xml";

    public static final String WICKET = "wicket";

    public static final String TEMP_JDBC_DATABASE = "temp.jdbc.database";

    public static final String APP_JDBC_DRIVER = "app.jdbc.driver";
    public static final String APP_JDBC_URL = "app.jdbc.url";
    public static final String APP_JDBC_USERNAME = "app.jdbc.username";
    public static final String APP_JDBC_PASSWORD = "app.jdbc.password";
    public static final String APP_JDBC_DATABASE = "app.jdbc.database";

    public static final String TEST_JDBC_DRIVER = "test.jdbc.driver";
    public static final String TEST_JDBC_URL = "test.jdbc.url";
    public static final String TEST_JDBC_USERNAME = "test.jdbc.username";
    public static final String TEST_JDBC_PASSWORD = "test.jdbc.password";
    public static final String TEST_JDBC_DATABASE = "test.jdbc.database";

    public static final String JDBC_COLUMN_EXTRA = "jdbc.column.extra";
    public static final String JDBC_COLUMN_OPTIMISTIC = "jdbc.column.optimistic";
    public static final String JDBC_COLUMN_DELETED = "jdbc.column.deleted";
    public static final String JDBC_COLUMN_OWNER_USER_ID = "jdbc.column.owner.user.id";
    public static final String JDBC_COLUMN_DATE_CREATED = "jdbc.column.date.created";

    public static final String ROLE_ADMINISTRATOR = "role.administrator";
    public static final String ROLE_ADMINISTRATOR_DESCRIPTION = "role.administrator.description";
    public static final String ROLE_BACKOFFICE = "role.backoffice";
    public static final String ROLE_BACKOFFICE_DESCRIPTION = "role.backoffice.description";
    public static final String ROLE_REGISTERED = "role.registered";
    public static final String ROLE_REGISTERED_DESCRIPTION = "role.registered.description";
    public static final String ROLE_ANONYMOUS = "role.anonymous";
    public static final String ROLE_ANONYMOUS_DESCRIPTION = "role.anonymous.description";
    public static final String ROLE_OAUTH2_AUTHORIZATION = "role.oauth2.authorization";
    public static final String ROLE_OAUTH2_AUTHORIZATION_DESCRIPTION = "role.oauth2.authorization.description";
    public static final String ROLE_OAUTH2_IMPLICIT = "role.oauth2.implicit";
    public static final String ROLE_OAUTH2_IMPLICIT_DESCRIPTION = "role.oauth2.implicit.description";
    public static final String ROLE_OAUTH2_PASSWORD = "role.oauth2.password";
    public static final String ROLE_OAUTH2_PASSWORD_DESCRIPTION = "role.oauth2.password.description";
    public static final String ROLE_OAUTH2_CLIENT = "role.oauth2.client";
    public static final String ROLE_OAUTH2_CLIENT_DESCRIPTION = "role.oauth2.client.description";

    public static final String USER_ADMIN = "user.admin";
    public static final String USER_ADMIN_ROLE = "user.admin.role";
    public static final String USER_ADMIN_PASSWORD = "user.admin.password";
    public static final String USER_MBAAS = "user.mbaas";
    public static final String USER_MBAAS_ROLE = "user.mbaas.role";
    public static final String USER_MBAAS_PASSWORD = "user.mbaas.password";
    public static final String USER_INTERNAL_ADMIN = "user.internal_admin";
    public static final String USER_INTERNAL_ADMIN_ROLE = "user.internal_admin.role";
    public static final String USER_INTERNAL_ADMIN_PASSWORD = "user.internal_admin.password";

    public static final String PATTERN_DATETIME = "pattern.datetime";
    public static final String PATTERN_TIME = "pattern.time";
    public static final String PATTERN_DATE = "pattern.date";
    public static final String PATTERN_FOLDER = "pattern.folder";
    public static final String PATTERN_ATTRIBUTE_NAME = "pattern.attribute.name";
    public static final String PATTERN_COLLECTION_NAME = "pattern.collection.name";
    public static final String PATTERN_OAUTH_ROLE_NAME = "pattern.oauth.role.name";
    public static final String PATTERN_PATH = "pattern.path";
    public static final String PATTERN_QUERY_PARAMETER_NAME = "pattern.query.parameter.name";

    public static final String ENCRYPTION_PASSWORD = "encryption.password";
    public static final String ENCRYPTION_OUTPUT = "encryption.output";

    public static final String RESOURCE_REPO = "resource.repo";

    public static final String AUTHORIZATION_TIME_TO_LIVE = "authorization.time_to_live";
    public static final String ACCESS_TOKEN_TIME_TO_LIVE = "access_token.time_to_live";

    public static final String MAIL_SERVER = "mail.server";
    public static final String MAIL_PORT = "mail.port";
    public static final String MAIL_LOGIN = "mail.login";
    public static final String MAIL_PASSWORD = "mail.password";
    public static final String MAIL_PROTOCOL = "mail.protocol";
    public static final String MAIL_FROM = "mail.from";

    public static final String APP_VERSION = "app.version";

    private static XMLPropertiesConfiguration configuration;
    private static long lastModified = -1;

    public static XMLPropertiesConfiguration getXmlPropertiesConfiguration() {
        File home = new File(System.getProperty("user.home"));
        try {
            File file = new File(home, ".xml/" + Constants.KEY);
            if (configuration == null) {
                configuration = new XMLPropertiesConfiguration(file);
            } else {
                if (lastModified != file.lastModified()) {
                    configuration = new XMLPropertiesConfiguration(file);
                }
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
