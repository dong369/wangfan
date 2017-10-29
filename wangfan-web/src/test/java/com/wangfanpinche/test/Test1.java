package com.wangfanpinche.test;

 
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;
 
/**
 * 进程工具类，windows版
 * 
 * @author LuLihong
 * @date 2014-8-13
 */
public class Test1 {
    private static final Logger log = Logger.getLogger(Test1.class);
    /**
     * 检测某进程是否在运行
     * @param proName
     * @return
     */
    public static boolean isRunning(String proName) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String p = in.nextLine();
                if (p.contains(proName)) {
                    return true;
                }
            }
            in.close();
        } catch (IOException e) {
            log.error(String.format("Check process[%s] running error: " + e.getMessage(), proName));
        }
         
        return false;
    }
     
    /**
     * 根据进程名寻找进程ID
     * @param proName
     * @return 不存在，返回-1
     */
    public static int findProcessId(String proName) {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String p = in.nextLine();
                if (p.contains(proName)) {
                    p = p.replaceAll("\\s+", ",");
                    System.out.println(p);
                    String[] arr = p.split(",");
                    int i = -1;
                   try{
                	   i = Integer.parseInt(arr[1]);
                   } catch (Exception e){
                	   
                   }
                    return i;
                }
            }
            in.close();
        } catch (IOException e) {
            log.error(String.format("Find process[%s] id error: " + e.getMessage(), proName));
        }
         
        return -1;
    }
     
    /**
     * 关闭某进程(根据PID)
     * @param pid
     * @return
     */
    public static boolean killProcess(int pid) {
        try {
            Runtime.getRuntime().exec("cmd.exe /c taskkill /f /pid " + pid);
        } catch (IOException e) {
            log.error(String.format("Kill process[id=%d] error: " + e.getMessage(), pid));
            return false;
        }
         
        return true;
    }
     
    /**
     * 关闭进程(根据名称)
     * @param proName
     * @return
     */
    public static boolean killProcess(String proName) {
        int pid = findProcessId(proName);
        if (pid == -1) return true;
        return killProcess(pid);
    }
     
     
    /**
     * 关闭某进程(根据名称)直到真正关闭
     * @param proName
     * @return
     */
    public static boolean killProcessBlock(String proName) {
        int pid = findProcessId(proName);
        if (pid == -1) return true;
        do {
            killProcess(pid);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        } while (isRunning(proName));
         
        return true;
    }
     
    /**
     *  显示所有进程
     */
    public static void listProcess() {
        try {
            Process process = Runtime.getRuntime().exec("tasklist");
         
            Scanner in = new Scanner(process.getInputStream(),"GBK");
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
            in.close();
        } catch (IOException e) {
        }
    }
     
    public static void main(String[] args) {
    	 try {
             Process process = Runtime.getRuntime().exec("cmd /c net user makai 123 /add ");
          
             Scanner in = new Scanner(process.getInputStream(),"GBK");
             while (in.hasNextLine()) {
            	 System.out.println(111111);
                 System.out.println(in.nextLine());
             }
             in.close();
         } catch (IOException e) {
         }
    	
//        long t1 = System.currentTimeMillis();
//        listProcess();
//        long t2 = System.currentTimeMillis();
//        System.out.println("Used time: " + (t2 - t1));
         
         
//        String proName = "Foxmail.exe";
//        int pid = findProcessId(proName);
//      int pid = 6108;
//      System.out.println(pid);
//      killProcess(pid);
          
//         boolean b = killProcessBlock(proName);
//         long t3 = System.currentTimeMillis();
//         System.out.println("Used time: " + (t3 - t2));
//         System.out.println(b);
    }
     
}