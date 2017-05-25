package com.testing.app.my_test2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.cmdline.CmdLineUtil;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * Opennlp POS Tagger evaluator.
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println( "POS-Tagger For English!" );
        POSTag();
        evaluator();
    }
    
    public static void POSTag() throws IOException {
    	//Object to write to a file 
    	PrintStream printer = null;
    	printer = new PrintStream(new FileOutputStream("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/testout.txt"));
    	
    	//Loading the model
    	POSModel model = new POSModelLoader()	
		.load(new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/en-pos-maxent.bin"));
	PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
	POSTaggerME tagger = new POSTaggerME(model);
	
	//Reading the data from a file using inputstreamfactory and PlainTextByLineStream
    	InputStreamFactory inputStreamFactory = null; 
    	try { 
    	      inputStreamFactory = new MarkableFileInputStreamFactory( 
    	          new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/testdata.txt")); 
    	    } catch (FileNotFoundException e) { 
    	      e.printStackTrace(); 
    	    } 
    	    ObjectStream<String> lineStream = null; 
    	    try { 
    	      lineStream = new PlainTextByLineStream( 
    	          (inputStreamFactory), "UTF-8"); 
    	    } catch (IOException e) { 
    	      CmdLineUtil.handleCreateObjectStreamError(e); 
    	    } 
 
    	perfMon.start();
    	String line;
    	while ((line = lineStream.read()) != null) {
            if (line.isEmpty()) {
              printer.println();
            } else if (line.startsWith("//")) {
              printer.println(line);
            } else {
              String[] sent = WhitespaceTokenizer.INSTANCE.tokenize(line);
              for(int i=0; i<sent.length;++i){
            	  if(sent[i].contains("."))
            		 sent[i] = sent[i].replace(".", "");
              }
              String[] tags = tagger.tag(sent);
              POSSample sample = new POSSample(sent, tags);
              printer.println(sample.toString());
            }
            perfMon.incrementCounter();
          }
          lineStream.close();
          printer.close();
    	perfMon.stopAndPrintFinalResult();
    }
    
    public static void evaluator(){
    	
    	float overallAccracy;
    	System.out.println("Evaluation Result :");
    	//inputstreamfactory for reading POS output data
    	InputStreamFactory inputStreamFactory = null;
    	//inputstramfactory for reading Genia corpus
    	InputStreamFactory inputStreamFactory1 = null;
    	//inputstreamfactory for reading from testdata_gold.txt
    	InputStreamFactory inputStreamFactory2 = null;
    	try { 
    	      inputStreamFactory = new MarkableFileInputStreamFactory( 
    	          new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/testout.txt"));
    	      inputStreamFactory1 = new MarkableFileInputStreamFactory( 
        	          new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/Genia/GENIAcorpus3.02.pos.txt"));
    	      inputStreamFactory2 = new MarkableFileInputStreamFactory(
    	    		  new File("/home/devil/Projects/Thesis/OpenNLP/apache-opennlp-1.7.2/bin/testdata_gold.txt"));
    	    } catch (FileNotFoundException e) { 
    	      e.printStackTrace(); 
    	    } 
    	    ObjectStream<String> lineStream1 = null;
    	    ObjectStream<String> lineStream2 = null;
    	    ObjectStream<String> lineStream3 = null;
    	    try { 
    	      lineStream1 = new PlainTextByLineStream( 
    	          (inputStreamFactory), "UTF-8"); 
    	      lineStream2 = new PlainTextByLineStream( 
        	          (inputStreamFactory1), "UTF-8");
    	      lineStream3 = new PlainTextByLineStream(inputStreamFactory2, "UTF-8");
    	    } catch (IOException e) { 
    	      CmdLineUtil.handleCreateObjectStreamError(e); 
    	    }
    	    int Tp = 0;
    	    int Fp = 0;
    	 //   int Tn = 0;
    	    int Fn = 0;
    	    String line1;
    	    String line2;
    	    String line3;
    	    String whitespaceTokenizerLine[] = {};
    	    List<String> POSout = new ArrayList<String>();
    	    List<String> myList_Genia = new ArrayList<String>();
    	    List<String> POSout_gold = new ArrayList<String>();
    	    //dealing with POStag output
    		try {
				while ((line1 = lineStream1.read()) != null) {
  	 
					whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
							.tokenize(line1);
					//String[] tags = tagger.tag(whitespaceTokenizerLine);
					//System.out.println(line1);
					//POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
					for(int j=0;j<whitespaceTokenizerLine.length;++j){
						POSout.add(whitespaceTokenizerLine[j].replace('_', '/'));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		//dealing with Genia
    		try {
				while ((line2 =lineStream2.read()) != null){
					if(!line2.matches("====================") && !line2.matches(".===================="))
						myList_Genia.add(line2);
				}
				//System.out.println(myList);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
    			while ((line3 =lineStream3.read()) != null){
    				whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE
							.tokenize(line3);
    				for(int j=0;j<whitespaceTokenizerLine.length;++j){
						POSout_gold.add(whitespaceTokenizerLine[j].replace('_', '/'));
					}
    			}
				
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
    		System.out.println(POSout_gold);
    		//code section for the evauation.
    		
    		List<String> myList_GeniaWord = new ArrayList<String>();
    		List<String> myList_GeniaTag = new ArrayList<String>();
    		for(int k=0;k<myList_Genia.size();++k){
    			String[] spliting = myList_Genia.get(k).split("/");
    			//System.out.print(spliting[0]);
    			//System.out.print(k);
    			//System.out.print("  ");
    			//System.out.println(spliting[1]);
    			myList_GeniaWord.add(spliting[0]);
    			myList_GeniaTag.add(spliting[1]);
    		}
    		
    		List<String> POSoutWord = new ArrayList<String>();
    		List<String> POSoutTag = new ArrayList<String>();
    		for(int k=0;k<POSout.size();++k){
    		
    			String[] spliting = POSout.get(k).split("/");
    			POSoutWord.add(spliting[0]);
    			POSoutTag.add(spliting[1]);
    			
    		}
    		
    		
     		for (int k=0; k<POSout.size();++k){
    			if(myList_Genia.contains(POSout.get(k))){
    				++Tp;
//    				System.out.print("found   ");
//    				System.out.println(POSout.get(k));
    			}
    			else if(myList_GeniaWord.contains(POSoutWord.get(k))){
    				++Fp;
//    				System.out.print("word matched   ");
//    				System.out.println(POSoutWord.get(k));
    			}
    			
    			else{
    				++Fn;
//    				System.out.print("notfound  ");
//    				System.out.println(POSout.get(k));
    			}
    	        }
    		//evaluation eval = new evaluation();
    		//eval.evaluationOverall();
     		
     	      overallAccracy = (float)Tp/(Tp+Fp+Fn);
     	      System.out.print("Overall Accuracy is :");
     	      System.out.println(overallAccracy);
     	      
     	      //calculation to find the per tag accuracy
     	      System.out.println("Calculating per tag accuracy");
     	      
     	     List<String> POSout_goldWord = new ArrayList<String>();
     	     List<String> POSout_goldTag = new ArrayList<String>();
     	     List<String> TagsChecked = new ArrayList<String>(); 
     	     List<Double> F1 = new ArrayList<Double>();
     	     for (int i=0; i<POSout_gold.size(); ++i){
     	    	String[] spliting = POSout_gold.get(i).split("/");
    			POSout_goldWord.add(spliting[0]);
    			POSout_goldTag.add(spliting[1]);
     	     }
     	     
     	     System.out.println("Checking the size of POSout and POSoutgold");
     	     System.out.println(POSout_goldWord.size());
     	    System.out.println(POSoutWord.size());
     	    Map<String, TagAccuracy> map = new HashMap<String, TagAccuracy>();
     	     for (int i=0; i<POSout_gold.size(); ++i){
     	    	 if(!TagsChecked.contains(POSout_goldTag.get(i))){
     	    		System.out.println("created new");
     	    		System.out.println(POSout_goldTag.get(i));
     	    		 map.put(POSout_goldTag.get(i), new TagAccuracy());
     	    		 TagsChecked.add(POSout_goldTag.get(i));
//     	    		map.put(POSout_goldTag.get(i), map.get(POSout_goldTag.get(i)).incrementTp());
     	    		 }
     	    	 
     	    	 if (TagsChecked.contains(POSout_goldTag.get(i))){
     	    		 System.out.println("inside firstcheck");
     	    		 if (POSout_goldWord.get(i).equals(POSoutWord.get(i)))
     	    		 {
     	    			 if (POSout_goldTag.get(i).equals(POSoutTag.get(i))){
     	    				 System.out.println(POSout_goldTag.get(i));
     	    				 map.put(POSout_goldTag.get(i), map.get(POSout_goldTag.get(i)).incrementTp());
     	    				 TagAccuracy n = map.get(POSout_goldTag.get(i));
     	    				System.out.println(n.Tpositive); 
     	    			 }
     	    			 else {
     	    				 System.out.println("inside else");
     	    				 map.put(POSout_goldTag.get(i), map.get(POSout_goldTag.get(i)).incrementFn());
     	    				if (TagsChecked.contains(POSoutTag.get(i)))
         	    				map.put(POSoutTag.get(i), map.get(POSoutTag.get(i)).incrementFp());
         	    				 else{
         	    					map.put(POSoutTag.get(i), new TagAccuracy());
         	    					System.out.println("-------------------------------------------");
         	    					System.out.println(POSoutTag.get(i));
         	    					TagsChecked.add(POSoutTag.get(i));
         	    					map.put(POSoutTag.get(i), map.get(POSoutTag.get(i)).incrementFp());
         	    				 }
     	    			 }
     	    			 }
     	    	 }
     	    	 }
     	     
     	     System.out.println(TagsChecked);
     	    System.out.println(map.get("JJ").Tpositive);
     	    for(int i=0; i<TagsChecked.size(); ++i){
     	    	System.out.println(TagsChecked.get(i));
     	    	F1.add(map.get(TagsChecked.get(i)).f1score());
     	    	}
     	     System.out.println(F1);
     	     
    		}
    }

