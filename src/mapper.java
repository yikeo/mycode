package test;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * GenerateMyBatisMapper
 *
 * @author Wu Jing
 * @date 2017-09-14 17:15:00
 */
public class GenerateMyBatisMapper {

    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final Map<String, String> SQL_TYPE_MAP = new HashMap();

    static {
        SQL_TYPE_MAP.put("String", "VARCHAR2(255)");
        SQL_TYPE_MAP.put("BigDecimal", "NUMBER");
        SQL_TYPE_MAP.put("boolean", "NUMBER");
        SQL_TYPE_MAP.put("byte", "NUMBER");
        SQL_TYPE_MAP.put("short", "NUMBER");
        SQL_TYPE_MAP.put("int", "NUMBER");
        SQL_TYPE_MAP.put("long", "NUMBER");
        SQL_TYPE_MAP.put("float", "NUMBER");
        SQL_TYPE_MAP.put("double", "NUMBER");
        SQL_TYPE_MAP.put("Boolean", "NUMBER");
        SQL_TYPE_MAP.put("Byte", "NUMBER");
        SQL_TYPE_MAP.put("Short", "NUMBER");
        SQL_TYPE_MAP.put("Integer", "NUMBER");
        SQL_TYPE_MAP.put("Long", "NUMBER");
        SQL_TYPE_MAP.put("Float", "NUMBER");
        SQL_TYPE_MAP.put("Double", "NUMBER");
        SQL_TYPE_MAP.put("Date", "TIMESTAMP");
        SQL_TYPE_MAP.put("Timestamp", "TIMESTAMP");
    }


    public static void main(String[] args) throws Exception {

        //通过VO自动生成 Mapper.java, Mapper.xml,
        String modelClassName = "com.thunis.ba.model.cisco.report.CiscoReportDeptNoPurchaseVo";
        String tableName = "CiscoReportDeptNoPurchaseVo";
        String mapperClassName = "";
        String sequenceName = "";
        String filePath = "";

        generate(modelClassName, tableName, mapperClassName, sequenceName, filePath);

    }

    private static void generate(
            String modelClassName, String tableName,
            String mapperClassName, String sequenceName,
            String filePath) throws Exception {

        String xmlBuff = generateMapperXmlBuff(modelClassName, tableName, mapperClassName, sequenceName);

        String mapperBuff = generateMapperBuff(modelClassName, mapperClassName);

        String sqlBuff = generateSqlBuff(modelClassName, tableName);

        String sequenceBuff = generateSequenceSqlBuff(tableName, sequenceName);

        if (mapperClassName == null || "".equals(mapperClassName)) {
            mapperClassName = modelClassName.replace(".model.", ".dao.inf.");
            mapperClassName = mapperClassName + "Mapper";
            mapperClassName = mapperClassName.replace("VoMapper", "Mapper");
        }

        String simpleClassName = mapperClassName.substring(mapperClassName.lastIndexOf(".") + 1);
        if (sequenceName == null || "".equals(sequenceName)) {
            sequenceName = tableName + "_seq";
        }

        if (filePath == null || "".equals(filePath.trim())) {
            filePath = System.getProperty("user.dir") + "/src/main/java/test";
        }

        String xmlFile = filePath + "/" + simpleClassName + ".xml";
        String mapperFile = filePath + "/" + simpleClassName + ".java";
        String sqlFile = filePath + "/" + tableName.toUpperCase() + ".sql";
        String sequenceFile = filePath + "/" + sequenceName.toUpperCase() + ".sql";

        System.out.println("generateMapperXml:" + xmlFile);
        copyToFile(new ByteArrayInputStream(xmlBuff.getBytes()), new File(xmlFile));

        System.out.println("generateMapper:" + mapperFile);
        copyToFile(new ByteArrayInputStream(mapperBuff.getBytes()), new File(mapperFile));

        System.out.println("generateSql:" + sqlFile);
        copyToFile(new ByteArrayInputStream(sqlBuff.getBytes()), new File(sqlFile));

        System.out.println("generateSequenceSql:" + sequenceFile);
        copyToFile(new ByteArrayInputStream(sequenceBuff.getBytes()), new File(sequenceFile));

    }

