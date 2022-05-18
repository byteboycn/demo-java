package cn.byteboy.demo.spring.gateway.aspect;

import cn.byteboy.demo.spring.gateway.annotation.RequestCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * @author hongshaochuan
 * @date 2022/3/23
 */
@Aspect
@Component
public class RequestCacheAspect {

    /**
     * 解析spel表达式
     */
    ExpressionParser parser = new SpelExpressionParser();

    /**
     * 将方法参数纳入Spring管理
     */
    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(cn.byteboy.demo.spring.gateway.annotation.RequestCache)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RequestCache requestCache = method.getAnnotation(RequestCache.class);
        Object[] args = pjp.getArgs();
        //获取方法参数名
        String[] params = discoverer.getParameterNames(method);

        //将参数纳入Spring管理
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }

        Expression expression = parser.parseExpression(requestCache.key());
        Object value = expression.getValue(context);
        System.out.println(value);

        int hashCode = value.hashCode();

        return pjp.proceed(pjp.getArgs());
    }

}
