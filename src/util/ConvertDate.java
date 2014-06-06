package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConvertDate {
	
    public int getUnixTime (String type, int i){
    	int current, subtracted = 0;
    	long time;
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        if (type.equals("DAY")) {
        	cal.set(Calendar.HOUR_OF_DAY, 0);
        	cal.set(Calendar.SECOND, 0);
        	cal.set(Calendar.MINUTE, 0);
        	subtracted = i * 86400;
		} else if (type.equals("HOUR")) {
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			subtracted = i * 3600;
		} else if (type.equals("MIN")) {
			cal.set(Calendar.SECOND, 0);
			subtracted = i * 60;
		} 
        // SECOND
        time = cal.getTimeInMillis()/1000;
        current = Integer.parseInt(String.valueOf(time));
        current = current - subtracted;
        return current;
    }
    
    public int getUnixTime(String dd, String mm, String yyyy) {
    	int dateInInt = 0;
    	long dateInLong;
    	String strDate;
        Date date = null;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
        	strDate = dd+"/"+mm+"/"+yyyy;
        	date = formatter.parse(strDate);
        	dateInLong = date.getTime()/1000;
            dateInInt = Integer.parseInt(String.valueOf(dateInLong));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return dateInInt;
    }
}

