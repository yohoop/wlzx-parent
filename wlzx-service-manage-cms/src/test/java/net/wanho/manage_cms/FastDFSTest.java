package net.wanho.manage_cms;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void upload() throws Exception {
        InputStream inputStream = new FileInputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala.jpg");
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(), "jpg", null);
        System.out.println(storePath);
    }

    @Test
    public void uploadMyTemplate() throws Exception {
//        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\IdeaProjects\\wlzx-parent\\wlzx-service-manage-cms\\src\\main\\resources\\templates\\index_banner.ftl");
        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\IdeaProjects\\wlzx-parent\\wlzx-service-manage-cms\\src\\main\\resources\\templates\\course.ftl");
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, inputStream.available(), "ftl", null);
        System.out.println(storePath);
    }

    @Test
    public void uploadCrtThumbImage() {
        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala.jpg");            // 测试上传 缩略图
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream,inputStream.available(), "jpg", null);
            System.out.println(storePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void download() throws Exception {
        byte[] bytes = fastFileStorageClient.downloadFile("group1", "M00/00/00/wKhkBV3JEZ-AaskdAAvqH0-N5gE572.jpg", new DownloadByteArray());
        FileOutputStream stream = new FileOutputStream("a.jpg");
        stream.write(bytes);
    }

    @Test
    public void delete(){
        fastFileStorageClient.deleteFile("group1","M00/00/00/wKhkBV3JEZ-AaskdAAvqH0-N5gE572.jpg");
    }
}
