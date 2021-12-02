package com.a2mee;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.a2mee.util.GlobalUtils;

public class Test {

	public static void main(String[] args) {
		
		final Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		 java.util.Date d= cal.getTime();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(d);
		LocalDate localDate = LocalDate.parse(date);
		System.out.println("current date=="+LocalDate.now());
		System.out.println("requrioed  date=="+localDate);
		System.out.println(date+"==="+LocalDate.now()+"@@"+localDate);
	}
}
