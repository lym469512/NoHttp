/*
 * Copyright © YOLANDA. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sample.nohttp.activity.cancel;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.OnResponseListener;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.RequestQueue;
import com.yolanda.nohttp.Response;
import com.yolanda.nohttp.StringRequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 演示怎么取消一个请求</br>
 * Created in Oct 23, 2015 1:13:06 PM
 * 
 * @author YOLANDA
 */
public class NoHttpCancelActivity extends Activity implements View.OnClickListener, OnResponseListener<String> {

	private static final int REQUEST_1 = 0;
	private static final int REQUEST_2 = 1;
	private static final int REQUEST_3 = 2;

	/**
	 * 请求队列
	 */
	private RequestQueue mRequestQueue;
	/**
	 * 请求对象
	 */
	private Request<String> mRequest;
	/**
	 * 请求对象的取消标志
	 */
	private Object cancelSign;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("NoHttp取消请求演示");

		TextView textView = new TextView(this);
		textView.setText("请看本Activity的代码");
		setContentView(textView);

		String url = "http://www.baidu.com";

		// 闯将一个请求队列
		mRequestQueue = NoHttp.newRequestQueue(getApplicationContext());

		// 实例化取消标志
		cancelSign = new Object();

		// 请求1
		mRequest = new StringRequest(url, RequestMethod.GET);
		mRequest.setCancelSign(cancelSign);

		// 请求2
		Request<String> mRequest1 = new StringRequest(url, RequestMethod.GET);
		mRequest1.setCancelSign(cancelSign);

		// 请求3
		Request<String> mRequest2 = new StringRequest(url, RequestMethod.GET);
		mRequest2.setCancelSign(cancelSign);

		// 添加到请求队列
		mRequestQueue.add(REQUEST_1, mRequest, this);
		mRequestQueue.add(REQUEST_2, mRequest1, this);
		mRequestQueue.add(REQUEST_3, mRequest2, this);

		// 取消一个请求
		mRequest.cancel();

		// 取消执行sign的请求
		mRequestQueue.cancelAll(cancelSign);

		// 取消所有队列的请求
		mRequestQueue.stop();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onStart(int what) {
	}

	@Override
	public void onSucceed(int what, Response<String> response) {
		// what是添加到请求队列时的what, 结果回调时原样返回，当多个what使用同一个listener时，可以区分请求结果
		Logger.i(what + "请求响应结果: " + response.get());
	}

	@Override
	public void onFailed(int what, String url, Object tag, CharSequence message) {
	}

	@Override
	public void onFinish(int what) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时取消请求
		if (mRequest != null)
			mRequest.cancel();
	}
}