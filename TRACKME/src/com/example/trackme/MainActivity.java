package com.example.trackme;

//import info.androidhive.androidcameraapi.R;

//import java.io.File;
//import java.text.SimpleDateFormat;
////import java.util.Date;
//import java.util.Locale;

//import com.example.trackinglocation.AppLocationService;
//import com.example.trackinglocation.LocationAddress;
//import com.example.trackinglocation.MainActivity;
//import com.example.trackinglocation.MainActivity.GeocoderHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
//import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
//import android.sax.StartElementListener;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
//import java.io.File;
/*import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;*/


public class MainActivity extends Activity {

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private Uri fileUri; // file url to store image/video

	 ImageView imgPreview;
	 VideoView videoPreview;
	private Button btnCapturePicture, btnRecordVideo,btnGPSShowLocation;
	private Button sms,btnShowAddress,mail;
	private TextView tvaddress;
	AppLocationService appLocationService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imgPreview = (ImageView) findViewById(R.id.imagepreview);
		videoPreview = (VideoView) findViewById(R.id.videopreview);
		btnCapturePicture = (Button) findViewById(R.id.photo1);
		btnRecordVideo = (Button) findViewById(R.id.video1);
		 btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
		 btnShowAddress = (Button) findViewById(R.id.btnShowAddress);
		 tvaddress = (TextView) findViewById(R.id.tvAddress);
		 sms=(Button)findViewById(R.id.sendsms);
		 mail=(Button)findViewById(R.id.sendmail);
	        appLocationService = new AppLocationService(MainActivity.this);
	        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// capture picture
					captureImage();
				}
			});

			/*
			 * Record video button click event
			 */
			btnRecordVideo.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// record video
					recordVideo();
				}
			});
			/*b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i=new Intent(MainActivity.this,MainActivity2.class);
					startActivity(i);
					
				}
			});*/

			// Checking camera availability
			if (!isDeviceSupportCamera()) {
				Toast.makeText(getApplicationContext(),
						"Sorry! Your device doesn't support camera",
						Toast.LENGTH_LONG).show();
				// will close the app if the device does't have camera
				finish();
			}
			}
			private boolean isDeviceSupportCamera() {
				if (getApplicationContext().getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA)) {
					// this device has a camera
					return true;
				} else {
					// no camera on this device
					return false;
				}
			}

			/*
			 * Capturing Camera Image will lauch camera app requrest image capture
			 */
			private void captureImage() {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				// start the image capture Intent
				startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
			}

			private Uri getOutputMediaFileUri(int mediaTypeImage) {
				// TODO Auto-generated method stub
				return null;
			}
			/*
			 * Here we store the file url as it will be null after returning from camera
			 * app
			 */
			@Override
			protected void onSaveInstanceState(Bundle outState) {
				super.onSaveInstanceState(outState);

				// save file url in bundle as it will be null on scren orientation
				// changes
				outState.putParcelable("file_uri", fileUri);
			}

			@Override
			protected void onRestoreInstanceState(Bundle savedInstanceState) {
				super.onRestoreInstanceState(savedInstanceState);

				// get the file url
				fileUri = savedInstanceState.getParcelable("file_uri");
			}

			/*
			 * Recording video
			 */
			private void recordVideo() {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

				fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

				// set video quality
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
																	// name

				// start the video capture Intent
				startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
			}

			/**
			 * Receiving activity result method will be called after closing the camera
			 * */
			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				// if the result is capturing Image
				if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
					if (resultCode == RESULT_OK) {
						// successfully captured the image
						// display it in image view
						previewCapturedImage();
					} else if (resultCode == RESULT_CANCELED) {
						// user cancelled Image capture
						Toast.makeText(getApplicationContext(),
								"User cancelled image capture", Toast.LENGTH_SHORT)
								.show();
					} else {
						// failed to capture image
						Toast.makeText(getApplicationContext(),
								"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
								.show();
					}
				} else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
					if (resultCode == RESULT_OK) {
						// video successfully recorded
						// preview the recorded video
						previewVideo();
					} else if (resultCode == RESULT_CANCELED) {
						// user cancelled recording
						Toast.makeText(getApplicationContext(),
								"User cancelled video recording", Toast.LENGTH_SHORT)
								.show();
					} else {
						// failed to record video
						Toast.makeText(getApplicationContext(),
								"Sorry! Failed to record video", Toast.LENGTH_SHORT)
								.show();
					}

			 btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View arg0) {
		            	
		                Location gpsLocation = appLocationService
		                        .getLocation(LocationManager.GPS_PROVIDER);
		                if (gpsLocation != null) {
		                    gpsLocation.getLatitude();
		                    gpsLocation.getLongitude();
		                    String result = "Latitude: " + gpsLocation.getLatitude() +
		                            " Longitude: " + gpsLocation.getLongitude();
		                    tvaddress.setText(result);
		                } else {
		                    showSettingsAlert();
		                }
		            }
		        });

		        btnShowAddress = (Button) findViewById(R.id.btnShowAddress);
		        btnShowAddress.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View arg0) {

		                Location location = appLocationService
		                        .getLocation(LocationManager.GPS_PROVIDER);

		                //you can hard-code the lat & long if you have issues with getting it
		                //remove the below if-condition and use the following couple of lines
		                //double latitude = 37.422005;
		                //double longitude = -122.084095

		                if (location != null) {
		                    double latitude = location.getLatitude();
		                    double longitude = location.getLongitude();
		                    new LocationAddress();
		                    LocationAddress.getAddressFromLocation(latitude, longitude,
		                            getApplicationContext(), new GeocoderHandler());
		                } else {
		                    showSettingsAlert();
		                }

		            }
		        });
				}
		    }

		    private void previewVideo() {
				// TODO Auto-generated method stub
				
			}
			private void previewCapturedImage() {
				// TODO Auto-generated method stub
				
			}
			public void showSettingsAlert() {
		        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		                MainActivity.this);
		        alertDialog.setTitle("SETTINGS");
		        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
		        alertDialog.setPositiveButton("Settings",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                        Intent intent = new Intent(
		                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		                        MainActivity.this.startActivity(intent);
		                    }
		                });
		        alertDialog.setNegativeButton("Cancel",
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int which) {
		                        dialog.cancel();
		                    }
		                });
		        alertDialog.show();
		    }
		    @SuppressLint("HandlerLeak")
			private class GeocoderHandler extends Handler {
		        @Override
		        public void handleMessage(Message message) {
		            String locationAddress;
		            switch (message.what) {
		                case 1:
		                    Bundle bundle = message.getData();
		                    locationAddress = bundle.getString("address");
		                    break;
		                default:
		                    locationAddress = null;
		            }
		            tvaddress.setText(locationAddress);
		        
		       // send=(Button)findViewById(R.id.SHARE1);
				final String gett1=tvaddress.getText().toString();
				sms.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						try {
							
							 Intent sendIntent = new Intent(Intent.ACTION_VIEW);
						     sendIntent.putExtra("sms_body",gett1); 
						     sendIntent.setType("vnd.android-dir/mms-sms");
						     startActivity(sendIntent);
						        
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"SMS faild, please try again later!",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

					}
				});
				mail.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent email = new Intent(Intent.ACTION_SEND);
						//  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
						  //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
						  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
						  //email.putExtra(Intent.EXTRA_SUBJECT, subject);
						  email.putExtra(Intent.EXTRA_TEXT,gett1);

						  //need this to prompts email client only
						  email.setType("message/rfc822");
						  
						  startActivity(Intent.createChooser(email, "Choose an Email client :"));

						
					}
				});


		       /* String gett1=tvAddress.getText().toString();
		        Intent i=new Intent(MainActivity.this,SHARE.class);
		        Bundle b=new Bundle();
		       // b.putString("one",gett1);
		       // i.putE
		        b.putString("TWO", gett1);
				i.putExtras(b);*/

		        
		    }

			

			@SuppressWarnings("unused")
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.activity_main, menu);
				return true;
			}

		}
		}



	        

	




        
    

	

	/*@SuppressWarnings("unused")
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	
    

    btnCapturePicture.setOnClickListener(new View.OnClickListener() {

    	public void onClick(View v) {
    		// capture picture
    		captureImage();
    	}
    });

    /*
     * Record video button click event
     */
    /*btnRecordVideo.setOnClickListener(new View.OnClickListener() {

    	@Override
    	public void onClick(View v) {
    		// record video
    		recordVideo();
    	}
    });
    /*b1.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View arg0) {
    		// TODO Auto-generated method stub
    		Intent i=new Intent(MainActivity.this,MainActivity2.class);
    		startActivity(i);
    		
    	}
    });*/

    // Checking camera availability
   /* if (!isDeviceSupportCamera()) {
    	Toast.makeText(getApplicationContext(),
    			"Sorry! Your device doesn't support camera",
    			Toast.LENGTH_LONG).show();
    	// will close the app if the device does't have camera
    	finish();
    }
    }

    /**
    * Checking device has camera hardware or not
    * */
    /* boolean isDeviceSupportCamera() {
    if (getApplicationContext().getPackageManager().hasSystemFeature(
    		PackageManager.FEATURE_CAMERA)) {
    	// this device has a camera
    	return true;
    } else {
    	// no camera on this device
    	return false;
    }
    }

    /*
    * Capturing Camera Image will lauch camera app requrest image capture
    */
   /* private void captureImage() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);*/

   // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

   // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

    // start the image capture Intent
   // startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    

    /*
    * Here we store the file url as it will be null after returning from camera
    * app
    */
   // protected void onSaveInstanceState(Bundle outState) {
    //super.onSaveInstanceState(outState);

    // save file url in bundle as it will be null on scren orientation
    // changes
    //utState.putParcelable("file_uri", fileUri);
   // }

    /*protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    // get the file url
    fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /*
    * Recording video
    */
  /*  private void recordVideo() {
    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

    fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

    // set video quality
    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
    													// name

    // start the video capture Intent
    startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }*/

    /**
    * Receiving activity result method will be called after closing the camera
    * */
    
  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // if the result is capturing Image
    if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
    	if (resultCode == RESULT_OK) {*/
    		
