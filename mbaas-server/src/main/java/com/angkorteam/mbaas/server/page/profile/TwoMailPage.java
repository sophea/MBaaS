package com.angkorteam.mbaas.server.page.profile;

import com.angkorteam.framework.extension.wicket.feedback.TextFeedbackPanel;
import com.angkorteam.framework.extension.wicket.html.form.Form;
import com.angkorteam.framework.extension.wicket.markup.html.form.Button;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.UserTable;
import com.angkorteam.mbaas.model.entity.tables.records.UserRecord;
import com.angkorteam.mbaas.plain.enums.AuthenticationEnum;
import com.angkorteam.mbaas.server.validator.UserEmailAddressValidator;
import com.angkorteam.mbaas.server.wicket.MasterPage;
import com.angkorteam.mbaas.server.wicket.Mount;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.jooq.DSLContext;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by socheat on 4/3/16.
 */
@AuthorizeInstantiation({"administrator", "backoffice", "registered"})
@Mount("/profile/two/mail")
public class TwoMailPage extends MasterPage {

    private String emailAddress;
    private TextField<String> emailAddressField;
    private TextFeedbackPanel emailAddressFeedback;

    private Form<Void> form;

    private Button disableButton;
    private Button okayButton;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        DSLContext context = getDSLContext();
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(getSession().getUserId())).fetchOneInto(userTable);

        this.form = new Form<>("form");
        add(this.form);

        this.emailAddress = userRecord.getEmailAddress();
        this.emailAddressField = new TextField<>("emailAddressField", new PropertyModel<>(this, "emailAddress"));
        this.emailAddressField.setRequired(true);
        this.emailAddressField.setEnabled(!AuthenticationEnum.TwoEMail.getLiteral().equals(userRecord.getAuthentication()));
        this.emailAddressField.add(EmailAddressValidator.getInstance());
        this.emailAddressField.add(new UserEmailAddressValidator(getSession().getUserId()));
        this.form.add(this.emailAddressField);
        this.emailAddressFeedback = new TextFeedbackPanel("emailAddressFeedback", this.emailAddressField);
        this.form.add(this.emailAddressFeedback);

        this.disableButton = new Button("disableButton");
        this.disableButton.setOnSubmit(this::disableButtonOnSubmit);
        this.disableButton.setVisible(AuthenticationEnum.TwoEMail.getLiteral().equals(userRecord.getAuthentication()));
        this.form.add(this.disableButton);

        this.okayButton = new Button("okayButton");
        this.okayButton.setOnSubmit(this::okayButtonOnSubmit);
        this.okayButton.setVisible(!AuthenticationEnum.TwoEMail.getLiteral().equals(userRecord.getAuthentication()));
        this.form.add(this.okayButton);
    }

    private void okayButtonOnSubmit(Button button) {
        MailSender mailSender = getMailSender();
        SimpleMailMessage message = new SimpleMailMessage();
        String verify = RandomStringUtils.randomNumeric(6);
        message.setSubject("One Time Password");
        message.setText(verify);
        message.setTo(emailAddress);
        try {
            mailSender.send(message);
            TwoVerifyPage verifyPage = new TwoVerifyPage(AuthenticationEnum.TwoEMail.getLiteral(), Integer.valueOf(verify), this.emailAddress);
            setResponsePage(verifyPage);
        } catch (MailAuthenticationException e) {
        }
    }

    private void disableButtonOnSubmit(Button button) {
        DSLContext context = getDSLContext();
        UserTable userTable = Tables.USER.as("userTable");
        UserRecord userRecord = context.select(userTable.fields()).from(userTable).where(userTable.USER_ID.eq(getSession().getUserId())).fetchOneInto(userTable);
        userRecord.setAuthentication(AuthenticationEnum.None.getLiteral());
        userRecord.update();
        setResponsePage(InformationPage.class);
    }

}
