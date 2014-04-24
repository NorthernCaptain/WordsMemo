package northern.captain.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import northern.captain.app.WordsMemo.AndroidContext;
import northern.captain.app.WordsMemo.R;

import java.util.HashMap;
import java.util.Map;

public class MyToast
{
	public static final int INFO = 0;
	public static final int WARN = 1;
	public static final int CHAT = 2;
	public static final int ALWAYS = 3;
	
	public static final long repeatDelay = 20000L;
	private static MyToast singleton;
	private boolean canShow = false;
	private long lastShown = 0L;
	private int lastMsg = 0;
	
	private Map<Integer, Integer> msgTrans = new HashMap<Integer, Integer>();
	
	
	
	public static MyToast instance()
	{
		return singleton;
	}
	
	public static void initialize()
	{
		if(singleton == null)
			singleton = new MyToast();
	}
	
	private MyToast()
	{
        setCanShow(true);
	}
	
	private static final int TOP_MARGIN = 50;

	/**
	 * Is it too early to show next message or not?
	 * @return true means too early
	 */
	public static boolean tooEarly()
	{
		return singleton.isTooEarly();
	}

	
	public static void toast(int type, int msg_id, boolean longtime)
	{
		singleton.doToast(type, msg_id, longtime);
	}
	
	public static void toast(int type, String msg, boolean longtime)
	{
		singleton.doToast(type, msg, longtime);
	}
	

	private void doToast(int type, int msg_id, boolean longtime)
	{
		if(!canShow && type != ALWAYS)
			return;
		
		if(type == ALWAYS)
			type = WARN;
		
		if(type != WARN && !canToast(msg_id))
			return;
		
		Toast toast = makeText(type, msg_id, longtime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, TOP_MARGIN);
		toast.show();
	}
	
	private void doToast(int type, String msg, boolean longtime)
	{
		if((!canShow && type != ALWAYS) || msg == null)
			return;
		
		if(type == ALWAYS)
			type = WARN;
		
		Toast toast = makeText(type, msg, longtime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, TOP_MARGIN);
		toast.show();
	}
	
	private boolean canToast(int msg_id)
	{
		if(!canShow)
			return false;
		long currentTime = System.currentTimeMillis();
		
		if(msg_id == lastMsg && currentTime - lastShown < repeatDelay)
			return false;
		
		lastShown = currentTime;
		lastMsg = msg_id;
		
		return true;
	}
	
	private boolean isTooEarly()
	{
		long currentTime = System.currentTimeMillis();
		
		if(currentTime - lastShown < repeatDelay)
			return true;
		return false;
	}
	
	public void setCanShow(boolean canShow)
	{
		this.canShow = canShow;
	}
	
	public Toast makeText(int type, int msgId, int duration)
	{
		String msg = AndroidContext.current.app.getResources().getString(msgId);
		Toast toast = newToast(type, msg);
		toast.setDuration(duration);
		return toast;
	}
	
	public Toast makeText(int type, String msg, int duration)
	{
		Toast toast = newToast(type, msg);
		toast.setDuration(duration);
		return toast;		
	}
	
	public Toast newToast(int type, String textMsg)
	{
		LayoutInflater inflater = 
			(LayoutInflater) AndroidContext.current.app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		int layId, viewId, textId;
		
		switch(type)
		{
		case CHAT:
			layId = R.layout.toast_chat_layout;
			viewId = R.id.toastChatLay;
			textId = R.id.toastChatText;
			break;
		case WARN:
			layId = R.layout.toast_warn_layout;
			viewId = R.id.toastWarnLay;
			textId = R.id.toastWarnText;
			break;
		default:
			layId = R.layout.toast_info_layout;
			viewId = R.id.toastInfoLay;
			textId = R.id.toastInfoText;
			break;
			
		}
		
		View layout = inflater.inflate(layId,
		                               (ViewGroup) AndroidContext.current.app.findViewById(viewId));

		TextView text = (TextView) layout.findViewById(textId);
		text.setText(textMsg);

		Toast toast = new Toast(AndroidContext.current.app.getApplicationContext());
		toast.setView(layout);
		return toast;
	}
}
