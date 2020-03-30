package com.publiccms.common.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.tool.api.export.Exporter;
import org.hibernate.tool.api.export.ExporterConstants;
import org.hibernate.tool.api.metadata.MetadataDescriptor;
import org.hibernate.tool.api.reveng.ReverseEngineeringSettings;
import org.hibernate.tool.api.reveng.ReverseEngineeringStrategy;
import org.hibernate.tool.api.reveng.ReverseEngineeringStrategyFactory;
import org.hibernate.tool.internal.export.pojo.POJOExporter;
import org.hibernate.tool.internal.metadata.JdbcMetadataDescriptor;

public class EntitysGenerator {

    public static void main(String[] args) {
        String jdbcProperties = "src/test/resources/hibernate/hibernate.properties";
        String revengFilePath = "src/test/resources/hibernate/hibernate.reveng.xml";
        String packageName = "com.publiccms.entites";
        String outputDirectory = "src/main/java";
        Properties properties = new Properties();
        try (FileInputStream is = new FileInputStream(new File(jdbcProperties));) {
            properties.load(is);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        ReverseEngineeringStrategy strategy = ReverseEngineeringStrategyFactory.createReverseEngineeringStrategy(
                "org.hibernate.tool.internal.reveng.DefaultReverseEngineeringStrategy", new File[] { new File(revengFilePath) });
        ReverseEngineeringSettings settings = new ReverseEngineeringSettings(strategy).setDefaultPackageName(packageName)
                .setDetectManyToMany(true).setDetectOneToOne(true).setDetectOptimisticLock(true)
                .setCreateCollectionForForeignKey(true).setCreateManyToOneForForeignKey(true);
        strategy.setSettings(settings);
        Exporter pojoExporter = new POJOExporter();
        MetadataDescriptor metadataDescriptor = new JdbcMetadataDescriptor(strategy, properties, true);
        pojoExporter.getProperties().put(ExporterConstants.METADATA_DESCRIPTOR, metadataDescriptor);
        pojoExporter.getProperties().put(ExporterConstants.OUTPUT_FOLDER, new File(outputDirectory));
        pojoExporter.getProperties().setProperty("ejb3", String.valueOf(true));
        pojoExporter.getProperties().setProperty("jdk5", String.valueOf(true));
        pojoExporter.start();
        System.out.println("Generated successfully, please refresh the project!");
        System.exit(0);
    }

}
