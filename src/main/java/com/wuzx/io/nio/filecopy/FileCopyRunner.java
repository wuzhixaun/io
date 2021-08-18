package com.wuzx.io.nio.filecopy;

import java.io.File;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName FuleCopyRunner.java
 * @Description 文件拷贝
 * @createTime 2021年08月18日 10:05:00
 */
public interface FileCopyRunner {

    void copyFile(File Source, File target);
}
