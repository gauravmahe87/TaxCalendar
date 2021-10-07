package com.xid.taxcalendar;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaxCalendarParser {

    public static void main(String[] args) throws IOException, GeneralSecurityException, ParseException {

        //JSONObject json = new JSONObject();
        //JSONArray arr = new JSONArray();
        //JSONObject item = new JSONObject();
        GoogleSheet gSheet = new GoogleSheet();
        Document doc = Jsoup.connect("https://www.incometaxindia.gov.in/Pages/yearly-deadlines-details.aspx?yfmv=2021").get();
        List<String> list = new ArrayList<>();
        Elements dateValues = doc.select("strong");
        for (Element dateValue : dateValues) {
            Elements taxDetails = dateValue.siblingElements().select("p");
            for (Element taxDetail : taxDetails) {
                if (!taxDetail.text().isEmpty()) {
                    //item.put("desc", taxDetail.text());
                    //item.put("title", taxDetail.text().substring(0,10));
                    list.add(dateValue.text() + " , " + taxDetail.text());
                }
                }
            //arr.add(item);
            //json.put(dateValue.text(), arr);
            }
        //System.out.println(json.toJSONString());
        gSheet.updateGoogleSheetTaxCalendar(list);
    }
}
