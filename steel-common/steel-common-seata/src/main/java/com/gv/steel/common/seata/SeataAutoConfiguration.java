package com.gv.steel.common.seata;

import com.gv.steel.common.core.factory.YamlPropertySourceFactory;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableAutoDataSourceProxy
@PropertySource(
        factory = YamlPropertySourceFactory.class,
        value = {"classpath:seata.yml"}
)
public class SeataAutoConfiguration {
    @Bean
    public DetectTable detectTable(DataSource dataSource) {
        return new DetectTable(dataSource);
    }

    @RequiredArgsConstructor
    public static class DetectTable implements ApplicationRunner {
        private final DataSource dataSource;

        public void run(ApplicationArguments args) {
            try {
                Statement stmt = this.dataSource.getConnection().createStatement();
                String createTableSql = createTableSql();
                stmt.addBatch(createTableSql);
                stmt.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException("seata undo_log 表创建失败！", e);
            }

        }

        private String createTableSql() throws SQLException {
            return "CREATE TABLE IF NOT EXISTS undo_log(" +
                    "id SERIAL NOT NULL," +
                    "branch_id BIGINT NOT NULL," +
                    "xid VARCHAR(100) NOT NULL," +
                    "context VARCHAR(128) NOT NULL," +
                    "rollback_info BYTEA NOT NULL," +
                    "log_status INT NOT NULL," +
                    "log_created TIMESTAMP NOT NULL," +
                    "log_modified TIMESTAMP NOT NULL," +
                    "ext VARCHAR(100) DEFAULT NULL," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE (xid, branch_id));";

        }
    }
}
