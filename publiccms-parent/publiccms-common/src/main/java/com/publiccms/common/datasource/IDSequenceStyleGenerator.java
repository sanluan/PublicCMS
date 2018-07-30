package com.publiccms.common.datasource;

import java.util.Properties;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * IDSequenceStyleGenerator
 *
 */
public class IDSequenceStyleGenerator extends SequenceStyleGenerator {
    /**
     * 
     */
    public static final String CONFIG_TARGET_TABLE = "target_table";
    /**
     * 
     */
    public static final String DEF_SEQUENCE_SUFFIX = "_ID_SEQ";

    @Override
    protected QualifiedName determineSequenceName(Properties params, Dialect dialect, JdbcEnvironment jdbcEnv,
            ServiceRegistry serviceRegistry) {
        String tableName = ConfigurationHelper.getString(CONFIG_TARGET_TABLE, params);
        if (null == tableName) {
            return super.determineSequenceName(params, dialect, jdbcEnv, serviceRegistry);
        } else {
            Identifier catalog = jdbcEnv.getIdentifierHelper()
                    .toIdentifier(ConfigurationHelper.getString(CATALOG, params));
            Identifier schema = jdbcEnv.getIdentifierHelper()
                    .toIdentifier(ConfigurationHelper.getString(SCHEMA, params));
            return new QualifiedNameParser.NameParts(catalog, schema,
                    jdbcEnv.getIdentifierHelper().toIdentifier(tableName.toUpperCase() + DEF_SEQUENCE_SUFFIX));
        }
    }

}
