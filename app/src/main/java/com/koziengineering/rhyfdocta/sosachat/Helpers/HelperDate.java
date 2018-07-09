package com.koziengineering.rhyfdocta.sosachat.Helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Franvanna on 12/22/2017.
 */

public class HelperDate {

    public static Long getLongDateFromDateStr(String dateStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Long res = -1L;
        String dateStart = dateStr;


        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
            res = date.getTime();
        } catch (ParseException e) {
            Log.e("HELPER_DATE", "EXCEPTION PARSE DATE STR" );
        }

        return res;

    }

    public static String getCurrentDateStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateEnd = dateFormat.format(new Date());

        return dateEnd;
    }

    public static Long getCurrentLondDate(){
        return getLongDateFromDateStr(getCurrentDateStr());
    }

    public static  class DateDiff {
        private long diffDays = 0;
        private long diffHours = 0;
        private long diffMinutes = 0;
        private long diffSeconds = 0;

        public DateDiff(){

        }

        public String toString() {
            String str = "";

            str = str.concat(getDiffDays() + " day(s), "  + getDiffHours() + " hour(s), " + getDiffMinutes() + " min(s), " +
                    getDiffSeconds() + " sec(s).");

            return str;
        }

        public long getDiffHours() {
            return diffHours;
        }
        public void setDiffHours(long diffHours) {
            this.diffHours = diffHours;
        }

        public long getDiffDays() {
            return diffDays;
        }

        public void setDiffDays(long diffDays) {
            this.diffDays = diffDays;
        }

        public long getDiffMinutes() {
            return diffMinutes;
        }

        public void setDiffMinutes(long diffMinutes) {
            this.diffMinutes = diffMinutes;
        }

        public long getDiffSeconds() {
            return diffSeconds;
        }

        public void setDiffSeconds(long diffSeconds) {
            this.diffSeconds = diffSeconds;
        }


        public String toSocialFormat() {


            return "SOCIAL";
        }
    }

    public static DateDiff dateDiff(String dateStart, String dateStop) {

		/*String dateStart = "01/14/2012 09:29:58";
		String dateStop = "01/15/2012 10:31:48";*/

        //HH converts hour in 24 hours format (0-23), day calculation
        DateDiff dateo = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            dateo = new DateDiff();

            dateo.setDiffSeconds(diffSeconds);
            dateo.setDiffMinutes(diffMinutes);
            dateo.setDiffHours(diffHours);
            dateo.setDiffDays(diffDays);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DATE_DIFF", "dateDiff: Failed to parce dates -> " + e.getLocalizedMessage() );
        }

        return dateo;

    }

    public static class DateComponentsFromDateStr {

        private Date date = new Date();

        //pi


        // FORMAT YYYY-mm-dd HH:ii:SS
        public DateComponentsFromDateStr(String dateStr){

            String[] dateTime = dateStr.split(" ");

            String datec = dateTime[0];
            String timec = dateTime[1];

            String[] dateComps = datec.split("-");
            String[] timeComps = timec.split(":");

            long year = LVO(dateComps[0]);
            long month = LVO(dateComps[1]);
            long day = LVO(dateComps[2]);
            
            long hour = LVO(timeComps[0]);
            long min = LVO(timeComps[1]);
            long sec = LVO(timeComps[2]);





        }

        public Long LVO(String val){
            return Long.valueOf(val);
        }

    }

    public static DateComponentsFromDateStr getDateComponenst(String dateStr){

        return new DateComponentsFromDateStr(dateStr);
    }
}
