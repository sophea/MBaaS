package com.angkorteam.mbaas.server.provider;

import com.angkorteam.framework.extension.share.provider.JooqProvider;
import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.AssetTable;
import com.angkorteam.mbaas.model.entity.tables.AttributeTable;
import com.angkorteam.mbaas.model.entity.tables.CollectionTable;
import com.angkorteam.mbaas.model.entity.tables.UserTable;
import com.angkorteam.mbaas.model.entity.tables.records.AttributeRecord;
import com.angkorteam.mbaas.model.entity.tables.records.CollectionRecord;
import com.angkorteam.mbaas.plain.enums.TypeEnum;
import com.angkorteam.mbaas.server.function.MariaDBFunction;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.TableLike;
import org.jooq.impl.DSL;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by socheat on 3/11/16.
 */
public class AssetProvider extends JooqProvider {

    private AssetTable assetTable;
    private UserTable userTable;

    private TableLike<?> from;

    public AssetProvider() {
        this.assetTable = Tables.ASSET.as("assetTable");
        this.userTable = Tables.USER.as("userTable");

        this.from = assetTable.join(userTable).on(assetTable.OWNER_USER_ID.eq(userTable.USER_ID));
        DSLContext context = getDSLContext();

        CollectionTable collectionTable = Tables.COLLECTION.as("collectionTable");
        CollectionRecord collectionRecord = context.select(collectionTable.fields())
                .from(collectionTable)
                .where(collectionTable.NAME.eq(Tables.ASSET.getName()))
                .fetchOneInto(collectionTable);
        AttributeTable attributeTable = Tables.ATTRIBUTE.as("attributeTable");

        List<AttributeRecord> attributeRecords = context.select(attributeTable.fields())
                .from(attributeTable)
                .where(attributeTable.COLLECTION_ID.eq(collectionRecord.getCollectionId()))
                .fetchInto(attributeTable);

        Map<String, AttributeRecord> virtualAttributeRecords = new HashMap<>();
        for (AttributeRecord attributeRecord : attributeRecords) {
            virtualAttributeRecords.put(attributeRecord.getAttributeId(), attributeRecord);
        }

        for (AttributeRecord attributeRecord : attributeRecords) {
            if (attributeRecord.getSystem()) {
                continue;
            }
            if (TypeEnum.Boolean.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Boolean.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Boolean> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Boolean.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Byte.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Byte.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Byte> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Byte.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Short.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Short.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Short> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Short.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Integer.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Integer.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Integer> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Integer.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Long.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Long.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Long> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Long.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Float.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Float.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Float> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Float.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Double.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Double.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Double> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Double.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Character.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Character.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Character> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Character.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.String.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), String.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<String> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), String.class);
                    boardField(attributeRecord.getName(), field);
                }
            } else if (TypeEnum.Time.getLiteral().equals(attributeRecord.getJavaType())
                    || TypeEnum.Date.getLiteral().equals(attributeRecord.getJavaType())
                    || TypeEnum.DateTime.getLiteral().equals(attributeRecord.getJavaType())) {
                if (!attributeRecord.getVirtual()) {
                    boardField(attributeRecord.getName(), DSL.field(assetTable.getName() + "." + attributeRecord.getName(), Date.class));
                } else {
                    AttributeRecord virtualAttributeRecord = virtualAttributeRecords.get(attributeRecord.getVirtualAttributeId());
                    Field<Date> field = DSL.field(MariaDBFunction.columnGet(assetTable.getName() + "." + virtualAttributeRecord.getName(), attributeRecord.getName(), attributeRecord.getJavaType()), Date.class);
                    boardField(attributeRecord.getName(), field);
                }
            }
        }
    }

    public Field<String> getAssetId() {
        return this.assetTable.ASSET_ID;
    }

    public Field<String> getOwner() {
        return this.userTable.LOGIN;
    }

    public Field<Integer> getLength() {
        return this.assetTable.LENGTH;
    }

    public Field<String> getMime() {
        return this.assetTable.MIME;
    }

    public Field<String> getName() {
        return this.assetTable.LABEL;
    }

    @Override
    protected TableLike<?> from() {
        return this.from;
    }

    @Override
    protected List<Condition> where() {
        return null;
    }

    @Override
    protected List<Condition> having() {
        return null;
    }
}