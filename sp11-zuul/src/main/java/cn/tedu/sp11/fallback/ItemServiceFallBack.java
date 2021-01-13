package cn.tedu.sp11.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import cn.tedu.web.util.JsonResult;
@Component
public class ItemServiceFallBack implements FallbackProvider{
	//返回一个服务ID，来确定那个服务失败时执行这个类中的降级代码
	@Override
	public String getRoute() {
//		return "*"; //所有服务降级都可以执行这段代码，或者return null;也是返回所有服务
//		return null;//也是所有服务，降级可以执行下方fallbackResponse方法的代码
		return "item-service";
	}

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		return response();
	}

	private ClientHttpResponse response() {
		// TODO 返回ClientHttpResponse()实例
		return new ClientHttpResponse() {

			@Override
			public InputStream getBody() throws IOException { //协议体的呈现
				// TODO 响应的数据"{code:400,msg:"后台服务请求失败",data:null}"
				String json = JsonResult.err("商品服务请求失败！！！").toString();
				return new ByteArrayInputStream(json.getBytes("UTF-8")); //输出协议体的内容，以字符串形式
			}

			@Override
			public HttpHeaders getHeaders() { //协议头
				// TODO Content-Type:application/json
				HttpHeaders h =new HttpHeaders();
				h.setContentType(MediaType.APPLICATION_JSON);
				return h;
			}

			@Override
			public HttpStatus getStatusCode() throws IOException {//HttpStatus Http的协议码和协议文本的封装200和OK
				// TODO Auto-generated method stub
				return HttpStatus.OK;
			}

			@Override
			public int getRawStatusCode() throws IOException { //就是状态码 200--数字类型
				// TODO Auto-generated method stub
				return HttpStatus.OK.value();
			}

			@Override
			public String getStatusText() throws IOException { //状态文本 OK --String类型
				return HttpStatus.OK.getReasonPhrase();
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

}
