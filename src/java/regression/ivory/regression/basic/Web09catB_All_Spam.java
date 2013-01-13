/*
 * Ivory: A Hadoop toolkit for web-scale information retrieval research
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ivory.regression.basic;

import ivory.core.eval.Qrels;
import ivory.regression.GroundTruth;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;
import ivory.smrf.retrieval.BatchQueryRunner;

import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.collect.Maps;

import edu.umd.cloud9.collection.DocnoMapping;

public class Web09catB_All_Spam {
  private static final Logger LOG = Logger.getLogger(Web09catB_All_Spam.class);

  private static String[] ql_base_rawAP = new String[] { "1", "0.3334", "10", "0.0491", "11", "0.4499", "12", "0.0996", "13", "0.0116", "14", "0.0887", "15", "0.3738", "16", "0.3162", "17", "0.1909", "18", "0.2925", "19", "0.0000", "2", "0.5089", "20", "0.0000", "21", "0.3149", "22", "0.4835", "23", "0.0604", "24", "0.1096", "25", "0.2683", "26", "0.1997", "27", "0.2857", "28", "0.3738", "29", "0.0529", "3", "0.0010", "30", "0.2192", "31", "0.4650", "32", "0.0780", "33", "0.4659", "34", "0.0175", "35", "0.4140", "36", "0.0941", "37", "0.0572", "38", "0.0978", "39", "0.1298", "4", "0.0328", "40", "0.2212", "41", "0.1492", "42", "0.0227", "43", "0.5941", "44", "0.0636", "45", "0.3066", "46", "0.7008", "47", "0.5660", "48", "0.1974", "49", "0.3588", "5", "0.0360", "50", "0.0916", "6", "0.0963", "7", "0.0936", "8", "0.0408", "9", "0.1933" };

  private static String[] ql_sd_rawAP = new String[] { "1", "0.3571", "10", "0.0680", "11", "0.4590", "12", "0.0965", "13", "0.0128", "14", "0.0937", "15", "0.4019", "16", "0.3258", "17", "0.2058", "18", "0.3573", "19", "0.0000", "2", "0.5294", "20", "0.0000", "21", "0.2621", "22", "0.4831", "23", "0.0621", "24", "0.1046", "25", "0.2647", "26", "0.2313", "27", "0.2861", "28", "0.3745", "29", "0.0760", "3", "0.0012", "30", "0.2461", "31", "0.4551", "32", "0.0733", "33", "0.4601", "34", "0.0163", "35", "0.4022", "36", "0.0900", "37", "0.0537", "38", "0.1004", "39", "0.1589", "4", "0.0281", "40", "0.2296", "41", "0.2135", "42", "0.0660", "43", "0.6084", "44", "0.0679", "45", "0.3211", "46", "0.6946", "47", "0.5686", "48", "0.2154", "49", "0.3682", "5", "0.0930", "50", "0.0843", "6", "0.0941", "7", "0.0959", "8", "0.0414", "9", "0.2166" };

  private static String[] ql_wsd_rawAP = new String[] { "1", "0.5445", "10", "0.1070", "11", "0.4426", "12", "0.1361", "13", "0.0170", "14", "0.0807", "15", "0.4093", "16", "0.2995", "17", "0.2085", "18", "0.3566", "19", "0.0000", "2", "0.5747", "20", "0.0000", "21", "0.4016", "22", "0.4816", "23", "0.0387", "24", "0.1281", "25", "0.2721", "26", "0.1846", "27", "0.2595", "28", "0.3613", "29", "0.0454", "3", "0.0007", "30", "0.2419", "31", "0.4837", "32", "0.0713", "33", "0.4574", "34", "0.0143", "35", "0.4151", "36", "0.0997", "37", "0.0549", "38", "0.0989", "39", "0.1721", "4", "0.0372", "40", "0.2121", "41", "0.3063", "42", "0.0942", "43", "0.5996", "44", "0.0723", "45", "0.3217", "46", "0.6803", "47", "0.4590", "48", "0.2171", "49", "0.3137", "5", "0.1582", "50", "0.0664", "6", "0.0997", "7", "0.0838", "8", "0.0324", "9", "0.2041" };

  private static String[] bm25_base_rawAP = new String[] { "1", "0.4997", "10", "0.0143", "11", "0.4289", "12", "0.1047", "13", "0.0004", "14", "0.1024", "15", "0.3747", "16", "0.3486", "17", "0.1982", "18", "0.1652", "19", "0.0000", "2", "0.5138", "20", "0.0000", "21", "0.3463", "22", "0.4841", "23", "0.0569", "24", "0.1285", "25", "0.2724", "26", "0.0446", "27", "0.2630", "28", "0.3741", "29", "0.0109", "3", "0.0014", "30", "0.2314", "31", "0.4916", "32", "0.0767", "33", "0.4944", "34", "0.0065", "35", "0.4419", "36", "0.0954", "37", "0.0711", "38", "0.1107", "39", "0.1552", "4", "0.0305", "40", "0.1847", "41", "0.2540", "42", "0.0268", "43", "0.6769", "44", "0.0593", "45", "0.3352", "46", "0.7187", "47", "0.5514", "48", "0.1956", "49", "0.3041", "5", "0.1416", "50", "0.0817", "6", "0.1191", "7", "0.0517", "8", "0.0511", "9", "0.1440" };

  private static String[] bm25_sd_rawAP = new String[] { "1", "0.5850", "10", "0.1853", "11", "0.4105", "12", "0.0994", "13", "0.0003", "14", "0.1090", "15", "0.3311", "16", "0.3316", "17", "0.2156", "18", "0.2750", "19", "0.0000", "2", "0.5010", "20", "0.0000", "21", "0.3145", "22", "0.4803", "23", "0.0512", "24", "0.1253", "25", "0.2724", "26", "0.1005", "27", "0.2700", "28", "0.3809", "29", "0.0320", "3", "0.0014", "30", "0.2686", "31", "0.4783", "32", "0.0300", "33", "0.4716", "34", "0.0059", "35", "0.4403", "36", "0.0959", "37", "0.0628", "38", "0.1135", "39", "0.1681", "4", "0.0275", "40", "0.1848", "41", "0.3560", "42", "0.1056", "43", "0.6966", "44", "0.0746", "45", "0.3430", "46", "0.7211", "47", "0.5804", "48", "0.1192", "49", "0.2854", "5", "0.2186", "50", "0.0533", "6", "0.1160", "7", "0.0660", "8", "0.0527", "9", "0.1897" };

  private static String[] bm25_wsd_rawAP = new String[] { "1", "0.5352", "10", "0.1917", "11", "0.4116", "12", "0.0966", "13", "0.0002", "14", "0.1139", "15", "0.3569", "16", "0.3284", "17", "0.2603", "18", "0.2706", "19", "0.0000", "2", "0.5062", "20", "0.0000", "21", "0.2752", "22", "0.4790", "23", "0.0538", "24", "0.1191", "25", "0.2733", "26", "0.0955", "27", "0.2817", "28", "0.3874", "29", "0.0274", "3", "0.0014", "30", "0.2503", "31", "0.4732", "32", "0.0420", "33", "0.4752", "34", "0.0020", "35", "0.4381", "36", "0.0938", "37", "0.0711", "38", "0.1018", "39", "0.1715", "4", "0.0258", "40", "0.1854", "41", "0.3584", "42", "0.1228", "43", "0.6643", "44", "0.0628", "45", "0.3177", "46", "0.7270", "47", "0.6123", "48", "0.1982", "49", "0.3494", "5", "0.2062", "50", "0.0711", "6", "0.1155", "7", "0.0242", "8", "0.0553", "9", "0.1714" };

  @Test
  public void runRegression() throws Exception {
    Map<String, GroundTruth> g = Maps.newHashMap();
    g.put("ql-base", new GroundTruth("ql-base", Metric.AP, 50, ql_base_rawAP, 0.2134f));
    g.put("ql-sd", new GroundTruth("ql-sd", Metric.AP, 50, ql_sd_rawAP, 0.2223f));
    g.put("ql-wsd", new GroundTruth("ql-wsd", Metric.AP, 50, ql_wsd_rawAP, 0.2283f));
    g.put("bm25-base", new GroundTruth("bm25-base", Metric.AP, 50, bm25_base_rawAP, 0.2167f));
    g.put("bm25-sd", new GroundTruth("bm25-sd", Metric.AP, 50, bm25_sd_rawAP, 0.2280f));
    g.put("bm25-wsd", new GroundTruth("bm25-wsd", Metric.AP, 50, bm25_wsd_rawAP, 0.2290f));

    Qrels qrels = new Qrels("data/clue/qrels.web09catB.txt");

    String[] params = new String[] {
        "data/clue/run.web09catB.all.spam.xml",
        "data/clue/queries.web09.xml" };

    FileSystem fs = FileSystem.getLocal(new Configuration());

    BatchQueryRunner qr = new BatchQueryRunner(params, fs);

    long start = System.currentTimeMillis();
    qr.runQueries();
    long end = System.currentTimeMillis();

    LOG.info("Total query time: " + (end - start) + "ms");

    DocnoMapping mapping = qr.getDocnoMapping();

    for (String model : qr.getModels()) {
      LOG.info("Verifying results of model \"" + model + "\"");
      Map<String, Accumulator[]> results = qr.getResults(model);
      g.get(model).verify(results, mapping, qrels);
      LOG.info("Done!");
    }
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(Web09catB_All_Spam.class);
  }
}
