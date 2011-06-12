package ivory.regression;

import ivory.cascade.retrieval.CascadeBatchQueryRunner;
import ivory.eval.GradedQrels;
import ivory.regression.GroundTruth.Metric;
import ivory.smrf.retrieval.Accumulator;

import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.junit.Test;

import edu.umd.cloud9.collection.DocnoMapping;

public class Gov2_SIGIR2011_varying_tradeoff_cascade {

	private static final Logger sLogger = Logger.getLogger(Gov2_SIGIR2011_varying_tradeoff_cascade.class);

	private static String[] p1 = new String[] {
	 "776", "0.2009",  "777", "0.1204",  "778", "0.4935",  "779", "0.818",  "780", "0.426", 
	 "781", "0.4328",  "782", "0.4858",  "783", "0.5473",  "784", "0.7395",  "785", "0.7571", 
	 "786", "0.2876",  "787", "0.826",  "788", "0.7559",  "789", "0.3708",  "790", "0.779", 
	 "791", "0.6353",  "792", "0.2766",  "793", "0.536",  "794", "0.4965",  "795", "0.0", 
	 "796", "0.3649",  "797", "0.2748",  "798", "0.3107",  "799", "0.4342",  "800", "0.2004", 
	 "801", "0.5646",  "802", "0.9636",  "803", "0.0",  "804", "0.4886",  "805", "0.2663", 
	 "806", "0.4602",  "807", "0.9347",  "808", "0.9972",  "809", "0.2702",  "810", "0.4071", 
	 "811", "0.5542",  "812", "0.7972",  "813", "0.7544",  "814", "0.7059",  "815", "0.1729", 
	 "816", "0.6187",  "817", "0.7951",  "818", "0.4648",  "819", "0.9238",  "820", "0.7309", 
	 "821", "0.2754",  "822", "0.1471",  "823", "0.6242",  "824", "0.3296",  "825", "0.2644", 
	 "826", "0.4148",  "827", "0.6896",  "828", "0.5188",  "829", "0.3364",  "830", "0.3133", 
	 "831", "0.7694",  "832", "0.8041",  "833", "0.3527",  "834", "0.4122",  "835", "0.1793", 
	 "836", "0.248",  "837", "0.0243",  "838", "0.6906",  "839", "0.6329",  "840", "0.1744", 
	 "841", "0.6478",  "842", "0.1061",  "843", "0.6843",  "844", "0.158",  "845", "0.2703", 
	 "846", "0.5815",  "847", "0.2706",  "848", "0.1836",  "849", "0.6963",  "850", "0.3452"};


	private static String[] p3 = new String[] {
	 "776", "0.238",  "777", "0.1011",  "778", "0.4935",  "779", "0.7951",  "780", "0.426", 
	 "781", "0.2812",  "782", "0.4473",  "783", "0.5553",  "784", "0.7395",  "785", "0.7345", 
	 "786", "0.2876",  "787", "0.826",  "788", "0.7559",  "789", "0.338",  "790", "0.7946", 
	 "791", "0.6365",  "792", "0.2533",  "793", "0.536",  "794", "0.4965",  "795", "0.0", 
	 "796", "0.4226",  "797", "0.2656",  "798", "0.3045",  "799", "0.4554",  "800", "0.2314", 
	 "801", "0.5626",  "802", "0.9659",  "803", "0.0",  "804", "0.4886",  "805", "0.1866", 
	 "806", "0.4602",  "807", "0.9372",  "808", "0.9972",  "809", "0.2789",  "810", "0.4071", 
	 "811", "0.5542",  "812", "0.7958",  "813", "0.7534",  "814", "0.7059",  "815", "0.1756", 
	 "816", "0.6021",  "817", "0.7642",  "818", "0.4637",  "819", "0.9238",  "820", "0.7309", 
	 "821", "0.3132",  "822", "0.149",  "823", "0.6242",  "824", "0.3296",  "825", "0.27", 
	 "826", "0.4598",  "827", "0.6954",  "828", "0.5188",  "829", "0.1558",  "830", "0.3133", 
	 "831", "0.6312",  "832", "0.7216",  "833", "0.3527",  "834", "0.4305",  "835", "0.0669", 
	 "836", "0.2454",  "837", "0.0243",  "838", "0.7314",  "839", "0.6319",  "840", "0.1744", 
	 "841", "0.6358",  "842", "0.1061",  "843", "0.6843",  "844", "0.158",  "845", "0.2929", 
	 "846", "0.5815",  "847", "0.2605",  "848", "0.1603",  "849", "0.6966",  "850", "0.3118"};

