package fastdfs;

import org.csource.fastdfs.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        try {
            String client_conf_path =  "F:\\工作技术栈\\notebook-warehouse\\Java\\Java8Maven\\LazyCode\\src\\main\\resources\\fast-dfs\\fdfs_client.conf";
            String img_path = "F:\\工作技术栈\\notebook-warehouse\\Java\\Java8Maven\\LazyCode\\src\\main\\resources\\fast-dfs\\windows.jpg";


//            String client_conf_path =  args[0];
//            String img_path = args[1];

            Properties props = new Properties();
            InputStream in = new FileInputStream(client_conf_path);
            props.load(in);
            String tracker_server_ip = props.getProperty("tracker_server").split(":")[0];

            // 1、加载配置文件，配置文件中的内容就是tracker服务的地址（绝对路径）
            ClientGlobal.init(client_conf_path);
            // 2、创建一个TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            // 3、使用阿哥TrackerClient对象创建连接，获得一个TrackerServer对象
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            // 4、创建一个StorageServer的引用，值为null
            StorageServer storageServer = null;
            // 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、Storage、Server的引用
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            // 6、使用StorageClient对象上传图片,扩展名不用带“.”
            String[] sarr = img_path.split("\\.");
            String [] strs = storageClient.upload_file(img_path,sarr[sarr.length -1 ],null);
            // 7、返回数组，包含组名和图片的路径
            String path = "";
            for (String str : strs) {   // 组名+磁盘地址
                path = path + str + "/";
            }
            // 进行地址处理并输出
            String file_path = path.substring(0,path.length()-1);
            System.out.print("地址为：" + "http://"+tracker_server_ip+":8888/"+file_path);
            // http://192.168.138.140:8888/group1/M00/00/00/wKiKjGOd9-KAT0P0AAGNxYtFUc4616.png

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
