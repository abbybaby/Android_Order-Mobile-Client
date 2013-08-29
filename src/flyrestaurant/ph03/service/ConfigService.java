package flyrestaurant.ph03.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;

public class ConfigService {
	static final String CONFIG_STRING="config";
	
	Context context;
	
	public ConfigService(Context context) 
	{
		this.context=context;
	}
	public Properties read() throws IOException 
	{
		createFile();
		Properties config=new Properties();
		FileInputStream fis=context.openFileInput(CONFIG_STRING);
		config.load(fis);
		fis.close();
		return config;
	}
	public void write(Properties config) throws IOException 
	{
		createFile();
		FileOutputStream fos=context.openFileOutput(CONFIG_STRING, Context.MODE_PRIVATE);
		config.store(fos, null);
		fos.close();
	}
	void createFile() throws IOException{
		// TODO Auto-generated method stub
		File file=new File(context.getFilesDir(),CONFIG_STRING);
		if (!file.exists())
		{
			file.createNewFile();
		}
	}
}