	private static String [] p5 = new String [] {
	 "776", "0.2503",  "777", "0.1589",  "778", "0.4602",  "779", "0.8181",  "780", "0.4249", 
	 "781", "0.384",  "782", "0.4619",  "783", "0.5754",  "784", "0.7395",  "785", "0.7516", 
	 "786", "0.3339",  "787", "0.847",  "788", "0.7236",  "789", "0.338",  "790", "0.7994", 
	 "791", "0.6559",  "792", "0.2231",  "793", "0.457",  "794", "0.4179",  "795", "0.0", 
	 "796", "0.3856",  "797", "0.334",  "798", "0.3049",  "799", "0.348",  "800", "0.1559", 
	 "801", "0.5642",  "802", "0.9324",  "803", "0.0",  "804", "0.4617",  "805", "0.1989", 
	 "806", "0.3374",  "807", "0.9411",  "808", "0.9972",  "809", "0.279",  "810", "0.4", 
	 "811", "0.5518",  "812", "0.7993",  "813", "0.7566",  "814", "0.6493",  "815", "0.1682", 
	 "816", "0.6878",  "817", "0.7785",  "818", "0.4526",  "819", "0.925",  "820", "0.7247", 
	 "821", "0.2712",  "822", "0.1503",  "823", "0.6026",  "824", "0.3296",  "825", "0.1307", 
	 "826", "0.4863",  "827", "0.6855",  "828", "0.5161",  "829", "0.255",  "830", "0.2912", 
	 "831", "0.7034",  "832", "0.7861",  "833", "0.3955",  "834", "0.4657",  "835", "0.0658", 
	 "836", "0.2862",  "837", "0.0511",  "838", "0.6983",  "839", "0.5732",  "840", "0.1744", 
	 "841", "0.6011",  "842", "0.0866",  "843", "0.6829",  "844", "0.1294",  "845", "0.3058", 
	 "846", "0.5715",  "847", "0.3014",  "848", "0.1783",  "849", "0.6779",  "850", "0.3484"};

	private static String [] p7 = new String[] {
	 "776", "0.2503",  "777", "0.1589",  "778", "0.4434",  "779", "0.8181",  "780", "0.4249", 
	 "781", "0.384",  "782", "0.4619",  "783", "0.5754",  "784", "0.7395",  "785", "0.7516", 
	 "786", "0.3339",  "787", "0.847",  "788", "0.7236",  "789", "0.338",  "790", "0.7994", 
	 "791", "0.6559",  "792", "0.2231",  "793", "0.457",  "794", "0.4179",  "795", "0.0", 
	 "796", "0.3856",  "797", "0.334",  "798", "0.3049",  "799", "0.348",  "800", "0.1559", 
	 "801", "0.5642",  "802", "0.9324",  "803", "0.0",  "804", "0.4617",  "805", "0.1989", 
	 "806", "0.2865",  "807", "0.9411",  "808", "0.9972",  "809", "0.279",  "810", "0.4", 
	 "811", "0.5518",  "812", "0.7993",  "813", "0.7566",  "814", "0.6493",  "815", "0.1682", 
	 "816", "0.6878",  "817", "0.7785",  "818", "0.4526",  "819", "0.925",  "820", "0.7247", 
	 "821", "0.2712",  "822", "0.1503",  "823", "0.6026",  "824", "0.3296",  "825", "0.1307", 
	 "826", "0.4863",  "827", "0.6855",  "828", "0.5161",  "829", "0.255",  "830", "0.2912", 
	 "831", "0.7034",  "832", "0.7861",  "833", "0.3955",  "834", "0.4657",  "835", "0.0658", 
	 "836", "0.2862",  "837", "0.0511",  "838", "0.6983",  "839", "0.5732",  "840", "0.1744", 
	 "841", "0.6011",  "842", "0.0866",  "843", "0.6829",  "844", "0.1294",  "845", "0.3058", 
	 "846", "0.5715",  "847", "0.3014",  "848", "0.1783",  "849", "0.6779",  "850", "0.3484"};

