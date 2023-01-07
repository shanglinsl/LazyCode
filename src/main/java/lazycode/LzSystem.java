package lazycode;

import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.util.Objects;

public class LzSystem {
    public static String executeInWindows(String command) {
        return executeInWindows("cmd.exe", "/c", command);
    }

    public static String executeInWindows(String... command) {
        String result = null;
        try {
            String tempFilePath = LzFileDirTool.pathJoin(System.getProperty("user.dir"), "java_temp.tmp");
            LzFileDirTool.createFile(tempFilePath);

            ProcessBuilder pb = new ProcessBuilder().command(command).inheritIO();
            pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            pb.redirectOutput(new File(tempFilePath));//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。

            // 读取命令输出的内容
            result = LzFileDirTool.readFileText(tempFilePath, "GBK");

            // 删除临时文件
            LzFileDirTool.remove(tempFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String runPythonScript(String scriptPath, String... args) {
//        String python = "F:\\00_software\\Python39\\myenvs\\deepfos\\Scripts\\python.exe";
        String python = "python";
        return runPythonScript(python, scriptPath, args);
    }

    public static String runPythonScript(String python, String scriptPath, String... args) {
        String command = LzString.joinWith(" ", python, scriptPath, LzString.joinWith(" ", args));
        return executeInWindows(command);
    }


//    public static void main(String[] args) {
//        String s1 = "F:\\工作技术栈\\notebook-warehouse\\工作\\丰贺科技\\workdir\\lazycode\\lazycode\\script\\test.py";
//        System.out.println(runPythonScript(s1));
//
//    }
}
