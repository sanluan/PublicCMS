package com.publiccms.common.datasource;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.NullPrecedence;
import org.hibernate.ScrollMode;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.dialect.ColumnAliasExtractor;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.LobMergeStrategy;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.lock.LockingStrategy;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.engine.jdbc.env.spi.IdentifierHelper;
import org.hibernate.engine.jdbc.env.spi.IdentifierHelperBuilder;
import org.hibernate.engine.jdbc.env.spi.NameQualifierSupport;
import org.hibernate.engine.jdbc.env.spi.SchemaNameResolver;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.SQLExceptionConverter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.hibernate.loader.BatchLoadSizingStrategy;
import org.hibernate.mapping.Constraint;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Index;
import org.hibernate.mapping.Table;
import org.hibernate.persister.entity.Lockable;
import org.hibernate.procedure.spi.CallableStatementSupport;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.sql.CaseFragment;
import org.hibernate.sql.JoinFragment;
import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;
import org.hibernate.tool.schema.spi.Exporter;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class MultiDialect extends Dialect {
    private static Map<String, Dialect> dialectMap = new HashMap<>();
    private static Dialect defaultDialect;

    private Dialect getCurrentDialect() {
        Dialect dialect = dialectMap.get(MultiDataSource.getDataSourceName());
        return null == dialect ? defaultDialect : dialect;
    }

    /**
     * @param defaultDialect
     *            the defaultDialect to set
     */
    public static void setDefaultDialect(Dialect defaultDialect) {
        MultiDialect.defaultDialect = defaultDialect;
    }

    /**
     * @param dataSourceName
     * @param dialect
     */
    public static void putDialect(String dataSourceName, Dialect dialect) {
        dialectMap.put(dataSourceName, dialect);
    }

    @Override
    public String toString() {
        return getCurrentDialect().toString();
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        getCurrentDialect().contributeTypes(typeContributions, serviceRegistry);
    }

    @Override
    public String getTypeName(int code) throws HibernateException {
        return getCurrentDialect().getTypeName(code);
    }

    @Override
    public String getTypeName(int code, long length, int precision, int scale) throws HibernateException {
        return getCurrentDialect().getTypeName(code, length, precision, scale);
    }

    @Override
    public String getCastTypeName(int code) {
        return getCurrentDialect().getCastTypeName(code);
    }

    @Override
    public String cast(String value, int jdbcTypeCode, int length, int precision, int scale) {
        return getCurrentDialect().cast(value, jdbcTypeCode, length, precision, scale);
    }

    @Override
    public String cast(String value, int jdbcTypeCode, int length) {
        return getCurrentDialect().cast(value, jdbcTypeCode, length);
    }

    @Override
    public String cast(String value, int jdbcTypeCode, int precision, int scale) {
        return getCurrentDialect().cast(value, jdbcTypeCode, precision, scale);
    }

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        return getCurrentDialect().remapSqlTypeDescriptor(sqlTypeDescriptor);
    }

    @Override
    public LobMergeStrategy getLobMergeStrategy() {
        return getCurrentDialect().getLobMergeStrategy();
    }

    @Override
    public String getHibernateTypeName(int code) throws HibernateException {
        return getCurrentDialect().getHibernateTypeName(code);
    }

    @Override
    public boolean isTypeNameRegistered(String typeName) {
        return getCurrentDialect().isTypeNameRegistered(typeName);
    }

    @Override
    public String getHibernateTypeName(int code, int length, int precision, int scale) throws HibernateException {
        return getCurrentDialect().getHibernateTypeName(code, length, precision, scale);
    }

    @Override
    @Deprecated
    public Class<?> getNativeIdentifierGeneratorClass() {
        return getCurrentDialect().getNativeIdentifierGeneratorClass();
    }

    @Override
    public String getNativeIdentifierGeneratorStrategy() {
        return getCurrentDialect().getNativeIdentifierGeneratorStrategy();
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return getCurrentDialect().getIdentityColumnSupport();
    }

    @Override
    public boolean supportsSequences() {
        return getCurrentDialect().supportsSequences();
    }

    @Override
    public boolean supportsPooledSequences() {
        return getCurrentDialect().supportsPooledSequences();
    }

    @Override
    public String getSequenceNextValString(String sequenceName) throws MappingException {
        return getCurrentDialect().getSequenceNextValString(sequenceName);
    }

    @Override
    public String getSelectSequenceNextValString(String sequenceName) throws MappingException {
        return getCurrentDialect().getSelectSequenceNextValString(sequenceName);
    }

    @Override
    @Deprecated
    public String[] getCreateSequenceStrings(String sequenceName) throws MappingException {
        return getCurrentDialect().getCreateSequenceStrings(sequenceName);
    }

    @Override
    public String[] getCreateSequenceStrings(String sequenceName, int initialValue, int incrementSize) throws MappingException {
        return getCurrentDialect().getCreateSequenceStrings(sequenceName, initialValue, incrementSize);
    }

    @Override
    public String[] getDropSequenceStrings(String sequenceName) throws MappingException {
        return getCurrentDialect().getDropSequenceStrings(sequenceName);
    }

    @Override
    public String getQuerySequencesString() {
        return getCurrentDialect().getQuerySequencesString();
    }

    @Override
    public SequenceInformationExtractor getSequenceInformationExtractor() {
        return getCurrentDialect().getSequenceInformationExtractor();
    }

    @Override
    public String getSelectGUIDString() {
        return getCurrentDialect().getSelectGUIDString();
    }

    @Override
    public LimitHandler getLimitHandler() {
        return getCurrentDialect().getLimitHandler();
    }

    @Override
    @Deprecated
    public boolean supportsLimit() {
        return getCurrentDialect().supportsLimit();
    }

    @Override
    @Deprecated
    public boolean supportsLimitOffset() {
        return getCurrentDialect().supportsLimitOffset();
    }

    @Override
    @Deprecated
    public boolean supportsVariableLimit() {
        return getCurrentDialect().supportsVariableLimit();
    }

    @Override
    @Deprecated
    public boolean bindLimitParametersInReverseOrder() {
        return getCurrentDialect().bindLimitParametersInReverseOrder();
    }

    @Override
    @Deprecated
    public boolean bindLimitParametersFirst() {
        return getCurrentDialect().bindLimitParametersFirst();
    }

    @Override
    @Deprecated
    public boolean useMaxForLimit() {
        return getCurrentDialect().useMaxForLimit();
    }

    @Override
    @Deprecated
    public boolean forceLimitUsage() {
        return getCurrentDialect().forceLimitUsage();
    }

    @Override
    @Deprecated
    public String getLimitString(String query, int offset, int limit) {
        return getCurrentDialect().getLimitString(query, offset, limit);
    }

    @Override
    @Deprecated
    public int convertToFirstRowValue(int zeroBasedFirstResult) {
        return getCurrentDialect().convertToFirstRowValue(zeroBasedFirstResult);
    }

    @Override
    public boolean supportsLockTimeouts() {
        return getCurrentDialect().supportsLockTimeouts();
    }

    @Override
    public boolean isLockTimeoutParameterized() {
        return getCurrentDialect().isLockTimeoutParameterized();
    }

    @Override
    public LockingStrategy getLockingStrategy(Lockable lockable, LockMode lockMode) {
        return getCurrentDialect().getLockingStrategy(lockable, lockMode);
    }

    @Override
    public String getForUpdateString(LockOptions lockOptions) {
        return getCurrentDialect().getForUpdateString(lockOptions);
    }

    @Override
    public String getForUpdateString(LockMode lockMode) {
        return getCurrentDialect().getForUpdateString(lockMode);
    }

    @Override
    public String getForUpdateString() {
        return getCurrentDialect().getForUpdateString();
    }

    @Override
    public String getWriteLockString(int timeout) {
        return getCurrentDialect().getWriteLockString(timeout);
    }

    @Override
    public String getWriteLockString(String aliases, int timeout) {
        return getCurrentDialect().getWriteLockString(aliases, timeout);
    }

    @Override
    public String getReadLockString(int timeout) {
        return getCurrentDialect().getReadLockString(timeout);
    }

    @Override
    public String getReadLockString(String aliases, int timeout) {
        return getCurrentDialect().getReadLockString(aliases, timeout);
    }

    @Override
    public boolean forUpdateOfColumns() {
        return getCurrentDialect().forUpdateOfColumns();
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return getCurrentDialect().supportsOuterJoinForUpdate();
    }

    @Override
    public String getForUpdateString(String aliases) {
        return getCurrentDialect().getForUpdateString(aliases);
    }

    @Override
    public String getForUpdateString(String aliases, LockOptions lockOptions) {
        return getCurrentDialect().getForUpdateString(aliases, lockOptions);
    }

    @Override
    public String getForUpdateNowaitString() {
        return getCurrentDialect().getForUpdateNowaitString();
    }

    @Override
    public String getForUpdateSkipLockedString() {
        return getCurrentDialect().getForUpdateSkipLockedString();
    }

    @Override
    public String getForUpdateNowaitString(String aliases) {
        return getCurrentDialect().getForUpdateNowaitString(aliases);
    }

    @Override
    public String getForUpdateSkipLockedString(String aliases) {
        return getCurrentDialect().getForUpdateSkipLockedString(aliases);
    }

    @Override
    @Deprecated
    public String appendLockHint(LockMode mode, String tableName) {
        return getCurrentDialect().appendLockHint(mode, tableName);
    }

    @Override
    public String appendLockHint(LockOptions lockOptions, String tableName) {
        return getCurrentDialect().appendLockHint(lockOptions, tableName);
    }

    @Override
    public String applyLocksToSql(String sql, LockOptions aliasedLockOptions, Map<String, String[]> keyColumnNames) {
        return getCurrentDialect().applyLocksToSql(sql, aliasedLockOptions, keyColumnNames);
    }

    @Override
    public String getCreateTableString() {
        return getCurrentDialect().getCreateTableString();
    }

    @Override
    public String getAlterTableString(String tableName) {
        return getCurrentDialect().getAlterTableString(tableName);
    }

    @Override
    public String getCreateMultisetTableString() {
        return getCurrentDialect().getCreateMultisetTableString();
    }

    @Override
    public MultiTableBulkIdStrategy getDefaultMultiTableBulkIdStrategy() {
        return getCurrentDialect().getDefaultMultiTableBulkIdStrategy();
    }

    @Override
    public int registerResultSetOutParameter(CallableStatement statement, int position) throws SQLException {
        return getCurrentDialect().registerResultSetOutParameter(statement, position);
    }

    @Override
    public int registerResultSetOutParameter(CallableStatement statement, String name) throws SQLException {
        return getCurrentDialect().registerResultSetOutParameter(statement, name);
    }

    @Override
    public ResultSet getResultSet(CallableStatement statement) throws SQLException {
        return getCurrentDialect().getResultSet(statement);
    }

    @Override
    public ResultSet getResultSet(CallableStatement statement, int position) throws SQLException {
        return getCurrentDialect().getResultSet(statement, position);
    }

    @Override
    public ResultSet getResultSet(CallableStatement statement, String name) throws SQLException {
        return getCurrentDialect().getResultSet(statement, name);
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return getCurrentDialect().supportsCurrentTimestampSelection();
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return getCurrentDialect().isCurrentTimestampSelectStringCallable();
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return getCurrentDialect().getCurrentTimestampSelectString();
    }

    @Override
    public String getCurrentTimestampSQLFunctionName() {
        return getCurrentDialect().getCurrentTimestampSQLFunctionName();
    }

    @Override
    @Deprecated
    public SQLExceptionConverter buildSQLExceptionConverter() {
        return getCurrentDialect().buildSQLExceptionConverter();
    }

    @Override
    public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
        return getCurrentDialect().buildSQLExceptionConversionDelegate();
    }

    @Override
    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return getCurrentDialect().getViolatedConstraintNameExtracter();
    }

    @Override
    public String getSelectClauseNullString(int sqlType) {
        return getCurrentDialect().getSelectClauseNullString(sqlType);
    }

    @Override
    public boolean supportsUnionAll() {
        return getCurrentDialect().supportsUnionAll();
    }

    @Override
    public JoinFragment createOuterJoinFragment() {
        return getCurrentDialect().createOuterJoinFragment();
    }

    @Override
    public CaseFragment createCaseFragment() {
        return getCurrentDialect().createCaseFragment();
    }

    @Override
    public String getNoColumnsInsertString() {
        return getCurrentDialect().getNoColumnsInsertString();
    }

    @Override
    public String getLowercaseFunction() {
        return getCurrentDialect().getLowercaseFunction();
    }

    @Override
    public String getCaseInsensitiveLike() {
        return getCurrentDialect().getCaseInsensitiveLike();
    }

    @Override
    public boolean supportsCaseInsensitiveLike() {
        return getCurrentDialect().supportsCaseInsensitiveLike();
    }

    @Override
    public String transformSelectString(String select) {
        return getCurrentDialect().transformSelectString(select);
    }

    @Override
    public int getMaxAliasLength() {
        return getCurrentDialect().getMaxAliasLength();
    }

    @Override
    public String toBooleanValueString(boolean bool) {
        return getCurrentDialect().toBooleanValueString(bool);
    }

    @Override
    @Deprecated
    public Set<String> getKeywords() {
        return getCurrentDialect().getKeywords();
    }

    @Override
    public IdentifierHelper buildIdentifierHelper(IdentifierHelperBuilder builder, DatabaseMetaData dbMetaData)
            throws SQLException {
        return getCurrentDialect().buildIdentifierHelper(builder, dbMetaData);
    }

    @Override
    public char openQuote() {
        return getCurrentDialect().openQuote();
    }

    @Override
    public char closeQuote() {
        return getCurrentDialect().closeQuote();
    }

    @Override
    public Exporter<Table> getTableExporter() {
        return getCurrentDialect().getTableExporter();
    }

    @Override
    public Exporter<Sequence> getSequenceExporter() {
        return getCurrentDialect().getSequenceExporter();
    }

    @Override
    public Exporter<Index> getIndexExporter() {
        return getCurrentDialect().getIndexExporter();
    }

    @Override
    public Exporter<ForeignKey> getForeignKeyExporter() {
        return getCurrentDialect().getForeignKeyExporter();
    }

    @Override
    public Exporter<Constraint> getUniqueKeyExporter() {
        return getCurrentDialect().getUniqueKeyExporter();
    }

    @Override
    public Exporter<AuxiliaryDatabaseObject> getAuxiliaryDatabaseObjectExporter() {
        return getCurrentDialect().getAuxiliaryDatabaseObjectExporter();
    }

    @Override
    public boolean canCreateCatalog() {
        return getCurrentDialect().canCreateCatalog();
    }

    @Override
    public String[] getCreateCatalogCommand(String catalogName) {
        return getCurrentDialect().getCreateCatalogCommand(catalogName);
    }

    @Override
    public String[] getDropCatalogCommand(String catalogName) {
        return getCurrentDialect().getDropCatalogCommand(catalogName);
    }

    @Override
    public boolean canCreateSchema() {
        return getCurrentDialect().canCreateSchema();
    }

    @Override
    public String[] getCreateSchemaCommand(String schemaName) {
        return getCurrentDialect().getCreateSchemaCommand(schemaName);
    }

    @Override
    public String[] getDropSchemaCommand(String schemaName) {
        return getCurrentDialect().getDropSchemaCommand(schemaName);
    }

    @Override
    public String getCurrentSchemaCommand() {
        return getCurrentDialect().getCurrentSchemaCommand();
    }

    @Override
    public SchemaNameResolver getSchemaNameResolver() {
        return getCurrentDialect().getSchemaNameResolver();
    }

    @Override
    public boolean hasAlterTable() {
        return getCurrentDialect().hasAlterTable();
    }

    @Override
    public boolean dropConstraints() {
        return getCurrentDialect().dropConstraints();
    }

    @Override
    public boolean qualifyIndexName() {
        return getCurrentDialect().qualifyIndexName();
    }

    @Override
    public String getAddColumnString() {
        return getCurrentDialect().getAddColumnString();
    }

    @Override
    public String getAddColumnSuffixString() {
        return getCurrentDialect().getAddColumnSuffixString();
    }

    @Override
    public String getDropForeignKeyString() {
        return getCurrentDialect().getDropForeignKeyString();
    }

    @Override
    public String getTableTypeString() {
        return getCurrentDialect().getTableTypeString();
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable,
            String[] primaryKey, boolean referencesPrimaryKey) {
        return getCurrentDialect().getAddForeignKeyConstraintString(constraintName, foreignKey, referencedTable, primaryKey,
                referencesPrimaryKey);
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName, String foreignKeyDefinition) {
        return getCurrentDialect().getAddForeignKeyConstraintString(constraintName, foreignKeyDefinition);
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return getCurrentDialect().getAddPrimaryKeyConstraintString(constraintName);
    }

    @Override
    public boolean hasSelfReferentialForeignKeyBug() {
        return getCurrentDialect().hasSelfReferentialForeignKeyBug();
    }

    @Override
    public String getNullColumnString() {
        return getCurrentDialect().getNullColumnString();
    }

    @Override
    public boolean supportsCommentOn() {
        return getCurrentDialect().supportsCommentOn();
    }

    @Override
    public String getTableComment(String comment) {
        return getCurrentDialect().getTableComment(comment);
    }

    @Override
    public String getColumnComment(String comment) {
        return getCurrentDialect().getColumnComment(comment);
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return getCurrentDialect().supportsIfExistsBeforeTableName();
    }

    @Override
    public boolean supportsIfExistsAfterTableName() {
        return getCurrentDialect().supportsIfExistsAfterTableName();
    }

    @Override
    public boolean supportsIfExistsBeforeConstraintName() {
        return getCurrentDialect().supportsIfExistsBeforeConstraintName();
    }

    @Override
    public boolean supportsIfExistsAfterConstraintName() {
        return getCurrentDialect().supportsIfExistsAfterConstraintName();
    }

    @Override
    public boolean supportsIfExistsAfterAlterTable() {
        return getCurrentDialect().supportsIfExistsAfterAlterTable();
    }

    @Override
    public String getDropTableString(String tableName) {
        return getCurrentDialect().getDropTableString(tableName);
    }

    @Override
    public boolean supportsColumnCheck() {
        return getCurrentDialect().supportsColumnCheck();
    }

    @Override
    public boolean supportsTableCheck() {
        return getCurrentDialect().supportsTableCheck();
    }

    @Override
    public boolean supportsCascadeDelete() {
        return getCurrentDialect().supportsCascadeDelete();
    }

    @Override
    public String getCascadeConstraintsString() {
        return getCurrentDialect().getCascadeConstraintsString();
    }

    @Override
    public String getCrossJoinSeparator() {
        return getCurrentDialect().getCrossJoinSeparator();
    }

    @Override
    public ColumnAliasExtractor getColumnAliasExtractor() {
        return getCurrentDialect().getColumnAliasExtractor();
    }

    @Override
    public boolean supportsEmptyInList() {
        return getCurrentDialect().supportsEmptyInList();
    }

    @Override
    public boolean areStringComparisonsCaseInsensitive() {
        return getCurrentDialect().areStringComparisonsCaseInsensitive();
    }

    @Override
    public boolean supportsRowValueConstructorSyntax() {
        return getCurrentDialect().supportsRowValueConstructorSyntax();
    }

    @Override
    public boolean supportsRowValueConstructorSyntaxInInList() {
        return getCurrentDialect().supportsRowValueConstructorSyntaxInInList();
    }

    @Override
    public boolean useInputStreamToInsertBlob() {
        return getCurrentDialect().useInputStreamToInsertBlob();
    }

    @Override
    public boolean supportsParametersInInsertSelect() {
        return getCurrentDialect().supportsParametersInInsertSelect();
    }

    @Override
    public boolean replaceResultVariableInOrderByClauseWithPosition() {
        return getCurrentDialect().replaceResultVariableInOrderByClauseWithPosition();
    }

    @Override
    public String renderOrderByElement(String expression, String collation, String order, NullPrecedence nulls) {
        return getCurrentDialect().renderOrderByElement(expression, collation, order, nulls);
    }

    @Override
    public boolean requiresCastingOfParametersInSelectClause() {
        return getCurrentDialect().requiresCastingOfParametersInSelectClause();
    }

    @Override
    public boolean supportsResultSetPositionQueryMethodsOnForwardOnlyCursor() {
        return getCurrentDialect().supportsResultSetPositionQueryMethodsOnForwardOnlyCursor();
    }

    @Override
    public boolean supportsCircularCascadeDeleteConstraints() {
        return getCurrentDialect().supportsCircularCascadeDeleteConstraints();
    }

    @Override
    public boolean supportsSubselectAsInPredicateLHS() {
        return getCurrentDialect().supportsSubselectAsInPredicateLHS();
    }

    @Override
    public boolean supportsExpectedLobUsagePattern() {
        return getCurrentDialect().supportsExpectedLobUsagePattern();
    }

    @Override
    public boolean supportsLobValueChangePropogation() {
        return getCurrentDialect().supportsLobValueChangePropogation();
    }

    @Override
    public boolean supportsUnboundedLobLocatorMaterialization() {
        return getCurrentDialect().supportsUnboundedLobLocatorMaterialization();
    }

    @Override
    public boolean supportsSubqueryOnMutatingTable() {
        return getCurrentDialect().supportsSubqueryOnMutatingTable();
    }

    @Override
    public boolean supportsExistsInSelect() {
        return getCurrentDialect().supportsExistsInSelect();
    }

    @Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return getCurrentDialect().doesReadCommittedCauseWritersToBlockReaders();
    }

    @Override
    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
        return getCurrentDialect().doesRepeatableReadCauseReadersToBlockWriters();
    }

    @Override
    public boolean supportsBindAsCallableArgument() {
        return getCurrentDialect().supportsBindAsCallableArgument();
    }

    @Override
    public boolean supportsTupleCounts() {
        return getCurrentDialect().supportsTupleCounts();
    }

    @Override
    public boolean supportsTupleDistinctCounts() {
        return getCurrentDialect().supportsTupleDistinctCounts();
    }

    @Override
    public boolean requiresParensForTupleDistinctCounts() {
        return getCurrentDialect().requiresParensForTupleDistinctCounts();
    }

    @Override
    public int getInExpressionCountLimit() {
        return getCurrentDialect().getInExpressionCountLimit();
    }

    @Override
    public boolean forceLobAsLastValue() {
        return getCurrentDialect().forceLobAsLastValue();
    }

    @Override
    @Deprecated
    public boolean useFollowOnLocking() {
        return getCurrentDialect().useFollowOnLocking();
    }

    @Override
    public boolean useFollowOnLocking(QueryParameters parameters) {
        return getCurrentDialect().useFollowOnLocking(parameters);
    }

    @Override
    public String getNotExpression(String expression) {
        return getCurrentDialect().getNotExpression(expression);
    }

    @Override
    public UniqueDelegate getUniqueDelegate() {
        return getCurrentDialect().getUniqueDelegate();
    }

    @Override
    @Deprecated
    public boolean supportsUnique() {
        return getCurrentDialect().supportsUnique();
    }

    @Override
    @Deprecated
    public boolean supportsUniqueConstraintInCreateAlterTable() {
        return getCurrentDialect().supportsUniqueConstraintInCreateAlterTable();
    }

    @Override
    @Deprecated
    public String getAddUniqueConstraintString(String constraintName) {
        return getCurrentDialect().getAddUniqueConstraintString(constraintName);
    }

    @Override
    @Deprecated
    public boolean supportsNotNullUnique() {
        return getCurrentDialect().supportsNotNullUnique();
    }

    @Override
    public String getQueryHintString(String query, List<String> hintList) {
        return getCurrentDialect().getQueryHintString(query, hintList);
    }

    @Override
    public String getQueryHintString(String query, String hints) {
        return getCurrentDialect().getQueryHintString(query, hints);
    }

    @Override
    public ScrollMode defaultScrollMode() {
        return getCurrentDialect().defaultScrollMode();
    }

    @Override
    public boolean supportsTuplesInSubqueries() {
        return getCurrentDialect().supportsTuplesInSubqueries();
    }

    @Override
    public CallableStatementSupport getCallableStatementSupport() {
        return getCurrentDialect().getCallableStatementSupport();
    }

    @Override
    public NameQualifierSupport getNameQualifierSupport() {
        return getCurrentDialect().getNameQualifierSupport();
    }

    @Override
    public BatchLoadSizingStrategy getDefaultBatchLoadSizingStrategy() {
        return getCurrentDialect().getDefaultBatchLoadSizingStrategy();
    }

    @Override
    public boolean isJdbcLogWarningsEnabledByDefault() {
        return getCurrentDialect().isJdbcLogWarningsEnabledByDefault();
    }

    @Override
    public void augmentRecognizedTableTypes(List<String> tableTypesList) {
        getCurrentDialect().augmentRecognizedTableTypes(tableTypesList);
    }

    @Override
    public boolean supportsPartitionBy() {
        return getCurrentDialect().supportsPartitionBy();
    }

    @Override
    public boolean supportsNamedParameters(DatabaseMetaData databaseMetaData) throws SQLException {
        return getCurrentDialect().supportsNamedParameters(databaseMetaData);
    }

    @Override
    public boolean supportsNationalizedTypes() {
        return getCurrentDialect().supportsNationalizedTypes();
    }

    @Override
    public boolean supportsNonQueryWithCTE() {
        return getCurrentDialect().supportsNonQueryWithCTE();
    }

    @Override
    public boolean supportsValuesList() {
        return getCurrentDialect().supportsValuesList();
    }

    @Override
    public boolean supportsSkipLocked() {
        return getCurrentDialect().supportsSkipLocked();
    }

    @Override
    public boolean supportsNoWait() {
        return getCurrentDialect().supportsNoWait();
    }

    @Override
    public boolean isLegacyLimitHandlerBehaviorEnabled() {
        return getCurrentDialect().isLegacyLimitHandlerBehaviorEnabled();
    }

    @Override
    public String inlineLiteral(String literal) {
        return getCurrentDialect().inlineLiteral(literal);
    }

    @Override
    public String addSqlHintOrComment(String sql, QueryParameters parameters, boolean commentsEnabled) {
        return getCurrentDialect().addSqlHintOrComment(sql, parameters, commentsEnabled);
    }
}