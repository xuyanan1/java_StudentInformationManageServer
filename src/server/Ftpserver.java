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
		 System.out.println("服务已启动,绑定1234端口!");
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
		
			 
			   
		//为了简单起见，所有的异常信息都往外抛  
	      int port = 8899;  
	      //定义一个ServerSocket监听在端口8899上  
	      ServerSocket server = new ServerSocket(port);  
	      //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的 
	      while(true)
	      {
	    	  Socket socket = server.accept();  
	    	  //跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。  
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
	    		  if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
	    			  sb.append(temp.substring(0, index));  
	    			  break;  
	    		  }  
	    		  sb.append(temp);  
	    	  }  
	    	  System.out.println("from client: " + sb); 
	      }
	      else{
	    	  //读完后写一句  
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



//用于带文件名的读写
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
	    		  if ((index = temp.indexOf("eof")) != -1) {//遇到eof时就结束接收  
	    			  sb.append(temp.substring(0, index));  
	    			  break;  
	    		  }  
	    		  sb.append(temp);  
	    	  }  
	    	  System.out.println("from client: " + sb); 
	      }
	      else{
	    	  //读完后写一句  
	    	  Writer writer = new OutputStreamWriter(socket.getOutputStream());  
	    	  writer.write("Hello Client.");  
	    	  writer.flush();  
	    	  writer.close();
	      }
	      reader.close();  
	      socket.close(); 
	      
	}
	
	
}


//带文件名读写
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
			System.out.println("进入文件发送");
			while((len = fin.read(buffer)) != -1)
			{
				out.write(buffer, 0, len);
				System.out.println("发送中");
			}
			fin.close();
			out.close();
		}
	}
}




