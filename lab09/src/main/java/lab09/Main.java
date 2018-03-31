package lab09;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.google.gson.*;


import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main extends Application {


    List<Float> closingPricesGOOG;
    List<Float> closingPricesAAPL;
    GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Lab09: Stock Performance");

        //Collect data
        closingPricesGOOG = new ArrayList<>();
        closingPricesAAPL = new ArrayList<>();


        downloadStockPrices("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=GOOG&apikey=V3TYFURMZMBL59ZN", true);

        downloadStockPrices("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&apikey=V3TYFURMZMBL59ZN", false);


        Group root = new Group();

        Canvas canvas = new Canvas(500, 500);



        gc = canvas.getGraphicsContext2D();


        //Graph begins here
        drawLinePlot();




        root.getChildren().add(canvas);


        //Show graph
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();


    }

    void drawLinePlot()
    {
        gc.setFill(Color.GREY);

        //Vertical
        gc.strokeLine(50.0f, 450.0f, 50.0f, 50);

        //Horizontal
        gc.strokeLine(50.0f, 450, 450.0f, 450);

        //GOOG graph
        gc.setStroke(Color.BLUE);
        plotLine(closingPricesGOOG);

        //AAPL graph
        gc.setStroke(Color.RED);
        plotLine(closingPricesAAPL);

    }

    //Take list and make it into a graph
    void plotLine(List<Float> list)
    {

        //Ordered from 2017-2018
        for (int i = list.size() - 2; i >= 0; i--)
        {


        int pointIndex1 = i + 1;
        int pointIndex2 = i;

        double price1 = 0;
        double price2 = 0;

         //Get point from list
         price1 = list.get(pointIndex1);
         price2 = list.get(pointIndex2);

        double graphX1 = 50.0 + pointIndex1 * 4;
        double graphX2 = 50.0 + pointIndex2 * 4;

        double graphY1 = (price1)/(1200);
        double graphY2 = (price2)/(1200);


        gc.strokeLine(graphX1,  450-(graphY1 * 400), graphX2, 450-(graphY2 * 400));

        }


    }


    void downloadStockPrices(String api, boolean goog)
    {
        URL apiURL = null;
        try {
            apiURL = new URL(api);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String apiOut = " ";
        JsonObject document = null;
        //Can just graph last month

        try {

            URLConnection apiConn = apiURL.openConnection();
            Scanner connScan = new Scanner(apiConn.getInputStream());

            while (connScan.hasNextLine())
            {
                apiOut += connScan.nextLine();

            }
            connScan.close();

        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(apiOut);
        }



        try{

            JsonParser parser = new JsonParser();

            document = parser.parse(apiOut).getAsJsonObject();


        } catch (JsonParseException e)
        {
            e.printStackTrace();
        }


        if (document != null)
        {
            JsonObject series = document.get("Time Series (Daily)").getAsJsonObject();
            Set<String> dates = series.keySet();


            JsonObject metaData = document.get("Meta Data").getAsJsonObject();
            String info = metaData.get("1. Information").getAsString();
            String symbol = metaData.get("2. Symbol").getAsString();

            System.out.println(info + " for " + symbol + "\n\nClosing Prices: ");

            for (String date: dates)
            {
                JsonObject dateData = series.get(date).getAsJsonObject();

                    float closingPrice = dateData.get("4. close").getAsFloat();
                    System.out.printf("\t%s : %.2f \n", date, closingPrice);

                    //Add to list 1 or 2

                //For first month only
                    //if (goog && closingPricesGOOG.size() < 21) closingPricesGOOG.add(closingPrice);
                    //else if (!goog && closingPricesAAPL.size() < 21) closingPricesAAPL.add(closingPrice);

               if (goog) closingPricesGOOG.add(closingPrice);
               else  closingPricesAAPL.add(closingPrice);
            }





        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
