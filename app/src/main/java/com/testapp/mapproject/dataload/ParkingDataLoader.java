package com.testapp.mapproject.dataload;

import android.content.Context;

import com.testapp.mapproject.model.Parking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.testapp.mapproject.R.raw.businesses;

public class ParkingDataLoader {

    private static final int DATA_SET_SMALL_NUM = 500;

    public final void loadParkingSmallDataSet(Context context) {
        loadParkingData(context, DATA_SET_SMALL_NUM);
    }

    public final List<Parking> loadParkingData(Context context, int limit) {
        List<Parking> spaces = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(businesses);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null && (limit == -1 || lineNumber < limit)) {
                if (lineNumber++ == 0) {
                    continue;
                }

                String[] rowData = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (rowData[6].isEmpty()) {
                    continue;
                }

                spaces.add(new Parking(
                        Integer.parseInt(rowData[0]), // id
                        removeQuotes(rowData[1]), // name
                        removeQuotes(rowData[2]), // address
                        removeQuotes(rowData[3]), // city
                        removeQuotes(rowData[4]), // state
                        removeQuotes(rowData[5]), // postal code
                        Double.parseDouble(removeQuotes(rowData[6])), // latitude
                        Double.parseDouble(removeQuotes(rowData[7]))));
            }
        }
        catch (IOException ex) {}
        finally {
            try {
                is.close();
            }
            catch (IOException e) {}
        }

        return spaces;
    }

    private String removeQuotes(String original) {
        return original.subSequence(1, original.length() - 1).toString();
    }
}
