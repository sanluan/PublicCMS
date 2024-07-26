package com.publiccms.common.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.tool.api.export.Exporter;
import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.api.metadata.MetadataDescriptor;
import org.hibernate.tool.api.metadata.MetadataDescriptorFactory;
import org.hibernate.tool.api.reveng.RevengStrategy;
import org.hibernate.tool.api.reveng.RevengStrategyFactory;
import org.hibernate.tool.internal.export.ddl.DdlExporter;

public class TableDDLGenerator {
    protected static final Log log = LogFactory.getLog(TableDDLGenerator.class);

    public static void main(String[] args) {
        String outPutFileName = "cms.sql";
        Properties properties = new Properties();
        try (InputStream is = EntitysGenerator.class.getResourceAsStream("/generator/hibernate/hibernate.properties")) {
            properties.load(is);
        } catch (IOException e) {
        }
        RevengStrategy strategy = RevengStrategyFactory.createReverseEngineeringStrategy();
        MetadataDescriptor metadataDescriptor = MetadataDescriptorFactory.createReverseEngineeringDescriptor(strategy,
                properties);
        Exporter pojoExporter = new DdlExporter();
        pojoExporter.getProperties().put(ExporterConstants.METADATA_DESCRIPTOR, metadataDescriptor);
        pojoExporter.getProperties().put(ExporterConstants.OUTPUT_FILE_NAME, outPutFileName);
        pojoExporter.start();
        log.info("Generated successfully, please refresh the project!");
        System.exit(0);
    }

}
