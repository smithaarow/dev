/**
 * Created by aaronsmith on 6/16/15.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;




public class EBayAuctionScaper {

    private String inputFile; // input File

    private String auctionTitle; // auction Title
    private String itemCondition; //item condition
    private String timeLeft;
    private String currentBid;
    private String sellerId;
    private int sellerFeedbackCount;
    private String shippingCost;

    /**
     * It scrapes the required informations from an ebay auction page
     *
     */
    public void parseEbayAuctionPage(EBayAuctionScaper s) {
        BufferedReader in = null;
        try {
            Pattern pattern;
            Matcher matcher;

            // open input file
            in = new BufferedReader(new FileReader(s.inputFile));

            String line = "";

            // boolean variables to check if the particular information is already found
            boolean titlefound = false;
            boolean condFound = false;
            boolean bidEndDateFound = false;
            boolean biddingAmtFound = false;
            boolean shippingCostFound = false;
            boolean sellerIdFound = false;
            boolean sellerFeedbackCountFound = false;

            // Read the file line by line
            while ((line = in.readLine()) != null) {

                // auction title
                // If action title is not found, then check the line to find match
                if (!titlefound) {
                    pattern = Pattern.compile("<h1.*id=\"itemTitle\">");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) { // if the matcher finds the pattern

                        // Exa: - <h1 id="bla bla " class="bla bla bla"><span bla bla bla></span>Title</h1>
                        String[] tokens = line.split("</span>"); // splits the line with delimiter "</span>"
                        s.auctionTitle = tokens[1].substring(0, tokens[1].indexOf("</h1>"));

                        // switch the flag to true once it is found
                        titlefound = true;
                    }
                }


                // <div class="u-flL condText  "  id="vi-itm-cond">Used</div>
                // condition
                if (!condFound) {
                    pattern = Pattern.compile("<div.*id=\"vi-itm-cond\">");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        s.itemCondition = line.substring(matcher.end(), line.indexOf("</div>"));
                        condFound = true;
                    }
                }

                // <span id="bb_tlft">
                //		Apr 09, 2014<span class="endedDate">
                // <span>16:52:55 PDT</span></span>
                //</span>
                if (!bidEndDateFound) {
                    pattern = Pattern.compile("<span\\sid=\"bb_tlft\">");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        line = in.readLine(); // reads the next line from file
                        s.timeLeft = line.substring(0, line.indexOf("<span")).trim(); //trim() trims spaces before and after string
                        line = in.readLine().trim();
                        s.timeLeft = s.timeLeft + " " + line.substring(6, line.indexOf("</span"));
                        bidEndDateFound = true;
                    }
                }


                //<span id="" class="notranslate vi-VR-cvipPrice">US $269.00</span>
                if (!biddingAmtFound) {
                    pattern = Pattern.compile("<span.*US\\s[$]\\d+[.]\\d\\d</span>");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        s.currentBid = line.substring(line.indexOf("US"), line.indexOf("</span>"));
                        biddingAmtFound = true;
                    }
                }


                //<span id="fshippingCost" class="notranslate sh-cst ">
                //<span>$16.25</span>
                if (!shippingCostFound) {
                    pattern = Pattern.compile("<span\\sid=\"fshippingCost\".*");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        line = in.readLine().trim(); // read next line from file
                        pattern = Pattern.compile("<span>[$]\\d+[.]\\d\\d</span>"); // pattern is for amount
                        matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            s.shippingCost = line.substring(6, line.indexOf("</span>"));
                            shippingCostFound = true;
                        }
                    }
                }


                // <a href="http://www.ebay.com/usr/papawn652?_trksid=p2047675.l2559" title="Member ID:&nbsp;papawn652"> <span class="mbg-nw">papawn652</span></a>
                if (!sellerIdFound) {
                    pattern = Pattern.compile("<a\\shref.*title=\"Member\\sID.*");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String[] tokens = line.split("</span>");
                        s.sellerId = tokens[0].substring(tokens[0].lastIndexOf(">") + 1);
                        sellerIdFound = true;
                    }
                }


                //(<a href="http://feedback.ebay.com/ws/eBayISAPI.dll?ViewFeedback&userid=papawn652&iid=281298650161&ssPageName=VIP:feedback&ftab=FeedbackAsSeller&rt=nc&_trksid=p2047675.l2560" title="feedback score: 78">78</a>
                if (!sellerFeedbackCountFound) {
                    pattern = Pattern.compile("<a\\shref.*title=\"feedback\\sscore:.*");
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String[] tokens = line.split("</a>");

                        s.sellerFeedbackCount = Integer.parseInt(tokens[0].substring(tokens[0].lastIndexOf(">") + 1));

                        sellerFeedbackCountFound = true;
                    }
                }


                if (titlefound && condFound && bidEndDateFound && biddingAmtFound
                        && shippingCostFound && sellerIdFound && sellerFeedbackCountFound) {
                    break; // come out of while loop
                }

            }

            // close input file
            in.close();
        } catch (IOException io) {
            System.out.println("Error occured while opening input file");
        }
    } // parsePages method

    public static void main(String args[]) {

        EBayAuctionScaper temp = new EBayAuctionScaper();
        // Assigning the file name to instance variable inputFile
        temp.inputFile = "ebayauctionpage.txt";

        temp.parseEbayAuctionPage(temp);


        System.out.println("Action Title:" + temp.auctionTitle + "\tItem Condition:" + temp.itemCondition
                + "\tBidding End Date:" + temp.timeLeft + "\tCurrent Bid:" + temp.currentBid + "\tSeller Id:"
                + temp.sellerId + "\tSeller Feedback Count:" + temp.sellerFeedbackCount + "\tShipping Cost:" + temp.shippingCost);

        System.out.println(temp.auctionTitle + "\\t" + temp.itemCondition
                + "\\t" + temp.timeLeft + "\\t:" + temp.currentBid + "\\t"
                + temp.sellerId + "\\t" + temp.sellerFeedbackCount + "\\t" + temp.shippingCost);
    }

}