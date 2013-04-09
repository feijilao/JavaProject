package h.zh.doorbell;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class zhNetUdpSender extends Thread {
	boolean isStop = true;
	DatagramSocket m_socket;
	Queue<DatagramPacket> queue = new LinkedList<DatagramPacket>();
	ReadWriteLock lock = new ReentrantReadWriteLock();  
    Lock read = lock.readLock();  
    Lock write = lock.writeLock();  

	public zhNetUdpSender(DatagramSocket m_socket) {
		this.m_socket = m_socket;
	}

	@Override
	public void run() {
		try {
			System.out.println("Start to send");
			synchronized (queue) {
				while (!isStop) {
					read.lock();
					DatagramPacket dp=null;
					while((dp=queue.poll())!=null) {// 发送消息
						m_socket.send(dp);
					}
					read.unlock();
					sleep(10);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void sendTo(String host, int port, byte[] bs) {
		write.lock();
		try {
			DatagramPacket packet = new DatagramPacket(bs, bs.length,
					InetAddress.getByName(host), port);
			queue.add(packet);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write.unlock();
	}
	public void startSender(){
		isStop=false;
		this.start();
	}
	public void stopSender() {
		isStop = true;
	}
}

public class zhNetUdp extends Thread {
	private zhNetUdpDelegate m_delegate;
	private boolean m_thread;
	private DatagramPacket m_recvPacket;
	private DatagramSocket m_socket;
	private byte[] m_recvBuf = new byte[2048];
	private zhNetUdpSender udpSender;

	public zhNetUdp(int bindPort, zhNetUdpDelegate delegate) {
		startNet(bindPort);
		setDelegate(delegate);
	}

	public void startNet(int bindPort) {
		try {
			m_thread = true;
			m_socket = new DatagramSocket(bindPort);
			udpSender = new zhNetUdpSender(m_socket);
			udpSender.startSender();
			this.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopNet() {
		udpSender.stopSender();
		m_socket.close();
		m_thread = false;
	}

	public void setDelegate(zhNetUdpDelegate delegate) {
		m_delegate = delegate;
	}

	public void sendTo(String host, int port, byte[] bs) {
		udpSender.sendTo(host, port, bs);
	}

	public void run() {
		try {
			while (m_thread) {
				// 接受数据
				m_recvPacket = new DatagramPacket(m_recvBuf, m_recvBuf.length);
				m_socket.receive(m_recvPacket);
				m_delegate.zhNetUdpDelegateRecvfrom(m_recvPacket.getAddress()
						.getHostAddress(), m_recvPacket.getPort(), m_recvPacket
						.getData().clone(), m_recvPacket.getLength());
				sleep(10);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
