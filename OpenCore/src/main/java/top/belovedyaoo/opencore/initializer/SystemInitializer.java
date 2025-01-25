package top.belovedyaoo.opencore.initializer;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.oshi.OshiUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import oshi.hardware.HWDiskStore;
import oshi.software.os.OSFileStore;
import top.belovedyaoo.opencore.common.OcMap;
import top.belovedyaoo.opencore.constants.SystemConst;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 系统初始化
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Component
public class SystemInitializer {

    @PostConstruct
    public void systemInfoRecord() {
        // 获取操作系统信息
        OsInfo osInfo = SystemUtil.getOsInfo();
        // 记录系统启动时间
        SystemConst.SYSTEM_START_TIME = System.currentTimeMillis();
        // 记录系统工作路径
        SystemConst.SYSTEM_WORK_PATH = System.getProperty("user.dir");
        // 记录操作系统名称和架构
        SystemConst.OS_NAME = osInfo.getName();
        SystemConst.OS_ARCH = osInfo.getArch();
        // 记录Java名称和版本
        SystemConst.JAVA_NAME = SystemUtil.getJvmInfo().getName();
        SystemConst.JAVA_VERSION = SystemUtil.getJavaInfo().getVersion();
        // 记录操作系统内存信息，保留两位小数
        SystemConst.MEMORY_SIZE = new DecimalFormat("#.00").format((double) OshiUtil.getMemory().getTotal() / Math.pow(1024, 3)) + "GB";
        // 获取硬盘存储列表
        List<HWDiskStore> diskStores = OshiUtil.getDiskStores();
        // 获取文件系统信息
        List<OSFileStore> fileStores = OshiUtil.getOs().getFileSystem().getFileStores();
        // 初始化硬盘信息列表
        OcMap diskInfo = OcMap.build();
        // 遍历所有硬盘，记录每个硬盘的大小、挂载路径
        for (int i = 0; i < diskStores.size(); i++) {
            HWDiskStore diskStore = diskStores.get(i);
            OSFileStore fileStore = fileStores.get(i);
            // 获取挂载路径和盘符
            String mountPoint = fileStore.getMount();
            // 获取硬盘大小（字节）
            long diskSizeBytes = diskStore.getSize();
            // 转换为 GB，保留两位小数
            double diskSize = (double) diskSizeBytes / Math.pow(1024, 3);
            DecimalFormat df = new DecimalFormat("#.00");
            String diskSizeStr = df.format(diskSize) + "GB";
            // 添加硬盘信息
            diskInfo.set(mountPoint, diskSizeStr);
        }
        // 记录操作系统硬盘信息
        SystemConst.DISK_SIZE = diskInfo;
    }

}
