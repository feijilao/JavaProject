package h.zh.doorbell;

public interface zhNetUdpDelegate {
	/*
	 * 递归回去的内容是一个新的线程,如果要操作UI要用Message
	 * 
	 * Handler handler=new Handler(){
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * super.handleMessage(msg); switch(msg.what){ case 0:
	 * Toast.makeText(MainActivity.this, "hehehehe...", Toast.LENGTH_SHORT)
	 * .show(); break; } } };
	 * 
	 * Message msg=new Message(); msg.what=0; //消息code msg.obj=null; //消息内容
	 * handler.sendMessage(msg);
	 */
	void zhNetUdpDelegateRecvfrom(String fromIP, int port, byte[] buff, int len);
}