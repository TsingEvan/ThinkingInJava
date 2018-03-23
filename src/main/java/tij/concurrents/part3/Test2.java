package tij.concurrents.part3;

public class Test2 {
    private int i = 1000;
    private Student student  = new Student("tsing-evan",25);
    public int next() {
        try{
            return i;
        } finally {
            i++;
//            return ++i;
        }
//        try{
////            return student;
//                                /**
//                                 * 在try中，return时发现有finally子句，
//                                 * 然后跑去执行finally子句的时候，
//                                 * try中的return语句其实已经决定好了自己究竟要返回哪个对象，
//                                 * return手中拿的已经不是student这个引用变量名了，
//                                 * 而是对象在内存中的地址！！！！！！！！！！
//                                 */
//        }finally {
//            student = new Student();
//            student.setName("dingyf");
//            student.setAge(26);
////            return student;
//        }
//        return student;
    }

    public static void main(String[] args) {
        int student = new Test2().next();
//        Student student = new Test2().next();
        System.out.println(student);
    }
}
class Student{
    private String name ;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString(){
        return name+","+age;
    }
}
