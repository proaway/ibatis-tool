package ${javaClassBean.javaPackage};

<#list javaClassBean.importJavas as importJava>
import	${importJava} ;
</#list>

/**
 * 
 * @author ${javaClassBean.author}
 *
 */
@Service("${javaClassBean.AntionName} ")
public class ${javaClassBean.name}   implements  ${javaClassBean.implementsServiceInterfaceName}{
	private static final Log logger = LogFactory.getLog(${javaClassBean.name}.class);
	@Resource
	private  ${javaClassBean.relyServiceInterfaceName} ${javaClassBean.relyServiceInterfacePropertyName} ;
	
	
	public Map<Object,Object> add(Map<Object,Object> condition ) {
		Map<Object,Object> result  = new HashMap<Object,Object>();
		try{
			${javaClassBean.dtoClassName} bean = (${javaClassBean.dtoClassName}) ${javaClassBean.beanMapToolClassName}.map2Bean(condition);
			Integer id = ${javaClassBean.relyServiceInterfacePropertyName}.add(bean);
			${javaClassBean.resultReturnToolCLassName}.returnSuccess(id);
		}catch(Exception e){
			LoggerUtil.showErrorMessage(logger,"function add error:"+e);
			${javaClassBean.resultReturnToolCLassName}.returnSystemError();
		}
	}
	public Map<Object,Object>   findById (Map<Object,Object> condition){
	
	}
	public  Map<Object,Object>  findByCondition(Map<Object,Object> condition){
	
	}
	public  Map<Object,Object> findCountByCondition(Map<Object,Object> condition){
	
	}
	public Map<Object,Object>  update(Map<Object,Object> condition){
	
	}
	public Map<Object,Object> delete(Map<Object,Object> condition){
	
	}
}