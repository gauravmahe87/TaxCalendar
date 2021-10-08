package com.xid.taxcalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TaxCalendarParser {

    public static void main(String args[]) throws IOException, GeneralSecurityException, ParseException {
        GoogleSheet gSheet = new GoogleSheet();
        // URL to fetch latest data
        Document doc = Jsoup.connect("https://www.incometaxindia.gov.in/Pages/yearly-deadlines-details.aspx?yfmv=2021").get();
        List<String> list = new ArrayList<>();
        Elements dateValues = doc.select("strong");
        for (Element dateValue : dateValues) {
            Elements taxDetails = dateValue.siblingElements().select("p");
            for (Element taxDetail : taxDetails) {
                if (!taxDetail.text().isEmpty())
                    list.add(dateValue.text() + " , " + taxDetail.text());
                }
            }
        // Calling Function to update the Google Sheet with the data
        gSheet.updateGoogleSheetTaxCalendar(list);
    }
}
