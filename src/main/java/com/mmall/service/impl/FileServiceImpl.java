package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.utils.FTPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //把上传后的文件名返回
    public String upload(MultipartFile file,String path) {
        String fileName = file.getOriginalFilename();
        //文件的扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName; //考虑到两个用户上传图片的名称可能会一致的问题，需要用到UUID唯一区别两张图片
        logger.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
        //声明目录的dir
        File fileDir = new File(path);
        //判断文件夹是否存在，如果不存在，就赋予可写权限，然后创建它
        if (!fileDir.exists()){
            fileDir.setWritable(true);//赋予可写权限
            fileDir.mkdirs();
        }
        //创建文件
        File targetFile = new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);        //文件已经上传成功
            //todo 将targerFile上传到FTP服务器上
            FTPUtils.uploadFile(Lists.<File>newArrayList(targetFile));  //已经上传到FTP服务器上

            //todo 上传完后，删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常：",e);
            return null;
        }

        return targetFile.getName();
    }
}
