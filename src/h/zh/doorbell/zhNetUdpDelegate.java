package h.zh.doorbell;

public interface zhNetUdpDelegate {
	/*
	 * �ݹ��ȥ��������һ���µ��߳�,���Ҫ����UIҪ��Message
	 * 
	 * Handler handler=new Handler(){
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * super.handleMessage(msg); switch(msg.what){ case 0:
	 * Toast.makeText(MainActivity.this, "hehehehe...", Toast.LENGTH_SHORT)
	 * .show(); break; } } };
	 * 
	 * Message msg=new Message(); msg.what=0; //��Ϣcode msg.obj=null; //��Ϣ����
	 * handler.sendMessage(msg);
	 */
	void zhNetUdpDelegateRecvfrom(String fromIP, int port, byte[] buff, int len);
}