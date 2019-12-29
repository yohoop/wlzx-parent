package net.wanho.manage_media.controller;

import net.wanho.api.media.MediaUploadControllerApi;
import net.wanho.common.vo.response.AjaxResult;
import net.wanho.common.web.BaseController;
import net.wanho.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media/upload")
public class MediaUploadController extends BaseController implements MediaUploadControllerApi {
    @Autowired
    MediaUploadService mediaUploadService;

    //文件上传前的注册
    @Override
    @PostMapping("/register")
    public AjaxResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
        return success();
    }

    @Override
    @PostMapping("/checkchunk")
    public AjaxResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return success(mediaUploadService.checkchunk(fileMd5,chunk,chunkSize));
    }

    @Override
    @PostMapping("/uploadchunk")
    public AjaxResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        mediaUploadService.uploadchunk(file,fileMd5,chunk);
        return success();
    }

    @Override
    @PostMapping("/mergechunks")
    public AjaxResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.mergechunks(fileMd5,fileName,fileSize, mimetype,fileExt);
        return success();
    }
}
