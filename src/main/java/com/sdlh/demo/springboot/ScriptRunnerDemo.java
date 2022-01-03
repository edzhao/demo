/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.springboot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class ScriptRunnerDemo {
    private static String new_url;
    private static String old_url;
    private static String username;
    private static String password;
    private static String dbname = "ocean";
    private static HikariDataSource hikariDataSource;

    static {
        try {
            Properties props = Resources.getResourceAsProperties("application.properties");
            new_url = props.getProperty("mysql.jdbc.url");
            old_url = props.getProperty("spring.datasource.url");
            username = props.getProperty("spring.datasource.username");
            password = props.getProperty("spring.datasource.password");

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(props.getProperty("spring.datasource.driver-class-name"));
            hikariConfig.setJdbcUrl(new_url + "/" + dbname + "?" + props.getProperty("mysql.jdbc.url.parameter"));
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setMinimumIdle(Integer.parseInt(props.getProperty("spring.datasource.hikari.minimum-idle")));
            hikariConfig.setMaximumPoolSize(Integer.parseInt(props.getProperty("spring.datasource.hikari.maximum-pool-size")));
            hikariConfig.setIdleTimeout(Integer.parseInt(props.getProperty("spring.datasource.hikari.idle-timeout")));
            hikariConfig.setMaxLifetime(Integer.parseInt(props.getProperty("spring.datasource.hikari.max-lifetime")));
            hikariConfig.setConnectionTimeout(Integer.parseInt(props.getProperty("spring.datasource.hikari.connection-timeout")));
            hikariConfig.setConnectionTestQuery(props.getProperty("spring.datasource.hikari.connection-test-query"));
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection(old_url, username, password)) {
            copyDB(conn);
            initDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyDB(Connection conn) {
        try(Statement statement = conn.createStatement()) {
            log.info("删除测试数据库......");
            statement.execute("DROP DATABASE IF EXISTS " + dbname);
            log.info("创建新数据库......");
            statement.execute("CREATE DATABASE IF NOT EXISTS " + dbname +" DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initDatabase() {
        try {
            log.info("数据库创建完毕，开始执行脚本");
            List<CompletableFuture> futureList = new ArrayList<>();
            long t = System.currentTimeMillis();
            futureList.add(initTables("ocean.sql"));
            futureList.add(initTables("downlink_minn.sql"));
            futureList.add(initTables("monitor_MinN.sql"));
            futureList.add(initProcedure("ocean_procedure.sql"));
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
            log.info("所有脚本执行完毕，花费时间" + (System.currentTimeMillis() - t) + "ms");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private static CompletableFuture<Void> initTables(String sql) {
        CompletableFuture<Void> future = null;

        try {
            Connection conn = hikariDataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setSendFullScript(true);
            runner.setLogWriter(null);
            Resources.setCharset(Charset.forName("UTF-8"));
            future = runAsync(runner, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return future;
    }

    private static CompletableFuture<Void> initProcedure(String sql) {
        CompletableFuture<Void> future = null;

        try {
            Connection conn = hikariDataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setSendFullScript(false);
            runner.setFullLineDelimiter(true);
            runner.setDelimiter("delimiter ;;");
            Resources.setCharset(Charset.forName("UTF-8"));
            runner.setLogWriter(null);
            future = runAsync(runner, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return future;
    }

    private static CompletableFuture<Void> runAsync(ScriptRunner runner, String script) {
        return CompletableFuture.runAsync(() -> {
            long t =System.currentTimeMillis();
            try {
                runner.setAutoCommit(false);
                runner.runScript(Resources.getResourceAsReader(script));
                runner.setAutoCommit(true);
                runner.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info(script + "脚本执行完毕，花费时间" + (System.currentTimeMillis() - t) + "ms");
        });
    }
}
