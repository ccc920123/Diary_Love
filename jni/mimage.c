#include <jni.h>
#include <stdio.h>
#include <android/bitmap.h>
#include <math.h>
#include "img.h"
/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    whitening
 * Signature: ([III)[I
 * 美白
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_whitening(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	float light = 0.2f;
	float dbd = 0.2f;
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dest[newsize];
	int r, g, b, a;
	// 开始美白
	// 增加亮度 0~255
	int bfi = (int) (light * 255);
	// 对比度
	float cf = 1.0f + dbd;
	cf *= cf; // 对比度增加1.44倍
	// 32为真彩色 2的16次方
	int cfi = (int) (cf * 65536) + 1;
	int x = 0;
	int y = 0;
	for (x = 0; x < width; x++) {
		for (y = 0; y < height; y++) {
			int color = sources[y * width + x];
			r = red(color);
			g = green(color);
			b = blue(color);
			a = alpha(color);
			// 修改亮度
			int ri = r + bfi;
			int gi = g + bfi;
			int bi = b + bfi;
			// 防止三原色超出255
			r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
			g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
			b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
			// 修改对比度-128~127
			// 偏移到正确范围
			ri = r - 128;
			gi = g - 128;
			bi = b - 128;
			ri = (ri * cfi) >> 16;
			gi = (gi * cfi) >> 16;
			bi = (bi * cfi) >> 16;
			// 偏移回来0~255
			ri = ri + 128;
			gi = gi + 128;
			bi = bi + 128;
			r = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
			g = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
			b = bi > 255 ? 255 : (bi < 0 ? 0 : bi);

			int destColor = ARGB(a, r, g, b);
			dest[y * width + x] = destColor;

		}
	}
	//新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dest);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    ice
 * Signature: ([III)[I
 * 冰冻
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_ice(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dst[newsize];
	int R, G, B, pixel, A;
	int pos, pixColor;
	int x = 0;
	int y = 0;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			pos = y * width + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			pixel = R - G - B;
			pixel = pixel * 3 / 2;
			if (pixel < 0)
				pixel = -pixel;
			if (pixel > 255)
				pixel = 255;
			R = pixel; // 计算后重置R值，以下类同

			pixel = G - B - R;
			pixel = pixel * 3 / 2;
			if (pixel < 0)
				pixel = -pixel;
			if (pixel > 255)
				pixel = 255;
			G = pixel;

			pixel = B - R - G;
			pixel = pixel * 3 / 2;
			if (pixel < 0)
				pixel = -pixel;
			if (pixel > 255)
				pixel = 255;
			B = pixel;
			int destColor = ARGB(A, R, G, B);
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
	  //新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    fire熔铸
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_fire(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dst[newsize];
	int R, G, B, pixel, A;
	int pos, pixColor;
	int x = 0;
	int y = 0;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			pos = y * width + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			pixel = R * 128 / (G + B + 1);
			if (pixel < 0)
				pixel = 0;
			if (pixel > 255)
				pixel = 255;
			R = pixel;

			pixel = G * 128 / (B + R + 1);
			if (pixel < 0)
				pixel = 0;
			if (pixel > 255)
				pixel = 255;
			G = pixel;

			pixel = B * 128 / (R + G + 1);
			if (pixel < 0)
				pixel = 0;
			if (pixel > 255)
				pixel = 255;
			B = pixel;
			int destColor = ARGB(A, R, G, B);
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
	  //新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    comicStrip连环画
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_comicStrip(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dst[newsize];
	int R, G, B, pixel, A;
	int pos, pixColor;
	int x = 0;
	int y = 0;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			pos = y * width + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			pixel = G - B + G + R;
			if (pixel < 0)
				pixel = -pixel;
			pixel = pixel * R / 256;
			if (pixel > 255)
				pixel = 255;
			R = pixel;

			// G = |b – g + b + r| * r / 256;
			pixel = B - G + B + R;
			if (pixel < 0)
				pixel = -pixel;
			pixel = pixel * R / 256;
			if (pixel > 255)
				pixel = 255;
			G = pixel;

			// B = |b – g + b + r| * g / 256;
			pixel = B - G + B + R;
			if (pixel < 0)
				pixel = -pixel;
			pixel = pixel * G / 256;
			if (pixel > 255)
				pixel = 255;
			B = pixel;
			int destColor = ARGB(A, R, G, B);
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
	  //新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    light 边缘照亮
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_light(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	int rectTop = 0;
	int rectBottom = height - 1;
	int rectLeft = 0;
	int rectRight = width - 1;
	jint dst[newsize];
	int R, G, B, pixel, A;
	int pos, pixColor;
	int x = 0;
	int y = 0;
	for (y = rectTop; y < rectBottom; y++) {
		for (x = rectLeft; x < rectRight; x++) {
			pos = y * rectRight + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			{
				pixel = (int) (pow((B - blue(sources[pos + rectRight])), 2)
						+ pow((B - blue(sources[pos + 1])), 2));
				pixel = (int) (sqrt(pixel) * 2);

				if (pixel < 0)
					pixel = 0;
				if (pixel > 255)
					pixel = 255;

				B = pixel;
			}
			{
				pixel = (int) (pow((G - green(sources[pos + rectRight])), 2)
						+ pow((G - green(sources[pos + 1])), 2));
				pixel = (int) (sqrt(pixel) * 2);

				if (pixel < 0)
					pixel = 0;
				if (pixel > 255)
					pixel = 255;

				G = pixel;
			}
			{
				pixel = (int) (pow((R - red(sources[pos + rectRight])), 2)
						+ pow((R - red(sources[pos + 1])), 2));
				pixel = (int) (sqrt(pixel) * 2);

				if (pixel < 0)
					pixel = 0;
				if (pixel > 255)
					pixel = 255;

				R = pixel;
			}
			int destColor = ARGB(A, R, G, B);
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
//新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    eclosion羽化
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_eclosion(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dst[newsize];
	float Size = 0.5f;
	int R, G, B, pixel, A;
	int pos, pixColor;
	int ratio =
			width > height ? height * 32768 / width : width * 32768 / height;

	int cx = width >> 1;
	int cy = height >> 1;
	int max = cx * cx + cy * cy;
	int min = (int) (max * (1 - Size));
	int diff = max - min;
	int x = 0;
	int y = 0;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			pos = y * width + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			int dx = cx - x;
			int dy = cy - y;
			if (width > height) {
				dx = (dx * ratio) >> 15;
			} else {
				dy = (dy * ratio) >> 15;
			}
			int distSq = dx * dx + dy * dy;
			float v = ((float) distSq / diff) * 255;
			R = (int) (R + (v));
			G = (int) (G + (v));
			B = (int) (B + (v));
			R = (R > 255 ? 255 : (R < 0 ? 0 : R));
			G = (G > 255 ? 255 : (G < 0 ? 0 : G));
			B = (B > 255 ? 255 : (B < 0 ? 0 : B));
			int destColor = ARGB(A, R, G, B);
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
	  //新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    eclosionAndWhite 羽化美白
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_eclosionAndWhite(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    dim 模糊
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_dim(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
	jint *sources = (*env)->GetIntArrayElements(env, buff, 0);
	int newsize = width * height;
	jint dst[newsize];
	int m_length = 1;
	double m_offset_x;
	double m_offset_y;
	int m_fcx, m_fcy;
	int R, G, B, pixel, A;
	int pos, pixColor;
	int RADIUS_LENGTH = 64;
	m_fcx = (int) (width * m_offset_x * 32768.0) + (width * 32768);
	m_fcy = (int) (height * m_offset_y * 32768.0) + (height * 32768);
	int ta = 255;
	int x = 0;
	int y = 0;
	for (y = 0; y < height; y++) {
		for (x = 0; x < width; x++) {
			int sr = 0, sg = 0, sb = 0, sa = 0;
			pos = y * width + x;
			pixColor = sources[pos]; // 获取图片当前点的像素值

			R = red(pixColor); // 获取RGB三原色
			G = green(pixColor);
			B = blue(pixColor);
			A = alpha(pixColor);
			sr = R * ta;
			sg = G * ta;
			sb = B * ta;
			sa += ta;
			int fx = (x * 65536) - m_fcx;
			int fy = (y * 65536) - m_fcy;
			int i = 0;
			for (i = 0; i < RADIUS_LENGTH; i++) {
				fx = fx - (fx / 16) * m_length / 1024;
				fy = fy - (fy / 16) * m_length / 1024;

				int u = (fx + m_fcx + 32768) / 65536;
				int v = (fy + m_fcy + 32768) / 65536;
				if (u >= 0 && u < width && v >= 0 && v < height) {

					pos = v * width + u;
					pixColor = sources[pos];
					sr += red(pixColor) * ta;
					sg += green(pixColor) * ta;
					sb += blue(pixColor) * ta;
					sa += ta;
				}
			}
			int destColor = ARGB(A, safeColor(sr/sa), safeColor(sg/sa), safeColor(sb/sa));
			dst[pos] = destColor; // 重置当前点的像素值
		} // x
	} // y
	//新建一个数组
	jintArray result = (*env)->NewIntArray(env, newsize);
	//数组复制，从dest复制到result
	(*env)->SetIntArrayRegion(env, result, 0, newsize, dst);
	//释放内存
	(*env)->ReleaseIntArrayElements(env, buff, sources, 0);
	return result;
}

/*
 * Class:     com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk
 * Method:    lomo
 * Signature: ([III)[I
 */JNIEXPORT jintArray JNICALL Java_com_chenpan_heart_diary_view_imagefilterndk_ImageFilterNdk_lomo(
		JNIEnv * env, jclass clz, jintArray buff, jint width, jint height) {
}
