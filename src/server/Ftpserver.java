package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

public class Ftpserver {
	public static void main(String args[])throws IOException{
		/*
			ServerSocket serverSocket = null ;
		try {
			serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		 System.out.println("����������,��1234�˿�!");
		 while(true){
			   Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   new ServerThread(socket).start();
			  }
		
	}

}



	
	
	
	*/
		
			 
			   
		//Ϊ�˼���������е��쳣��Ϣ��������  
	      int port = 8899;  
	      //����һ��ServerSocket�����ڶ˿�8899��  
	      ServerSocket server = new ServerSocket(port);  
	      //server���Խ�������Socket����������server��accept����������ʽ�� 
	      while(true)
	      {
	    	  Socket socket = server.accept();  
	    	  //���ͻ��˽���������֮�����ǾͿ��Ի�ȡsocket��InputStream�������ж�ȡ�ͻ��˷���������Ϣ�ˡ�  
	    	  //new Thread(new ServerThread(socket)).start();
	    	  new Thread(new ServerThread1(socket)).start();
	      }
	      //server.close();
	}
			  	
}
 
class Myserver implements Runnable{

	Socket socket;
	public Myserver(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			handle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void handle() throws IOException{
		
			Reader reader = new InputStreamReader(socket.getInputStream());  
	      char chars[] = new char[64];  
	      int len;  
	      StringBuilder sb = new StringBuilder();  
	      String temp;  
	      int index;
	      int m = reader.read();
	      if(m==1)
	      {
	    	  while ((len=reader.read(chars)) != -1) {  
	    		  temp = new String(chars, 0, len);  
	    		  if ((index = temp.indexOf("eof")) != -1) {//����eofʱ�ͽ�������  
	    			  sb.append(temp.substring(0, index));  
	    			  break;  
	    		  }  
	    		  sb.append(temp);  
	    	  }  
	    	  System.out.println("from client: " + sb); 
	      }
	      else{
	    	  //�����дһ��  
	    	  Writer writer = new OutputStreamWriter(socket.getOutputStream());  
	    	  writer.write("Hello Client.");  
	    	  writer.flush();  
	    	  writer.close();
	      }
	      reader.close();  
	      socket.close(); 
	      
	}
}
	
class ServerThread implements Runnable{
		Socket socket;
		public ServerThread(Socket socket){
			this.socket = socket;
		}
		
		public void run(){
			try {
				myhandle();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void myhandle() throws IOException{
			int line;
			InputStream in = null;
			in = socket.getInputStream();
			line = in.read();
			System.out.println(line);
			if(line==1)
			{
				FileOutputStream fos = null;
				fos = new FileOutputStream("ftp.txt");
				byte[] buffer = new byte[1024];
				int len;
				while((len=in.read(buffer)) != -1){
					fos.write(buffer,0,len);
				}
				fos.close();
				socket.close();
			}
			else{
				FileInputStream fin = null;
				OutputStream out =null;
				fin = new FileInputStream("ftp.txt");
				out = socket.getOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while((len = fin.read(buffer)) != -1)
					out.write(buffer, 0, len);
				fin.close();
				out.close();
			}
		}
}



//���ڴ��ļ����Ķ�д
class Myserver1 implements Runnable{

	Socket socket;
	public Myserver1(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			handle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void handle() throws IOException{
		
		 Reader reader = new InputStreamReader(socket.getInputStream());  
	      char chars[] = new char[64];  
	      int len;  
	      StringBuilder sb = new StringBuilder();  
	      String temp;  
	      int index;
	      int m = reader.read();
	      reader.read(chars);
	      String filename = new String(chars);
	      System.out.println(filename);
	      if(m==1)
	      {
	    	  while ((len=reader.read(chars)) != -1) {  
	    		  temp = new String(chars, 0, len);  
	    		  if ((index = temp.indexOf("eof")) != -1) {//����eofʱ�ͽ�������  
	    			  sb.append(temp.substring(0, index));  
	    			  break;  
	    		  }  
	    		  sb.append(temp);  
	    	  }  
	    	  System.out.println("from client: " + sb); 
	      }
	      else{
	    	  //�����дһ��  
	    	  Writer writer = new OutputStreamWriter(socket.getOutputStream());  
	    	  writer.write("Hello Client.");  
	    	  writer.flush();  
	    	  writer.close();
	      }
	      reader.close();  
	      socket.close(); 
	      
	}
	
	
}


//���ļ�����д
class ServerThread1 implements Runnable{
	Socket socket;
	public ServerThread1(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		try {
			myhandle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void myhandle() throws IOException{
		int line;
		InputStream in = null;
		in = socket.getInputStream();
		line = in.read();
		int namelen = in.read();
		byte[] f = new byte[namelen];
		in.read(f);
		String filename = new String(f,"UTF-8");
		System.out.println(line);
		System.out.println(filename);
		if(line==1)
		{
			FileOutputStream fos = null;
			fos = new FileOutputStream(filename);
			byte[] buffer = new byte[1024];
			int len;
			while((len=in.read(buffer)) != -1){
				fos.write(buffer,0,len);
			}
			fos.close();
			socket.close();
		}
		else{
			FileInputStream fin = null;
			OutputStream out =null;
			fin = new FileInputStream(filename);
			out = socket.getOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			System.out.println("�����ļ�����");
			while((len = fin.read(buffer)) != -1)
			{
				out.write(buffer, 0, len);
				System.out.println("������");
			}
			fin.close();
			out.close();
		}
	}
}




