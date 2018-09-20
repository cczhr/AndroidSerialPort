package cczhr.com.aoptest;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class AspectTest {
    //Around和After是不能同时作用在同一个方法上
    //execution是在被切入的方法中，call是在调用被切入的方法前或者后
    private static final String TAG = "cczhr-----";


    //@Before具体的插入点 第一个*为返回值 （）表示方法参数
    @Before("execution(* cczhr.com.aoptest.MainActivity.on*(android.os.Bundle))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^onActivityMethodBefore: " + key);
    }

    @After("execution(* cczhr.com.aoptest.MainActivity.on*(android.os.Bundle))")
    public void onActivityMethodAfter(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.e(TAG, "********************onActivityMethodAfter: " + key);
    }

    /*  @Around("execution(* cczhr.com.aoptest.MainActivity.testAOP())")
      public void onActivityMethodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
          String key = proceedingJoinPoint.getSignature().toString();
          Log.d(TAG, "--------------------onActivityMethodAroundFirst: " + key);
          proceedingJoinPoint.proceed();
          Log.d(TAG, "--------------------onActivityMethodAroundSecond: " + key);
      }*/
    @Pointcut("execution(@cczhr.com.aoptest.DebugTool * *(..))&& @annotation(debugTool)")  //@annotation(debugTool)用来表示debugTool参数是注解类型
    public void DebugToolMethod(DebugTool debugTool) {
    }


   // @Around 会替换原先执行的代码，但如果你仍然希望执行原先的代码，可以使用joinPoint.proceed()。
   @Around("DebugToolMethod(debugTool)")
    public void onDebugToolMethodBefore(ProceedingJoinPoint joinPoint,DebugTool debugTool) throws Throwable {
     //  Log.e(TAG, "before->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());


        String key = joinPoint.getSignature().toString();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            Log.d(TAG, "第" + (i + 1) + "个参数为:" + args[i]);

        }

        Log.d(TAG, "$$$$$$$$$$$$$$$$$$onDebugToolMethodBefore注解: " + debugTool.resourceId());
        Log.d(TAG, "$$$$$$$$$$$$$$$$$$onDebugToolMethodBefore: " + key);
        joinPoint.proceed();
    }



    @Pointcut("get(int cczhr.com.aoptest.MainActivity.data)")
    public void fieldget() {}
    @Around("fieldget()")
    public int fieldget(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        Object proceed = thisJoinPoint.proceed();
        Log.d(TAG,thisJoinPoint.toLongString());
        Log.d(TAG, thisJoinPoint.getSignature().getName());
        Log.d(TAG,"--->"+proceed);
        return (int) proceed; //必须return 否则报错（applying to join point that doesn't return void: field-get）
    }
   /* @AfterReturning(pointcut = "fieldget()",returning = "data")
    public void  fieldget2(JoinPoint joinPoint,int data){
        Log.e("zoudong", "fieldget2====" + "joinPoint = [" + joinPoint + "], data = [" + data + "]");
        // 不能使用return
    }*/



}