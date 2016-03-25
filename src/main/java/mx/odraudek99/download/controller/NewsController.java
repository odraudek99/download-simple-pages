package mx.odraudek99.download.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mx.odraudek99.download.exception.ErrorDetail;
import mx.odraudek99.download.exception.MSMException;

@RestController
class NewsController {


	static final Logger logger = Logger.getLogger(NewsController.class);

		@RequestMapping(value = "/pagina/**", method = RequestMethod.GET)
		@ResponseBody
		public FileSystemResource get(HttpServletRequest request, HttpServletResponse response) throws IOException  {
		
			
		String url = request.getParameter("url");
		logger.info("url: "+url);
			
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		
		String path = executeCommand(url);
		File file = new File (path);
	
		
		return new FileSystemResource(file);
	}

	
	private String executeCommand(String url) {

		StringBuffer output = new StringBuffer();

		Process p;
		Date date = new Date();
		try {
			
			p = Runtime.getRuntime().exec("wget "+url+ " --output-document=/tmp/"+date.getTime()+".html");
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/tmp/"+date.getTime()+".html";

	}
	
    @ExceptionHandler(MSMException.class)
	public ErrorDetail myError(HttpServletRequest request, Exception exception) {
	    ErrorDetail error = new ErrorDetail();
	    error.setStatus(HttpStatus.BAD_REQUEST.value());
	    error.setMessage(exception.getMessage());
	    error.setUrl(request.getRequestURL().toString());
	    return error;
	}
}
