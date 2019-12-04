//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.hibernate.dialect;

import org.hibernate.JDBCException;
import org.hibernate.PessimisticLockException;
import org.hibernate.dialect.function.AvgWithArgumentCastFunction;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.spi.SQLExceptionConversionDelegate;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.StandardBasicTypes;
import org.jboss.logging.Logger;

import java.sql.SQLException;

public class H2Dialect extends Dialect {
    private static final CoreMessageLogger LOG = (CoreMessageLogger)Logger.getMessageLogger(CoreMessageLogger.class, H2Dialect.class.getName());
    private final String querySequenceString;
    private static final ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {
        public String extractConstraintName(SQLException sqle) {
            String constraintName = null;
            if (sqle.getSQLState().startsWith("23")) {
                String message = sqle.getMessage();
                int idx = message.indexOf("violation: ");
                if (idx > 0) {
                    constraintName = message.substring(idx + "violation: ".length());
                }
            }

            return constraintName;
        }
    };

    public H2Dialect() {
        String querySequenceString = "select sequence_name from information_schema.sequences";

        try {
            Class h2ConstantsClass = ReflectHelper.classForName("org.h2.engine.Constants");
            int majorVersion = (Integer)h2ConstantsClass.getDeclaredField("VERSION_MAJOR").get((Object)null);
            int minorVersion = (Integer)h2ConstantsClass.getDeclaredField("VERSION_MINOR").get((Object)null);
            int buildId = (Integer)h2ConstantsClass.getDeclaredField("BUILD_ID").get((Object)null);
            if (buildId < 32) {
                querySequenceString = "select name from information_schema.sequences";
            }

            if (majorVersion <= 1 && minorVersion <= 2 && buildId < 139) {
                LOG.unsupportedMultiTableBulkHqlJpaql(majorVersion, minorVersion, buildId);
            }
        } catch (Exception var6) {
            LOG.undeterminedH2Version();
        }

        this.querySequenceString = querySequenceString;
        this.registerColumnType(16, "boolean");
        this.registerColumnType(-5, "bigint");
        this.registerColumnType(-2, "binary");
        this.registerColumnType(-7, "boolean");
        this.registerColumnType(1, "char($l)");
        this.registerColumnType(91, "date");
        this.registerColumnType(3, "decimal($p,$s)");
        this.registerColumnType(2, "decimal($p,$s)");
        this.registerColumnType(8, "double");
        this.registerColumnType(6, "float");
        this.registerColumnType(4, "integer");
        this.registerColumnType(-4, "longvarbinary");
        this.registerColumnType(-1, "longvarchar");
        this.registerColumnType(7, "real");
        this.registerColumnType(5, "smallint");
        this.registerColumnType(-6, "tinyint");
        this.registerColumnType(92, "time");
        this.registerColumnType(93, "timestamp");
        this.registerColumnType(12, "varchar($l)");
        this.registerColumnType(-3, "binary($l)");
        this.registerColumnType(2004, "blob");
        this.registerColumnType(2005, "clob");
        this.registerFunction("avg", new AvgWithArgumentCastFunction("double"));
        this.registerFunction("acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE));
        this.registerFunction("asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE));
        this.registerFunction("atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE));
        this.registerFunction("atan2", new StandardSQLFunction("atan2", StandardBasicTypes.DOUBLE));
        this.registerFunction("bitand", new StandardSQLFunction("bitand", StandardBasicTypes.INTEGER));
        this.registerFunction("bitor", new StandardSQLFunction("bitor", StandardBasicTypes.INTEGER));
        this.registerFunction("bitxor", new StandardSQLFunction("bitxor", StandardBasicTypes.INTEGER));
        this.registerFunction("ceiling", new StandardSQLFunction("ceiling", StandardBasicTypes.DOUBLE));
        this.registerFunction("cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE));
        this.registerFunction("compress", new StandardSQLFunction("compress", StandardBasicTypes.BINARY));
        this.registerFunction("cot", new StandardSQLFunction("cot", StandardBasicTypes.DOUBLE));
        this.registerFunction("decrypt", new StandardSQLFunction("decrypt", StandardBasicTypes.BINARY));
        this.registerFunction("degrees", new StandardSQLFunction("degrees", StandardBasicTypes.DOUBLE));
        this.registerFunction("encrypt", new StandardSQLFunction("encrypt", StandardBasicTypes.BINARY));
        this.registerFunction("exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE));
        this.registerFunction("expand", new StandardSQLFunction("compress", StandardBasicTypes.BINARY));
        this.registerFunction("floor", new StandardSQLFunction("floor", StandardBasicTypes.DOUBLE));
        this.registerFunction("hash", new StandardSQLFunction("hash", StandardBasicTypes.BINARY));
        this.registerFunction("log", new StandardSQLFunction("log", StandardBasicTypes.DOUBLE));
        this.registerFunction("log10", new StandardSQLFunction("log10", StandardBasicTypes.DOUBLE));
        this.registerFunction("pi", new NoArgSQLFunction("pi", StandardBasicTypes.DOUBLE));
        this.registerFunction("power", new StandardSQLFunction("power", StandardBasicTypes.DOUBLE));
        this.registerFunction("radians", new StandardSQLFunction("radians", StandardBasicTypes.DOUBLE));
        this.registerFunction("rand", new NoArgSQLFunction("rand", StandardBasicTypes.DOUBLE));
        this.registerFunction("round", new StandardSQLFunction("round", StandardBasicTypes.DOUBLE));
        this.registerFunction("roundmagic", new StandardSQLFunction("roundmagic", StandardBasicTypes.DOUBLE));
        this.registerFunction("sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER));
        this.registerFunction("sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE));
        this.registerFunction("tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE));
        this.registerFunction("truncate", new StandardSQLFunction("truncate", StandardBasicTypes.DOUBLE));
        this.registerFunction("ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER));
        this.registerFunction("char", new StandardSQLFunction("char", StandardBasicTypes.CHARACTER));
        this.registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "(", "||", ")"));
        this.registerFunction("difference", new StandardSQLFunction("difference", StandardBasicTypes.INTEGER));
        this.registerFunction("hextoraw", new StandardSQLFunction("hextoraw", StandardBasicTypes.STRING));
        this.registerFunction("insert", new StandardSQLFunction("lower", StandardBasicTypes.STRING));
        this.registerFunction("left", new StandardSQLFunction("left", StandardBasicTypes.STRING));
        this.registerFunction("lcase", new StandardSQLFunction("lcase", StandardBasicTypes.STRING));
        this.registerFunction("ltrim", new StandardSQLFunction("ltrim", StandardBasicTypes.STRING));
        this.registerFunction("octet_length", new StandardSQLFunction("octet_length", StandardBasicTypes.INTEGER));
        this.registerFunction("position", new StandardSQLFunction("position", StandardBasicTypes.INTEGER));
        this.registerFunction("rawtohex", new StandardSQLFunction("rawtohex", StandardBasicTypes.STRING));
        this.registerFunction("repeat", new StandardSQLFunction("repeat", StandardBasicTypes.STRING));
        this.registerFunction("replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING));
        this.registerFunction("right", new StandardSQLFunction("right", StandardBasicTypes.STRING));
        this.registerFunction("rtrim", new StandardSQLFunction("rtrim", StandardBasicTypes.STRING));
        this.registerFunction("soundex", new StandardSQLFunction("soundex", StandardBasicTypes.STRING));
        this.registerFunction("space", new StandardSQLFunction("space", StandardBasicTypes.STRING));
        this.registerFunction("stringencode", new StandardSQLFunction("stringencode", StandardBasicTypes.STRING));
        this.registerFunction("stringdecode", new StandardSQLFunction("stringdecode", StandardBasicTypes.STRING));
        this.registerFunction("stringtoutf8", new StandardSQLFunction("stringtoutf8", StandardBasicTypes.BINARY));
        this.registerFunction("ucase", new StandardSQLFunction("ucase", StandardBasicTypes.STRING));
        this.registerFunction("utf8tostring", new StandardSQLFunction("utf8tostring", StandardBasicTypes.STRING));
        this.registerFunction("curdate", new NoArgSQLFunction("curdate", StandardBasicTypes.DATE));
        this.registerFunction("curtime", new NoArgSQLFunction("curtime", StandardBasicTypes.TIME));
        this.registerFunction("curtimestamp", new NoArgSQLFunction("curtimestamp", StandardBasicTypes.TIME));
        this.registerFunction("current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE));
        this.registerFunction("current_time", new NoArgSQLFunction("current_time", StandardBasicTypes.TIME));
        this.registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("datediff", new StandardSQLFunction("datediff", StandardBasicTypes.INTEGER));
        this.registerFunction("dayname", new StandardSQLFunction("dayname", StandardBasicTypes.STRING));
        this.registerFunction("dayofmonth", new StandardSQLFunction("dayofmonth", StandardBasicTypes.INTEGER));
        this.registerFunction("dayofweek", new StandardSQLFunction("dayofweek", StandardBasicTypes.INTEGER));
        this.registerFunction("dayofyear", new StandardSQLFunction("dayofyear", StandardBasicTypes.INTEGER));
        this.registerFunction("monthname", new StandardSQLFunction("monthname", StandardBasicTypes.STRING));
        this.registerFunction("now", new NoArgSQLFunction("now", StandardBasicTypes.TIMESTAMP));
        this.registerFunction("quarter", new StandardSQLFunction("quarter", StandardBasicTypes.INTEGER));
        this.registerFunction("week", new StandardSQLFunction("week", StandardBasicTypes.INTEGER));
        this.registerFunction("database", new NoArgSQLFunction("database", StandardBasicTypes.STRING));
        this.registerFunction("user", new NoArgSQLFunction("user", StandardBasicTypes.STRING));
        this.getDefaultProperties().setProperty("hibernate.jdbc.batch_size", "15");
        this.getDefaultProperties().setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
    }

    public String getAddColumnString() {
        return "add column";
    }

    public boolean supportsIdentityColumns() {
        return true;
    }

    public String getIdentityColumnString() {
        return "generated by default as identity";
    }

    public String getIdentitySelectString() {
        return "call identity()";
    }

    public String getIdentityInsertString() {
        return "null";
    }

    public String getForUpdateString() {
        return " for update";
    }

    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, boolean hasOffset) {
        return sql + (hasOffset ? " limit ? offset ?" : " limit ?");
    }

    public boolean bindLimitParametersInReverseOrder() {
        return true;
    }

    public boolean bindLimitParametersFirst() {
        return false;
    }

    public boolean supportsIfExistsAfterTableName() {
        return true;
    }

    public boolean supportsIfExistsAfterConstraintName() {
        return true;
    }

    public boolean supportsSequences() {
        return true;
    }

    public boolean supportsPooledSequences() {
        return true;
    }

    public String getCreateSequenceString(String sequenceName) {
        return "create sequence " + sequenceName;
    }

    public String getDropSequenceString(String sequenceName) {
        return "drop sequence " + sequenceName;
    }

    public String getSelectSequenceNextValString(String sequenceName) {
        return "next value for " + sequenceName;
    }

    public String getSequenceNextValString(String sequenceName) {
        return "call next value for " + sequenceName;
    }

    public String getQuerySequencesString() {
        return this.querySequenceString;
    }

    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter() {
        return EXTRACTER;
    }

    public SQLExceptionConversionDelegate buildSQLExceptionConversionDelegate() {
        SQLExceptionConversionDelegate delegate = super.buildSQLExceptionConversionDelegate();
        if (delegate == null) {
            delegate = new SQLExceptionConversionDelegate() {
                public JDBCException convert(SQLException sqlException, String message, String sql) {
                    int errorCode = JdbcExceptionHelper.extractErrorCode(sqlException);
                    if (40001 == errorCode) {
                        return new LockAcquisitionException(message, sqlException, sql);
                    } else if (50200 == errorCode) {
                        return new PessimisticLockException(message, sqlException, sql);
                    } else if (90006 == errorCode) {
                        String constraintName = H2Dialect.this.getViolatedConstraintNameExtracter().extractConstraintName(sqlException);
                        return new ConstraintViolationException(message, sqlException, sql, constraintName);
                    } else {
                        return null;
                    }
                }
            };
        }

        return delegate;
    }

    public boolean supportsTemporaryTables() {
        return true;
    }

    public String getCreateTemporaryTableString() {
        return "create cached local temporary table if not exists";
    }

    public String getCreateTemporaryTablePostfix() {
        return "on commit drop transactional";
    }

    public Boolean performTemporaryTableDDLInIsolation() {
        return Boolean.FALSE;
    }

    public boolean dropTemporaryTableAfterUse() {
        return false;
    }

    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    public String getCurrentTimestampSelectString() {
        return "call current_timestamp()";
    }

    public boolean supportsUnionAll() {
        return true;
    }

    public boolean supportsLobValueChangePropogation() {
        return false;
    }

    public boolean requiresParensForTupleDistinctCounts() {
        return true;
    }

    public boolean doesReadCommittedCauseWritersToBlockReaders() {
        return true;
    }

    public boolean supportsTuplesInSubqueries() {
        return false;
    }
    
    @Override
    public char openQuote() {
        return '`';
    }
    
    @Override
    public char closeQuote() {
        return '`';
    }
}
