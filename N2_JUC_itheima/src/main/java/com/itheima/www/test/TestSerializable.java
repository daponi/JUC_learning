package com.itheima.www.test;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.util.Date;

/**
 * java序列化主要有两个接口，这两个接口的实现方式：
 * 1. Serializable：一个对象想要被序列化，那么它的类就要实现此接口，这个对象的所有属性（包括private属性、包括其引用的对象）都可以被序列化和反序列化来保存、传递；
 * 2. Externalizable：他是Serializable接口的子类，有时我们不希望序列化那么多，可以使用这个接口，这个接口的writeExternal()和readExternal()方法可以指定序列化哪些属性;
 *
 * 但是如果你只想隐藏一个属性，比如用户对象user的密码pwd，如果使用Externalizable，并除了pwd之外的每个属性都写在writeExternal()方法里，
 * 这样显得麻烦，可以使用Serializable接口，并在要隐藏的属性pwd前面加上transient就可以实现，使用transient关键字修饰的变量不会被序列化。
 */
public class TestSerializable {


    public static void main(String[] args) {

        serialize("text.txt",new UserInfo("用户", "123456", 21));
        deserialize("text.txt",UserInfo.class);

        System.out.println(System.getProperty("user.dir"));

        serialize("text2.txt",new ManagerInfo("管理员", "123456", 14,22));
        deserialize("text2.txt",ManagerInfo.class);
    }

    /**
     * 序列化对象到文件
     *
     * @param fileName
     */
    public static <T> void serialize(String fileName, T t) {
        try {
            // 将java对象序列化到文件输出流
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject("序列化的日期是：");//序列化一个字符串到文件
            out.writeObject(new Date());//序列化一个当前日期对象到文件
            out.writeObject(t);//序列化一个会员对象

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中反序列化对象
     *
     * @param fileName
     */
    public static <T> void deserialize(String fileName, T t) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            String str = (String) in.readObject();//刚才的字符串对象
            Date date = (Date) in.readObject();//日期对象
            T tObjct= (T) in.readObject();//会员对象

            System.out.println(str);
            System.out.println(date);
            System.out.println(tObjct);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


/**
 * 使用Serializable接口才能被序列化
 */
@AllArgsConstructor
@Data
class UserInfo implements Serializable {
    private String userName;
    private transient String password;//使用transient关键字修饰的变量不会被序列化，所以得到的是默认值
    private int userAge;

    /**
     * 修改了一下UserInfo的无参构造，在无参构造中给userAge属性赋值蛋反序列化得到的结果还是一样。
     *  得出结论：
     *    当从磁盘中读出某个类的实例时，实际上并不会执行这个类的构造函数，
     *    而是载入了一个该类对象的持久化状态，并将这个状态赋值给该类的另一个对象。
     */
    public UserInfo() {
        userAge = 20;
    }
}

/**
 * 使用Externalizable接口才能被序列化
 */
@AllArgsConstructor
@Data
class ManagerInfo implements Externalizable {
    private String userName;
    private  String password;
    private  int num;
    private int userAge;

    /**
     * 我修改了一下UserInfo的无参构造，在无参构造中给userAge属性赋值蛋反序列化得到的结果是userAge变成了20。
     * 得出结论：
     * 当从磁盘中读出某个类的实例时，如果该实例使用的是Externalizable序列化，会执行这个类的构造函数，
     * 然后调用readExternal给其他属性赋值
     */
    public ManagerInfo() {
        userAge = 20;
    }

    /**
     *指定序列化时候写入的属性。这里仍然不写入年龄
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(userName);
        out.writeObject(password);
        out.writeObject(num);
    }


    /**
     * 指定反序列化的时候读取属性的顺序以及读取的属性,
     * 若顺序写反则读取出的值是反的，因为在文件中装载对象是有序的
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        userName = (String) in.readObject();
        password = (String) in.readObject();
        num = (int) in.readObject();
    }
}