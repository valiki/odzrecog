package by.sunnycore.recognition;

import java.net.URL;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import by.sunnycore.recognition.image.util.ThreadLocalStorage;

/**
 * main class of the project
 * 
 * @author Valentine Shukaila
 * 
 */
public class StartRecognition {
	public static final String RECOGNITION_APP_CONTEXT = "recognition_context";
	private static ApplicationContext context;

	public static void main(String[] args) {
		URL resource = StartRecognition.class.getClassLoader().getResource("META-INF/spring-application.xml");
		context = new FileSystemXmlApplicationContext(resource.getFile());
		ThreadLocalStorage.store(RECOGNITION_APP_CONTEXT, context);
		RecognitionComponent component = (RecognitionComponent) context.getBean("recognition.app");
		component.start();
	}
}
