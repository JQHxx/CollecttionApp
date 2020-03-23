package com.tools.model.scaner;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

	CaptureHanlder hanlder;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(CaptureHanlder hanlder) {
		this.hanlder = hanlder;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(hanlder);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
