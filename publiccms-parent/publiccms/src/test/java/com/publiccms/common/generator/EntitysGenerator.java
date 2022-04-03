package com.publiccms.common.generator;

//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
//import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
//import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
//import org.hibernate.tool.api.metadata.MetadataDescriptor;
//import org.hibernate.tool.hbm2x.Exporter;
//import org.hibernate.tool.hbm2x.POJOExporter;
//import org.hibernate.tool.internal.metadata.JdbcMetadataDescriptor;

public class EntitysGenerator {

    public static void main(String[] args) {
//        String packageName = "com.publiccms.entities";
//        String outputDirectory = "src/main/java";
        Properties properties = new Properties();
        try (InputStream is = EntitysGenerator.class.getResourceAsStream("/generator/hibernate/hibernate.properties");) {
            properties.load(is);
        } catch (IOException e) {
        }
//        ReverseEngineeringStrategy strategy = new DefaultReverseEngineeringStrategy();
//        ReverseEngineeringSettings settings = new ReverseEngineeringSettings(strategy).setDefaultPackageName(packageName)
//                .setDetectManyToMany(true).setDetectOneToOne(true).setDetectOptimisticLock(true)
//                .setCreateCollectionForForeignKey(true).setCreateManyToOneForForeignKey(true);
//        strategy.setSettings(settings);
//        Exporter pojoExporter = new POJOExporter();
//        MetadataDescriptor metadataDescriptor = new JdbcMetadataDescriptor(strategy, properties, true);
//        pojoExporter.setMetadataDescriptor(metadataDescriptor);
//        pojoExporter.setOutputDirectory(new File(outputDirectory));
//        pojoExporter.getProperties().setProperty("ejb3", String.valueOf(true));
//        pojoExporter.getProperties().setProperty("jdk5", String.valueOf(true));
//        pojoExporter.start();
//        System.out.println("Generated successfully, please refresh the project!");
//        System.exit(0);
    }

}
