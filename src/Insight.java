/**
 * Insight module.
 * @module Insight
 * @author EternityWall
 * @license LPGL3
 */

import com.oracle.javafx.jmx.json.JSONFactory;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/** Class used to query Insight API */
class Insight {

    String urlBlockindex;
    String urlBlock;


    /**
     * Create a RemoteCalendar.
     */
    Insight(String url) {
        this.urlBlockindex = url + "/block-index";
        this.urlBlock = url + "/block";

        // this.urlBlockindex = 'https://search.bitaccess.co/insight-api/block-index';
        // this.urlBlock = 'https://search.bitaccess.co/insight-api/block';
        // this.urlBlock = "https://insight.bitpay.com/api/block-index/447669";
    }


    /**
     * Retrieve the block hash from the block height.
     * @param {string} height - Height of the block.
     */
    public InsightResponse blockhash(String height) {
        try {

            URL obj = new URL(this.urlBlockindex + '/' + height);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Accept", "application/vnd.opentimestamps.v1");
            con.setRequestProperty("User-Agent", "java-opentimestamps");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Response
            int responseCode = con.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            String jsonString = stringBuilder.toString();

            // Response Hanlder
            JSONObject json = new JSONObject(jsonString);
            String blockHash = json.getString("blockHash");
            InsightResponse insightResponse = new InsightResponse();
            insightResponse.setBlockHash( blockHash );
            return  insightResponse;

        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve the block information from the block hash.
     * @param {string} height - Height of the block.
     */
    public InsightResponse block(String hash) {
        try {

            URL obj = new URL(this.urlBlock + '/' + hash);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("Accept", "application/vnd.opentimestamps.v1");
            con.setRequestProperty("User-Agent", "java-opentimestamps");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Response
            int responseCode = con.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            String jsonString = stringBuilder.toString();

            // Response Hanlder
            JSONObject json = new JSONObject(jsonString);
            String merkleroot = json.getString("merkleroot");
            String time = json.getString("time");
            InsightResponse insightResponse = new InsightResponse();
            insightResponse.setMerkleroot( merkleroot );
            insightResponse.setTime( time );
            return  insightResponse;

        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}