        private static String [] p9 = new String[] {
	 "776", "0.2297",  "777", "0.2168",  "778", "0.4434",  "779", "0.8206",  "780", "0.4392", 
	 "781", "0.4516",  "782", "0.4546",  "783", "0.4763",  "784", "0.7317",  "785", "0.6622", 
	 "786", "0.4232",  "787", "0.7718",  "788", "0.7537",  "789", "0.3516",  "790", "0.8069", 
	 "791", "0.6621",  "792", "0.2104",  "793", "0.3758",  "794", "0.1348",  "795", "0.0", 
	 "796", "0.3863",  "797", "0.4411",  "798", "0.2733",  "799", "0.1685",  "800", "0.1414", 
	 "801", "0.5912",  "802", "0.7735",  "803", "0.0",  "804", "0.3919",  "805", "0.0844", 
	 "806", "0.113",  "807", "0.8668",  "808", "0.999",  "809", "0.2584",  "810", "0.3086", 
	 "811", "0.4901",  "812", "0.809",  "813", "0.8941",  "814", "0.7003",  "815", "0.1236", 
	 "816", "0.6839",  "817", "0.8519",  "818", "0.3939",  "819", "0.9821",  "820", "0.7109", 
	 "821", "0.1757",  "822", "0.2279",  "823", "0.6743",  "824", "0.3156",  "825", "0.1065", 
	 "826", "0.4355",  "827", "0.6512",  "828", "0.4171",  "829", "0.2689",  "830", "0.1837", 
	 "831", "0.8051",  "832", "0.5456",  "833", "0.6858",  "834", "0.5333",  "835", "0.0675", 
	 "836", "0.1825",  "837", "0.0306",  "838", "0.6537",  "839", "0.5113",  "840", "0.1744", 
	 "841", "0.55",  "842", "0.1059",  "843", "0.6511",  "844", "0.0619",  "845", "0.3234", 
	 "846", "0.5644",  "847", "0.4759",  "848", "0.2927",  "849", "0.5632",  "850", "0.3406"};



	@Test
	public void runRegression() throws Exception {
		Map<String, GroundTruth> g = new HashMap<String, GroundTruth>();

		g.put("gov2-cascade-0.9", new GroundTruth("gov2-cascade-0.9", Metric.NDCG20, 75, p9, 0.4457f));

		g.put("gov2-cascade-0.7", new GroundTruth("gov2-cascade-0.7", Metric.NDCG20, 75, p7, 0.4625f));

		g.put("gov2-cascade-0.5", new GroundTruth("gov2-cascade-0.5", Metric.NDCG20, 75, p5, 0.4634f));

		g.put("gov2-cascade-0.3", new GroundTruth("gov2-cascade-0.3", Metric.NDCG20, 75, p3, 0.4653f));

		g.put("gov2-cascade-0.1", new GroundTruth("gov2-cascade-0.1", Metric.NDCG20, 75, p1, 0.4745f));

		GradedQrels qrels = new GradedQrels("data/gov2/qrels.gov2.all");

    String[] params = new String[] {
            "data/gov2/run.gov2.SIGIR2011.varying.tradeoff.cascade.xml",
            "data/gov2/gov2.title.776-850" };

		FileSystem fs = FileSystem.getLocal(new Configuration());

		CascadeBatchQueryRunner qr = new CascadeBatchQueryRunner(params, fs);

		long start = System.currentTimeMillis();
		qr.runQueries();
		long end = System.currentTimeMillis();

		sLogger.info("Total query time: " + (end - start) + "ms");

		DocnoMapping mapping = qr.getDocnoMapping();

		for (String model : qr.getModels()) {
			sLogger.info("Verifying results of model \"" + model + "\"");

			Map<String, Accumulator[]> results = qr.getResults(model);
			g.get(model).verify(results, mapping, qrels);

			sLogger.info("Done!");
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(Gov2_SIGIR2011_varying_tradeoff_cascade.class);
	}
}
