package org.hibernate.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.LockMode;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.dialect.identity.DMIdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.hql.spi.id.IdTableSupportStandardImpl;
import org.hibernate.hql.spi.id.MultiTableBulkIdStrategy;
import org.hibernate.hql.spi.id.global.GlobalTemporaryTableBulkIdStrategy;
import org.hibernate.hql.spi.id.local.AfterUseAction;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.StandardBasicTypes;

public class DmDialect extends Dialect {
	private static final AbstractLimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {
		public String processSql(String sql, RowSelection selection) {
			boolean hasOffset = LimitHelper.hasFirstRow(selection);

			sql = sql.trim();
			while (sql.endsWith(";")) {
				sql = sql.substring(0, sql.length() - 1);
			}

			boolean isForUpdate = false;
			if (sql.toLowerCase().endsWith(" for update")) {
				sql = sql.substring(0, sql.length() - 11);
				isForUpdate = true;
			}

			StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
			if (hasOffset) {
				pagingSelect.append(sql).append(" limit ? offset ? ");
			} else {
				pagingSelect.append(sql).append(" limit ? ");
			}
			if (isForUpdate) {
				pagingSelect.append(" for update");
			}

			return pagingSelect.toString();
		}

		public boolean supportsLimit() {
			return true;
		}

		public boolean bindLimitParametersInReverseOrder() {
			return true;
		}
	};

	int dmdbtype_cursor = 0;

