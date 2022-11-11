package io.sinso.dataland.common.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author lee
 */
public class DataLandMybatisPlusGenerator {
    private static final String RPC_API_JAVA_ROOT_PATH = "/dataland-admin/src/main/java/";
    private static final String RPC_SERVICE_JAVA_ROOT_PATH = "/dataland-admin/src/main/java/";
    private static final String RPC_SERVICE_RESOURCES_ROOT_PATH = "/dataland-admin/src/main/resources/";
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("Please enter" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("Please enter the correct one" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/dataland-admin/src/main/java");
        gc.setAuthor("lee");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.233:3306/dataland?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&useAffectedRows=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("dataland");
        dsc.setPassword("dl@!@#sdsf123");
        mpg.setDataSource(dsc);

        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("Module name"));
        mpg.setPackageInfo(pc);
        StrategyConfig strategy = new StrategyConfig();
        strategy.setVersionFieldName("revision");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("Table name, multiple English comma separated").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setEntityColumnConstant(true);
        strategy.setEntityBuilderModel(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        strategy.setEntityBooleanColumnRemoveIsPrefix(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        try {
            customPackagePath(pc, mpg);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        mpg.execute();
    }

    /**

     * @param pc
     * @param mpg
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void customPackagePath(PackageConfig pc, AutoGenerator mpg) throws NoSuchFieldException, IllegalAccessException {
        String projectPath = System.getProperty("user.dir");
        Map<String, String> packageInfo = new HashMap<>(5);
        packageInfo.put(ConstVal.SERVICE, "io.sinso.dataland.service");
        packageInfo.put(ConstVal.SERVICE_IMPL, "io.sinso.dataland.service.impl");
        packageInfo.put(ConstVal.ENTITY, "io.sinso.dataland.model");
        packageInfo.put(ConstVal.MAPPER, "io.sinso.dataland.mapper");
        packageInfo.put(ConstVal.XML, "mapper");
        Map<String, String> pathInfo = new HashMap<>(6);
        pathInfo.put(ConstVal.SERVICE_PATH, projectPath + RPC_API_JAVA_ROOT_PATH + packageInfo.get(ConstVal.SERVICE).replaceAll("\\.", StringPool.BACK_SLASH + File.separator));
        pathInfo.put(ConstVal.SERVICE_IMPL_PATH, projectPath + RPC_SERVICE_JAVA_ROOT_PATH + packageInfo.get(ConstVal.SERVICE_IMPL).replaceAll("\\.", StringPool.BACK_SLASH + File.separator));
        pathInfo.put(ConstVal.ENTITY_PATH, projectPath + RPC_API_JAVA_ROOT_PATH + packageInfo.get(ConstVal.ENTITY).replaceAll("\\.", StringPool.BACK_SLASH + File.separator));
        pathInfo.put(ConstVal.MAPPER_PATH, projectPath + RPC_SERVICE_JAVA_ROOT_PATH + packageInfo.get(ConstVal.MAPPER).replaceAll("\\.", StringPool.BACK_SLASH + File.separator));
        pathInfo.put(ConstVal.XML_PATH, projectPath + RPC_SERVICE_RESOURCES_ROOT_PATH + packageInfo.get(ConstVal.XML));
        pc.setPathInfo(pathInfo);
        ConfigBuilder configBuilder = new ConfigBuilder(mpg.getPackageInfo(), mpg.getDataSource(), mpg.getStrategy(), mpg.getTemplate(), mpg.getGlobalConfig());
        Field packageInfoField = configBuilder.getClass().getDeclaredField("packageInfo");
        packageInfoField.setAccessible(true);
        packageInfoField.set(configBuilder, packageInfo);
        mpg.setConfig(configBuilder);
    }
}
