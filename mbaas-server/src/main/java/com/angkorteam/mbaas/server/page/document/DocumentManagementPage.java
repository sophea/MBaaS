package com.angkorteam.mbaas.server.page.document;

import com.angkorteam.framework.extension.wicket.feedback.TextFeedbackPanel;
import com.angkorteam.framework.extension.wicket.table.DataTable;
import com.angkorteam.framework.extension.wicket.table.DefaultDataTable;
import com.angkorteam.framework.extension.wicket.table.filter.*;
import com.angkorteam.mbaas.configuration.Constants;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.AttributeTable;
import com.angkorteam.mbaas.model.entity.tables.CollectionTable;
import com.angkorteam.mbaas.model.entity.tables.pojos.CollectionPojo;
import com.angkorteam.mbaas.model.entity.tables.records.AttributeRecord;
import com.angkorteam.mbaas.plain.enums.AttributeTypeEnum;
import com.angkorteam.mbaas.server.function.DocumentFunction;
import com.angkorteam.mbaas.server.page.attribute.AttributeManagementPage;
import com.angkorteam.mbaas.server.provider.DocumentProvider;
import com.angkorteam.mbaas.server.wicket.JooqUtils;
import com.angkorteam.mbaas.server.wicket.MasterPage;
import com.angkorteam.mbaas.server.wicket.Mount;
import org.apache.commons.configuration.XMLPropertiesConfiguration;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jooq.DSLContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by socheat on 3/3/16.
 */
@AuthorizeInstantiation("administrator")
@Mount("/document/management")
public class DocumentManagementPage extends MasterPage implements ActionFilteredJooqColumn.Event {

    private CollectionPojo collection;
    private DropDownChoice<CollectionPojo> collectionField;
    private TextFeedbackPanel collectionFeedback;

    private DocumentProvider provider;

    @Override
    public String getPageHeader() {
        return "Document Management :: " + collection.getName();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        DSLContext context = getDSLContext();
        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");

        String collectionId = getPageParameters().get("collectionId").toString();

        this.collection = context.select(collectionTable.fields()).from(collectionTable).where(collectionTable.COLLECTION_ID.eq(collectionId)).fetchOneInto(CollectionPojo.class);

        AttributeTable attributeTable = Tables.ATTRIBUTE.as("attributeTable");
        List<AttributeRecord> attributeRecords = context.select(attributeTable.fields())
                .from(attributeTable)
                .where(attributeTable.COLLECTION_ID.eq(collection.getCollectionId()))
                .fetchInto(attributeTable);

        this.provider = new DocumentProvider(this.collection.getCollectionId());

        FilterForm<Map<String, String>> filterForm = new FilterForm<>("filter-form", provider);
        add(filterForm);

        List<IColumn<Map<String, Object>, String>> columns = new ArrayList<>();
        columns.add(new TextFilteredJooqColumn(String.class, JooqUtils.lookup(collection.getName() + "_id", this), collection.getName() + "_id", provider));
        XMLPropertiesConfiguration configuration = Constants.getXmlPropertiesConfiguration();
        String jdbcColumnOwnerUserId = configuration.getString(Constants.JDBC_COLUMN_OWNER_USER_ID);
        for (AttributeRecord attributeRecord : attributeRecords) {
            if (attributeRecord.getName().equals(jdbcColumnOwnerUserId) || attributeRecord.getName().equals(collection.getName() + "_id")) {
                continue;
            }
            if (AttributeTypeEnum.Boolean.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Boolean.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Byte.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Byte.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Short.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Short.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Integer.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Integer.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Long.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Long.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Float.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Float.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Double.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Double.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Character.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(Character.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.String.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TextFilteredJooqColumn(String.class, JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Time.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new TimeFilteredJooqColumn(JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.Date.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new DateFilteredJooqColumn(JooqUtils.lookup(column, this), column, provider));
            } else if (AttributeTypeEnum.DateTime.getLiteral().equals(attributeRecord.getJavaType())) {
                String column = attributeRecord.getName();
                columns.add(new DateTimeFilteredJooqColumn(JooqUtils.lookup(column, this), column, provider));
            }
        }

        columns.add(new TextFilteredJooqColumn(String.class, JooqUtils.lookup("owner", this), "owner", provider));
        columns.add(new ActionFilteredJooqColumn(JooqUtils.lookup("action", this), JooqUtils.lookup("filter", this), JooqUtils.lookup("clear", this), this, "Edit", "Delete"));

        DataTable<Map<String, Object>, String> dataTable = new DefaultDataTable<>("table", columns, provider, 20);
        dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm));
        filterForm.add(dataTable);

        PageParameters parameters = new PageParameters();
        parameters.add("collectionId", collection.getCollectionId());
        BookmarkablePageLink<Void> newDocumentLink = new BookmarkablePageLink<>("newDocumentLink", DocumentCreatePage.class, parameters);
        add(newDocumentLink);

        BookmarkablePageLink<Void> refreshLink = new BookmarkablePageLink<>("refreshLink", DocumentManagementPage.class, getPageParameters());
        add(refreshLink);

        BookmarkablePageLink<Void> attributeLink = new BookmarkablePageLink<>("attributeLink", AttributeManagementPage.class, getPageParameters());
        add(attributeLink);


    }

    @Override
    public void onClickEventLink(String link, Map<String, Object> object) {
        if ("Delete".equals(link)) {
            String documentId = (String) object.get(this.collection.getName() + "_id");
            DocumentFunction.deleteDocument(getDSLContext(), getJdbcTemplate(), this.collection.getName(), documentId);
        }
        if ("Edit".equals(link)) {
            String collectionId = this.collection.getCollectionId();
            String documentId = (String) object.get(this.collection.getName() + "_id");
            PageParameters parameters = new PageParameters();
            parameters.add("collectionId", collectionId);
            parameters.add("documentId", documentId);
            setResponsePage(DocumentModifyPage.class, parameters);
        }
    }

    @Override
    public boolean isClickableEventLink(String link, Map<String, Object> object) {
        if ("Edit".equals(link)) {
            return true;
        }
        if ("Delete".equals(link)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isVisibleEventLink(String link, Map<String, Object> object) {
        if ("Edit".equals(link)) {
            return true;
        }
        if ("Delete".equals(link)) {
            return true;
        }
        return false;
    }

    @Override
    public String onCSSLink(String link, Map<String, Object> object) {
        if ("Delete".equals(link)) {
            return "btn-xs btn-danger";
        }
        if ("Edit".equals(link)) {
            return "btn-xs btn-info";
        }
        return "";
    }
}
