package cczhr.com.aoptest;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/*@Aspect*/
public class ClickFilterHook {
  /*  private static Long sLastclick = 0L;
    private static final Long FILTER_TIMEM = 1000L;
    private boolean isFastClick = false;
    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) {
        if (System.currentTimeMillis() - sLastclick >= FILTER_TIMEM) {
            sLastclick = System.currentTimeMillis();
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            isFastClick=!isFastClick;
            Log.e("ClickFilterHook", "重复点击,已过滤"+isFastClick);
        }
    }*/



}


