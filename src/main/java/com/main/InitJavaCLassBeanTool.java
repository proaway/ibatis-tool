package com.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.JavaDtoCLassBean;
import com.bean.JavaDtoConditionClassBean;
import com.bean.QueryCondition;
import com.bean.SqlMapXmlBean;
import com.bean.TableColumnsMappingJavaPropertyBean;
import com.constants.JavaPropertyTypeAndFullNameEnum;
import com.constants.MysqlDataBaseEnum;
import com.dao.TableColumnDao;
import com.dao.impl.TableColumnDaoImpl;
/**
 * 
 * @author wuzhq
 *
 */
public class InitJavaCLassBeanTool {
	 
	/**
	 * 
	 * @param tableName
	 * @param BasePackage
	 * @param createPath
	 * @param JavaDtoCLassBean
	 * @param type 生成bean的类型 1 dto ,2 Dao ,3 dao实现,4 service, 5 service 实现
	 * @throws Exception 
	 */
	public static void  init(String tableName,String BasePackage,String createPath,JavaDtoCLassBean JavaDtoCLassBean ,int type,String author,String dataBase) throws Exception{
		if(1==type){
			JavaDtoCLassBean.setDataBase(dataBase);
			JavaDtoCLassBean.setJavaPackage(createJavaPackage(BasePackage,"DTO"));
			JavaDtoCLassBean.setName(createJavaDTOClassBeanName(tableName,"_"));
			JavaDtoCLassBean.setTableColumnsMappingJavaPropertyBeans(getTableColumnsMappingJavaPropertyBeans(tableName));
			setTableColumnsMappingJavaPropertyBeanPropertyValue(JavaDtoCLassBean);
			
		}else if(2==type){
			JavaDtoCLassBean.setJavaPackage(createJavaPackage(BasePackage,"DAO"));
			JavaDtoCLassBean.setName(createDAOORServiceJavaClassBeanName(createJavaDTOClassBeanName(tableName,"_"),"DAO"));
		}
		JavaDtoCLassBean.setCreatePath(createPath);
		JavaDtoCLassBean.setAuthor(author);
	}
	
	public static void  setTableColumnsMappingJavaPropertyBeanPropertyValue(JavaDtoCLassBean JavaDtoCLassBean) throws Exception{
		if(null==JavaDtoCLassBean){
			System.out.println("javaCLassBean is null!");
			throw new Exception();
		}
		String dataBase = JavaDtoCLassBean.getDataBase();
		List<TableColumnsMappingJavaPropertyBean> list = JavaDtoCLassBean.getTableColumnsMappingJavaPropertyBeans();
		Set<String> importJavas = new HashSet<String>();
		for(TableColumnsMappingJavaPropertyBean bean:list){
			if("mysql".equals(dataBase)){
				bean.setJavaBeanPropertyType(MysqlDataBaseEnum.getJavaBeanPropertyType(bean.getTableColumnType()));
				bean.setJavaBeanPropertyName(createJavaBeanPropertyName(bean.getTableColumnName(),"_"));
			}else if("oralce".equals(dataBase)){
				
			}
			String javaBeanPropertyName = bean.getJavaBeanPropertyName();
			StringBuffer bf = new StringBuffer();
			bf.append(javaBeanPropertyName.substring(0,1).toUpperCase()).append(javaBeanPropertyName.substring(1, javaBeanPropertyName.length()));
			bean.setJavaBeanPropertyForSetOrGetMethodName(bf.toString());
			bean.setJavaBeanPropertyTypeFullPath(JavaPropertyTypeAndFullNameEnum.getJavaBeanPropertyTypeFullName(bean.getJavaBeanPropertyType()));
			importJavas.add(JavaPropertyTypeAndFullNameEnum.getJavaBeanPropertyTypeFullName(bean.getJavaBeanPropertyType()));
		}
		JavaDtoCLassBean.setImportJavas(importJavas);
	}
	
	public static List<TableColumnsMappingJavaPropertyBean> getTableColumnsMappingJavaPropertyBeans(String tableName){
		List<TableColumnsMappingJavaPropertyBean> list = null;
		ApplicationContext acnt = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		TableColumnDao tableColumnDao = acnt.getBean("tableColumnDao", TableColumnDaoImpl.class);
		QueryCondition condition = new QueryCondition();
		condition.setTableName(tableName);
		list = tableColumnDao.query(condition);
		return list;
	}
	
	public static String createDAOORServiceJavaClassBeanName(String dtoClassName,String suf){
		String result = null;
		StringBuffer bf = new StringBuffer();
		bf.append(dtoClassName).append(suf);
		result = bf.toString();
		return result;
	}
	
