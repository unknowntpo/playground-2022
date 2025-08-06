package org.example.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class AuditInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuditInterceptor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        logger.debug("Intercepting SQL operation: {} for statement: {}",
                    sqlCommandType, mappedStatement.getId());

        if (sqlCommandType == SqlCommandType.INSERT ||
            sqlCommandType == SqlCommandType.UPDATE ||
            sqlCommandType == SqlCommandType.DELETE) {

            logger.debug("Executing original SQL operation: {}", sqlCommandType);
            Object result = invocation.proceed();

            logger.debug("Original SQL operation completed, now logging audit event");
            logChangeEvent(mappedStatement, parameter, sqlCommandType, invocation);

            return result;
        }

        logger.debug("Skipping audit for SELECT operation: {}", sqlCommandType);
        return invocation.proceed();
    }

    private void logChangeEvent(MappedStatement mappedStatement, Object parameter,
                               SqlCommandType sqlCommandType, Invocation invocation) {
        try {
            Executor executor = (Executor) invocation.getTarget();

            String tableName = extractTableName(mappedStatement.getId());
            String operationType = sqlCommandType.name();
            String dataJson = convertParameterToJson(parameter);

            logger.debug("Creating audit log entry for table: {}, operation: {}", tableName, operationType);

            String insertSql = "INSERT INTO change_event (timestamp, table_name, operation_type, data) VALUES (?, ?, ?, ?)";

            Connection connection = executor.getTransaction().getConnection();
            logger.debug("Using connection from transaction: {}, autoCommit: {}, transactionIsolation: {}",
                        connection.hashCode(), connection.getAutoCommit(), connection.getTransactionIsolation());
            logger.debug("Connection toString: {}", connection.toString());

            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(2, tableName);
                ps.setString(3, operationType);
                ps.setString(4, dataJson);

                logger.debug("Executing audit log insert SQL: {}", insertSql);
                logger.debug("About to execute audit insert - connection state before: autoCommit={}",
                            connection.getAutoCommit());
                int rowsAffected = ps.executeUpdate();
                logger.debug("Audit log insert completed, rows affected: {}", rowsAffected);
                logger.debug("After audit insert - connection state: autoCommit={}",
                            connection.getAutoCommit());
            }

        } catch (Exception e) {
            logger.error("Failed to log change event", e);
        }
    }

    private String extractTableName(String statementId) {
        String[] parts = statementId.split("\\.");
        if (parts.length >= 2) {
            String mapperName = parts[parts.length - 2];
            return mapperName.toLowerCase().replaceAll("mapper$", "");
        }
        return "unknown";
    }

    private String convertParameterToJson(Object parameter) {
        try {
            if (parameter == null) {
                return "{}";
            }

            Map<String, Object> paramMap = new HashMap<>();
            if (parameter instanceof Map) {
                paramMap.putAll((Map<String, Object>) parameter);
            } else {
                paramMap.put("data", parameter);
            }

            return objectMapper.writeValueAsString(paramMap);
        } catch (Exception e) {
            return "{\"error\": \"Failed to serialize parameter\"}";
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
