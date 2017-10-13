 
create table ${tableName} (
<#if  idField.type== 'Integer' >
        ${idField.columnName} INTEGER,
<#elseif idField.type== 'Long' >
        ${idField.columnName} BIGINT,
<#else>
        ${idField.columnName} VARCHAR(<#if idField.length gt 0>${idField.length}<#else>50</#if>),
</#if>
<#if pojo_fields?exists>
  <#list  pojo_fields as field>
   <#if field.name?exists && field.columnName?exists && field.type?exists>
	<#if field.type?exists && ( field.type== 'Integer' )>
        ${field.columnName}  INTEGER,
        <#elseif field.type?exists && ( field.type== 'Long' )>
        ${field.columnName} BIGINT,
	<#elseif field.type?exists && ( field.type== 'Boolean' )>
        ${field.columnName} BOOLEAN,
	<#elseif field.type?exists && ( field.type== 'Double' )>
        ${field.columnName} DOUBLE,
	<#elseif field.type?exists && ( field.type== 'Date')>
        ${field.columnName} TIMESTAMP,
	<#elseif field.type?exists && ( field.type== 'String')>
        ${field.columnName} VARCHAR(<#if field.length gt 0>${field.length?string("####")}<#else>250</#if>),
	</#if>
    </#if>
  </#list>
</#if>
        CONSTRAINT pk PRIMARY KEY (${idField.columnName})
) ;

