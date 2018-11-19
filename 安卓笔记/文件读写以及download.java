
    //step1： 判断
    File file = new File(Environment.getExternalStorageDirectory(), "myVideo.mp4");
                if (file.exists()){//文件存在 直接播放
                    Log.e("===直接播放==",Environment.getExternalStorageDirectory().getAbsolutePath()+"/myVideo.mp4");
                    setupVideo(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myVideo.mp4");
                }else {//文件不存在 先下载文件
                    Log.e("===开始下载==","=======");
                    doDownload("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4","myVideo.mp4");
                }
				
				
	
	
	 //下载apk文件
    private void doDownload(String url, final String fileName) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient clint = new OkHttpClient();
        clint.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               Log.e("===网络请求失败==",e.toString()+"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();

                byte[] buf = new byte[2048];
                FileOutputStream fos = null;
                int len;
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    setupVideo(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileName);
                    Log.e("===下载完成s==","===========");
                } catch (Exception e) {
                    Log.e("===下载 error==",e.toString()+"");
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        });
    }
	
	
	//播放器的设置
    private void setupVideo(String url) {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.start();
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlaybackVideo();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                stopPlaybackVideo();
                return true;
            }
        });

        try {
            Uri uri = Uri.parse(url);
            vv.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaybackVideo() {
        try {
            vv.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }