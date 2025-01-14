package com.angkorteam.mbaas.server.page.role;

import com.angkorteam.framework.extension.wicket.table.DataTable;
import com.angkorteam.framework.extension.wicket.table.DefaultDataTable;
import com.angkorteam.framework.extension.wicket.table.filter.ActionFilteredJooqColumn;
import com.angkorteam.framework.extension.wicket.table.filter.FilterToolbar;
import com.angkorteam.framework.extension.wicket.table.filter.TextFilteredJooqColumn;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.RoleTable;
import com.angkorteam.mbaas.server.provider.RoleProvider;
import com.angkorteam.mbaas.server.wicket.JooqUtils;
import com.angkorteam.mbaas.server.wicket.MasterPage;
import com.angkorteam.mbaas.server.wicket.Mount;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jooq.DSLContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by socheat on 3/1/16.
 */
@AuthorizeInstantiation("administrator")
@Mount("/role/management")
public class RoleManagementPage extends MasterPage implements ActionFilteredJooqColumn.Event {

    private DataTable<Map<String, Object>, String> dataTable;

    @Override
    public String getPageHeader() {
        return "Role Management";
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        RoleProvider provider = new RoleProvider();
        provider.selectField(String.class, "roleId");
        provider.selectField(Boolean.class, "system");

        provider.setGroupBy(provider.getRoleId());

        FilterForm<Map<String, String>> filterForm = new FilterForm<>("filter-form", provider);
        add(filterForm);

        List<IColumn<Map<String, Object>, String>> columns = new ArrayList<>();
        columns.add(new TextFilteredJooqColumn(String.class, JooqUtils.lookup("name", this), "name", provider));
        columns.add(new TextFilteredJooqColumn(String.class, JooqUtils.lookup("description", this), "description", provider));
        columns.add(new ActionFilteredJooqColumn(JooqUtils.lookup("action", this), JooqUtils.lookup("filter", this), JooqUtils.lookup("clear", this), this, "Edit", "Delete"));

        dataTable = new DefaultDataTable<>("table", columns, provider, 16);
        dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm));
        filterForm.add(dataTable);

        BookmarkablePageLink<Void> refreshLink = new BookmarkablePageLink<Void>("refreshLink", RoleManagementPage.class, getPageParameters());
        add(refreshLink);
    }

    @Override
    public void onClickEventLink(String link, Map<String, Object> model) {
        String roleId = (String) model.get("roleId");
        if ("Edit".equals(link)) {
            PageParameters parameters = new PageParameters();
            parameters.add("id", roleId);
            setResponsePage(RoleModifyPage.class, parameters);
        } else if ("Delete".equals(link)) {
            DSLContext context = getDSLContext();
            RoleTable roleTable = Tables.ROLE.as("roleTable");
            context.delete(roleTable).where(roleTable.ROLE_ID.eq(roleId)).execute();
        }
    }

    @Override
    public boolean isVisibleEventLink(String link, Map<String, Object> model) {
        if ((Boolean) model.get("system")) {
            return false;
        } else {
            return true;
        }
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

    @Override
    public boolean isClickableEventLink(String link, Map<String, Object> object) {
        return true;
    }
}
