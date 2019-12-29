package net.wanho.manage_course.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import net.wanho.common.exception.ExceptionCast;
import net.wanho.common.util.StringUtils;
import net.wanho.manage_course.mapper.FileSystemMapper;
import net.wano.po.filesystem.FileSystem;
import net.wano.po.filesystem.response.FileSystemCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileSystemService extends ServiceImpl<FileSystemMapper, FileSystem> {

    @Resource
    private FileSystemMapper fileSystemMapper;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    public StorePath upload(MultipartFile multipartFile, String fileTag, String businessKey, String metadata) {
        //文件验证
        if(StringUtils.isNull(multipartFile)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        InputStream in = null;
        try {
            in = multipartFile.getInputStream();
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            StorePath storePath = fastFileStorageClient.uploadFile(in, in.available(), suffix, null);
            FileSystem fileSystem = new FileSystem();
            fileSystem.setFileId(storePath.getFullPath());
            fileSystem.setFileSize(in.available());
            fileSystem.setFileName(originalFilename);
            fileSystem.setFileType(multipartFile.getContentType());
            fileSystem.setFileTag(fileTag);
            fileSystem.setBusinessKey(businessKey);
            //todo 登录功能实现之后完善
            //fileSystem.setUserId("");
            fileSystemMapper.insert(fileSystem);
            return storePath;
        } catch (IOException e) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
            return null;
        }
    }
}