	public static String  createJavaDTOClassBeanName(String tableName,String splitStr){
		String result = null;
		if((null==tableName||tableName.length()<1)||(null==splitStr||splitStr.length()<1)){
			return null;
		}
		String [] strs = tableName.toLowerCase().split(splitStr);
		StringBuffer bf  = new StringBuffer();
		int len = strs.length;
		if(1==len){
			String first =tableName.substring(0,1) ;
			first = first.toUpperCase();
			String other = tableName.substring(1, tableName.length());
			bf.append(first).append(other);
		}else{
			for(int i = 0;i<len;i++){
				if(i!=0){
					String first =strs[i].substring(0,1) ;
					first = first.toUpperCase();
					String other = strs[i].substring(1, strs[i].length());
					bf.append(first).append(other);
				}
			}
		}
	    result = bf.toString();
		return result;
	}
	
	
	public static String createJavaBeanPropertyName(String tableColumnName,String splitStr){
		String result = null;
		if((null==tableColumnName||tableColumnName.length()<1)||(null==splitStr||splitStr.length()<1)){
			return null;
		}
		String [] strs = tableColumnName.toLowerCase().split(splitStr);
		StringBuffer bf  = new StringBuffer();
		int len = strs.length;
		if(1==len){
			
			bf.append(tableColumnName.toLowerCase());
		}else{
			for(int i = 0;i<len;i++){
				if(i!=0){
					String first =strs[i].substring(0,1) ;
					first = first.toUpperCase();
					String other = strs[i].substring(1, strs[i].length());
					bf.append(first).append(other);
				}else{
					bf.append(strs[i]);
				}
			}
		}
	    result = bf.toString();
		return result;
	}
	
	public static String createJavaPackage(String basePackage,String packageName) throws Exception {
		String result = null;
		if(null==basePackage||basePackage.length()<1){
			System.out.println("BasePackage is null!");
			throw new Exception();
		}
		StringBuffer bf = new StringBuffer();
		bf.append(basePackage).append(".").append(packageName);
		result = bf.toString();
		return result;
	}
	
	public static void  initJavaDtoConditionClassBeanPropertyVals(JavaDtoConditionClassBean javaDtoConditionClassBean,JavaDtoCLassBean javaDtoCLassBean)throws Exception{
		if(javaDtoConditionClassBean==null){
			throw new Exception("javaDtoConditionClassBean is null");
		}
		if(null==javaDtoCLassBean){
			throw new Exception("javaDtoConditionClassBean is null");
		}
		javaDtoConditionClassBean.setJavaDtoCLassBean(javaDtoCLassBean);
		javaDtoConditionClassBean.setName(javaDtoCLassBean.getName()+"Condition");
		javaDtoConditionClassBean.setJavaPackage(javaDtoCLassBean.getJavaPackage());
		Set<String> importJavas = new HashSet<String>();
		importJavas.add(javaDtoCLassBean.getJavaPackage()+"."+javaDtoCLassBean.getName());
		javaDtoConditionClassBean.setImportJavas(importJavas);
		javaDtoConditionClassBean.setAuthor(javaDtoCLassBean.getAuthor());
		javaDtoConditionClassBean.setCreatePath(javaDtoCLassBean.getCreatePath());
	}
	
	public static void initSqlMapXmlBeanPropertyVals(SqlMapXmlBean sqlMapXmlBean,JavaDtoCLassBean javaDtoCLassBean,String tableName,String basePackage)throws Exception{
		if(sqlMapXmlBean==null){
			throw new Exception("sqlMapXmlBean is null");
		}
		if(null==javaDtoCLassBean){
			throw new Exception("javaDtoConditionClassBean is null");
		}
		sqlMapXmlBean.setTableName(tableName);
		String javaDtoCLassBeanName = javaDtoCLassBean.getName();
		sqlMapXmlBean.setAliasDtoClassFullName(javaDtoCLassBean.getJavaPackage()+javaDtoCLassBeanName);
		sqlMapXmlBean.setAliasDtoClassName(javaDtoCLassBeanName);
		sqlMapXmlBean.setAliasDtoOpConditionClassName(javaDtoCLassBeanName+"Condition");
		sqlMapXmlBean.setAliasDtoOpConditionClassFullName(javaDtoCLassBean.getJavaPackage()+sqlMapXmlBean.getAliasDtoOpConditionClassName());
		sqlMapXmlBean.setJavaDtoCLassBean(javaDtoCLassBean);
		sqlMapXmlBean.setNameSpace(javaDtoCLassBeanName.replace(javaDtoCLassBeanName.substring(0, 1), javaDtoCLassBeanName.substring(0, 1).toLowerCase()));
		sqlMapXmlBean.setCreatePath(javaDtoCLassBean.getCreatePath());
		sqlMapXmlBean.setJavaPackage(createJavaPackage(basePackage, "sqlmap"));
		sqlMapXmlBean.setFileName("sqlmap-"+javaDtoCLassBeanName.toLowerCase());
	}
	
	
	public static void main(String[] args) {
		System.out.println(InitJavaCLassBeanTool.createJavaDTOClassBeanName("account", "_"));
	}
}
