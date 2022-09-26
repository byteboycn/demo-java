package cn.byteboy.demo.jvm.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

/**
 * @author hongshaochuan
 */
@ExtendWith(MockitoExtension.class)

public class MockTest {

    @Spy
    A a;


    @Test
    public void a() {
//        A a = spy(A.class);
        B b = mock(B.class);

        a.say(b);
        verify(a).say(b);
        verify(b, times(2)).name();
    }
}

class A {

    public void say(B b) {
        b.name();
        System.out.println("exec by say");
        System.out.println(b.name());
    }
}

class B {
    public String name() {
        return "bb";
    }

    public int age() {
        return 99;
    }


}
