package org.hibernate.tool.internal.export.pojo;

import java.util.Iterator;
import java.util.List;

import org.hibernate.mapping.Property;

/**
 * Wrapper class over PersistentClass used in hbm2java and hbm2doc tool
 * @author max
 * @author <a href="mailto:abhayani@jboss.org">Amit Bhayani</a>
 *
 */
public interface POJOClass extends ImportContext {

	/** 
	 * Returns "package packagename;" where packagename is either the declared packagename,
	 * or the one provide via meta attribute "generated-class".
	 * 
	 * Returns "// default package" if no package declarition available.
	 * @return 
	 *  
	 */
	String getPackageDeclaration();
	
	String getClassModifiers();

	String getQualifiedDeclarationName();
	
	/**
	 * Returns the javadoc associated with the class.
	 * 
	 * @param fallback the default text if nothing else is found
	 * @param indent how many spaces should be added
	 * @return
	 */
	String getClassJavaDoc(String fallback, int indent);
	
	/**
	 * 
	 * @return declaration type "interface" or "class"
	 */
	String getDeclarationType();
	
	/**
	 * @return unqualified classname for this class (can be changed by meta attribute "generated-class")
	 */
	String getDeclarationName();
	
	String getImplementsDeclaration();
	String getImplements();
	
	String getExtendsDeclaration();
	String getExtends();
	
	String generateEquals(String thisName, String otherName, boolean useGenerics);
	
	boolean isComponent();
	
	String getExtraClassCode();
		
	boolean needsEqualsHashCode();
	
	boolean hasIdentifierProperty();
	
	String generateAnnColumnAnnotation(Property property);
	String generateAnnIdGenerator();
	String generateAnnTableUniqueConstraint();
	String generateBasicAnnotation(Property property);
	Iterator<Property> getAllPropertiesIterator();

	String getPackageName();
	String getShortName();

	Iterator<Property> getToStringPropertiesIterator();
	Iterator<Property> getEqualsHashCodePropertiesIterator();
	
	boolean needsToString();
	
	String getFieldJavaDoc(Property property, int indent);
	String getFieldDescription(Property property);

	Object getDecoratedObject();

	boolean isInterface();
	
	boolean isSubclass();

	List<Property> getPropertiesForFullConstructor();
	List<Property> getPropertyClosureForFullConstructor();
	List<Property> getPropertyClosureForSuperclassFullConstructor();
	
	boolean needsMinimalConstructor();
	boolean needsFullConstructor();
	List<Property> getPropertiesForMinimalConstructor();
	List<Property> getPropertyClosureForMinimalConstructor();
	List<Property> getPropertyClosureForSuperclassMinimalConstructor();
	
	POJOClass getSuperClass();
	
	String getJavaTypeName(Property p, boolean useGenerics);
	String getFieldInitialization(Property p, boolean useGenerics);
	
	Property getIdentifierProperty();
	
	boolean hasVersionProperty();
	Property getVersionProperty();
		
}
