package com.angkorteam.mbaas.server.validator;

import com.angkorteam.framework.extension.share.validation.JooqValidator;
import com.angkorteam.mbaas.configuration.Constants;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.AttributeTable;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.jooq.DSLContext;

import java.util.regex.Pattern;

/**
 * Created by socheat on 3/8/16.
 */
public class AttributeNameValidator extends JooqValidator<String> {

    private String collectionId;

    private String attributeId;

    public AttributeNameValidator(String collectionId) {
        this.collectionId = collectionId;
    }

    public AttributeNameValidator(String collectionId, String attributeId) {
        this.collectionId = collectionId;
        this.attributeId = attributeId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String name = validatable.getValue();
        if (name != null && !"".equals(name)) {
            Pattern patternAttributeName = Pattern.compile(Constants.getXmlPropertiesConfiguration().getString(Constants.PATTERN_ATTRIBUTE_NAME));
            if (!patternAttributeName.matcher(name).matches()) {
                validatable.error(new ValidationError(this, "format"));
            } else {
                AttributeTable attributeTable = Tables.ATTRIBUTE.as("attributeTable");

                DSLContext context = getDSLContext();
                int count = 0;
                if (attributeId == null || "".equals(attributeId)) {
                    count = context.selectCount().from(attributeTable).where(attributeTable.NAME.eq(name)).and(attributeTable.COLLECTION_ID.eq(collectionId)).fetchOneInto(int.class);
                } else {
                    count = context.selectCount().from(attributeTable).where(attributeTable.NAME.eq(name)).and(attributeTable.COLLECTION_ID.eq(this.collectionId)).and(attributeTable.ATTRIBUTE_ID.ne(attributeId)).fetchOneInto(int.class);
                }
                if (count > 0) {
                    validatable.error(new ValidationError(this, "duplicated"));
                }
            }
        }
    }
}
