package vorobiova.yuliya;


public class SubscribeChannel extends SentRequest{
		static String url = "http://tracks.osll.spb.ru:81/service/subscribe";
		public SubscribeChannel(String req1) {
			super(req1,url);
		}

	/*	public static void main(String[] args) {
			SubscribeChannel l=new SubscribeChannel();
		}*/
	}

