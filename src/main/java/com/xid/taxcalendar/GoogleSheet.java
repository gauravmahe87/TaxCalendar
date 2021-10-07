package com.xid.taxcalendar;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
import static com.xid.taxcalendar.Constants.*;

public class GoogleSheet {

        private static final String APPLICATION_NAME = "Google Sheets API";
        public static final String SPREADSHEETS = "https://www.googleapis.com/auth/spreadsheets";
        public static HttpCredentialsAdapter authorize() throws IOException {
            FileInputStream fis = new FileInputStream(getGoogleSheetCredential());
            List<String> scopes = Collections.singletonList(SPREADSHEETS);
            final GoogleCredentials googleCredentials = ServiceAccountCredentials
                    .fromStream(fis)
                    .createScoped(scopes);
            return new HttpCredentialsAdapter(googleCredentials);
        }

        public static Sheets sheetBuilder() throws IOException, GeneralSecurityException {
            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), GoogleSheet.authorize())
                    .setApplicationName(APPLICATION_NAME).build();
        }


        public static File getGoogleSheetCredential() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", JSON_CREDENTIAL_TYPE);
            jsonObject.addProperty("project_id", JSON_CREDENTIAL_PROJECT_ID);
            jsonObject.addProperty("private_key_id", JSON_CREDENTIAL_PRIVATE_KEY_ID);
            jsonObject.addProperty("private_key", JSON_CREDENTIAL_PRIVATE_KEY);
            jsonObject.addProperty("client_email", JSON_CREDENTIAL_CLIENT_EMAIL);
            jsonObject.addProperty("client_id", JSON_CREDENTIAL_CLIENT_ID);
            jsonObject.addProperty("auth_uri", JSON_CREDENTIAL_AUTH_URI);
            jsonObject.addProperty("token_uri", JSON_CREDENTIAL_TOKEN_URI);
            jsonObject.addProperty("auth_provider_x509_cert_url", JSON_CREDENTIAL_AUTH_PROVIDER_URL);
            jsonObject.addProperty("client_x509_cert_url", JSON_CREDENTIAL_CLIENT_CERT_URL);
            File file = null;
            try {
                file = new File("/Users/gaurav.maheshwari/Desktop/repos/taxcalendar/src/main/resources/googleSheetCredential.json");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonObject.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }


        public static void updateGoogleSheetTaxCalendar(List<String> listData) throws GeneralSecurityException, IOException, ParseException {
            Sheets service = sheetBuilder();
            List<List<Object>> oValues = new ArrayList<>();
            for (int i=0; i< listData.size(); i++)
            {
                String val3="";
                List<Object> values = new ArrayList<>();
                String val[] = listData.get(i).split("- ,");
                values.add(val[0].trim());
                values.add(val[1].trim());
                StringTokenizer st = new StringTokenizer(val[1]);
                for(int j = 0; j < 10 && st.hasMoreTokens(); j++) {
                    val3 = val3 + " " + st.nextToken();
                }
                values.add(val3.trim());
                oValues.add(values);
            }

            ValueRange valueRange = new ValueRange()
                    .setValues(oValues);
            try {
                service.spreadsheets().values()
                        .update("1WqjfDwPCCv4UnPPxGSZ-pVpClskOY4QaKcLK4aS4oH8", "Sheet1!A2", valueRange)
                        .setValueInputOption("USER_ENTERED")
                        .execute();
            }
            catch (EOFException eofException) {
                eofException.getStackTrace();
            }
        }
}
