package szjt.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConvertVideo {
    private final static String path = "D:\\hkctp\\photo\\2019\\08\\20\\201908206613a49ce69d.mp4";
    private final static String pathDir = "D:\\hkctp\\photo\\2019\\08\\20\\201908206613a49ce69d\\";
    private final static String fileName = "201908206613a49ce69d.m3u8";

    public static void main(String args[]){
        MyThread myThread = new MyThread();
        System.out.println("上传成功");
        myThread.start();
    }

    static class MyThread extends Thread{
        @Override
        public void start() {
         int resultType = process();
        if(resultType == 0){
            System.out.println("成功转码");
        }else if(resultType == 1){
            System.out.println("文件不存在");
        }else if(resultType == 2){
            System.out.println("后缀文件不符合转码要求");
        }else if(resultType == 3){
            System.out.println("转码失败");
        }
        }
    }

    /**
     *
     * @return
     *  0:成功转码
     *  1:文件不存在
     *  2:后缀文件不符合转码要求
     *  3:转码失败
     */
    private static int process(){
        if(!checkfile(path)){
            return 1;
        }
        int type = checkContentType();
        boolean status = false;
        //将mp4转乘m3u8
        if(type == 0){
            if(!checkfileDir(pathDir)){
                File fileDir=new File(pathDir);
                fileDir.mkdirs();
            }

            String commendStr = "D:\\ffmpeg\\windows\\ffmpeg-20171225\\bin\\ffmpeg.exe -i "+path+" -c:v libx264 -hls_time 10 -hls_list_size 0 -c:a aac  -strict -5 -f hls "+pathDir+fileName+"";
            System.out.println(commendStr);
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = null;

                process = runtime.exec(commendStr);

                InputStream stderr = process.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);
                BufferedReader br = new BufferedReader(isr);
                String line = null;

                while ( (line = br.readLine()) != null)
                    System.out.println(line);
                int exitVal = process.waitFor();
                System.out.println(exitVal);
                return 0;
            }catch (Exception e){
                e.printStackTrace();
                return 3;
            }

        }else{
            return 2;
        }
    }

    private static int checkContentType(){
        String type = path.substring(path.lastIndexOf(".")+1,path.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("mp4")) {
            return 0;
        }else if(type.equals("avi")){
            return 0;
        }else if(type.equals("mov")){
            return 0;
        }else if(type.equals("3gp")){
            return 0;
        }else if(type.equals("wmv")){
            return 0;
        }
        // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等),
        // 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
        else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    //判断文件是否存在
    private static boolean checkfile(String path){
        File file = new File(path);
        if(!file.isFile()){
            return false;
        }
        return true;
    }

    //判断文件夹是否存在
    private static boolean checkfileDir(String pathDir) {
        File file = new File(pathDir);
        if(!file.isFile()){
            return false;
        }
        return true;
    }
}
