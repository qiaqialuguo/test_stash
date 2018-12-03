package utils;

public class ToLinux {
    public void runJar(String filepath) throws Exception{

        String shpath = "/root/test.sh";   //程序路径
        Process process = null;

        String command1 = "java -cp "+filepath;
        process = Runtime.getRuntime().exec(command1);
        process.waitFor();


    }
}