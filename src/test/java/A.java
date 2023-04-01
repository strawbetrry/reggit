import org.junit.jupiter.api.Test;

public class A {

    @Test
    public void test(){
        String origin = "abc.jpg";
        String substring = origin.substring(origin.lastIndexOf("."));
        System.out.println(substring);
    }
}
