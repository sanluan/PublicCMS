package com.sanluan.common.dialect;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.type.StandardBasicTypes;

/**
 * 
 * MyDialect Mysql方言处理
 *
 */
public class MyDialect extends MySQLDialect{

    protected void registerVarcharTypes() {
        super.registerVarcharTypes();
        registerColumnType(Types.CHAR, 255, "char($l)");
        registerHibernateType( Types.CHAR, StandardBasicTypes.STRING.getName() );
    }

}
