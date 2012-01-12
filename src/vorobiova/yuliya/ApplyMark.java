package vorobiova.yuliya;

public class ApplyMark extends SentRequest{
	static String url = "http://tracks.osll.spb.ru:81/service/apply";
	public ApplyMark(String req1) {
		super(req1,url);
	}
}