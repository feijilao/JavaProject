package h.zh.doorbell;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements zhNetUdpDelegate, OnClickListener {
	zhNetUdp udp;
	EditText text1;
	Button btn1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1=(EditText)this.findViewById(R.id.editText1);
        btn1=(Button)this.findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        udp=new zhNetUdp(8000, null);
        udp.setDelegate(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void zhNetUdpDelegateRecvfrom(String fromIP, int port, byte[] buff,
			int len) {
		Log.i("", fromIP+" "+port+ " "+buff.length);
	}
	private Runnable udpRun=new Runnable(){

		@Override
		public void run() {
	        udp.sendTo("192.168.10.78", 8080, "".getBytes());
		}
		
	};
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.button1:
			String text=text1.getText().toString();
	        udp.sendTo("192.168.10.78", 8080, text.getBytes());
			break;
		}
	}

    
}
