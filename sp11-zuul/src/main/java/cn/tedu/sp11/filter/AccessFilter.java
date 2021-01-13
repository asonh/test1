package cn.tedu.sp11.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import cn.tedu.web.util.JsonResult;
@Component
public class AccessFilter extends ZuulFilter{
	//对于当前请求，是否执行此处的过滤代码
	@Override
	public boolean shouldFilter() {
		//对于商品服务的方法要执行过滤代码，其他服务不执行过滤代码
		//获得访问的服务id
		RequestContext ctx = RequestContext.getCurrentContext();//得到请求的上下文对象
		String serverId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);//通过服务id的key键，获得服务id的值
		if ("item-service".equals(serverId)) {
			return true;
		}
		return false;
	}
	//过滤代码，权限校验
	@Override
	public Object run() throws ZuulException {
		// TODO 从路径参数获得token参数，有这个参数认为已经登录，没有这个参数认为没有登录
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String token = request.getParameter("token");
		if (token==null||token.length()==0) {
			//TODO 阻止这次请求继续执行，
			ctx.setSendZuulResponse(false);
			//直接返回一个未登录的提示
			ctx.setResponseStatusCode(200);
			ctx.setResponseBody(JsonResult.err("no login!").code(JsonResult.NOT_LOGIN).toString());
		}
		return null; //zuul为了将来扩展，设计了一个返回值，现在任然没有使用。
	}
	//返回过滤器的类型，前置过滤器类型
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	//过滤器的顺序号，需要放入大于5的值,因为zuul的默认过滤器中的第五个过滤器，向ctx对象保存了一个service-id
	//必须在第五个过滤器之后才能访问这个service-id
	@Override
	public int filterOrder() {
		return 6;
	}

}
