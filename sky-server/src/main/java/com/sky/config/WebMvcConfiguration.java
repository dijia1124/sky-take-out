package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * configuration class for web mvc
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * register custom interceptor
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("registering custom interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * generate interface doc via knife4j
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sky takeout project interface document")
                .version("2.0")
                .description("sky takeout project interface document")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docket1() {
        log.info("create docket1...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sky takeout project interface document")
                .version("2.0")
                .description("sky takeout project interface document")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin interface")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docket2() {
        log.info("create docket2...");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sky takeout project interface document")
                .version("2.0")
                .description("sky takeout project interface document")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("user interface")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }



    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * extend message converter spring mvc framework
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("extend message converters...");
        // create a message converter object
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // set an object converter for the message converter
        // as it can convert java objects to json format
        converter.setObjectMapper(new JacksonObjectMapper());
        // add the message converter to the list of message converters
        converters.add(0, converter);
    }
}
