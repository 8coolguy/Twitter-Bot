package main;

import twitter4j.*;
import twitter4j.auth.*;
import java.io.*;
import java.util.Calendar;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Bot {
	
	
	
	private static final String CONSUMERKEY ="";
	
	private static final String CONSUMERKEYS= "";

	private static final String ACESSTOKEN="";

	private static final String ACESSTOKENS="" ;
	
	


	public void Start() throws TwitterException,IOException {
		//twitter set up acess 

		Scanner yNums =new Scanner(new File("/users/arnav/Desktop/coronaYD.txt"));
			
		while(true) {

			final String DATAURL ="https://www.worldometers.info/coronavirus/";
			
			
			
				if(verify24(17,05,00)) {	
					String topScorer="";
					double topScorerdelta=0.0;
					int topScorerTotal=0;
					double worldDelta=0.0;
					int worldTotal=0;
					try {

						
						final Document DATAPAGE = Jsoup.connect(DATAURL).get();
						
						Element chart =DATAPAGE.select("table").get(0);
						Elements rows = chart.select("tr");		
						for(Element x: rows) {
							if(x.text().substring(0,2).equals("1 ")){
								for(int i =0; i<x.childrenSize();i++) {			//Element y: x.children()
									if(i==1)
										topScorer = x.child(i).text();
									if(i==2)
										topScorerTotal =Integer.parseInt((x.child(i).text().replace(",", "")));
									if(i==3)	
										topScorerdelta =Double.parseDouble(x.child(i).text().substring(1).replace(",", ""));
										
								}
							}
							else if(x.text().substring(0,5).equals("World")) {
								for(int i =0; i<x.childrenSize();i++) {			//Element y: x.children()
									if(i==2)
										worldTotal =Integer.parseInt(x.child(i).text().replace(",", ""));
									if(i==3)
										worldDelta =Double.parseDouble(x.child(i).text().substring(1).replace(",", ""));
								}
								
							}
						
						}
					}
					catch(Exception ex){
						ex.printStackTrace();
						System.out.print("error");
					}
					int v =0;
					double worldFactor =0.0;
					double countryFactor=0.0;
					
					while(yNums.hasNextDouble()) {
						if(v==0)
							worldFactor =worldDelta/yNums.nextDouble();
						if(v==1)
							countryFactor =topScorerdelta/yNums.nextDouble();
						v++;
					}
					
					Twitter twitter = new TwitterFactory().getInstance();
			
					twitter.setOAuthConsumer(CONSUMERKEY,CONSUMERKEYS);
				
					RequestToken rToken =twitter.getOAuthRequestToken(); 
			
					AccessToken  aToken= new AccessToken(ACESSTOKEN,ACESSTOKENS);
																												
					twitter.setOAuthAccessToken(aToken);					
					
					twitter.updateStatus("The total infected population is: "+ worldTotal +" and the change in infected today is: "+worldDelta +" with a multiplicative factor of: "+ worldFactor +"\r\n" +"The highest scoring country is: "+topScorer + " with a total of: "+topScorerTotal +", an increase of: "+topScorerdelta + " and a multiplicative factor of: "+countryFactor);
					
					System.setOut(new PrintStream(new File("/users/arnav/Desktop/coronaYD.txt")));
					
					System.out.println(worldDelta +"\r\n"+topScorerdelta); //will get top scorer factor wrong if change in country occurs
					
					
				}
				
		}
		
		
	}
	

	public static void main(String[] args) throws Exception {
		new Bot().Start();
		
		
		
		
		
	}

	public synchronized boolean verify24(int hour,int minute,int second) {
		Calendar time =Calendar.getInstance();
		
		try {
			this.wait(1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		if(time.getTime().getHours()==hour && time.getTime().getMinutes()==minute && time.getTime().getSeconds()==second ) {
			System.setOut(System.out);
			System.out.print(time.getTime().toString());
			return true;
		}
		else {
			return false;
		}
		
	}
}
