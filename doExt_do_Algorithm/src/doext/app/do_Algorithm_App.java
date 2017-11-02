package doext.app;
import android.content.Context;
import core.interfaces.DoIAppDelegate;

/**
 * APP启动的时候会执行onCreate方法；
 *
 */
public class do_Algorithm_App implements DoIAppDelegate {

	private static do_Algorithm_App instance;
	
	private do_Algorithm_App(){
		
	}
	
	public static do_Algorithm_App getInstance() {
		if(instance == null){
			instance = new do_Algorithm_App();
		}
		return instance;
	}
	
	@Override
	public void onCreate(Context context) {
		// ...do something
	}
	
	@Override
	public String getTypeID() {
		return "do_Algorithm";
	}
}
