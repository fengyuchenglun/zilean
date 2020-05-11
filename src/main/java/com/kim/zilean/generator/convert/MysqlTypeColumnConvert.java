package com.kim.zilean.generator.convert;

import com.kim.zilean.generator.convert.IColumnType;

import javax.swing.text.html.Option;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mysql字段类型转换器
 *
 */
public enum MysqlTypeColumnConvert implements IColumnType {
    BIT("bit", JDBCType.BIT, ""),
    TINYINT("tinyint", JDBCType.TINYINT, ""),
    SMALLINT("smallint", JDBCType.SMALLINT, ""),
    MEDIUMINT("mediumint", JDBCType.INTEGER, ""),
    INTEGER("int", JDBCType.INTEGER, ""),
    BIGINT("bigint", JDBCType.BIGINT, ""),
    FLOAT("float", JDBCType.FLOAT, ""),
    REAL("real", JDBCType.REAL, ""),
    DOUBLE("double", JDBCType.DOUBLE, ""),
    NUMERIC("numeric", JDBCType.NUMERIC, ""),
    DECIMAL("decimal", JDBCType.DECIMAL, "java.math.BigDecimal"),
    CHAR("char", JDBCType.CHAR, ""),
    VARCHAR("varchar", JDBCType.VARCHAR, ""),
    LONGVARCHAR("longvarchar", JDBCType.LONGVARCHAR, ""),
    TEXT("text", JDBCType.LONGNVARCHAR, ""),
    DATE("date", JDBCType.DATE, "java.time.LocalDate"),
    TIME("time", JDBCType.TIME, "java.time.LocalTime"),
    DATETIME("datetime", JDBCType.TIMESTAMP, "java.time.LocalDateTime"),
    TIMESTAMP("timestamp", JDBCType.TIMESTAMP, "java.time.LocalDateTime"),
    BINARY("binary", JDBCType.BINARY, "Byte[]"),
    VARBINARY("varbinary", JDBCType.VARBINARY, "Byte[]"),
    LONGVARBINARY("longvarbinary", JDBCType.LONGVARBINARY, "Byte[]"),
    JAVA_OBJECT("object", JDBCType.JAVA_OBJECT, "Object"),
    BLOB("blob", JDBCType.BLOB, "Object"),
    BOOL("bool", JDBCType.BOOLEAN, "Boolean"),
    BOOLEAN("boolean", JDBCType.BOOLEAN, "Boolean"),
    OTHER("other", JDBCType.OTHER, "Object"),
    ;
    /**
     * 类型
     */
    private final String type;

    /**
     * java类型
     */
    private final String javaType;

    /**
     * jdbc类型
     */
    private final JDBCType jdbcType;

    MysqlTypeColumnConvert(final String type, final JDBCType jdbcType, final String javaType) {
        this.type = type;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getJavaType() {
        return javaType;
    }

    @Override
    public JDBCType getJdbcType() {
        return jdbcType;
    }

    private static Map<String,IColumnType> COLUMN_CACHE_MAP = new ConcurrentHashMap<>();

    static {
        for (IColumnType columnType : MysqlTypeColumnConvert.values()) {
            COLUMN_CACHE_MAP.put(columnType.getType().toLowerCase(), columnType);
        }
    }

    public static IColumnType typeOf(String type) {
        return Optional.ofNullable(COLUMN_CACHE_MAP.get(type)).orElse(OTHER);
    }
}
