package top.belovedyaoo.openiam.core;

import top.belovedyaoo.openiam.config.ServerConfigurer;
import top.belovedyaoo.openiam.dao.OpenAuthDao;
import top.belovedyaoo.openiam.dao.OpenAuthDaoDefaultImpl;
import top.belovedyaoo.openiam.data.convert.OpenAuthDataConverter;
import top.belovedyaoo.openiam.data.convert.OpenAuthDataConverterDefaultImpl;
import top.belovedyaoo.openiam.data.generate.OpenAuthDataGenerate;
import top.belovedyaoo.openiam.data.generate.OpenAuthDataGenerateDefaultImpl;
import top.belovedyaoo.openiam.data.loader.OpenAuthDataLoader;
import top.belovedyaoo.openiam.data.loader.OpenAuthDataLoaderDefaultImpl;
import top.belovedyaoo.openiam.data.resolver.OpenAuthDataResolver;
import top.belovedyaoo.openiam.data.resolver.OpenAuthDataResolverDefaultImpl;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

/**
 * OpenAuth 模块 总控类
 * 
 * @author BelovedYaoo
 * @version 1.0
 */
public class OpenAuthManager {

	/**
	 * OAuth2 配置 Bean 
	 */
	private static volatile ServerConfigurer serverConfig;
	public static ServerConfigurer getServerConfig() {
		if (serverConfig == null) {
			// 初始化默认值
			synchronized (OpenAuthManager.class) {
				if (serverConfig == null) {
					setServerConfig(new ServerConfigurer());
				}
			}
		}
		return serverConfig;
	}
	public static void setServerConfig(ServerConfigurer serverConfig) {
		OpenAuthManager.serverConfig = serverConfig;
	}

	/**
	 * OAuth2 数据加载器 Bean
	 */
	private static volatile OpenAuthDataLoader dataLoader;
	public static OpenAuthDataLoader getDataLoader() {
		if (dataLoader == null) {
			synchronized (OpenAuthManager.class) {
				if (dataLoader == null) {
					setDataLoader(new OpenAuthDataLoaderDefaultImpl());
				}
			}
		}
		return dataLoader;
	}
	public static void setDataLoader(OpenAuthDataLoader dataLoader) {
		OpenAuthManager.dataLoader = dataLoader;
	}

	/**
	 * OAuth2 数据解析器 Bean
	 */
	private static volatile OpenAuthDataResolver dataResolver;
	public static OpenAuthDataResolver getDataResolver() {
		if (dataResolver == null) {
			synchronized (OpenAuthManager.class) {
				if (dataResolver == null) {
					setDataResolver(new OpenAuthDataResolverDefaultImpl());
				}
			}
		}
		return dataResolver;
	}
	public static void setDataResolver(OpenAuthDataResolver dataResolver) {
		OpenAuthManager.dataResolver = dataResolver;
	}

	/**
	 * OAuth2 数据格式转换器 Bean
	 */
	private static volatile OpenAuthDataConverter dataConverter;
	public static OpenAuthDataConverter getDataConverter() {
		if (dataConverter == null) {
			synchronized (OpenAuthManager.class) {
				if (dataConverter == null) {
					setDataConverter(new OpenAuthDataConverterDefaultImpl());
				}
			}
		}
		return dataConverter;
	}
	public static void setDataConverter(OpenAuthDataConverter dataConverter) {
		OpenAuthManager.dataConverter = dataConverter;
	}

	/**
	 * OAuth2 数据构建器 Bean
	 */
	private static volatile OpenAuthDataGenerate dataGenerate;
	public static OpenAuthDataGenerate getDataGenerate() {
		if (dataGenerate == null) {
			synchronized (OpenAuthManager.class) {
				if (dataGenerate == null) {
					setDataGenerate(new OpenAuthDataGenerateDefaultImpl());
				}
			}
		}
		return dataGenerate;
	}
	public static void setDataGenerate(OpenAuthDataGenerate dataGenerate) {
		OpenAuthManager.dataGenerate = dataGenerate;
	}

	/**
	 * OAuth2 数据持久 Bean
	 */
	private static volatile OpenAuthDao dao;
	public static OpenAuthDao getDao() {
		if (dao == null) {
			synchronized (OpenAuthManager.class) {
				if (dao == null) {
					setDao(new OpenAuthDaoDefaultImpl());
				}
			}
		}
		return dao;
	}
	public static void setDao(OpenAuthDao dao) {
		OpenAuthManager.dao = dao;
	}

	/**
	 * OAuth2 模板方法 Bean
	 */
	private static volatile OpenAuthTemplate template;
	public static OpenAuthTemplate getTemplate() {
		if (template == null) {
			synchronized (OpenAuthManager.class) {
				if (template == null) {
					setTemplate(new OpenAuthTemplate());
				}
			}
		}
		return template;
	}
	public static void setTemplate(OpenAuthTemplate template) {
		OpenAuthManager.template = template;
	}

	/**
	 * OAuth2 StpLogic
	 */
	private static volatile StpLogic stpLogic;
	public static StpLogic getStpLogic() {
		if (stpLogic == null) {
			synchronized (OpenAuthManager.class) {
				if (stpLogic == null) {
					setStpLogic(StpUtil.stpLogic);
				}
			}
		}
		return stpLogic;
	}
	public static void setStpLogic(StpLogic stpLogic) {
		OpenAuthManager.stpLogic = stpLogic;
	}

}
