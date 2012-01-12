package vorobiova.yuliya;


public class RSSFeed extends SentRequest{
		static String url = "http://tracks.osll.spb.ru:81/service/rss";
		public RSSFeed(String req1) {
			super(req1,url);
		}
	
	}

