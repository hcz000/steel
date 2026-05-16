package com.gv.steel.codegen.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.gv.steel.codegen.dto.CodeGenReqDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GeneratorUtils {
    private static final String schema = "public";
    private static final String ALL = "all";
    private static final String serviceName = "%sService";
    private static final String serviceImplName = "%sServiceImpl";
    private static final String mapperName = "%sDao";
    private static final String controllerName = "%sController";
    private static final String xmlName = "%sMapper";
    private static final String modelTemplate = "templates/Entity.java.vm";
    private static final String daoTemplate = "templates/Dao.java.vm";
    private static final String mapperTemplate = "templates/Mapper.xml.vm";
    private static final String serviceTemplate = "templates/Service.java.vm";
    private static final String serviceImplTemplate = "templates/ServiceImpl.java.vm";
    private static final String controllerTemplate = "templates/Controller.java.vm";
    private static final String superBaseModelClass = "com.gv.steel.common.mybatis.base.entity.BaseEntity";
    private static final String superBaseDaoClass = "com.gv.steel.common.mybatis.base.dao.BaseDao";
    private static final String superBaseServiceClass = "com.gv.steel.common.mybatis.base.service.BaseService";
    private static final String superBaseServiceImplClass = "com.gv.steel.common.mybatis.base.service.impl.BaseServiceImpl";
    private static final String[] superEntityColumns = new String[]{"id" , "version" , "delete_flag" , "tenant_id" , "create_time" , "create_by" , "update_time" , "update_by"};
    private static final String tableCommentSql = "SELECT obj_description(relfilenode) AS comments FROM sys_tables A, sys_class B WHERE A.schemaname='%s' AND A.tablename ='%s' AND A.tablename = B.relname";

    public static void execute(CodeGenReqDto dto, OutputStream os) {

        dto.setPrefix(StrUtil.isBlank(dto.getPrefix()) ? "" : dto.getPrefix());

        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        dto.setOutputDir("temp/" + UUID.fastUUID().toString(true) + "/" );
        gc.setOutputDir(dto.getOutputDir());

        gc.setIdType(null);
        gc.setFileOverride(true);
        //ActiveRecord特性
        gc.setActiveRecord(true);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setEnableCache(false);
        // 自动打开输出目录
        gc.setOpen(false);
        gc.setAuthor("administrator" );
        gc.setSwagger2(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName(serviceName);
        gc.setServiceImplName(serviceImplName);
        gc.setMapperName(mapperName);
        gc.setControllerName(controllerName);
        gc.setXmlName(xmlName);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(dto.getDbType());
        dsc.setSchemaName(schema);
        dsc.setDriverName(dto.getDriverName());
        dsc.setUrl(dto.getUrl());
        dsc.setUsername(dto.getUsername());
        dsc.setPassword(dto.getPassword());
        mpg.setDataSource(dsc);

        // 包配置b
        PackageConfig pc = new PackageConfig();
        // 此处设置包名，需要自定义
        pc.setParent(dto.getPackageName());
        pc.setModuleName(dto.getModelName());
        pc.setXml("mapper");
        pc.setMapper("dao" );
        pc.setEntity("entity");
        mpg.setPackageInfo(pc);

        InjectionConfig cfg = new InjectionConfig() {
            Map<String, Object> map = new HashMap<>(1);

            @Override
            public void initMap() {
                this.setMap(map);
            }

            @Override
            public void initTableMap(TableInfo tableInfo) {
                map.put("servicename", StringUtils.uncapitalize(tableInfo.getServiceName()));
                map.put("classname", StringUtils.uncapitalize(tableInfo.getEntityName()));
                map.put("baseWhere", true);
                map.put("conditionWhere", true);
            }

            @Override
            public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
       /*         TableInfo tableInfo = null;
                if (ObjectUtil.isNotEmpty(objectMap.get("table"))) {
                    tableInfo = (TableInfo) objectMap.get("table");
                }
                if (ObjectUtil.isNotEmpty(tableInfo) && StringUtils.isNotBlank(tableInfo.getName()) && StringUtils.isEmpty(tableInfo.getComment())) {
                    String tableComment = String.format(tableCommentSql, schema, tableInfo.getName());
                    try (PreparedStatement preparedStatement = dsc.getConn().prepareStatement(tableComment);
                         ResultSet results = preparedStatement.executeQuery()) {
                        while (results.next()) {
                            tableInfo.setComment(results.getString("comments"));
                        }
                    } catch (Exception e) {
                        log.error("获取表描述失败", e);
                    }
                }
*/
                return objectMap;
            }
        };
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(modelTemplate);
        templateConfig.setMapper(daoTemplate);
        templateConfig.setXml(mapperTemplate);
        templateConfig.setService(serviceTemplate);
        templateConfig.setServiceImpl(serviceImplTemplate);
        templateConfig.setController(controllerTemplate);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperEntityClass(superBaseModelClass);
        strategy.setSuperMapperClass(superBaseDaoClass);
        strategy.setSuperServiceClass(superBaseServiceClass);
        strategy.setSuperServiceImplClass(superBaseServiceImplClass);

        // 写于父类中的公共字段
        strategy.setSuperEntityColumns(superEntityColumns);
        if (ObjectUtil.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getTableName()) && !ALL.equalsIgnoreCase(dto.getTableName())) {
            strategy.setInclude(org.apache.commons.lang3.StringUtils.split(dto.getTableName(), "," ));
        }
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(dto.getPrefix());
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
