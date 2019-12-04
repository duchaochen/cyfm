package com.ppcxy.common.utils;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;

import java.util.List;

public class SQLParser {

    public static StringBuffer sqlParserUitl(String sql) throws Exception {
        StringBuffer buffer = new StringBuffer();
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);

        for (SQLStatement stmt : statementList) {
            stmt.accept(visitor);
            buffer.append(out.toString());
            out.setLength(0);
        }
        return buffer;
    }

}
