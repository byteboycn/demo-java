//package cn.byteboy.demo.spring.web.aspect;
//
//import cn.byteboy.demo.spring.web.annotation.RequestCache;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.stereotype.Component;
////import org.springframework.web.context.request.RequestContextHolder;
////import org.springframework.web.context.request.ServletRequestAttributes;
//
////import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author hongshaochuan
// * @date 2022/3/23
// */
//@Aspect
//@Component
//@Slf4j
//public class RequestCacheAspect {
//
//    /**
//     * 解析spel表达式
//     */
//    ExpressionParser parser = new SpelExpressionParser();
//
//    /**
//     * 将方法参数纳入Spring管理
//     */
//    LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//
//    @Pointcut("@annotation(cn.byteboy.demo.spring.web.annotation.RequestCache)")
//    public void pointCut() {}
//
//    @Around("pointCut()")
//    public Object around(ProceedingJoinPoint pjp) throws Throwable {
//        MethodSignature signature = (MethodSignature) pjp.getSignature();
//        Method method = signature.getMethod();
//        RequestCache requestCache = method.getAnnotation(RequestCache.class);
//        Object[] args = pjp.getArgs();
//        //获取方法参数名
//        String[] params = discoverer.getParameterNames(method);
//
//        //将参数纳入Spring管理
//        EvaluationContext context = new StandardEvaluationContext();
//        for (int len = 0; len < params.length; len++) {
//            context.setVariable(params[len], args[len]);
//        }
//
//        Expression expression = parser.parseExpression(requestCache.key());
//        Object key = expression.getValue(context);
//        System.out.println(key);
//
//
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null) {
//            HttpServletRequest request = requestAttributes.getRequest();
//            Map<Object, Object> objMap = (Map<Object, Object>) request.getAttribute(requestCache.name());
//            if (objMap == null) {
//                objMap = new ConcurrentHashMap<>();
//                request.setAttribute(requestCache.name(), objMap);
//            }
//            Object value = objMap.get(key);
//            if (value != null) {
//                return value;
//            }
//            value = pjp.proceed(pjp.getArgs());
//            objMap.put(key, value);
//            return value;
//        } else {
//            return pjp.proceed(pjp.getArgs());
//        }
//    }
//
//}
