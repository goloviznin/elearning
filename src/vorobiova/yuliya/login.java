package vorobiova.yuliya;


public class login extends SentRequest{
	static String url = "http://tracks.osll.spb.ru:81/service/login";
	public login(String req1) {
		super(req1,url);
	}
}
