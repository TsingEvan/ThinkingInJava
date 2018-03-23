package xfiles;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Main {

    private Main(){

    }

    private static Main ma = new Main();

    public static Main getInstance(){
        return ma;
    }
    public static void main(String[] args) throws IOException {
/*
        testSingleton();

        testArrays();

        testMappedByteBuffer();

        testLockingMappedFiles();

        testGZip();*/

//        int[] ints = {12,21,2132,2,43,54};
//        int i = ints[(int)(Math.random()*ints.length)];
//        System.out.println(i);
//        boolean b = int.class == Integer.TYPE;
//        System.out.println(""!=null?"":"");



        //逻辑运算符优先级
//        System.out.println(true||false&&false);//从右向左

        //匿名类测试
//        new AbstractList(){ //居然可以这样子做匿名类，我还以为匿名内部类只能通过return构建呢
//
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public Object get(int index) {
//                return null;
//            }
//        }.get(0);
    }

    public static void testSingleton(){
        //单例模式测试
        Main m1 = Main.getInstance();
        Main m2 = Main.getInstance();
        System.out.println("单例模式测试:\nm1==m2:"+(m1==m2));
    }

    public static void testArrays(){
        //Arrays测试
        List list = Arrays.asList();
        System.out.println("Arrays测试:\nlist==null:"+(list==null));
    }

    public static void testMappedByteBuffer() throws IOException {
        //文件内存映射测试
        int length = 0x8FFFFFF;
        MappedByteBuffer out = new RandomAccessFile("src/resource/test.dat","rw").getChannel()
                .map(FileChannel.MapMode.READ_WRITE, 0, length);
        for (int i = 0; i<length;i++){
            out.put((byte)'x');
        }
        System.out.println("文件内存映射测试：");
        for (int i = length/2;i<length/2+6;i++){
            System.out.print(i+":"+(char)out.get(i)+",");
        }
    }

    static FileChannel fc;
    static final int LENGTH = 0x8FFFFFF;
    public static void testLockingMappedFiles() throws IOException {
        System.out.println("锁测试：");
        fc = new RandomAccessFile("src/resource/locking_mapped_files.dat", "rw").getChannel();
        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);
        for (int i = 0;i<LENGTH;i++){
            mbb.put((byte)'x');
        }
        new LockAndModify(mbb, 0, LENGTH/3);
        new LockAndModify(mbb, LENGTH/2, LENGTH/2+LENGTH/3);
    }
    private static class LockAndModify extends Thread {
        ByteBuffer buff;
        int start,end;
        LockAndModify(ByteBuffer src, int start, int end) throws IOException {
            this.start = start;
            this.end = end;
            src.limit(end);
            src.position(start);
            buff = src.slice();
            start();
        }
        public void run(){
            try {
                FileLock fl = fc.lock(start, end, false);
                System.out.println("Lock:"+start +","+end);
                for (int i = 0; i < 50331647/2; i++){
                    buff.put((byte)(buff.get()+1));
                }
//                int i = 0;
//                while(buff.position()<buff.limit()-1){
//                    i++;
//                    buff.put((byte)(buff.get()+1));
//                }
//                System.out.println(i);
//                System.err.println("1#"+buff.position());
//                buff.get();
//                System.err.println("2#"+buff.position());
//                buff.put(new Byte("1"));
//                System.err.println("3#"+buff.position());
//                buff.limit();
//                System.err.println("4#"+buff.position());
//
//                buff.put((byte)(buff.get()+1));
//                buff.put((byte)(buff.get()+1));
                fl.release();
                System.out.println("Release:"+start +","+end);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testGZip() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/resource/gzip.dat"));
        BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream("src/resource/gzip.gz"))));
        String c ;
        while((c=br.readLine())!=null){
            bos.write(c+"\n");
        }
        br.close();
        bos.close();

        BufferedReader bis = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream("src/resource/gzip.gz"))));
        String s ;
        while((s=bis.readLine())!=null){
            System.out.println(s);
        }
    }

    public  static void testDefaultPackage(){
//        new Alien();
//        new DefaultPackageClass();
    }


}