package com.fitness.repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fitness.model.Steps;
import com.fitness.model.StepsInfo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * DOCUMENT ME!
 *
 * @author  JDraper
 */
public class StepsRepository {
    /**
     * @param   monthYear
     * @param   token
     * @return
     */
    public static List<Steps> findStepsPerMonth(String monthYear, String token) {
        try {
            return sendGet(monthYear, token);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * HTTP POST request
     *
     * @param   date   monthYear
     * @param   steps  token
     * @param   token
     * @throws  Exception
     */
    public static void sendPost(String date, String steps, String token) throws Exception {
        String url = "https://api.fitbit.com/1/user/-/activities.json";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        // add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", token);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // this is for running and it works but need it to work for walking steps.
        String urlParameters = "activityId=90013" +
            "&startTime=12%3A20&durationMillis=600000&date=" + date + "&distance=" + steps + "&distanceUnit=steps";

        // Send post request
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        // print result
        System.out.println(response.toString());
    }

    /**
     * @param  monthYear
     * @param  token
     * @param  monthlySteps
     */
    public static void submitMonthlySteps(String monthYear, String token, List<Steps> monthlySteps) {
        String[] date = monthYear.split("&");
        String month = date[0];
        String year = date[1];
        int iYear = Integer.parseInt(month);
        int iMonth = Integer.parseInt(year);
        int iDay = 1;

        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

        // Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int x = 1; x <= daysInMonth; x++) {
            String day = (x < 10) ? ("0" + x) : ("" + x);
            String stepsDate = year + "-" + month + "-" + day;

            try {
                sendPost(stepsDate, monthlySteps.get(x - 1).getAmount(), token);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * HTTP GET request
     *
     * @param   monthYear
     * @param   token
     * @return
     * @throws  Exception
     */
    private static List<Steps> sendGet(String monthYear, String token) throws Exception {
        String[] date = monthYear.split(",");
        String month = date[0];
        String year = date[1];
        int iYear = Integer.parseInt(year);
        int iMonth = Integer.parseInt(month);
        int iDay = 1;

        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

        // Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String url = "https://api.fitbit.com/1/user/-/activities/steps/date/" + year + "-" + month + "-01/" + year + "-" + month + "-" +
            daysInMonth + ".json";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // add request headers
        token =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzN1RHRkgiLCJhdWQiOiIyMjhUTVAiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJ3aHIgd251dCB3cHJvIHdzbGUgd3dlaSB3c29jIHdzZXQgd2FjdCB3bG9jIiwiZXhwIjoxNDgzMTY3MDQxLCJpYXQiOjE0ODMxMzgyNDF9.8GJ7AcmYmt85GpBd20Icwqc8jT3QODjIcOFuSJYZd8s";
        con.setRequestProperty("Authorization", token);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        // print result
        String json = response.toString();
        json = json.substring(20, json.length() - 1);

        ObjectMapper mapper = new ObjectMapper();
        StepsInfo[] monthStepsInfo = mapper.readValue(json, StepsInfo[].class);
        List<Steps> monthSteps = new ArrayList<Steps>();

        for (StepsInfo info : monthStepsInfo) {
            Steps steps = new Steps();
            steps.setAmount(info.getValue());
            monthSteps.add(steps);
        }

        System.out.println(response.toString());
        monthStepsInfo.toString();
        return monthSteps;
    }
}
