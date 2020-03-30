package org.hibernate.tool.api.metadata;

import java.util.Properties;

import org.hibernate.tool.api.reveng.ReverseEngineeringStrategy;
import org.hibernate.tool.internal.metadata.JdbcMetadataDescriptor;

public class MetadataDescriptorFactory {
	
	public static MetadataDescriptor createJdbcDescriptor(
			ReverseEngineeringStrategy reverseEngineeringStrategy, 
			Properties properties,
			boolean preferBasicCompositeIds) {
		return new JdbcMetadataDescriptor(
				reverseEngineeringStrategy, 
				properties,
				preferBasicCompositeIds);
	}
	
}
