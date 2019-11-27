import org.apache.commons.lang3.StringUtils;

/**
 * Created by luzhibin on 2019/11/25 14:42
 */
public class test1 {
    public static void main(String[] args) {
        System.out.println(StringUtils.isBlank(""));       //true
        System.out.println(StringUtils.isBlank(" "));      //true
        System.out.println(StringUtils.isBlank(null));      //true
        System.out.println(StringUtils.isBlank("null"));    //false
        System.out.println(StringUtils.isBlank("abc"));     //false
        System.out.println("---------------------------------------");
        System.out.println(StringUtils.isEmpty(""));        //true
        System.out.println(StringUtils.isEmpty(" "));       //false
        System.out.println(StringUtils.isEmpty(null));      //true
        System.out.println(StringUtils.isEmpty("null"));    //false
        System.out.println(StringUtils.isEmpty("abc"));     //false
    }
}

