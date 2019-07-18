package cn.itechyou.blog.common;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import cn.itechyou.blog.entity.Theme;
import cn.itechyou.blog.front.service.ArticleService;
import cn.itechyou.blog.front.service.CategoryService;
import cn.itechyou.blog.front.service.LabelService;
import cn.itechyou.blog.front.service.NavigateService;
import cn.itechyou.blog.front.service.PageService;
import cn.itechyou.blog.service.SystemService;
import cn.itechyou.blog.service.ThemeService;


/**
 * 配置静态资源映射
 * @author sam
 * @since 2017/7/16
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Resource
	private SystemService systemService;
	
	@Resource
	private ThemeService themeService;
	
	@Resource
	private PageService pageService;
	
	@Resource
	private CategoryService categoryService;
	
	@Resource
	private ArticleService articleService;
	
	@Resource
	private LabelService labelService;
	
	@Resource
	private NavigateService navigateService;
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/favicon.ico").setViewName("forward:/resource/icon/favicon.ico");
        registry.addViewController("/admin").setViewName("forward:/admin/toLogin");
        registry.addViewController("/admin/").setViewName("forward:/admin/toLogin");
        registry.addViewController("/").setViewName("forward:/index");
        registry.addViewController("").setViewName("forward:/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }
    
    @Resource(name="thymeleafViewResolver")
    ThymeleafViewResolver thymeleafViewResolver;
    
    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
    	Theme theme = themeService.getCurrentTheme();
        if(thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap();
            vars.put("system", systemService.getSystemList());
            vars.put("templatesdir", "/themes/" + theme.getThemePath() + File.separator);
            /*vars.put("navigate", navigateService);
            */
            vars.put("pages", pageService);
            vars.put("categorys", categoryService);
            vars.put("archives", articleService);
            vars.put("labels", labelService);
            
            vars.put("constant", new FrontConstant());
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }
}