	public DmDialect() {
		registerColumnType(-7, "bit");
		registerColumnType(16, "bit");
		registerColumnType(-6, "tinyint");
		registerColumnType(5, "smallint");
		registerColumnType(4, "integer");
		registerColumnType(-5, "bigint");
		registerColumnType(6, "float");
		registerColumnType(8, "double");
		registerColumnType(2, "numeric($p,$s)");
		registerColumnType(7, "real");
		registerColumnType(3, "decimal($p,$s)");

		registerColumnType(91, "date");
		registerColumnType(92, "time");
		registerColumnType(93, "datetime");

		registerColumnType(-2, "binary($l)");
		registerColumnType(-3, "varbinary($l)");
		registerColumnType(-4, "image");
		registerColumnType(2004, "blob");

		registerColumnType(1, "char(1)");
		registerColumnType(12, "varchar($l)");
		registerColumnType(-1, "text");
		registerColumnType(2005, "clob");

		registerColumnType(-15, "char(1)");
		registerColumnType(-9, "varchar($l)");
		registerColumnType(-16, "text");
		registerColumnType(2011, "clob");

		registerKeyword("last");
		registerKeyword("size");
		registerHibernateType(5, StandardBasicTypes.SHORT.getName());

		registerFunction("substring", new SQLFunctionTemplate(StandardBasicTypes.STRING, "substring(?1, ?2, ?3)"));
		registerFunction("locate", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "locate(?1, ?2, ?3)"));
		registerFunction("trim", new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1 ?2 ?3 ?4)"));
		registerFunction("length", new StandardSQLFunction("length", StandardBasicTypes.INTEGER));
		registerFunction("bit_length", new StandardSQLFunction("bit_length", StandardBasicTypes.INTEGER));
		registerFunction("coalesce", new StandardSQLFunction("coalesce"));
		registerFunction("nullif", new StandardSQLFunction("nullif"));
		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("mod", new StandardSQLFunction("mod", StandardBasicTypes.LONG));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", StandardBasicTypes.DOUBLE));
		registerFunction("upper", new StandardSQLFunction("upper"));
		registerFunction("lower", new StandardSQLFunction("lower"));
		registerFunction("cast", new CastFunction());
		registerFunction("extract", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "extract(?1 ?2 ?3)"));

		registerFunction("second", new StandardSQLFunction("second", StandardBasicTypes.INTEGER));
		registerFunction("minute", new StandardSQLFunction("minute", StandardBasicTypes.INTEGER));
		registerFunction("hour", new StandardSQLFunction("hour", StandardBasicTypes.INTEGER));
		registerFunction("day", new StandardSQLFunction("day", StandardBasicTypes.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", StandardBasicTypes.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", StandardBasicTypes.INTEGER));

		registerFunction("str", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));

		registerFunction("asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE));
		registerFunction("acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE));
		registerFunction("atan2", new StandardSQLFunction("atan2", StandardBasicTypes.DOUBLE));
		registerFunction("ceil", new StandardSQLFunction("ceil", StandardBasicTypes.INTEGER));
		registerFunction("ceiling", new StandardSQLFunction("ceiling", StandardBasicTypes.INTEGER));
		registerFunction("cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", StandardBasicTypes.DOUBLE));
		registerFunction("cosh", new StandardSQLFunction("cosh", StandardBasicTypes.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees"));
		registerFunction("exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE));
		registerFunction("GREATEST", new StandardSQLFunction("GREATEST", StandardBasicTypes.DOUBLE));
		registerFunction("floor", new StandardSQLFunction("floor", StandardBasicTypes.INTEGER));
		registerFunction("ln", new StandardSQLFunction("ln", StandardBasicTypes.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", StandardBasicTypes.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", StandardBasicTypes.DOUBLE));
		registerFunction("pi", new NoArgSQLFunction("pi", StandardBasicTypes.DOUBLE));
		registerFunction("power", new StandardSQLFunction("power", StandardBasicTypes.DOUBLE));
		registerFunction("radians", new StandardSQLFunction("radians"));
		registerFunction("rand", new NoArgSQLFunction("rand", StandardBasicTypes.DOUBLE));
		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER));
		registerFunction("sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE));
		registerFunction("sinh", new StandardSQLFunction("sinh", StandardBasicTypes.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE));
		registerFunction("tanh", new StandardSQLFunction("tanh", StandardBasicTypes.DOUBLE));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("truncate", new StandardSQLFunction("truncate"));

		registerFunction("stddev", new StandardSQLFunction("stddev", StandardBasicTypes.DOUBLE));
		registerFunction("variance", new StandardSQLFunction("variance", StandardBasicTypes.DOUBLE));
		registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", ""));
		registerFunction("ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER));
		registerFunction("char", new StandardSQLFunction("char", StandardBasicTypes.CHARACTER));
		registerFunction("difference", new StandardSQLFunction("difference", StandardBasicTypes.INTEGER));

		registerFunction("char_length", new StandardSQLFunction("char_length", StandardBasicTypes.LONG));
		registerFunction("character_length", new StandardSQLFunction("character_length", StandardBasicTypes.LONG));
		registerFunction("chr", new StandardSQLFunction("chr", StandardBasicTypes.CHARACTER));
		registerFunction("initcap", new StandardSQLFunction("initcap", StandardBasicTypes.STRING));
		registerFunction("insert", new StandardSQLFunction("insert", StandardBasicTypes.STRING));
		registerFunction("insstr", new StandardSQLFunction("insstr", StandardBasicTypes.STRING));
		registerFunction("instr", new StandardSQLFunction("instr", StandardBasicTypes.LONG));
		registerFunction("instrb", new StandardSQLFunction("instrb", StandardBasicTypes.LONG));
		registerFunction("lcase", new StandardSQLFunction("lcase", StandardBasicTypes.STRING));
		registerFunction("left", new StandardSQLFunction("left", StandardBasicTypes.STRING));
		registerFunction("leftstr", new StandardSQLFunction("leftstr", StandardBasicTypes.STRING));
		registerFunction("len", new StandardSQLFunction("len", StandardBasicTypes.INTEGER));
		registerFunction("LENGTHB", new StandardSQLFunction("LENGTHB", StandardBasicTypes.INTEGER));
		registerFunction("octet_length", new StandardSQLFunction("octet_length", StandardBasicTypes.LONG));
		registerFunction("lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING));
		registerFunction("ltrim", new StandardSQLFunction("ltrim", StandardBasicTypes.STRING));
		registerFunction("position", new StandardSQLFunction("position", StandardBasicTypes.INTEGER));
		registerFunction("INS", new StandardSQLFunction("INS", StandardBasicTypes.STRING));
		registerFunction("repeat", new StandardSQLFunction("repeat", StandardBasicTypes.STRING));
		registerFunction("REPLICATE", new StandardSQLFunction("REPLICATE", StandardBasicTypes.STRING));
		registerFunction("STUFF", new StandardSQLFunction("STUFF", StandardBasicTypes.STRING));
		registerFunction("repeatstr", new StandardSQLFunction("repeatstr", StandardBasicTypes.STRING));
		registerFunction("replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING));
		registerFunction("reverse", new StandardSQLFunction("reverse", StandardBasicTypes.STRING));
		registerFunction("right", new StandardSQLFunction("right", StandardBasicTypes.STRING));
		registerFunction("rightstr", new StandardSQLFunction("rightstr", StandardBasicTypes.STRING));
		registerFunction("rpad", new StandardSQLFunction("rpad", StandardBasicTypes.STRING));
		registerFunction("TO_NUMBER", new StandardSQLFunction("TO_NUMBER"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim", StandardBasicTypes.STRING));
		registerFunction("soundex", new StandardSQLFunction("soundex", StandardBasicTypes.STRING));
		registerFunction("space", new StandardSQLFunction("space", StandardBasicTypes.STRING));
		registerFunction("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
		registerFunction("substrb", new StandardSQLFunction("substrb", StandardBasicTypes.STRING));
		registerFunction("to_char", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
		registerFunction("STRPOSDEC", new StandardSQLFunction("STRPOSDEC", StandardBasicTypes.STRING));
		registerFunction("STRPOSINC", new StandardSQLFunction("STRPOSINC", StandardBasicTypes.STRING));
		registerFunction("VSIZE", new StandardSQLFunction("VSIZE", StandardBasicTypes.INTEGER));
		registerFunction("translate", new StandardSQLFunction("translate", StandardBasicTypes.STRING));
		registerFunction("ucase", new StandardSQLFunction("ucase", StandardBasicTypes.STRING));

		registerFunction("OVERLAPS", new StandardSQLFunction("OVERLAPS"));
		registerFunction("DATEPART", new StandardSQLFunction("DATEPART"));
		registerFunction("DATE_PART", new StandardSQLFunction("DATE_PART"));
		registerFunction("add_days", new StandardSQLFunction("add_days"));
		registerFunction("add_months", new StandardSQLFunction("add_months"));
		registerFunction("add_weeks", new StandardSQLFunction("add_weeks"));
		registerFunction("curdate", new NoArgSQLFunction("curdate", StandardBasicTypes.DATE));
		registerFunction("curtime", new NoArgSQLFunction("curtime", StandardBasicTypes.TIME));
		registerFunction("current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE));
		registerFunction("current_time", new NoArgSQLFunction("current_time", StandardBasicTypes.TIME));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP));
		registerFunction("dateadd", new StandardSQLFunction("dateadd", StandardBasicTypes.TIMESTAMP));
		registerFunction("CUR_TICK_TIME", new StandardSQLFunction("CUR_TICK_TIME"));
		registerFunction("datediff", new StandardSQLFunction("datediff", StandardBasicTypes.INTEGER));
		registerFunction("datepart", new StandardSQLFunction("datepart", StandardBasicTypes.INTEGER));
		registerFunction("dayname", new StandardSQLFunction("dayname", StandardBasicTypes.STRING));
		registerFunction("dayofmonth", new StandardSQLFunction("dayofmonth", StandardBasicTypes.INTEGER));
		registerFunction("dayofweek", new StandardSQLFunction("dayofweek", StandardBasicTypes.INTEGER));
		registerFunction("dayofyear", new StandardSQLFunction("dayofyear", StandardBasicTypes.INTEGER));
		registerFunction("days_between", new StandardSQLFunction("days_between", StandardBasicTypes.INTEGER));
		registerFunction("getdate", new StandardSQLFunction("getdate", StandardBasicTypes.TIMESTAMP));
		registerFunction("LOCALTIMESTAMP", new StandardSQLFunction("LOCALTIMESTAMP"));
		registerFunction("NOW", new StandardSQLFunction("NOW"));
		registerFunction("last_day", new StandardSQLFunction("last_day"));
		registerFunction("month", new StandardSQLFunction("month", StandardBasicTypes.INTEGER));
		registerFunction("monthname", new StandardSQLFunction("monthname", StandardBasicTypes.STRING));
		registerFunction("months_between", new StandardSQLFunction("months_between"));
		registerFunction("GREATEST", new StandardSQLFunction("GREATEST", StandardBasicTypes.DATE));
		registerFunction("TO_DATETIME", new StandardSQLFunction("TO_DATETIME"));
		registerFunction("next_day", new StandardSQLFunction("next_day"));
		registerFunction("quarter", new StandardSQLFunction("quarter", StandardBasicTypes.INTEGER));
		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("timestampadd", new StandardSQLFunction("timestampadd", StandardBasicTypes.TIMESTAMP));
		registerFunction("timestampdiff", new StandardSQLFunction("timestampdiff", StandardBasicTypes.INTEGER));
		registerFunction("BIGDATEDIFF", new StandardSQLFunction("BIGDATEDIFF", StandardBasicTypes.BIG_INTEGER));
		registerFunction("sysdate", new StandardSQLFunction("sysdate", StandardBasicTypes.TIME));
		registerFunction("LEAST", new StandardSQLFunction("LEAST"));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("week", new StandardSQLFunction("week", StandardBasicTypes.INTEGER));
		registerFunction("weekday", new StandardSQLFunction("weekday", StandardBasicTypes.INTEGER));
		registerFunction("weeks_between", new StandardSQLFunction("weeks_between", StandardBasicTypes.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", StandardBasicTypes.INTEGER));
		registerFunction("years_between", new StandardSQLFunction("years_between", StandardBasicTypes.INTEGER));
		registerFunction("to_date", new StandardSQLFunction("to_date", StandardBasicTypes.TIMESTAMP));
		registerFunction("systimestamp", new NoArgSQLFunction("systimestamp", StandardBasicTypes.TIMESTAMP));

		registerFunction("ifnull", new StandardSQLFunction("ifnull"));
		registerFunction("isnull", new StandardSQLFunction("isnull"));
		registerFunction("nvl", new StandardSQLFunction("nvl"));
		registerFunction("decode", new StandardSQLFunction("decode"));
		registerFunction("cur_database", new StandardSQLFunction("cur_database", StandardBasicTypes.STRING));
		registerFunction("page", new StandardSQLFunction("page", StandardBasicTypes.INTEGER));
		registerFunction("sessid", new StandardSQLFunction("sessid", StandardBasicTypes.LONG));
		registerFunction("uid", new StandardSQLFunction("uid", StandardBasicTypes.LONG));
		registerFunction("user", new StandardSQLFunction("user", StandardBasicTypes.STRING));
		registerFunction("vsize", new StandardSQLFunction("vsize", StandardBasicTypes.INTEGER));
		registerFunction("tabledef", new StandardSQLFunction("tabledef", StandardBasicTypes.STRING));

		getDefaultProperties().setProperty("hibernate.use_outer_join", "true");
		getDefaultProperties().setProperty("hibernate.jdbc.batch_size", "0");
	}

	/**
	 * Get the appropriate {@link IdentityColumnSupport}
	 *
	 * @return the IdentityColumnSupport
	 * @since 5.1
	 */
	public IdentityColumnSupport getIdentityColumnSupport() {
		return new DMIdentityColumnSupport();
	}

	@Override
	public boolean supportsSequences() {
		return true;
	}

	@Override
	public boolean supportsPooledSequences() {
		return true;
	}

	@Override
	public String getSequenceNextValString(String sequenceName) {
		return "select " + getSelectSequenceNextValString(sequenceName);
	}

	@Override
	public String getSelectSequenceNextValString(String sequenceName) {
		return sequenceName + ".nextval";
	}

	@Override
	/** @deprecated */
	public String[] getCreateSequenceStrings(String sequenceName) {
		return new String[] { getCreateSequenceString(sequenceName) };
	}

	@Override
	public String[] getCreateSequenceStrings(String sequenceName, int initialValue, int incrementSize) {
		return new String[] { getCreateSequenceString(sequenceName, initialValue, incrementSize) };
	}

	@Override
	protected String getCreateSequenceString(String sequenceName) {
		return "create sequence " + sequenceName;
	}

	@Override
	protected String getCreateSequenceString(String sequenceName, int initialValue, int incrementSize) {
		return getCreateSequenceString(sequenceName) + " increment by " + incrementSize + " start with " + initialValue;
	}

	@Override
	public String[] getDropSequenceStrings(String sequenceName) {
		return new String[] { getDropSequenceString(sequenceName) };
	}

	@Override
	protected String getDropSequenceString(String sequenceName) {
		return "drop sequence " + sequenceName;
	}

	@Override
	public String getQuerySequencesString() {
		return "select name from sysobjects where type$ = 'SCHOBJ' and subtype$ = 'SEQ';";
	}

	@Override
	public String getSelectGUIDString() {
		return "select GUID()";
	}

	@Override
	public LimitHandler getLimitHandler() {
		return LIMIT_HANDLER;
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex != selectIndex ? 6 : 15);
	}

	@Override
	public boolean supportsLockTimeouts() {
		return true;
	}

	@Override
	public boolean isLockTimeoutParameterized() {
		return false;
	}
	
	@Override
	public String getForUpdateString() {
		return " for update";
	}

	@Override
	public String getWriteLockString(int timeout) {
		if (timeout == 0) {
			return " for update nowait";
		}
		if (timeout > 0) {
			float seconds = timeout / 1000.0F;
			timeout = Math.round(seconds);
			return " for update wait " + timeout;
		}

		return " for update";
	}

	@Override
	public String getReadLockString(int timeout) {
		return getWriteLockString(timeout);
	}

	@Override
	public boolean forUpdateOfColumns() {
		return true;
	}

	@Override
	public boolean supportsOuterJoinForUpdate() {
		return true;
	}

	@Override
	public String getForUpdateString(String aliases) {
		return getForUpdateString() + " of " + aliases;
	}

	@Override
	public String getForUpdateNowaitString() {
		return getForUpdateString() + " nowait";
	}

	@Override
	public String getForUpdateNowaitString(String aliases) {
		return getForUpdateString() + " of " + aliases + " nowait";
	}

	@Override
	public String appendLockHint(LockMode mode, String tableName) {
		return tableName;
	}

	@Override
	public int registerResultSetOutParameter(CallableStatement statement, int col) throws SQLException {
		if (this.dmdbtype_cursor == 0) {
			try {
				@SuppressWarnings("deprecation")
				Class<?> types = ReflectHelper.classForName("dm.jdbc.driver.DmdbType");
				this.dmdbtype_cursor = types.getField("CURSOR").getInt(types.newInstance());
			} catch (Exception se) {
				throw new SQLException("Problem while trying to load or access DmdbType.CURSOR value", se);
			}
		}

		statement.registerOutParameter(col, this.dmdbtype_cursor);
		col++;
		return col;
	}

	@Override
	public ResultSet getResultSet(CallableStatement statement) throws SQLException {
		statement.execute();
		return (ResultSet) statement.getObject(1);
	}

	@Override
	public boolean supportsCurrentTimestampSelection() {
		return true;
	}

	@Override
	public boolean isCurrentTimestampSelectStringCallable() {
		return false;
	}

	@Override
	public String getCurrentTimestampSelectString() {
		return "select current_timestamp()";
	}

	@Override
	public String getCurrentTimestampSQLFunctionName() {
		return "current_timestamp";
	}

	@Override
	public String getSelectClauseNullString(int sqlType) {
		return "null";
	}

	@Override
	public boolean supportsUnionAll() {
		return true;
	}

	@Override
	public String getLowercaseFunction() {
		return "lower";
	}

	@Override
	public String transformSelectString(String select) {
		return select;
	}

	@Override
	public String toBooleanValueString(boolean bool) {
		return bool ? "1" : "0";
	}

	@Override
	public char openQuote() {
		return '"';
	}

	@Override
	public char closeQuote() {
		return '"';
	}

	@Override
	public boolean hasAlterTable() {
		return true;
	}

	@Override
	public boolean dropConstraints() {
		return false;
	}

	@Override
	public boolean qualifyIndexName() {
		return true;
	}

	@Override
	public boolean supportsUnique() {
		return true;
	}

	@Override
	public boolean supportsUniqueConstraintInCreateAlterTable() {
		return true;
	}

	@Override
	public String getAddColumnString() {
		return " add column ";
	}

	@Override
	public String getDropForeignKeyString() {
		return " drop constraint ";
	}

	@Override
	public String getTableTypeString() {
		return "";
	}

	@Override
	public String getAddForeignKeyConstraintString(String constraintName, String[] foreignKey, String referencedTable,
			String[] primaryKey, boolean referencesPrimaryKey) {
		StringBuffer res = new StringBuffer(30);

		res.append(" add constraint ").append(constraintName).append(" foreign key (")
				.append(String.join(", ", foreignKey)).append(") references ").append(referencedTable);

		if (!referencesPrimaryKey) {
			res.append(" (").append(String.join(", ", primaryKey)).append(')');
		}

		return res.toString();
	}

	@Override
	public String getAddPrimaryKeyConstraintString(String constraintName) {
		return " add constraint " + constraintName + " primary key ";
	}

	@Override
	public boolean hasSelfReferentialForeignKeyBug() {
		return false;
	}

	@Override
	public String getNullColumnString() {
		return "";
	}

	@Override
	public boolean supportsCommentOn() {
		return false;
	}

	@Override
	public String getTableComment(String comment) {
		return "";
	}

	@Override
	public String getColumnComment(String comment) {
		return "";
	}

	@Override
	public boolean supportsIfExistsBeforeTableName() {
		return false;
	}

	@Override
	public boolean supportsIfExistsAfterTableName() {
		return false;
	}

	@Override
	public boolean supportsColumnCheck() {
		return true;
	}

	@Override
	public boolean supportsTableCheck() {
		return true;
	}

	@Override
	public boolean supportsCascadeDelete() {
		return true;
	}

	@Override
	public boolean supportsNotNullUnique() {
		return true;
	}

	@Override
	public String getCascadeConstraintsString() {
		return " cascade ";
	}

	@Override
	public String getCrossJoinSeparator() {
		return " cross join ";
	}

	@Override
	public boolean supportsEmptyInList() {
		return false;
	}

	@Override
	public boolean areStringComparisonsCaseInsensitive() {
		return true;
	}

	@Override
	public boolean supportsRowValueConstructorSyntax() {
		return false;
	}

	@Override
	public boolean supportsRowValueConstructorSyntaxInInList() {
		return true;
	}

	@Override
	public boolean useInputStreamToInsertBlob() {
		return true;
	}

	@Override
	public boolean replaceResultVariableInOrderByClauseWithPosition() {
		return false;
	}

	@Override
	public boolean requiresCastingOfParametersInSelectClause() {
		return false;
	}

	@Override
	public boolean supportsResultSetPositionQueryMethodsOnForwardOnlyCursor() {
		return true;
	}

	@Override
	public boolean supportsCircularCascadeDeleteConstraints() {
		return false;
	}

	@Override
	public boolean supportsSubselectAsInPredicateLHS() {
		return true;
	}

	@Override
	public boolean supportsExpectedLobUsagePattern() {
		return true;
	}

	@Override
	public boolean supportsLobValueChangePropogation() {
		return false;
	}

	@Override
	public boolean supportsUnboundedLobLocatorMaterialization() {
		return false;
	}

	@Override
	public boolean supportsSubqueryOnMutatingTable() {
		return true;
	}

	@Override
	public boolean supportsExistsInSelect() {
		return false;
	}

	@Override
	public boolean doesReadCommittedCauseWritersToBlockReaders() {
		return false;
	}

	@Override
	public boolean doesRepeatableReadCauseReadersToBlockWriters() {
		return false;
	}

	@Override
	public boolean supportsBindAsCallableArgument() {
		return true;
	}

	@Override
	public boolean supportsTupleCounts() {
		return false;
	}

	@Override
	public boolean supportsTupleDistinctCounts() {
		return false;
	}

	@Override
	public MultiTableBulkIdStrategy getDefaultMultiTableBulkIdStrategy() {
		return new GlobalTemporaryTableBulkIdStrategy(new IdTableSupportStandardImpl() {
			public String generateIdTableName(String baseName) {
				String name = super.generateIdTableName(baseName);
				return name.length() > 30 ? name.substring(0, 30) : name;
			}

			public String getCreateIdTableCommand() {
				return "create global temporary table";
			}

			public String getCreateIdTableStatementOptions() {
				return "on commit delete rows";
			}
		}, AfterUseAction.CLEAN);
	}
}