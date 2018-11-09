package com.vims.generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CancelIdGenerator implements IdentifierGenerator{

	public static int i=1;
	
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		
		
	
			
			int j;FileReader fr=null;FileWriter fw=null;
			BufferedReader br=null;
			BufferedWriter bw=null;
			File f=new File("src/cancelid.txt");
			try {
				
				 fr=new FileReader(f);
				 br=new BufferedReader(fr);
				 i=br.read();
				if(i==-1){
					i=1;
				}else{
					i++;
				}
			} catch (IOException e1) {
				System.out.println("file reader exception");
			}
			
			Date d1=new Date();
			Calendar cal=Calendar.getInstance();
			cal.setTime(d1);
			SimpleDateFormat df=new SimpleDateFormat("YYYY-MM-DD");
			String s=df.format(d1);
			//System.out.println(s);
			String id1=Integer.toString(i);
			String split[]=s.split("-");
			StringBuffer sb=new StringBuffer();
			sb.append(split[0]);
			sb.append(split[1]);
			sb.append(split[2]);
			if(id1.length()==1){
				sb.append("00");
				sb.append(id1);
			}else if(id1.length()==2){
				sb.append("0");
				sb.append(id1);
			}else if(id1.length()==3){
				sb.append(id1);
			}
			try {
				 fw=new FileWriter(f);
				 bw=new BufferedWriter(fw);
				 bw.write(i);
			} catch (IOException e) {
				System.out.println("file writer excpetion");
			}
			finally{try {
				
					//if(i!=1){
						
						br.close();bw.close();
					//}
					
				} catch (IOException e) {
					System.out.println("finally exception");
				}}
			
			return(sb.toString());
	}

}
