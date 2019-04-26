package cn.edu.njnu.geoproblemsolving.Aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Aspect
@Component
@Order(1)
public class NeedSessionAspect {

    @Pointcut("@annotation(cn.edu.njnu.geoproblemsolving.Annotation.NeedSession)")
    public void poin() {
    }

    @Before("poin()")
    public void doBeforePoint() throws IOException {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        HttpSession session = request.getSession();
//        HttpServletResponse response = attributes.getResponse();
//        String userId = session.getAttribute("userId").toString();
//        if(userId==null){
//            Writer writer = response.getWriter();
//            writer.write("NoSession");
//            writer.flush();
//        }
    }
}
