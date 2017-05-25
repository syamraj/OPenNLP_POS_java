package com.testing.app.my_test2;

import java.io.File;
import java.io.FileNotFoundException;

import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;

public class loadDataSets  {
	InputStreamFactory inputStreamFactory = null;
	
	
	public void loadIputText() throws FileNotFoundException{
		inputStreamFactory = new MarkableFileInputStreamFactory( 
  	          new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/testdata.txt"));
	}
}
