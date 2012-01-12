package vorobiova.yuliya;


public class AvailableChannel extends SentRequest{
		static String url = "http://tracks.osll.spb.ru:81/service/channels";
		public AvailableChannel(String req1) {
			super(req1,url);
		}
	
	}