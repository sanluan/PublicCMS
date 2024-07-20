package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.tool.api.export.Exporter;
import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.api.metadata.MetadataDescriptor;
import org.hibernate.tool.api.metadata.MetadataDescriptorFactory;
import org.hibernate.tool.api.reveng.RevengSettings;
import org.hibernate.tool.api.reveng.RevengStrategy;
import org.hibernate.tool.api.reveng.RevengStrategyFactory;
import org.hibernate.tool.internal.export.java.JavaExporter;

public class EntitysGenerator {
    protected static final Log log = LogFactory.getLog(EntitysGenerator.class);

    public static void main(String[] args) {
        String packageName = "com.publiccms.entities";
        String outputDirectory = "src/main/java";
        Properties properties = new Properties();
        try (InputStream is = EntitysGenerator.class.getResourceAsStream("/generator/hibernate/hibernate.properties")) {
            properties.load(is);
        } catch (IOException e) {
        }
        RevengStrategy strategy = RevengStrategyFactory.createReverseEngineeringStrategy();
        RevengSettings settings = new RevengSettings(strategy).setDefaultPackageName(packageName).setDetectManyToMany(true)
                .setDetectOneToOne(true).setDetectOptimisticLock(true).setCreateCollectionForForeignKey(true)
                .setCreateManyToOneForForeignKey(true);
        strategy.setSettings(settings);
        Exporter pojoExporter = new JavaExporter();
        MetadataDescriptor metadataDescriptor = MetadataDescriptorFactory.createReverseEngineeringDescriptor(strategy,
                properties);
        pojoExporter.getProperties().put(ExporterConstants.METADATA_DESCRIPTOR, metadataDescriptor);
        pojoExporter.getProperties().put(ExporterConstants.DESTINATION_FOLDER, new File(outputDirectory));
        pojoExporter.getProperties().setProperty("ejb3", String.valueOf(true));
        pojoExporter.getProperties().setProperty("jdk5", String.valueOf(true));
        pojoExporter.start();
        log.info("Generated successfully, please refresh the project!");
        System.exit(0);
    }

}
