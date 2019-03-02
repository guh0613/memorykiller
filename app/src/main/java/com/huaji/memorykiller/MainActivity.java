package com.huaji.memorykiller;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import java.io.*;
import android.content.*;
import java.nio.channels.*;
import java.nio.*;

public class MainActivity extends Activity 
{
	private EditText edit;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
    }


		public enum FileUnit {
			KB, MB, GB
			}

		/**
		 * 创建指定大小和类型的文件
		 * @author cxq
		 * @param targetFile 文件路径以及文件名，需要加后缀
		 * @param fileLength 文件大小
		 * @param unit 单位，KB,MB，GB
		 * @retrun boolean
		 */
		public boolean createFile(String targetFile, long fileLength, FileUnit unit) {
			//指定每次分配的块大小
			long KBSIZE = 1024;
			long MBSIZE1 = 1024 * 1024;
			long MBSIZE10 = 1024 * 1024 * 10;
			switch (unit) {
				case KB:
					fileLength = fileLength * 1024;
					break;
				case MB:
					fileLength = fileLength * 1024*1024;
					break;
				case GB:
					fileLength = fileLength * 1024*1024*1024;
					break;

				default:
					break;
			}
			FileOutputStream fos = null;
			File file = new File(targetFile);
			try {

				if (!file.exists()) {
					file.createNewFile();
				}

				long batchSize = 0;
				batchSize = fileLength;
				if (fileLength > KBSIZE) {
					batchSize = KBSIZE;
				}
				if (fileLength > MBSIZE1) {
					batchSize = MBSIZE1;
				}
				if (fileLength > MBSIZE10) {
					batchSize = MBSIZE10;
				}
				long count = fileLength / batchSize;
				long last = fileLength % batchSize;

				fos = new FileOutputStream(file);
				FileChannel fileChannel = fos.getChannel();
				for (int i = 0; i < count; i++) {
					ByteBuffer buffer = ByteBuffer.allocate((int) batchSize);
					fileChannel.write(buffer);

				}
				if (last != 0) {
					ByteBuffer buffer = ByteBuffer.allocate((int) last);
					fileChannel.write(buffer);
				}
				fos.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
	
	
	//写入内部储存
	public void b1(View view)
	{
		//由于写入文件时应用处理无响应，toast会在处理完成后弹出
		Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
		edit=(EditText) findViewById(R.id.ET1);//获取输入内容
		String input=edit.getText().toString();
		int filelength = Integer.parseInt(input);//转换成int型
		//写入文件
		String data="1";
		FileOutputStream out=null;
		BufferedWriter writer=null;
		try{
			out=openFileOutput("data",Context.MODE_APPEND);
			writer=new BufferedWriter(new OutputStreamWriter(out));
			//通过for循环写入1
			for(int a=0;a<filelength*1024*1024;a++)
			{
				writer.write(data);
			}
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
	    }
		//写入外部储存
		public void b2(View view)
		{
			//由于写入文件时应用处理无响应，toast会在处理完成后弹出
			Toast.makeText(MainActivity.this,"写入完成!",Toast.LENGTH_LONG).show();
			edit=(EditText) findViewById(R.id.ET1);//获取输入内容
			String input=edit.getText().toString();
			int filelength = Integer.parseInt(input);//转换成int型
			//写入文件
			String filePath="/sdcard/老子占内存.txt";

			int fileSize=filelength;

			createFile(filePath, fileSize, FileUnit.MB);
		}
	


}
