package com.publiccms.common.database;

import org.hibernate.generator.Generator;
import org.hibernate.id.IdentityGenerator;

import com.publiccms.common.datasource.IDSequenceStyleGenerator;

/**
 * IDSequenceStyleGenerator for sequence, IdentityGenerator for id
 */
public class IDStyleGenerator extends IdentityGenerator {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 表名_ID_SEQ SEQUENCE主键策略
     */
    public static final Class<? extends Generator> IDENTIFIER_GENERATOR_SEQUENCE = IDSequenceStyleGenerator.class;
    /**
     * ID自增主键策略
     */
    public static final Class<? extends Generator> IDENTIFIER_GENERATOR_IDENTITY = IdentityGenerator.class;
}
