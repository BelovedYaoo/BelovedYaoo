package top.belovedyaoo.opencore.constants.consist;

import top.belovedyaoo.opencore.common.OcMap;

/**
 * 系统常量
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class SystemConst {

    /**
     * 系统启动时间
     */
    public static Long SYSTEM_START_TIME;

    /**
     * 系统工作路径
     */
    public static String SYSTEM_WORK_PATH;

    /**
     * 系统名称
     */
    public static String OS_NAME;

    /**
     * 系统架构
     */
    public static String OS_ARCH;

    /**
     * Java名称
     */
    public static String JAVA_NAME;

    /**
     * Java版本
     */
    public static String JAVA_VERSION;

    /**
     * 内存大小
     */
    public static String MEMORY_SIZE;

    /**
     * 硬盘大小
     */
    public static OcMap DISK_SIZE;

    public static OcMap getSystemInfo() {
        return OcMap.build()
                .set("systemStartTime", SYSTEM_START_TIME)
                .set("systemWorkPath", SYSTEM_WORK_PATH)
                .set("osName", OS_NAME)
                .set("osArch", OS_ARCH)
                .set("javaName", JAVA_NAME)
                .set("javaVersion", JAVA_VERSION)
                .set("memorySize", MEMORY_SIZE)
                .set("diskSize", DISK_SIZE);
    }

}
