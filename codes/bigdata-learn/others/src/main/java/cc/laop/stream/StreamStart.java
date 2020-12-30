package cc.laop.stream;

import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/5/27 11:18
 * @Description:
 */
public class StreamStart {

    public static void main(String[] args) {

        List<Stu> stulist = new ArrayList<>();
        stulist.add(new Stu("a", 12, "f"));
        stulist.add(new Stu("b", 13, "f"));
        stulist.add(new Stu("c", 11, "m"));


        // sort 1
        //List<Stu> stu1 = stulist.stream().sorted(Comparator.comparing(Stu::getAge)).collect(Collectors.toList());
        //stu1.forEach(System.out::println);


        //List<Integer> agelist = stulist.parallelStream().mapToInt(Stu::getAge).asLongStream().collect()

        //Stream.of(Locale.getISOLanguages()).forEach(System.out::println);
        //Stream.of(Locale.getISOCountries()).forEach(System.out::println);
        List list = Stream.of(Locale.getISOLanguages()).map(it -> {
            Map map = new HashMap();
            map.put("value", it);
            map.put("label", new Locale(it).getDisplayLanguage(new Locale("en")));
            return map;
        }).distinct().collect(Collectors.toList());

        System.out.println(JSON.toJSONString(list));
    }

    private static Object apply(String it) {
        Locale locale = new Locale(it);
        return it + ", " + locale.getDisplayLanguage(new Locale("en"));
    }


    public static class Stu {
        private String name;
        private int age;
        private String sex;

        public Stu() {
        }

        public Stu(String name, int age, String sex) {
            this.age = age;
            this.name = name;
            this.sex = sex;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Stu{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }

}
