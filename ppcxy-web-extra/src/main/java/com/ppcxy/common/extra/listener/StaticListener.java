package com.ppcxy.common.extra.listener;

import com.ppcxy.common.Profiles;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.common.utils.LinkedProperties;
import com.ppcxy.common.utils.LogUtils;
import com.ppcxy.jdbc.dialect.DialectUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 监听器-启动过程根据当前数据源进行数据初始化操作.
 */
public class StaticListener implements ServletContextListener {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
    
    

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //判断数据情况进行数据初始化,读配置文件是否是安装,安装完毕后回写为安装完成
        System.out.println("**********************************************");
        System.out.println("*    宸艺快速开发框架V0.1 bate by ppcxy.com  *");
        System.out.println("*    github: https://github.com/ppcxy/cyfm   *");
        System.out.println("*                                            *");
        System.out.println("**********************************************");

        //如果没有设定 Profiles 则默认认为是线上环境.
        if (System.getProperty(Profiles.ACTIVE_PROFILE) == null) {
            Profiles.setProfileAsSystemProperty(Profiles.PRODUCTION);
        }


        LinkedProperties prop = new LinkedProperties();//属性集合对象

        File propFile = null;
        FileInputStream fis = null;//属性文件流

        try {

            propFile = ResourceUtils.getFile("classpath:application.properties");
            fis = new FileInputStream(propFile);
            prop.load(fis);//将属性文件流装载到Properties对象中

            //开发模式启动情况加载development属性文件
            if (System.getProperty(Profiles.ACTIVE_PROFILE).equals(Profiles.DEVELOPMENT)) {
                propFile = ResourceUtils.getFile("classpath:application.development.properties");
                fis = new FileInputStream(propFile);
                prop.load(fis);
            }

            //测试模式启动情况加载test属性文件
            if (System.getProperty(Profiles.ACTIVE_PROFILE).equals(Profiles.FUNCTIONAL_TEST)) {
                propFile = ResourceUtils.getFile("classpath:application.test.properties");
                fis = new FileInputStream(propFile);
                prop.load(fis);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String runModel = prop.getProperty("run.model");
        String jdbcUrl = prop.getProperty("jdbc.url");
        String dbType = DialectUtils.getDatabaseName(jdbcUrl);


        System.out.println(String.format("=============>%s", "运行模式:") + runModel);
        System.out.println(String.format("=============>%s", "存储类型:") + dbType);
        System.out.println(String.format("=============>%s", "因对于生产环境存在风险，暂时取消数据库初始化和更新能力。") + dbType);

        //if ("init".equals(runModel)) {
        //    try {
        //        switch (dbType) {
        //            case "mysql":
        //                initDbMysql();
        //                break;
        //            default:
        //                initDbH2();
        //        }
        //
        //        prop.setProperty("run.model", "update");
        //        FileOutputStream fos = new FileOutputStream(propFile);
        //        // 将Properties集合保存到流中
        //        prop.store(fos, new Date().toString() + " update run model from init to update.");
        //        fos.close();// 关闭流
        //    } catch (FileNotFoundException e) {
        //        System.out.println("严重错误:未找到用来初始化数据的脚本..." + e.getMessage());
        //        System.exit(3);
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
        //} else if ("update".equals(runModel)) {
        //    update();
        //}
    }

    /**
     * 根据当前数据库版本进行更新
     */
    private void update() {
        System.out.println("*    正在检查当前连接数据库版本...");
        System.out.println("**********************************************");
        System.err.println("*    数据库升级模块计划开发中,敬请期待...");
    }

    /**
     * 根据脚本进行数据库初始化操作
     *
     * @param schemaFlie
     * @param dataFile
     * @param otherSqlFiles
     */
    private void initDb(File schemaFlie, File dataFile, File... otherSqlFiles) {
        System.out.println("*    准备执行数据库初始化操作...");
        System.out.println("**********************************************");
        System.out.println("*    表初始化开始");
        execSqlFile(schemaFlie);
        System.out.println("*    表初始化结束");
        System.out.println("**********************************************");
        System.out.println("*    表数据初始化开始");
        execSqlFile(dataFile);
        System.out.println("*    表数据初始化结束");
        for (File ofile : otherSqlFiles) {
            System.out.println("**********************************************");
            System.out.println("*    其他初始化开始");
            execSqlFile(ofile);
            System.out.println("*    其他据初始化结束");
        }

    }

    /**
     * 初始化 sqlserver 数据库
     *
     * @throws FileNotFoundException
     */
    private void initDbMSSQLServer() throws FileNotFoundException {
        String dir = "classpath:sql/sqlserver/";

        File schemaFlie = ResourceUtils.getFile(dir + "schema.sql");
        File dataFile = ResourceUtils.getFile(dir + "import-data.sql");

        initDb(schemaFlie, dataFile);
    }

    /**
     * 初始化 oracle 数据库
     *
     * @throws FileNotFoundException
     */
    private void initDbOracle() throws FileNotFoundException {
        String dir = "classpath:sql/oracle/";

        File schemaFlie = ResourceUtils.getFile(dir + "schema.sql");
        File dataFile = ResourceUtils.getFile(dir + "import-data.sql");

        initDb(schemaFlie, dataFile);
    }

    /**
     * 初始化 mysql 数据库
     *
     * @throws FileNotFoundException
     */
    private void initDbMysql() throws FileNotFoundException {
        String dir = "classpath:sql/mysql/";

        File schemaFlie = ResourceUtils.getFile(dir + "schema.sql");
        File dataFile = ResourceUtils.getFile(dir + "import-data.sql");

        initDb(schemaFlie, dataFile);
    }

    /**
     * 初始化 h2
     *
     * @throws FileNotFoundException
     */
    private void initDbH2() throws FileNotFoundException {
        String dir = "classpath:sql/h2/";

        File schemaFlie = ResourceUtils.getFile(dir + "schema.sql");
        File dataFile = ResourceUtils.getFile(dir + "import-data.sql");

        initDb(schemaFlie, dataFile);
    }


    /**
     * 执行sql文件中的内容
     *
     * @param file
     * @return
     */
    private boolean execSqlFile(File file) {
        if (file == null) {
            return false;
        }
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
        }

        Connection connection = null;

        try {
            String sqls = FileUtils.readFileToString(file, "utf-8");
            batchExecute(sqls.replaceAll("\r", "").split(";\n"));
        } catch (IOException e) {
            LogUtils.logError("读取数据库初始化脚本发生错误,请注意.", e);
        }


        return true;
    }

    /**
     * 因为将数据源设设置为非自动提交,所以单独获取 connection 执行 sql
     *
     * @param sqls
     * @return
     */
    private int batchExecute(String[] sqls) {
        Connection connection = null;
        try {
            connection = jdbcTemplate.getDataSource().getConnection();
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            for (String sql : sqls) {
                statement.addBatch(sql);
            }

            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    if (!connection.isClosed()) {
                        connection.close();
                    }

                }
            } catch (SQLException e) {
                LogUtils.logError("初始化数据库过程释放链接失败,请注意.", e);
            }
        }


        return 1;

    }
}
