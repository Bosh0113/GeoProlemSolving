package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/projectlist").setViewName("redirect:/projectlist/index.html");
    }
}
