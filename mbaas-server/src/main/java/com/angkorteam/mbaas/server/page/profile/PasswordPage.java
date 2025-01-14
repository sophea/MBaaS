package com.angkorteam.mbaas.server.page.profile;

import com.angkorteam.framework.extension.wicket.feedback.TextFeedbackPanel;
import com.angkorteam.framework.extension.wicket.html.form.Form;
import com.angkorteam.framework.extension.wicket.markup.html.form.Button;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.UserTable;
import com.angkorteam.mbaas.server.validator.UserPasswordValidator;
import com.angkorteam.mbaas.server.wicket.JooqUtils;
import com.angkorteam.mbaas.server.wicket.MasterPage;
import com.angkorteam.mbaas.server.wicket.Mount;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.PropertyModel;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * Created by socheat on 4/2/16.
 */
@AuthorizeInstantiation({"administrator", "backoffice", "registered"})
@Mount("/profile/password")
public class PasswordPage extends MasterPage {

    private String currentPassword;
    private TextField<String> currentPasswordField;
    private TextFeedbackPanel currentPasswordFeedback;

    private String newPassword;
    private TextField<String> newPasswordField;
    private TextFeedbackPanel newPasswordFeedback;

    private String retypeNewPassword;
    private TextField<String> retypeNewPasswordField;
    private TextFeedbackPanel retypeNewPasswordFeedback;

    private Form<Void> form;

    private Button updateButton;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        this.form = new Form<>("form");
        add(this.form);

        this.currentPasswordField = new PasswordTextField("currentPasswordField", new PropertyModel<>(this, "currentPassword"));
        this.currentPasswordField.setRequired(true);
        this.currentPasswordField.setLabel(JooqUtils.lookup("currentPassword", this));
        this.currentPasswordField.add(new UserPasswordValidator(getSession().getUserId()));
        this.form.add(this.currentPasswordField);
        this.currentPasswordFeedback = new TextFeedbackPanel("currentPasswordFeedback", this.currentPasswordField);
        this.form.add(this.currentPasswordFeedback);

        this.newPasswordField = new PasswordTextField("newPasswordField", new PropertyModel<>(this, "newPassword"));
        this.newPasswordField.setRequired(true);
        this.newPasswordField.setLabel(JooqUtils.lookup("newPassword", this));
        this.form.add(this.newPasswordField);
        this.newPasswordFeedback = new TextFeedbackPanel("newPasswordFeedback", this.newPasswordField);
        this.form.add(this.newPasswordFeedback);

        this.retypeNewPasswordField = new PasswordTextField("retypeNewPasswordField", new PropertyModel<>(this, "retypeNewPassword"));
        this.retypeNewPasswordField.setRequired(true);
        this.retypeNewPasswordField.setLabel(JooqUtils.lookup("retypeNewPassword", this));
        this.form.add(this.retypeNewPasswordField);
        this.retypeNewPasswordFeedback = new TextFeedbackPanel("retypeNewPasswordFeedback", this.retypeNewPasswordField);
        this.form.add(this.retypeNewPasswordFeedback);

        this.form.add(new EqualPasswordInputValidator(this.newPasswordField, this.retypeNewPasswordField));

        this.updateButton = new Button("updateButton");
        this.updateButton.setOnSubmit(this::updateButtonOnSubmit);
        this.form.add(this.updateButton);
    }

    private void updateButtonOnSubmit(Button button) {
        DSLContext context = getDSLContext();
        UserTable userTable = Tables.USER.as("userTable");
        context.update(userTable).set(userTable.PASSWORD, DSL.md5(this.newPassword)).where(userTable.USER_ID.eq(getSession().getUserId())).execute();
    }

}