    public static void copyToFile(InputStream source, File destination) throws IOException {
        InputStream in = source;
        try {
            OutputStream out = new FileOutputStream(destination);
            try {
                long count;
                int n;
                byte[] buffer = new byte[4096];
                for (count = 0L; -1 != (n = source.read(buffer)); count += (long) n) {
                    out.write(buffer, 0, n);
                }

            } catch (Throwable e) {
                throw e;
            } finally {
                if (out != null) {
                    out.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

    private static String generateSequenceSqlBuff(String tableName, String sequenceName) {
        if (sequenceName == null || "".equals(sequenceName)) {
            sequenceName = tableName + "_seq";
        }
        return "CREATE SEQUENCE " + sequenceName + " START WITH 1 INCREMENT BY 1 NOMAXVALUE NOMINVALUE NOCACHE NOORDER";
    }

    private static String generateSqlBuff(
            String modelClassName, String tableName) throws Exception {
        tableName = tableName.toUpperCase();
        Class modelClazz = Class.forName(modelClassName);
        Field[] properties = modelClazz.getDeclaredFields();

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE" + LINE_SEPARATOR);
        sqlBuffer.append("    " + tableName + LINE_SEPARATOR);
        sqlBuffer.append("    (" + LINE_SEPARATOR);
        for (int i = 0; i < properties.length; i++) {
            Field field = properties[i];
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            String sqlType = "VARCHAR2(255)";
            if (SQL_TYPE_MAP.containsKey(fieldType)) {
                sqlType = SQL_TYPE_MAP.get(fieldType);
            }
            String fieldNameUnderline = addUnderlineBeforeUppercase(fieldName).toUpperCase();
            if (properties.length - i == 1) {
                sqlBuffer.append("        " + fieldNameUnderline + " " + sqlType + LINE_SEPARATOR);
            } else {
                sqlBuffer.append("        " + fieldNameUnderline + " " + sqlType + "," + LINE_SEPARATOR);

            }
        }
        sqlBuffer.append("    );" + LINE_SEPARATOR);

        return sqlBuffer.toString();
    }


    public static String generateMapperXmlBuff(
            String modelClassName, String tableName,
            String mapperClassName, String sequenceName) throws Exception {
        //通过VO自动生成 Mapper.java, Mapper.xml,

        tableName = tableName.toLowerCase();

        if (mapperClassName == null || "".equals(mapperClassName)) {
            mapperClassName = modelClassName.replace(".model.", ".dao.inf.");
            mapperClassName = mapperClassName + "Mapper";
            mapperClassName = mapperClassName.replace("VoMapper", "Mapper");
        }
        if (sequenceName == null || "".equals(sequenceName)) {
            sequenceName = tableName + "_seq";
        }
        Class modelClazz = Class.forName(modelClassName);
        Field[] properties = modelClazz.getDeclaredFields();

        StringBuffer xmlContent = new StringBuffer();
        xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + LINE_SEPARATOR);
        xmlContent.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >" + LINE_SEPARATOR);
        xmlContent.append("<mapper namespace=\"" + mapperClassName + "\">" + LINE_SEPARATOR);

        StringBuffer resultMapContent = new StringBuffer();
        StringBuffer allColumnsContent = new StringBuffer();
        StringBuffer wheresDataContent = new StringBuffer();
        StringBuffer insertContent = new StringBuffer();
        StringBuffer insertListContent = new StringBuffer();
        StringBuffer updateContent = new StringBuffer();
        StringBuffer deleteContent = new StringBuffer();
        StringBuffer queryContent = new StringBuffer();
        StringBuffer queryAllContent = new StringBuffer();
        StringBuffer queryCountContent = new StringBuffer();


        resultMapContent.append("    <resultMap id=\"AllColumnMap\" type=\"" + modelClassName + "\">" + LINE_SEPARATOR);
        allColumnsContent.append("    <sql id=\"all_column\">" + LINE_SEPARATOR);
        wheresDataContent.append("    <sql id=\"wheresData\">" + LINE_SEPARATOR);
        wheresDataContent.append("        <where>" + LINE_SEPARATOR);
        insertContent.append("    <insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"pojo.id\">" + LINE_SEPARATOR);
        insertContent.append("        <selectKey keyProperty=\"pojo.id\" order=\"BEFORE\" resultType=\"java.lang.String\">" + LINE_SEPARATOR);
        insertContent.append("            SELECT " + sequenceName + ".nextval from DUAL" + LINE_SEPARATOR);
        insertContent.append("        </selectKey>" + LINE_SEPARATOR);
        insertContent.append("        insert into " + tableName + "(" + LINE_SEPARATOR);
        insertListContent.append("    <insert id=\"insertList\">" + LINE_SEPARATOR);
        insertListContent.append("        insert into  " + tableName + " (" + LINE_SEPARATOR);
        insertListContent.append("            <include refid=\"all_column\"/>" + LINE_SEPARATOR);
        insertListContent.append("        )  " + LINE_SEPARATOR);
        insertListContent.append("        select " + sequenceName + ".nextval, A.* from  " + LINE_SEPARATOR);
        insertListContent.append("        (  " + LINE_SEPARATOR);
        insertListContent.append("        <foreach collection=\"pojos\" item=\"pojo\" index=\"index\" open=\"(\" separator=\"UNION ALL\" close=\")\" >" + LINE_SEPARATOR);
        insertListContent.append("            select" + LINE_SEPARATOR);
        updateContent.append("    <update id=\"update\">" + LINE_SEPARATOR);
        updateContent.append("        update  " + tableName + "" + LINE_SEPARATOR);
        updateContent.append("        <set>" + LINE_SEPARATOR);
        deleteContent.append("    <delete id=\"delete\" parameterType=\"map\">" + LINE_SEPARATOR);
        deleteContent.append("        delete" + LINE_SEPARATOR);
        deleteContent.append("        from " + tableName + " pojo" + LINE_SEPARATOR);
        deleteContent.append("        <include refid=\"wheresData\"/>" + LINE_SEPARATOR);
        deleteContent.append("    </delete>" + LINE_SEPARATOR);

        StringBuffer allColumns = new StringBuffer();
        StringBuffer allColumnValues = new StringBuffer();

        for (int i = 0; i < properties.length; i++) {
            Field field = properties[i];
            String fieldName = field.getName();
            String fieldNameUnderline = addUnderlineBeforeUppercase(fieldName);
            String fieldValue = "            #{pojo." + fieldName + "}";
            if ("id".equals(fieldName)) {
                fieldValue = "            " + sequenceName + ".nextval";
            }
            resultMapContent.append("        <result column=\"" + fieldNameUnderline + "\" property=\"" + fieldName + "\"/>" + LINE_SEPARATOR);
            //最后一行
            if (properties.length - i == 1) {
                allColumns.append("        " + fieldNameUnderline + "" + LINE_SEPARATOR);
                allColumnValues.append("        #{pojo." + fieldName + "}" + LINE_SEPARATOR);
                if (!"id".equals(fieldName)) {
                    insertListContent.append(fieldValue + LINE_SEPARATOR);
                }
                updateContent.append("            <if test=\"pojo." + fieldName + " != null\">" + fieldNameUnderline + " = #{pojo." + fieldName + "}</if>" + LINE_SEPARATOR);
            } else {
                allColumns.append("        " + fieldNameUnderline + "," + LINE_SEPARATOR);
                allColumnValues.append("        #{pojo." + fieldName + "}," + LINE_SEPARATOR);
                if (!"id".equals(fieldName)) {
                    insertListContent.append(fieldValue + "," + LINE_SEPARATOR);
                }
                updateContent.append("            <if test=\"pojo." + fieldName + " != null\">" + fieldNameUnderline + " = #{pojo." + fieldName + "},</if>" + LINE_SEPARATOR);
            }
            wheresDataContent.append("            <if test=\"" + fieldName + " != null and " + fieldName + " != ''\">" + LINE_SEPARATOR);
            wheresDataContent.append("                AND pojo." + fieldNameUnderline + " = #{" + fieldName + "}" + LINE_SEPARATOR);
            wheresDataContent.append("            </if>" + LINE_SEPARATOR);
        }

        allColumnsContent.append(allColumns);
        insertContent.append(allColumns);
        insertContent.append("        ) values (" + LINE_SEPARATOR);
        insertContent.append(allColumnValues);

        resultMapContent.append("    </resultMap>" + LINE_SEPARATOR);
        allColumnsContent.append("    </sql>" + LINE_SEPARATOR);
        wheresDataContent.append("        </where>" + LINE_SEPARATOR);
        wheresDataContent.append("    </sql>" + LINE_SEPARATOR);
        insertContent.append("        )" + LINE_SEPARATOR);
        insertContent.append("    </insert>" + LINE_SEPARATOR);
        insertListContent.append("            FROM dual" + LINE_SEPARATOR);
        insertListContent.append("        </foreach>" + LINE_SEPARATOR);
        insertListContent.append("        ) A" + LINE_SEPARATOR);
        insertListContent.append("    </insert>" + LINE_SEPARATOR);
        updateContent.append("        </set>" + LINE_SEPARATOR);
        updateContent.append("        WHERE id = #{pojo.id}" + LINE_SEPARATOR);
        updateContent.append("    </update>" + LINE_SEPARATOR);


        queryContent.append("    <select id=\"query\" parameterType=\"map\" resultMap=\"AllColumnMap\">" + LINE_SEPARATOR);
        queryContent.append("        ${pageVo.sqlPageStart}" + LINE_SEPARATOR);
        queryContent.append("        select" + LINE_SEPARATOR);
        queryContent.append("        <include refid=\"all_column\"/>" + LINE_SEPARATOR);
        queryContent.append("        from " + tableName + " pojo" + LINE_SEPARATOR);
        queryContent.append("        <include refid=\"wheresData\" />" + LINE_SEPARATOR);
        queryContent.append("        <if test=\"pageVo.sort != null and pageVo.sort != ''\">" + LINE_SEPARATOR);
        queryContent.append("            order by ${pageVo.sort} ${pageVo.order}" + LINE_SEPARATOR);
        queryContent.append("        </if>" + LINE_SEPARATOR);
        queryContent.append("        ${pageVo.sqlPageEnd}" + LINE_SEPARATOR);
        queryContent.append("    </select>" + LINE_SEPARATOR);

        queryAllContent.append("    <select id=\"queryAll\" parameterType=\"map\" resultMap=\"AllColumnMap\">" + LINE_SEPARATOR);
        queryAllContent.append("        select" + LINE_SEPARATOR);
        queryAllContent.append("        <include refid=\"all_column\"/>" + LINE_SEPARATOR);
        queryAllContent.append("        from " + tableName + " pojo" + LINE_SEPARATOR);
        queryAllContent.append("        <include refid=\"wheresData\" />" + LINE_SEPARATOR);
        queryAllContent.append("    </select>" + LINE_SEPARATOR);

        queryCountContent.append("    <select id=\"queryCount\" parameterType=\"map\" resultType=\"int\">" + LINE_SEPARATOR);
        queryCountContent.append("        select" + LINE_SEPARATOR);
        queryCountContent.append("        count(1)" + LINE_SEPARATOR);
        queryCountContent.append("        from " + tableName + " pojo" + LINE_SEPARATOR);
        queryCountContent.append("        <include refid=\"wheresData\"/>" + LINE_SEPARATOR);
        queryCountContent.append("    </select>" + LINE_SEPARATOR);


        addContentToXml(resultMapContent, xmlContent);
        addContentToXml(allColumnsContent, xmlContent);
        addContentToXml(wheresDataContent, xmlContent);
        addContentToXml(insertContent, xmlContent);
        addContentToXml(insertListContent, xmlContent);
        addContentToXml(updateContent, xmlContent);
        addContentToXml(queryContent, xmlContent);
        addContentToXml(queryAllContent, xmlContent);
        addContentToXml(queryCountContent, xmlContent);
        addContentToXml(deleteContent, xmlContent);

        xmlContent.append("</mapper>" + LINE_SEPARATOR);

        return xmlContent.toString();
    }

    public static void addContentToXml(StringBuffer sqlContent, StringBuffer xmlContent) {
        if (sqlContent.length() > 0) {
            xmlContent.append(sqlContent + LINE_SEPARATOR);
        }
    }


    public static String generateMapperBuff(String modelClassName, String mapperClassName) {

        if (mapperClassName == null || "".equals(mapperClassName)) {
            mapperClassName = modelClassName.replace(".model.", ".dao.inf.");
            mapperClassName = mapperClassName + "Mapper";
            mapperClassName = mapperClassName.replace("VoMapper", "Mapper");
        }

        String packageName = mapperClassName.substring(0, mapperClassName.lastIndexOf("."));
        String simpleClassName = mapperClassName.substring(mapperClassName.lastIndexOf(".") + 1);
        String simpleModelName = modelClassName.substring(modelClassName.lastIndexOf(".") + 1);

        StringBuffer mapperFormat = new StringBuffer();
        mapperFormat.append("package " + packageName + ";" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("import " + modelClassName + ";" + LINE_SEPARATOR);
        mapperFormat.append("import com.thunis.framework.common.annotation.MyBatisDao;" + LINE_SEPARATOR);
        mapperFormat.append("import org.apache.ibatis.annotations.Param;" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("import java.util.List;" + LINE_SEPARATOR);
        mapperFormat.append("import java.util.Map;" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("/**" + LINE_SEPARATOR);
        mapperFormat.append(" * @author GenerateMyBatisMapper" + LINE_SEPARATOR);
        mapperFormat.append(" * @since " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + LINE_SEPARATOR);
        mapperFormat.append(" */" + LINE_SEPARATOR);
        mapperFormat.append("@MyBatisDao" + LINE_SEPARATOR);
        mapperFormat.append("public interface " + simpleClassName + " {" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    int insert(@Param(\"pojo\") " + simpleModelName + " pojo);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    int insertList(@Param(\"pojos\") List<" + simpleModelName + "> pojo);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    int update(@Param(\"pojo\") " + simpleModelName + " pojo);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    List<" + simpleModelName + "> query(Map conditions);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    List<" + simpleModelName + "> queryAll(Map conditions);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    Integer queryCount(Map conditions);" + LINE_SEPARATOR);
        mapperFormat.append(LINE_SEPARATOR);
        mapperFormat.append("    void delete(Map conditions);" + LINE_SEPARATOR);
        mapperFormat.append("}" + LINE_SEPARATOR);

        return mapperFormat.toString();
    }


    public static String addUnderlineBeforeUppercase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        char[] strChars = str.toCharArray();

        StringBuffer retBuff = new StringBuffer();
        for (int i = 0; i < strChars.length; i++) {
            char c = strChars[i];
            if (i != 0 && c >= 'A' && c <= 'Z') {
                retBuff.append("_");
            }
            retBuff.append(c);
        }

        return retBuff.toString().toLowerCase();
    }

}
