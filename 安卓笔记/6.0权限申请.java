

//step1
 //动态申请权限
    public void requestPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE},REQUESTCODE);
    }
	
	//step2
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 3:
                Log.e("==call permission a",ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)+"");
                break;
        }
    }
	
	//step3
	
	//<uses-permission android:name="android.permission.CALL_PHONE" />