package cn.byteboy.demo.jvm.zip;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.Zip4jConfig;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hongshaochuan
 * @Date 2021/9/23
 */
public class Demo {

    private final String destFileName = "D:\\temp\\fuck.zip";

    private final String innerFolderName = "fuckAB";

    private final String fileAName = "D:\\temp\\a\\aa\\aaa\\fuck_a.txt";

    private final String fileBName = "D:\\temp\\b\\bb\\fuck_b.txt";

    public void fileMode() throws ZipException {
        ZipFile zipFile = new ZipFile(destFileName);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setRootFolderNameInZip(innerFolderName);
//        zipParameters.setFileNameInZip("fuckAB/a.txt");
        File fileA = FileUtil.file(fileAName);
        File fileB = FileUtil.file(fileBName);

        zipFile.addFile(fileA, zipParameters);
        zipFile.addFile(fileB, zipParameters);
    }


    public void streamMode() throws IOException {
        List<File> fileList = new ArrayList<>();
        fileList.add(FileUtil.file(fileAName));
        fileList.add(FileUtil.file(fileBName));

        ZipParameters zipParameters = new ZipParameters();

        byte[] buff = new byte[4096];
        int readLen;

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destFileName))) {

            for (File fileToAdd : fileList) {
//                zipParameters.setRootFolderNameInZip(innerFolderName); // (无效)

                zipParameters.setFileNameInZip(innerFolderName + File.separator + fileToAdd.getName());
                zos.putNextEntry(zipParameters);

                try (FileInputStream fis = new FileInputStream(fileToAdd)) {
                    while ((readLen = fis.read(buff)) != -1) {
                        zos.write(buff, 0, readLen);
                    }
                }
                zos.closeEntry();
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        new Demo().fileMode();
        new Demo().streamMode();
    }
}
