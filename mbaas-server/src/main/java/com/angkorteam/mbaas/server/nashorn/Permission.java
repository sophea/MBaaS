package com.angkorteam.mbaas.server.nashorn;

import com.angkorteam.mbaas.configuration.Constants;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.*;
import com.angkorteam.mbaas.model.entity.tables.records.*;
import org.apache.commons.configuration.XMLPropertiesConfiguration;
import org.jooq.DSLContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.script.ScriptException;

/**
 * Created by socheat on 3/18/16.
 */
public class Permission {

    private final DSLContext context;

    private final JdbcTemplate jdbcTemplate;

    private final MBaaS mbaas;

    public Permission(MBaaS mbaas, DSLContext context, JdbcTemplate jdbcTemplate) {
        this.context = context;
        this.jdbcTemplate = jdbcTemplate;
        this.mbaas = mbaas;
    }

    public boolean isCollectionOwner(String collectionName) throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        UserTable userTable = Tables.USER.as("userTable");
        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        CollectionRecord collectionRecord = context.select(collectionTable.fields()).from(collectionTable).where(collectionTable.NAME.eq(collectionName)).fetchOneInto(collectionTable);
        if (collectionRecord == null) {
            return false;
        }
        if (collectionRecord.getOwnerUserId().equals(userRecord.getUserId())) {
            return true;
        }
        return false;
    }

    public boolean isDocumentOwner(String collectionName, String documentId) throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        UserTable userTable = Tables.USER.as("userTable");
        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        CollectionRecord collectionRecord = context.select(collectionTable.fields()).from(collectionTable).where(collectionTable.NAME.eq(collectionName)).fetchOneInto(collectionTable);
        if (collectionRecord == null) {
            return false;
        }
        XMLPropertiesConfiguration configuration = Constants.getXmlPropertiesConfiguration();

        String ownerUserId = jdbcTemplate.queryForObject("SELECT " + configuration.getString(Constants.JDBC_COLUMN_OWNER_USER_ID) + " FROM `" + collectionName + "` WHERE " + collectionName + "_id = ?", String.class, documentId);
        if (ownerUserId == null) {
            return false;
        }

        if (userRecord.getUserId().equals(ownerUserId)) {
            return true;
        }
        return false;
    }

    public boolean isAdministratorUser() throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);
        if (roleRecord == null) {
            return false;
        }
        XMLPropertiesConfiguration configuration = Constants.getXmlPropertiesConfiguration();
        if (roleRecord.getName().equals(configuration.getString(Constants.ROLE_ADMINISTRATOR))) {
            return true;
        }
        return false;
    }

    public boolean isBackOfficeUser() throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);
        if (roleRecord == null) {
            return false;
        }
        XMLPropertiesConfiguration configuration = Constants.getXmlPropertiesConfiguration();
        if (roleRecord.getName().equals(configuration.getString(Constants.ROLE_BACKOFFICE))) {
            return true;
        }
        return false;
    }

    public boolean isRegisteredUser() throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);
        if (roleRecord == null) {
            return false;
        }
        XMLPropertiesConfiguration configuration = Constants.getXmlPropertiesConfiguration();
        if (roleRecord.getName().equals(configuration.getString(Constants.ROLE_REGISTERED))) {
            return true;
        }
        return false;
    }

    public boolean hasRole(String roleName) throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }
        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);
        if (roleRecord == null) {
            return false;
        }
        if (roleRecord.getName().equals(roleName)) {
            return true;
        }
        return false;
    }

    public boolean hasDocumentPermission(String collectionName, String documentId, int action) throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");
        DocumentUserPrivacyTable documentUserPrivacyTable = Tables.DOCUMENT_USER_PRIVACY.as("documentUserPrivacyTable");
        DocumentRolePrivacyTable documentRolePrivacyTable = Tables.DOCUMENT_ROLE_PRIVACY.as("documentRolePrivacyTable");

        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }

        CollectionRecord collectionRecord = context.select(collectionTable.fields()).from(collectionTable).where(collectionTable.NAME.eq(collectionName)).fetchOneInto(collectionTable);
        if (collectionRecord == null) {
            return false;
        }

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM `" + collectionRecord.getName() + "` WHERE " + collectionRecord.getName() + "_id = ?", Integer.class, documentId);
        if (count <= 0) {
            return false;
        }

        DocumentUserPrivacyRecord documentUserPrivacyRecord = context.select(documentUserPrivacyTable.fields())
                .from(documentUserPrivacyTable)
                .where(documentUserPrivacyTable.COLLECTION_ID.eq(collectionRecord.getCollectionId()))
                .and(documentUserPrivacyTable.USER_ID.eq(userRecord.getUserId()))
                .and(documentUserPrivacyTable.DOCUMENT_ID.eq(documentId))
                .fetchOneInto(documentUserPrivacyTable);

        if (documentUserPrivacyRecord != null && (documentUserPrivacyRecord.getPermisson() & action) == action) {
            return true;
        }

        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);

        DocumentRolePrivacyRecord documentRolePrivacyRecord = context.select(documentRolePrivacyTable.fields())
                .from(documentRolePrivacyTable)
                .where(documentRolePrivacyTable.COLLECTION_ID.eq(collectionRecord.getCollectionId()))
                .and(documentRolePrivacyTable.ROLE_ID.eq(roleRecord.getRoleId()))
                .and(documentRolePrivacyTable.DOCUMENT_ID.eq(documentId))
                .fetchOneInto(documentRolePrivacyTable);

        if (documentRolePrivacyRecord != null && (documentRolePrivacyRecord.getPermisson() & action) == action) {
            return true;
        }

        return false;
    }

    public boolean hasCollectionPermission(String collection, int action) throws ScriptException {
        if (this.mbaas.isAuthenticated()) {
            return false;
        }
        RoleTable roleTable = Tables.ROLE.as("roleTable");
        UserTable userTable = Tables.USER.as("userTable");
        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");
        CollectionUserPrivacyTable tableUserPrivacyTable = Tables.COLLECTION_USER_PRIVACY.as("tableUserPrivacyTable");
        CollectionRolePrivacyTable tableRolePrivacyTable = Tables.COLLECTION_ROLE_PRIVACY.as("tableRolePrivacyTable");

        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(this.mbaas.getCurrentUserId())).fetchOneInto(userTable);
        if (userRecord == null) {
            return false;
        }

        CollectionRecord collectionRecord = context.select(collectionTable.fields()).from(collectionTable).where(collectionTable.NAME.eq(collection)).fetchOneInto(collectionTable);
        if (collectionRecord == null) {
            return false;
        }

        CollectionUserPrivacyRecord collectionUserPrivacyRecord = context.select(tableUserPrivacyTable.fields())
                .from(tableUserPrivacyTable)
                .where(tableUserPrivacyTable.COLLECTION_ID.eq(collectionRecord.getCollectionId()))
                .and(tableUserPrivacyTable.USER_ID.eq(userRecord.getUserId()))
                .fetchOneInto(tableUserPrivacyTable);

        if (collectionUserPrivacyRecord != null && (collectionUserPrivacyRecord.getPermisson() & action) == action) {
            return true;
        }

        RoleRecord roleRecord = context.select(roleTable.fields()).from(roleTable).where(roleTable.ROLE_ID.eq(userRecord.getRoleId())).fetchOneInto(roleTable);

        CollectionRolePrivacyRecord collectionRolePrivacyRecord = context.select(tableRolePrivacyTable.fields())
                .from(tableRolePrivacyTable)
                .where(tableRolePrivacyTable.COLLECTION_ID.eq(collectionRecord.getCollectionId()))
                .and(tableRolePrivacyTable.ROLE_ID.eq(roleRecord.getRoleId()))
                .fetchOneInto(tableRolePrivacyTable);

        if (collectionRolePrivacyRecord != null && (collectionRolePrivacyRecord.getPermisson() & action) == action) {
            return true;
        }

        return false;
    }
}
