package vorobiova.yuliya;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TestGPSActivity extends Activity implements OnClickListener, LocationListener {
	private ViewFlipper flipper;
	private Button bt_sent_your;
	private Button bt_find_friend;
	private Button bt_find_friend2;
	private EditText login;
	private EditText your_friend;
	private EditText password;
	private TextView answer;
	private TextView answer2;
	private String response;
	private Location loc = null;          
	private LocationManager locManager = null;
	private double Lat=7.100001;
	private double Lon=7.0;
	private double LatFriend = 0;
	private double LonFriend = 0;
	private int mode = 1;
	public final String dateformat = "dd MM yyyy HH:mm:ss.SSS";
	private String auth_token=null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainLayout.setOnClickListener(this);

        // �������� ������ ViewFlipper
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper1);
       
              
        // ������� View � ��������� �� � ��� ������� flipper
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layouts[] = new int[]{R.layout.main_first, R.layout.main1,R.layout.main2};
        for (int layout : layouts) 
            flipper.addView(inflater.inflate(layout, null));
 
        bt_sent_your = (Button) findViewById(R.id.send_your);
        bt_find_friend = (Button) findViewById(R.id.find_your_friend);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        
        bt_sent_your.setOnClickListener(new View.OnClickListener() {
        	// �������� ������ �� ������
    		@Override
    		public void onClick(View v) {
    		answer = (TextView)findViewById(R.id.answer);		
    		String name = login.getText().toString();
    		String pass = password.getText().toString();
    		mode = 1;
    		if ((name.trim().equals("")) || (pass.trim().equals(""))) CheckDate(); // �� ��������� ��� ����
    		else {
    		flipper.showNext();
    		FindFriend();
    		}
    		}
		});
        bt_find_friend.setOnClickListener(new OnClickListener() {
        	// ����������� ��������� �� ������� ������������
    		@Override
    		public void onClick(View v) {
    			your_friend = (EditText) findViewById(R.id.your_friend);
    			bt_find_friend2 = (Button) findViewById(R.id.button_your_friend2);
    			answer2 = (TextView)findViewById(R.id.answer2);
    			String name = login.getText().toString();
    			String pass = password.getText().toString();
    			mode = 2;
    			if ((name.trim().equals("")) || (pass.trim().equals(""))) CheckDate(); // �� ��������� ��� ����
    			else {
    			flipper.showNext();
    			flipper.showNext();
    			GetLoginFriend();
    			}
    		}
        });
    }

    /* �������� ������������� ���� ������������, 
     * ���������� �� �������� ����� ������*/
    private void GetLoginFriend() {
    	bt_find_friend2.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			String name = your_friend.getText().toString();
    			if (name.trim().equals("")) CheckDate();
    			else FindFriend();
    			}
               });
	}

    /* ��������� �������� �������
     * ������������ �������� - ����� � ������� Geo2Tag"
     */
    private String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
		return formatter.format(new Date());
	}
    
    /* ������� ����������� ���������� �� ������������� ������������*/
	private void FindFriend() {
		TextView curAnswer;
		if (mode == 1)  curAnswer = answer;
		else curAnswer = answer2;
		//curAnswer.setText("mode = " + mode);
 		// 1. login
	    String request_login = "{\"login\":\""+login.getText()+"\",\"password\":\""+password.getText()+"\"}";
    	login Log = new login(request_login);
    	response = Log.Sent();
    	curAnswer.setText("log_user: " + response+ " Try again later.");
    	    	
    	if (response.contains("Error")== false) {
       	
    	auth_token=response.substring(18, response.indexOf("status")-4);
       
    	//2. ���������� ���� ����������
	    GPSInit();
	   	     
	   //3. �������� ������� �����
	   String date = getTime();
	    
	   //4. ������� ���� ���������� �� ������ 
	    String request_applymark = 	"{ \"auth_token\" : \""+auth_token+"\", \"channel\" : \""+login.getText()+"\", \"description\" : \"\", \"latitude\" : "+Lat+",\"link\" : \"unknown\", \"longitude\" : "+Lon+", \"time\" : \""+date+"\", \"title\" : \"tracker's tag\" }" ;
	    ApplyMark AM = new ApplyMark(request_applymark);
	    response = AM.Sent();	
	    curAnswer.setText("apply mark : "+response+ " Try again later.");
	   
	    if ((response.contains("Error")== false) && (mode != 1)) {
	    	//5. �������� ������ � ���� ��������� ������� 
	    	String request_AC_RSS = "{\"auth_token\":\""+auth_token+"\", \"latitude\":"+Lat+", \"longitude\":"+Lon+", \"radius\":30000.0}";
	    	AvailableChannel AC = new AvailableChannel(request_AC_RSS);
	    	response = AC.Sent();
	    	curAnswer.setText("available chanel: "+response + " Try again later.");
	    	
	    	if (response.contains("Error")== false) {
	    		//6. ��������� ����� �� ������� �� ������� �������, ��������� 
	    		//	 ���� �� ������
	    		boolean Friend = AvalChan(response,your_friend.getText().toString());	
	    	
	    		//7. ���� ������ ����� ����, ������������� �� ���� 
	    	if (Friend == true) {
	    		String request_sub_chanel = "{\"auth_token\":\""+auth_token+"\",\"channel\":\""+your_friend.getText()+"\"}" ;
	        	SubscribeChannel SC = new SubscribeChannel(request_sub_chanel);
	        	response = SC.Sent();
	        	curAnswer.setText("subscribe_chanel: " + response);
	        	
	        	if ((response.contains("Error")== false) || (response.contains("subscribed")==true)) {
	        		
	        		//8. �������� ���������� � ���� �������, �� ������� ��������� 
	        		RSSFeed RSS = new RSSFeed(request_AC_RSS);
	        		response = RSS.Sent();
	        		curAnswer.setText("rss: "+response+ " Try again later." );
	        		if (response.contains("Error")== false) {
	        			
	        			//9. ��������� ����� �� ������� ,�������� ��������� ����������
	        			//	 ������������� ��� ������������	
	        			RSS(response,login.getText().toString(), your_friend.getText().toString());
	        			
	        			//10. ���������� ���������� �� ������������ 
	        			double distanse = FindDistance();
	        			String Sdistance = Double.toString(distanse);
	        			curAnswer.setText("For your friend "+ Sdistance+ " km ");
	        		}
	        	}
	        	}
	    	else curAnswer.setText("Not found your friend :( ");
	    	}
	    }
	    else curAnswer.setText("All succesful :) ");
    	}
  	}
    			

	/* ����������� ���������� �� ��������� ������� � ��������
	 * ������������ �������� - ���������� � �� */
	private double FindDistance() {
	 double cos_d=Math.sin(Lat)*Math.sin(LatFriend)+Math.cos(Lat)*Math.cos(LatFriend)*Math.cos(Lon-LonFriend);
	 double d = Math.acos(cos_d);
	 double L = d*6371; // � ��
	 return L;		
	}

	/* �������� ��������, ��������� �������������*/ 
	private void CheckDate() {
		 CharSequence message = "Enter all the data";
	     Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
	     toast.setGravity(Gravity.CENTER, 0, 0);
	     toast.show();
		}
    
	/* ����������� ��������� ���������� */ 	   	
	private void GPSInit() {
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    	loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	if (loc == null)
    		loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
      if (loc!=null) {
      Lon = loc.getLongitude();
      Lat = loc.getLatitude();
      }
    }	
    
	
	
	/* ������� ������� ������ �� ������� � ����� �� ������ 
	 * � ���� ��������� ������� 
	 * channel - ��� ������ ������������
	 * name - ��� ������ ,������� ���� */
	public boolean AvalChan(String chanal, String name) {
		int index;
		boolean result = false;
		while ((index = chanal.indexOf("name"))!= -1) {
			//String my1 = chanal.substring(0, index+9);
			String my2 = chanal.substring(index+9, chanal.length());
			String my3 = my2.substring(0, my2.indexOf("\"")); // ��� ������
			if (my3.equalsIgnoreCase(name)) result = true;
			chanal = my2;
		}
		return result;
	}
	
	
	/* ������� ������� ������ �� ������� � ����� �� ������ 
	 * � �������, � ������� �������� ������������
	 * rss - ������ ������ �� ������� �� ������ ������ ����������� �������
	 * name_user - ��� ������������
	 * name_friend - ��� ������������, ���������� �������� ����*/ 
	
	public void RSS(String rss, String name_user, String name_friend ) {
		int index_name;
		boolean IsNeedUser = false;
		Double longitude = 0.0;
		Double latitude = 0.0;
		
	while ((index_name = rss.indexOf("name")) != -1) {
		String my1 = rss.substring(0,index_name+9);
		String my2 = rss.substring(index_name+9, rss.length());
		String my3 = my2.substring(0, my2.indexOf("\"")); // ��� ������
		if (!my3.equalsIgnoreCase(name_user)){ //��� ������  - �� ��� ������������
				
	 while (IsNeedUser == false) {
		// ��������� ,��� ������ ����� ������� ������ ������������
		 int index_user;
		if ((index_user= my1.indexOf("user")) !=-1 ) {
		String my4 = my1.substring(0,my1.indexOf("user")+9); // ������ � ������ �� ����� ������������, ����������� �����
		String my5 = my1.substring(my1.indexOf("user")+9, my1.length()); //������, ������������ � ����� ������������, ����������� �����
		String my6 = my5.substring(0, my5.indexOf("\"")); // ������ � ������ ������������ �����
		if (name_friend.equalsIgnoreCase(my6)) { 
		IsNeedUser = true;	
		// ���� ����� ������� ���	
	//	String my7 = my4.substring(0,my4.indexOf("latitude")+12); // � ������ � �� �������� latitude
		String my8 = my4.substring(my4.indexOf("latitude")+12,my4.length()); // ����������� � latitude
		String my9 = my8.substring(0,my8.indexOf(",")); // latitude
		latitude = new Double(my9);
				
	//	String my10 = my4.substring(0,my4.indexOf("longitude")+13); // � ������ � �� �������� longitude
		String my11 = my4.substring(my4.indexOf("longitude")+13,my4.length()); // ����������� � longitude
		String my12 = my11.substring(0,my11.indexOf(",")); // longitude
		longitude = new Double(my12);
		}
		else my1 = my5;
		}
	 	}
		}
		rss = my2;
		}
	LonFriend=longitude;
	LatFriend=latitude;
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		Lon = arg0.getLongitude();
	    Lat = arg0.getLatitude();
	    FindFriend();
		}
		
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}